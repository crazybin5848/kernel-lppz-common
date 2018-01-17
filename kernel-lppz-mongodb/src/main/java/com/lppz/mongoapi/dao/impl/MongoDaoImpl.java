package com.lppz.mongoapi.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.elasticsearch.action.delete.DeleteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import redis.clients.jedis.params.set.SetParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.result.SearchAllResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.bean.SyncRedisToEsModel;
import com.lppz.mongoapi.constants.MongoContants;
import com.lppz.mongoapi.dao.BaseMongoDao;
import com.lppz.mongoapi.dao.MongoDao;
import com.lppz.mongoapi.enums.EsOperationEnums;
import com.lppz.mongoapi.enums.MongoUpdate;
import com.lppz.mongoapi.util.EsIndexUtils;
import com.lppz.mongoapi.util.MongoSliceUtil;
import com.lppz.switchs.listener.SwitchListener;
import com.lppz.util.LppzConstants;
import com.lppz.util.curator.listener.ZookeeperProcessListen;
import com.lppz.util.rocketmq.ProducerParam;
import com.lppz.util.rocketmq.RocketMqProducer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@SuppressWarnings({"unchecked","rawtypes"})
@Component
public class MongoDaoImpl extends BaseMongoDao implements MongoDao{
	private static final Logger logger = LoggerFactory.getLogger(MongoDaoImpl.class);
	
	private String dayStr = null;
	
	private String producerGroup = "AppDayLivingGroup";
	private String producerInstance = "instance";
	private String topic = "AppDayLivingTopic";
	private int esSearchMaxRetryLimit = 10;
	
	@Value("${es.send.disruptor:true}")
	private boolean esSendDisruptor;
	
	Vector<Long> v = new Vector<>();
	
	private AtomicBoolean isInitMap = new AtomicBoolean(false);
	
	@Override
	public void insert(String table, Document document) {
//		long start = System.nanoTime();
//		while (sender == null) {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//			}
//		}
//		long cost = System.nanoTime()-start;
//		logger.info("cost {}",cost);
//		v.add(cost);
//		int size = v.size();
//		long total = 0;
//		for (int i = 0; i < size; i++) {
//			total+=v.get(i);
//		}
//		logger.info("avg {}",total/size);
		Map<String,Document> msg = new HashMap<>();
		msg.put(table, document);
		DictModel dict = dictMap.get(table);
		
		if (useNew()) {
			mongoClient.getCollection(table).insertOne(document);
			logger.info("insert mongo sync mq table={}",table);
		}else{
			if (dict != null && dict.isSaveSync()) {
				mongoClient.getCollection(table).insertOne(document);
				logger.info("insert mongo sync table={}",table);
			}else{
				try {
					sender.sendMsg(msg);
					logger.info("insert in disruptor table={}",table);
				} catch (IllegalStateException e) {
					logger.error(String.format("发送disruptor异常，{}，延迟1s重试一次", e.toString()),e);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					sender.sendMsg(msg);
				}
			}
		}
	}
	
	private boolean useNew() {
		return !SwitchListener.isOff(MongoContants.USE_NEW_OP_SWITCH);
	}

	@Override
	public void insertBatch(String table, List<Document> documents) {
		mongoClient.getCollection(table).insertMany(documents);
	}
	
	@Override
	public void push(String table, String pk, String pkValue, String arrayField,
			List<Object> documents) {
		DictModel dict = dictMap.get(table);
		if (dict != null && dict.getKegArrayFields()!=null && dict.getKegArrayFields().contains(arrayField)) {
			pushDeal(table, pk, pkValue, arrayField, documents);
		}else{
			//非分桶数组直接push
			doPush(table, pk, pkValue, documents, arrayField);
		}
	}
	
	private void pushDeal(String table, String pk, String pkValue, String arrayField,
			List<Object> documents){
		//根据传入的document长度先在redis中hincrce，再根据长度计算当前这批数据所在桶，做插入更新处理
		long thread = Thread.currentThread().getId();
		String redisKey = getRedisKey(pkValue);
		String fieldKey = getArraySizeField(table, arrayField);
		logger.debug("pushDeal table={},pk={},pkValue={},arrayField={},documents={},redisLength={}",table,pk,pkValue,arrayField,documents==null?0:documents.size(),jedis.hget(redisKey, fieldKey));
		String kegNoField = getKegNoField(table, arrayField);
		int documentSize = documents.size();
		//通过redis的原子操作占用数组长度
		long arrayLength = jedis.hincrBy(redisKey, fieldKey, documentSize);
		logger.debug("threadId={},arrayLength={},document size={}",thread,arrayLength,documentSize);
		long kegNum = (arrayLength - 1) / mongoArrayMaxLength + 1;
		//当前数据写入数组的起始位置
		long startIndex = arrayLength - documentSize;
		//当前数据起始桶号
		long startKegNum = (startIndex - 1) / mongoArrayMaxLength + 1;
		//起始桶中剩余空位
		long startKegFixLength = startKegNum * mongoArrayMaxLength - startIndex;
		int incrKegNum = 0;
		logger.debug("max kegNum={},startIndex={},startKegNum={},startKegFixLength={}",kegNum,startIndex,startKegNum,startKegFixLength);
		Document query = new Document(pk,pkValue);
		if (startKegNum > 1 || arrayLength != documentSize) {
			query.append(kegNoKey, startKegNum);
		}
		UpdateOptions updateOptions = new UpdateOptions();
		updateOptions.upsert(true);
		//push后数组在同一桶中
		if (documentSize <= startKegFixLength) {
			logger.debug("threadId={},直接push,arrayLength={},document size={},max kegNum={},startIndex={},startKegNum={},startKegFixLength={}",thread,arrayLength,documentSize,kegNum,startIndex,startKegNum,startKegFixLength);
			doPush(table, documents, arrayField, (int)startKegNum, query, updateOptions);
		}else{
			//push数据跨桶
			List<Object> fristKegDocuments = documents.subList(0, (int)startKegFixLength);
			List<Object> remainArray = documents.subList((int)startKegFixLength, documents.size());
			doPush(table, fristKegDocuments, arrayField, (int)startKegNum, query, updateOptions);
			List<List<Object>> dealList = splitArray(remainArray);
			logger.debug("threadId={},fristKegDocuments size={},remain Size={}, upsert array size={}",thread,fristKegDocuments.size(),remainArray.size(),dealList.size());
			List<Object> insertList = new ArrayList<>();
			for (List<Object> arrayDocument : dealList) {
				Document document = new Document(pk,pkValue);
				document.put(kegNoKey, ++startKegNum);
				document.put(arrayField, arrayDocument);
				insertList.add(document);
				query.append(kegNoKey, startKegNum);
				//避免pull造成添加数组行号小于最大行号，insert操作将出现多条同桶号的行问题，使用upsert操作而不使用批量插入
				doPush(table, arrayDocument, arrayField, (int)startKegNum, query, updateOptions);
				incrKegNum++;
			}
		}
		long currentkeg = startKegNum;
		//设置redis中的桶号 TODO 有并发问题
		if (incrKegNum > 0) {
			currentkeg = jedis.hincrBy(redisKey, kegNoField, incrKegNum);
			if (currentkeg < startKegNum) {
				jedis.hset(redisKey, kegNoField, String.valueOf(startKegNum));
			}
		}
		logger.debug("threadId={},incrKegNum={} ,startKegNum={},currentkeg={}",thread,incrKegNum,startKegNum,currentkeg);
	}
	
	private void doPush(String table, Object documents, String arrayField, int kegNum, Document query, UpdateOptions updateOptions){
		Document pushD = new Document(MongoUpdate.pushAll.getOp(),new Document(arrayField,documents));
		pushD.append(MongoUpdate.set.getOp(),new Document(kegNoKey, kegNum));
		mongoClient.getCollection(table).updateMany(query, pushD,updateOptions);
	}
	
	private void doPush(String table, String pk, String pkValue, Object documents, String arrayField){
		Document query = new Document(pk,pkValue);
		Document pushD = new Document(MongoUpdate.pushAll.getOp(),new Document(arrayField,documents));
		UpdateOptions updateOp = new UpdateOptions();
		updateOp.upsert(true);
		mongoClient.getCollection(table).updateMany(query, pushD, updateOp);
	}
	
	@Override
	public long pull(String table, String pk, String pkValue,
			String arrayField, Object value) {
		return pullAll(table, pk, pkValue, arrayField, Arrays.asList(value));
	}
	
	@Override
	public long pullAll(String table, String pk, String pkValue,
			String arrayField, List<Object> values) {
		logger.debug("pullAll table={},pk={},pkValue={},arrayField={},size={}",table,pk,pkValue,arrayField,values.size());
		DictModel dict = dictMap.get(table);
		String[] fields = arrayField.split("\\.");
		String field = fields[0];
		long modifiedCount = 0;
		Map<String, Object> kegs = getPullKegs(table, pk, pkValue, arrayField, values);
		if (dict != null && dict.getKegArrayFields()!=null && dict.getKegArrayFields().contains(field)) {
			int kegNo = 1;
			if (kegs != null && !kegs.isEmpty()) {
				kegNo = (int) kegs.get(kegNoKey);
			}
			
			modifiedCount = doPull(table, pk, pkValue, arrayField, values,kegs);
			if (modifiedCount > 0) {
				subArrayLength(table, pk, pkValue, field, values.size(), kegNo);
				logger.info("kegs= {}",kegs);
				/*桶补位处理*/
				fixKeg(table, pk, pkValue, arrayField,kegs);
			}else{
				logger.warn("pull fail modifiedCount ={},values={}",modifiedCount,values);
			}
		}else{
			modifiedCount = doPull(table, pk, pkValue, arrayField, values, kegs);
		}
		return modifiedCount;
	}
	
	/**
	 * 执行批量pull操作
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param arrayField
	 * @param values
	 * @return
	 */
	private long doPull(String table, String pk, String pkValue,
			String arrayField, List<Object> values, Map<String,Object> kegs){
		Document filter = new Document(pk,pkValue);
		String[] fields = arrayField.split("\\.");
		String field = fields[0];
		UpdateResult result = null;
		long modifiedCount = 0;
		/*由于分桶操作，updateOne可能不能命中需要pull的数组，这里使用updateMany，所以arrayField和value需要确保使用唯一键*/
		if (fields.length==1) {
			result = mongoClient.getCollection(table).updateMany(filter, Updates.pullAll(field, values));
			modifiedCount = result==null?0:result.getModifiedCount();
		}else{
			//由于mongodb 的pullAll api只支持传入数组完整元素批量pull，鉴于app使用大部分是内嵌文档的数组，这里使用循环pull操作实现批量pull
			if (kegs != null && !kegs.isEmpty()) {
				List<Object> array = (List<Object>) kegs.get("arrayList");
				if (CollectionUtils.isNotEmpty(array)) {
					result = mongoClient.getCollection(table).updateMany(filter, Updates.pullAll(field, array));
					modifiedCount = result==null?0:result.getModifiedCount();
				}
			}else{
				//正常情况这里只会处理单个并且不分桶的数组
				for (Object value : values) {
					result = mongoClient.getCollection(table).updateMany(filter, Updates.pull(field, new Document(fields[1], value)));
					modifiedCount += result==null?0:result.getModifiedCount();
				}
			}
		}
		logger.info("doPull table={},pk={},pkValue={},arrayField={},values={},modifiedCount={}",table,pk,pkValue,arrayField,values.size(),modifiedCount);
		
		return modifiedCount;
	}
	
	/**
	 * 获取pull的桶号
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param arrayField
	 * @param value
	 * @return kegNo：最大桶号
	 * resultList:被pull的桶号
	 */
	private Map<String,Object> getPullKegs(String table, String pk, String pkValue,
			String arrayField, List<Object> values) {
		Map<String,Object> resultMap = new HashMap<>();
		String[] fields = arrayField.split("\\.");
		//获取当前数组最大桶号
		int kegNo = getMaxKegNo(table, pkValue, fields[0]);
		//批量处理或需要补齐才查询
		if (values.size() > 1 || kegNo > 1) {
			Bson filters = null;
			Document projection = new Document();
			projection.append(kegNoKey, 1);
			projection.append(fields[0], 1);
			projection.append("_id", 0);
			filters = Filters.and(Filters.eq(pk, pkValue),Filters.in(arrayField, values));
			List<Document> resultList = new ArrayList<>();
			List<Document> arrayList = new ArrayList<>();
			MongoCursor<Document> curosr = mongoClient.getCollection(table).find(filters).projection(projection)
					.iterator();
			Document tmpDocument = null;
			Document tmpDocument2 = null;
			List<Object> tmpList = null;
			while (curosr.hasNext()) {
				tmpDocument = curosr.next();
				//最后行不用补位
				if (tmpDocument.getInteger(kegNoKey, 1) < kegNo) {
					resultList.add(tmpDocument);
					tmpList = (List<Object>) tmpDocument.get(fields[0]);
					if (CollectionUtils.isNotEmpty(tmpList)) {
						for (Object doc : tmpList) {
							if (fields.length>1) {
								tmpDocument2 = (Document)doc;
								if (values.contains(tmpDocument2.get(fields[1]))) {
									arrayList.add(tmpDocument2);
								}
							}else{
								if (values.contains(doc)) {
									arrayList.add(tmpDocument2);
								}
							}
						}
					}
				}
			}
			resultMap.put(kegNoKey, kegNo);
			resultMap.put("resultList", resultList);
			resultMap.put("arrayList", arrayList);
			logger.info("source ={} result={}",values,arrayList);
		}
		return resultMap;
	}
	
	/**
	 * 分桶数组填补pull操作后的不饱和桶情况
	 * 低位桶被pull后，高位桶向低位补齐，避免桶不饱和导致的分页错误情况
	 * 
	 * 分别记录被pull行标识 table:pk:pkvalue:arrayField集合1号
	 * 和被pull桶号集合，便于定时补齐线程根据redis中的需要补齐行和桶的集合2号
	 * 另一个线程会从1号集合获取需要补齐的行条件，然后从2号集合中获取所有pull操作过的桶号做批量补齐
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param arrayField
	 * @param value
	 */
	private void fixKeg(String table, String pk, String pkValue,
			String arrayField, Map<String, Object> map) {
		Integer kegNo = null;
		if (map != null && !map.isEmpty()) {
			List<Document> resultList = (List<Document>) map.get("resultList");
			if (isASyncPull) {
//				saddPullKegToRedis(resultList, table, pk, pkValue, arrayField);
				hsetPullKegToRedis(resultList, table, pk, pkValue, arrayField);
			}else{
				int maxKegNo = (int) map.get(kegNoKey);
				if (CollectionUtils.isNotEmpty(resultList)) {
					for (Document document : resultList) {
						kegNo = document.getInteger(kegNoKey, 1);
						doFix(table,pk,pkValue,arrayField,kegNo,maxKegNo);
					}
				}
			}
		}
	}
	
	@Override
	public void update(String table, Document query, Document update, boolean upsert, boolean isMutil) {
		UpdateOptions updateOptions = new UpdateOptions();
		updateOptions.upsert(upsert);
		if (isMutil) {
			mongoClient.getCollection(table).updateMany(query, new Document(MongoUpdate.set.getOp(),update),updateOptions);
		}else{
			mongoClient.getCollection(table).updateOne(query, new Document(MongoUpdate.set.getOp(),update), updateOptions);
		}
	}
	
	@Override
	public long incr(String table, String pk, String pkValue, String column, int opValue) {
		Asserts.notNull(table, "incr tableName");
		Asserts.notNull(pk, "incr pkName");
		Asserts.notNull(pkValue, "incr pkValue");
		Asserts.notNull(column, "incr fieldName");
		Asserts.notNull(opValue, "incr opValue");
		String redisKey = getRedisKey(pkValue);
		long redisResult = jedis.hincrBy(redisKey, column, opValue);
		logger.debug("直接加完成redisKey="+redisKey);
		// 同步写redis 天增量数据 es需同步的incr数据
		jedis.rpush(redisSyncKey, JSON.toJSONString(new SyncRedisToEsModel(table, pk, pkValue, column)));
		logger.debug("同步es push完成redisSyncKey="+redisSyncKey);
		checkDayLiving(table, pk, pkValue, column, opValue);
		return redisResult;
	}
	
	/**
	 * 检测是否记录日活数据
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param column
	 * @param opValue
	 */
	private void checkDayLiving(String table, String pk, String pkValue, String column, int opValue) {
		DictModel dictModel = dictMap.get(table);
		if (dictModel != null) {
			if (dictModel.isDayLiving()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				dayStr = sdf.format(new Date());
				String dayLivingSetKey = String.format(userDayBehaveKeyFormat, dictModel.getTable(), dayStr);
				String dayLiveDataKey = String.format(defaultKeyFormat, pkValue,dayStr);
				//记录日活数据
				jedis.hincrBy(dayLiveDataKey, column, opValue);
				logger.debug("同步日活完成dayLiveDataKey="+dayLiveDataKey);
				//记录日活集合
				jedis.sadd(dayLivingSetKey, dayLiveDataKey);
				logger.debug("记录日活完成dayLivingSetKey="+dayLivingSetKey);
				sendToMq(table, pk, pkValue, column, opValue);
			}
		}
	}
	
	private void sendToMq(String table, String pk,String pkValue, String column, int opValue) {
		DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, nameSrvAddress, producerInstance);
		ProducerParam<String> param=new ProducerParam<String>();
		param.setClazz(String.class);
		param.setKey(pkValue);
		param.setBody(buildMqMessage(table, pk, pkValue, column, opValue));
		param.setTopic(topic);
		param.setTag(table);
		RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
		logger.debug("发送mq完成topic="+topic+" key="+pkValue);
		
	}

	private String buildMqMessage(String table, String pk,String pkValue, String column, int opValue) {
		return "table=" + table + ":" + pk + "=" + pkValue + ":" + column + "=" + opValue;
	}

	@Override
	public long getIncrValue(String pkValue, String column) {
		String redisKey = getRedisKey(pkValue);
		String strValue = jedis.hget(redisKey, column);
		if (StringUtils.isNotBlank(strValue)) {
			try {
				return Long.valueOf(strValue);
			} catch (Exception e) {
				logger.error(String.format("查询自增类型数据异常，rediskey：%s，field：%s，value：%s", redisKey,column,strValue),e);
			}
		}
		return 0;
	}
	
	@Override
	public long delete(String table, Document filter) {
		long deleteCount = 0;
		DeleteResult result = mongoClient.getCollection(table).deleteMany(filter);
		if (result != null) {
			deleteCount = result.getDeletedCount();
		}
		return deleteCount;
	}
	
	@Override
	public void saveInRedis(Map<String, Map<String, String>> cacheModel) {
		Assert.notEmpty(cacheModel);
		Map<String,String> valueMap = null;
		String cacheKey = null;
		Set<Entry<String, Map<String,String>>> entrySet = cacheModel.entrySet();
		for (Entry<String, Map<String,String>> entry : entrySet) {
			cacheKey = entry.getKey();
			valueMap = entry.getValue();
			Set<Entry<String, String>> valueEntrySet = valueMap.entrySet();
			for (Entry<String,String> valueEntry : valueEntrySet) {
				if (valueEntry.getValue() != null) {
					jedis.hset(getRedisKey(cacheKey), valueEntry.getKey(), valueEntry.getValue());
				}
			}
		}
	}
	
	@Override
	public Map<String,String> findCache(String cacheKey, String table,String columnName,
			List<String> fieldNames) {
		String jedisKey = getRedisKey(cacheKey);
		Map<String,String> cacheMap = jedis.hgetAll(jedisKey);
		Document document = null;
		if (cacheMap == null || cacheMap.isEmpty()) {
			cacheMap = new HashMap<>();
			MongoCursor<Document> curosr = mongoClient.getCollection(table).find(new Document(columnName, cacheKey))
					.projection(Projections.include(fieldNames)).iterator();
			while (curosr.hasNext()) {
				document = curosr.next();
				Set<Entry<String, Object>> entrySet = document.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String value = String.valueOf(entry.getValue());
					if (!"_id".equals(entry.getKey())) {
						jedis.hset(jedisKey,entry.getKey(), value);
						cacheMap.put(entry.getKey(), value);
					}
				}
			}
		}
		return cacheMap;
	}
	
	@Override
	public List<Document> findMongo(String table, Document query, List<String> fieldNames, Document sort, Integer skip, Integer limit) {
//		MongoCursor<Document> curosr = mongoClient.getCollection(table).find(query).projection(Projections.include(fieldNames)).iterator();
		//避免分桶造成的一页查询到多个桶情况
		Bson filters = Filters.and(query,Filters.or(Filters.eq(kegNoKey, 1),Filters.exists(kegNoKey, false)));
		FindIterable<Document> fi = mongoClient.getCollection(table).find(filters);
//		if(CollectionUtils.isNotEmpty(excludeFieldNames)){
//			fi.projection(Projections.exclude(excludeFieldNames));
//		}else 
		if (CollectionUtils.isNotEmpty(fieldNames)) {
			fi.projection(Projections.include(fieldNames));
		}
		if (sort != null && !sort.isEmpty()) {
			fi.sort(sort);
		}
		if (skip != null) {
			fi.skip(skip);
		}
		if (limit != null) {
			fi.limit(limit);
		}
		MongoCursor<Document> curosr = fi.iterator();
		List<Document> resultList = new ArrayList<>();
		Document document = null;
		while (curosr.hasNext()) {
			document = curosr.next();
			resultList.add(document);
//			buildSource(document, classType);
		}
		return resultList;
	}
	
	@Override
	public List<Document> pagingArrays(String table, String pk, String pkValue, String fieldName
			, List<String> resultFields, int pageNo, int pageSize, boolean desc) {
		if (pageNo == 0) {
			logger.warn("param pageNo is 0 default value 1");
			pageNo = 1;
		}
		if (pageSize == 0) {
			logger.warn("param pageSize is 0 default value 10");
			pageSize = 10;
		}
		if(desc){
			return pagingArraysDesc(table, pk, pkValue, fieldName, resultFields, pageNo, pageSize);
		}else{
			return pagingArraysAsc(table, pk, pkValue, fieldName, resultFields, pageNo, pageSize);
		}
	}
	
	private List<Document> pagingArraysAsc(String table, String pk, String pkValue, String fieldName
			, List<String> resultFields, int pageNo, int pageSize){
		int skipNum = 0;
		int limitNum = pageSize > mongoArrayMaxLength ? mongoArrayMaxLength : pageSize;
		int kegNo = 1;
		if (pageNo > 1) {
			int totalSkip = (pageNo -1) * pageSize;
			int totalLimit = pageNo * pageSize;
			kegNo = (totalLimit - 1) / mongoArrayMaxLength + 1;
			if (totalSkip > mongoArrayMaxLength) {
				skipNum = totalSkip % mongoArrayMaxLength;
			}else if(totalSkip < mongoArrayMaxLength){
				skipNum = totalSkip;
			}
		}
		
		return Arrays.asList(new Document(fieldName,executeQueryArray(table, pk, pkValue, fieldName, resultFields, skipNum, limitNum, kegNo)));
	}
	
	private List<Document> pagingArraysDesc(String table, String pk, String pkValue, String fieldName
			, List<String> resultFields, int pageNo, int pageSize){
		int limitNum = pageSize > mongoArrayMaxLength ? mongoArrayMaxLength : pageSize;
		int totalSkip = 0;
		List<Document> resultList = new ArrayList<>();
		int arrayLength = getArrayLenth(table, pkValue, fieldName);
//		arrayLength = 14;
		if (arrayLength == 0) {
			resultList.addAll(executeQueryArray(table, pk, pkValue, fieldName, resultFields, 0, pageSize, 0));
		}else{
			int kegNo = getTotalPage(arrayLength);
			long[][] arrayIndexs = getArrayIndexs(arrayLength,kegNo);
			if (pageNo > 1) {
				totalSkip = (pageNo-1) * pageSize;
			}
			Map<Integer,long[]> sliceMap = MongoSliceUtil.getSliceMap(arrayIndexs, totalSkip, limitNum);
			for (Entry<Integer, long[]> entry : sliceMap.entrySet()) {
				resultList.addAll(executeQueryArray(table, pk, pkValue, fieldName, resultFields, (int)entry.getValue()[0], (int)entry.getValue()[1], entry.getKey()));
			}
		}
		
		Collections.reverse(resultList);
		return Arrays.asList(new Document(fieldName,resultList));
	}
	
	private List<Document> executeQueryArray(String table, String pk, String pkValue, String fieldName
			, List<String> resultFields, int skipNum, int pageSize, int kegNo){
		Map<String,Object> slice =  (Map<String, Object>) Projections.slice(fieldName, skipNum, pageSize);
		Document projection = new Document(slice);
		if(CollectionUtils.isNotEmpty(resultFields)){
			for (String field : resultFields) {
				//如果append条件包含数组字段，slice将失效 TODO
				if (!field.equalsIgnoreCase(fieldName)) {
					projection.append(field, 1);
				}
			}
		}else{
			//不允许返回整行，必须传返回字段条件
//			return null;
		}
		projection.append("_id", 0);
		Bson filters = null;
		if (kegNo == 1) {			
			filters = Filters.and(Filters.eq(pk, pkValue),Filters.or(Filters.eq(kegNoKey, kegNo),Filters.exists(kegNoKey, false)));
		}else{
			filters = Filters.and(Filters.eq(pk, pkValue),Filters.eq(kegNoKey, kegNo));
		}
		MongoCursor<Document> curosr = mongoClient.getCollection(table).find(filters)
				.projection(projection)
				.iterator();
		List<Document> resultList = new ArrayList<>();
		while (curosr.hasNext()) {
			resultList.add(curosr.next());
		}
		if (CollectionUtils.isNotEmpty(resultList)&&resultList.get(0).get(fieldName)!=null) {
			return resultList.get(0).get(fieldName, List.class);
		}
		return resultList;
	}
	
	private long[][] getArrayIndexs(int arrayLength, int kegNo) {
		long[][] arrayIndexs = new long[kegNo][2];
		for (int i = 0; i < kegNo; i++) {
			if (i != kegNo - 1) {				
				arrayIndexs[i] = new long[]{i*mongoArrayMaxLength,(i +1)*mongoArrayMaxLength};
			}else{
				arrayIndexs[i] = new long[]{i*mongoArrayMaxLength,arrayLength};
			}
		}
		return arrayIndexs;
	}
	
	@Override
	public Map<String, Object> searchInEs(SearchCondition sc) {
		SearchAllResult result = null;
		long total = 0;
		int retryNum = 0;
		boolean isRetry = false;
		do{
			try {
				total=LppzEsComponent.getInstance().count(new String[]{sc.getIdxName()},
						sc.getTypes(),
						sc.getSearchQuery());
				
				result=LppzEsComponent.getInstance().search(sc);
			} catch (Exception e) {
				if(++retryNum<=esSearchMaxRetryLimit){
					isRetry = true;
				}else{
					logger.error(String.format("查询es异常，重试%d次后依然失败,params=%s",esSearchMaxRetryLimit,buildParamLogString(sc)),e);
					isRetry = false;
				}
			}
		}while(isRetry);
		
		Map<String,Object> rtnMap=Maps.newHashMap();
		if(null!=result && CollectionUtils.isNotEmpty(result.getResultSearchList())){
			rtnMap.put("rows", result.getResultSearchList());
			rtnMap.put("total", total);
			rtnMap.put("success", true);
			rtnMap.put("message", "查询成功");
		}else{
			rtnMap.put("success", false);
			rtnMap.put("message", "数据库查询异常,请联系管理员!");
		}
		return rtnMap;
	}
	
	private String buildParamLogString(SearchCondition sc) {
		if (sc == null) {
			return null;
		}
		return JSON.toJSONString(sc);
	}

	@Override
	public void saveInEs(String table, String body, String id, Class esDtoClass, boolean ... isUpdate) {
		String idxSurffix = null;
		boolean isRetry = false;
		String type = null;
		String index = null;
		
		DictModel dic = dictMap.get(table);
		if (dic != null) {
			idxSurffix = EsIndexUtils.getSurffixByMode(id, EsIndexUtils.APP_SEARCH_MOD);
//			idxSurffix = new SimpleDateFormat(dic.getEsModel().getSurffixFormat()).format(new Date());
			type = dic.getEsModel().getType();
			index = dic.getEsModel().getIndexName();
		}else if (esDtoClass == null) {
			throw new RuntimeException("发送es模版类型为空，不能发送");
		}else{
			type = esDtoClass.getName();
			index = table;
		}
		boolean update = false;
		if (isUpdate != null && isUpdate.length > 0) {
			update = isUpdate[0];
		}
		String esid = EsIndexUtils.buildId(id);
		do{
			try {
				if (esSendDisruptor) {
					logger.info("es send start esid={} table={} idxSurffix={}",esid,table,idxSurffix);
					String params=getEsParams(index, type, esid, idxSurffix, update?EsOperationEnums.Update:EsOperationEnums.Insert);
					incrSyncSender.sendMsgOrderly(body, esid, params);
					logger.info("es send over esid={}",esid);
				}else{
					logger.info("es save start esid={} table={} idxSurffix={}",esid,table,idxSurffix);
					EsOperationEnums esOpType = update?EsOperationEnums.Update:EsOperationEnums.Insert;
					EsModel esModel = buildEsModel(index, idxSurffix, type, esid, body, esOpType);
					LppzEsComponent.getInstance().batch(Arrays.asList(esModel));
					logger.info("es save over esid={}",esid);
				}
//				BaseEsLogEvent2Sender sender = getEsSender(index,type,idxSurffix);
//				sender.sendMsg(body, id);
			} catch (NullPointerException e) {
				isRetry = !isRetry;
				if(isRetry){
					logger.warn("写es获sender为空，重试一次",e);
				}else{
					logger.error("写es失败",e);
				}
			}
		}while(isRetry);
	}
	
	@Override
	public int deleteInEs(String table, String id) {
		String index = null;
		String idxSurffix = null;
		String type = null;
		int deleteCount = 0;
		
		DictModel dic = dictMap.get(table);
		if (dic != null) {
			idxSurffix = EsIndexUtils.getSurffixByMode(id, EsIndexUtils.APP_SEARCH_MOD);
			type = dic.getEsModel().getType();
			index = dic.getEsModel().getIndexName() + idxSurffix;
		}else {
			throw new RuntimeException("发送es模版类型为空，不能发送");
		}
		try {
			String esid = EsIndexUtils.buildId(id);
			DeleteResponse response = LppzEsComponent.getInstance().delete(index, type, esid);
			deleteCount = response.contextSize();
			logger.info("deleteInEs response={} index={},id={}",response==null?null:JSON.toJSONString(response),index,esid);
		} catch (Exception e) {
			logger.error(String.format("删除es索引异常,table=%s,type=%s,id=%s",table,type,id),e);
		}
		return deleteCount;
	}

	private EsModel buildEsModel(String index,String idxSurffix, String type, String esid,
			String body, EsOperationEnums esType) {
		return new EsModel(index + idxSurffix,
				type, esid,body, EsDMlEnum.valueOf(esType.name()));
	}

	@Override
	public DictModel getDictModel(String table) {
		return dictMap.get(table);
	}

	@Override
	public void addUnRead(String userId, String category, String msgId) {
		jedis.sadd(getReadSetKey(userId, category), msgId);
		incrBy(userId, category, 1);
	}
	
	private void incrBy(String userId, String category,int value){
		String cacheKey = getRedisKey(userId);
		String categoryCountKey = String.format(categoryUnReadCountKeyFormat, category);
		jedis.hincrBy(cacheKey, totalUnreadCountKey, 1);
		jedis.hincrBy(cacheKey, categoryCountKey, 1);
	}

	@Override
	public void readed(String userId, String category, String msgId) {
		jedis.srem(getReadSetKey(userId, category), msgId);
		incrBy(userId, category, -1);
//		String userTable = "SnsUser";
//		String idxSurffix = null;
//		String type = null;
//		String indexName = null;
//		DictModel dic = dictMap.get(userTable);
//		if (dic != null) {
//			idxSurffix = new SimpleDateFormat(dic.getEsModel().getSurffixFormat()).format(new Date());
//			type = dic.getEsModel().getType();
//			BaseEsLogEvent2Sender sender = getEsSender(indexName,type,idxSurffix);
			//TODO 修改es上私信状态
////			sender.sendMsg(body, id);
//		}
	}
	
	@Override
	public Set<String> getUnReadMsgs(String userId, String category) {
		return jedis.smembers(getReadSetKey(userId, category));
	}
	
	private String lockFormat = "app:::lock:::%s";
	@Override
	public String setNxEx(String key, String value, int expire) {
		return jedis.set(String.format(lockFormat, key), value, SetParams.setParams().nx().ex(expire));
	}
	
	@Override
	public Long delNxEx(String key) {
		return jedis.del(String.format(lockFormat, key));
	}
	
	private String getReadSetKey(String userId, String category){
		return String.format("appUnread:::%s:::%s",category, userId);
	}
	
	protected Object buildSource(Map<String, Object> map,String classType) {
		Class<?> clazz = null;
		Object source = null;
		if (classType == null)
			return null;
		try {
			clazz = Class.forName(classType);
			source = clazz.newInstance();
			List<Field> list=Lists.newArrayList();
			getAllClassFields(clazz,list);
			buildDTO(source,list,map);
			Method converter = clazz.getDeclaredMethod("converter");
			converter.invoke(source, map);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | NoSuchMethodException 
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return source;
	}
	
	private void getAllClassFields(Class<?> clazz,List<Field> list) {
		for(Field f:clazz.getDeclaredFields()){
			list.add(f);
		}
		if(clazz.getSuperclass()==Object.class){
			return;
		}
		
		getAllClassFields(clazz.getSuperclass(),list);
	}
	
	private List<String> simpleTypeList = Arrays.asList("short","int","long","double","float","boolean","char"
			,"Short","Integer","Long","Double","Float","Boolean","Char");
	
	private void buildDTO(Object source,List<Field> ff,Map<String,Object> map) throws IllegalAccessException{
		for (Field f : ff) {
			try {
				if (f.getName().equals(LppzConstants.seriname))
					continue;
				if (simpleTypeList.indexOf(f.getName()) == -1) {
					continue;
				}
				f.setAccessible(true);
				f.set(source, map.get(f.getName()));
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
				if (f.getType() == Long.class || f.getType() == long.class) {
					f.set(source, Long.valueOf(map.get(f.getName()).toString()));
				}
				if (f.getType() == Double.class
						|| f.getType() == double.class) {
					f.set(source, Double.valueOf(map.get(f.getName()).toString()));
				}
				if (f.getType() == Integer.class
						|| f.getType() == int.class) {
					f.set(source, Integer.valueOf(map.get(f.getName()).toString()));
				}
			}
		}
	}

	@Override
	public Object notiflyProcess(boolean write) throws Exception {
		//启动监听时不重复加载字典
		if (isInitMap.get()) {
			logger.info("start to refresh dict");
			initDictMap();
		}else{
			logger.info("start notifly");
			isInitMap.set(true);
		}
		return null;
	}
	
	@Override
	public void addListen(String key, ZookeeperProcessListen zkListen) {
		zkListen.addListen(key, this);
		zkListen.watchPath(key, key);
	}

}
