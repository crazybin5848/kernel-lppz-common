package com.lppz.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.client.LppzMongoClientMulti;
import com.lppz.mongoapi.dao.impl.MongoDaoImpl;
import com.lppz.mongoapi.enums.MongoUpdate;
import com.lppz.mongoapi.util.ListUtils;
import com.mongodb.MongoQueryException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/mongodb-mongo-spring.xml"})
public class DataCleaning{
	
	private Logger logger = LoggerFactory.getLogger(DataCleaning.class);
	
	@Resource(name="lppzMongoClientMulti")
	private LppzMongoClientMulti mongoClient;
	private Map<String,ArrayCheckBean> coutMap = new HashMap<>();
	private int maxLength = 200;
	
	@Resource
	private Jedis jedis;
	boolean isRefreshReids = false;
	boolean isRefreshMongo = false;
	boolean isDoRefresh = true;
	
	String kegNoName = "kegNo";
	private static List<ArrayTableBean> tables = new ArrayList<>();
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
	
	@BeforeClass
	public static void before(){
		ArrayTableBean userFollowed = new ArrayTableBean("SnsUserFollowed","userId",Arrays.asList("followed"));
		tables.add(userFollowed);
		ArrayTableBean activty = new ArrayTableBean("SnsActivity","activityId",Arrays.asList("storeIds","storeNames"));
		tables.add(activty);
		ArrayTableBean SnsImageLabel = new ArrayTableBean("SnsImageLabel","labelId",Arrays.asList("images"));
		tables.add(SnsImageLabel);
		ArrayTableBean SnsStatusCommentLike = new ArrayTableBean("SnsStatusCommentLike","commentId",Arrays.asList("likedUsers"));
		tables.add(SnsStatusCommentLike);
		ArrayTableBean SnsTopicJoinedUser = new ArrayTableBean("SnsTopicJoinedUser","topicId",Arrays.asList("joinedUsers"));
		tables.add(SnsTopicJoinedUser);
		ArrayTableBean SnsUserCollect = new ArrayTableBean("SnsUserCollect","userId",Arrays.asList("collectedStatuses"));
		tables.add(SnsUserCollect);
		ArrayTableBean SnsUserFollowing = new ArrayTableBean("SnsUserFollowing","userId",Arrays.asList("following"));
		tables.add(SnsUserFollowing);
		ArrayTableBean SnsUserlike = new ArrayTableBean("SnsUserlike","userId",Arrays.asList("likeedStatuses"));
		tables.add(SnsUserlike);
	}
	
	@Test
	public void clean(){
		if (isRefreshMongo) {
			updateAppDict();
		}
		cleanArray();
	}
	
	private void updateAppDict(){
		List<DictModel> dictModels = buildDictModel();
		Document query;
		Document update;
		UpdateOptions updateOptions = new UpdateOptions();
		updateOptions.upsert(true);
		for (DictModel model : dictModels) {
			query = new Document("table", model.getTable());
			update = new Document(MongoUpdate.set.getOp(), new Document("kegArrayField", model.getKegArrayFields()));
			mongoClient.getCollection("AppDict").updateOne(query, update,updateOptions);
		}
	}
	
	private List<DictModel> buildDictModel() {
		List<DictModel> dictModels = new ArrayList<>();
		DictModel model;
		for (ArrayTableBean table : tables) {
			model = new DictModel();
			model.setTable(table.getTableName());
			model.setKegArrayFields(table.getArrayFields());
			dictModels.add(model);
		}
		return dictModels;
	}

	private void cleanArray(){
		long startTime = System.currentTimeMillis();
		for (ArrayTableBean table : tables) {
			long tableStart = System.currentTimeMillis();
			int pageSize = 2000000;
			int resultSize = 0;
			int pageNo = 0;
			do{
				resultSize = check(table.getTableName(), table.getPk(), table.getArrayFields(), pageSize, pageNo++ * pageSize);
				logger.info("++++ table={} pageNO={} resultSize={} mapsize={}",table.getTableName(),pageNo,resultSize,coutMap.size());
			}while(resultSize == pageSize);
			logger.info("----------------query cost table={} {}s",table.getTableName(),(System.currentTimeMillis()-tableStart)/1000f);
		}
		logger.info("----------------query cost {}s map size={}",(System.currentTimeMillis()-startTime)/1000f,coutMap.size());
		if (isDoRefresh) {
			ExecutorService pool = new ThreadPoolExecutor(10, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10000000));
			final AtomicInteger count = new AtomicInteger();
			for (Entry<String, ArrayCheckBean> entry : coutMap.entrySet()) {
				final ArrayCheckBean value = entry.getValue();
				final String key = entry.getKey();
				pool.execute(new Runnable() {
					
					@Override
					public void run() {
						if (value.getLines() > 1 || value.getOverKegLength().size() > 0) {
							try {
								
								doClean(value);
							} catch (Exception e) {
								logger.error(e.toString(),e);
							}
						}else{
							if (isRefreshReids) {
								if (value.getArrayLength() > 0) {
//									refreshRedis(value.getTable(), 1, value.getPkValue(), value.getArrayField(), value.getArrayLength());
								}
							}
						}
						count.getAndIncrement();
						queue.add(key);
					}
				});
				logger.debug("key={} count={}",entry.getKey(), entry.getValue());
			}
			int waitCount = 0;
			while (count.get() < coutMap.size()) {
				try {
					logger.info("-----------------------{} < {} {}",count.get(),coutMap.size(),waitCount);
					Thread.sleep(1000L);
					if(waitCount++ > 300 && waitCount%10==0){
						printDif();
					}
				} catch (InterruptedException e) {
				}
			}
		}
		logger.info("do clean finish cost {}s",(System.currentTimeMillis()-startTime)/1000f);
	}
	
	private void printDif() {
		StringBuilder sb = new StringBuilder();
		for (String key : coutMap.keySet()) {
			if (!queue.contains(key)) {
				sb.append(key).append(";");
			}
		}
		logger.info("+++++++++++++++++not complete keys={}",sb.toString());
	}

	private void doClean(ArrayCheckBean value) {
		List<Document> resultList = null;
		List<Document> remainList = null;
		String table =value.getTable();
		String pk = value.getPk();
		String pkValue = value.getPkValue();
		String field = value.getArrayField();
		Document update = null;
		List<Document> queryresult = null;
		Document tmpDoc = null;
		if (value.getLines() > 1) {
			queryresult = query(table, new Document(pk, pkValue));
			if (queryresult.size() > 1) {
				logger.debug("line > 1 size={} value={} data={}",queryresult.size(),value,queryresult);
				int fieldCount = 0;
				boolean isDeleteEmptyArrayLine = true;
				int fullLineCount = 0;
				List<Document> subList = null;
				for (Document document : queryresult) {
					subList = (List<Document>)document.get(field);
					if (subList != null && subList.size() > 0) {
						isDeleteEmptyArrayLine=isDeleteEmptyArrayLine|true;
					}
					if (fieldCount==0) {
						fieldCount=document.size();
					}else{
						if (fieldCount!=document.size()) {
							isDeleteEmptyArrayLine=false;
						}
					}
					if (subList != null && subList.size()==maxLength) {
						fullLineCount++;
					}
				}
				Set<Document> array = new java.util.HashSet<>();
				Iterator<Document> it = queryresult.iterator();
				DeleteResult deleteResult = null;
				while (it.hasNext()) {
					tmpDoc = it.next();
					ObjectId id = (ObjectId) tmpDoc.get("_id");
					resultList = (List<Document>) tmpDoc.get(field);
					if (resultList==null) {
						continue;
					}
					if (resultList.size()==0 && isDeleteEmptyArrayLine && queryresult.indexOf(tmpDoc)<queryresult.size()-1) {
						if(isRefreshMongo){
							deleteResult = mongoClient.getCollection(table).deleteOne(tmpDoc);
						}
						it.remove();
						logger.info("table={} id={} pk={} pkvalue={} field={} array is empty remove line deleteCount={}",table,id,pk,pkValue,field,deleteResult);
					}else{
						array.addAll(resultList);
					}
				}
				
				if (array.size() > 0 && array.size()<=maxLength) {
					logger.info("merage table={} pk={} pkvalue={}",table,pk,pkValue);
					if (array.size() > ((List)queryresult.get(0).get(field)).size()) {
						queryresult.get(0).append(field, array);
					}
					queryresult.get(0).append(kegNoName, 1);
					for (int i = 1; i < queryresult.size(); i++) {
						logger.info("delete empty line {}",queryresult.get(i));
						
						if(isRefreshMongo){							
							mongoClient.getCollection(table).deleteOne(queryresult.get(i));
						}
					}
					if(isRefreshMongo) {
						mongoClient.getCollection(table).findOneAndReplace(new Document("_id", queryresult.get(0).get("_id")), queryresult.get(0));
					}
					if (isRefreshReids){
						refreshRedis(table, 1, pkValue, field, array.size());
					}
				}else if(array.size()>maxLength && fullLineCount!= queryresult.size() &&fullLineCount+1!=queryresult.size()){
					logger.info("oooooo length over maxlength");
				}else{
					if (isRefreshReids){
						refreshRedis(table, queryresult.size(), pkValue, field, array.size());
					}
				}
			}else{
				logger.debug("line == 1 {} data={}",value,queryresult);
			}
		}else if (value.getOverKegLength().size() > 0) {
			logger.info("length > {} {}",maxLength,value);
			queryresult = query(table, new Document(pk, pkValue));
			
			try {
				for (Document document : queryresult) {
					Object id = document.get("_id");
//					String clazz = document.getString("_class");
					String pkvalue = document.getString(pk);
					Integer keg = document.getInteger(kegNoName);
					resultList = (List<Document>) document.get(field);
					remainList = resultList.subList(maxLength, resultList.size());
					update = new Document().append(MongoUpdate.pullAll.getOp(), new Document(field, remainList));
					if (keg == null) {
						keg = 1;
						update.append(MongoUpdate.set.getOp(),new Document(kegNoName, keg));
					}
					
					List<List<Document>> list = ListUtils.splitArray(remainList, maxLength);
					int kegNext = keg + 1;
					List<Document> insertList = new ArrayList<Document>();
					for (List<Document> documents : list) {
						tmpDoc = new Document(pk, pkvalue).append(field, documents).append(kegNoName, kegNext++);
//						if (StringUtils.isNotBlank(clazz)) {
//							tmpDoc.append("_class", clazz);
//						}
						insertList.add(tmpDoc);
					}
					logger.info("single line over maxlength insert table={} lines= {}",table,insertList.size());
					if(isRefreshMongo){
						mongoClient.getCollection(table).insertMany(insertList);
					}
					if (update != null) {
						Document query = new Document(pk, pkvalue).append("_id", id);
						logger.info("single line over maxlength table={} query={} update={}",table,query,update);
						if(isRefreshMongo){
							mongoClient.getCollection(table).updateOne(query, update);						
						}
						
					}
					if (isRefreshReids) {
						refreshRedis(table, kegNext-1, pkValue, field, resultList.size());
					}
				}
			}catch (Exception e) {
				logger.error(e.toString(),e);
			}
		}
	}
	
	private List<Document> query(String table,Document query){
		FindIterable<Document> fi = mongoClient.getCollection(table).find(query);
		Document document = null;
		List<Document> result = new ArrayList<>();
		
		try {
			MongoCursor<Document> curosr = fi.iterator();
			while (curosr.hasNext()) {
				document = curosr.next();
				if (document != null) {
					result.add(document);
				}
			}
		}catch (Exception e) {
			logger.error("查询数据异常",e);
		}
		return result;
	}

	private int check(String table, String pk,List<String> arrayFields, int pageSize, int skip){
		List<Document> resultList = null;
		Document document = null;
		int resultSize = 0;
		FindIterable<Document> fi = mongoClient.getCollection(table).find();
		fi.limit(pageSize);
		fi.skip(skip);
		Object kegNoObj = null;
		Integer keg = null;
		try {
			MongoCursor<Document> curosr = fi.iterator();
			while (curosr.hasNext()) {
				document = curosr.next();
				resultSize++;
				Object id = document.get("_id");
				String pkvalue = document.getString(pk);
				kegNoObj = document.get(kegNoName);
				if (kegNoObj instanceof Double) {
					keg = ((Double) kegNoObj).intValue();
				}else if (kegNoObj instanceof Integer) {
					keg = (Integer) kegNoObj;
				}
				String field = arrayFields.get(0);
					String key = buildKey(table, pk, pkvalue, field);
					ArrayCheckBean check = coutMap.get(key);
					if (check == null) {
						check = new ArrayCheckBean(table, pk, pkvalue, field,1, new ArrayList<Integer>(),0);
					}else{
//						logger.info("----key ={}",key);
						check.setLines(check.getLines() + 1);
					}
					coutMap.put(key, check);
					resultList = (List<Document>) document.get(field);
					if (pkvalue==null) {
						logger.info("_id={} {} is empty",id,pk);
					}
					if (keg==null) {
//						logger.info("_id={} {}={} kegNo is empty",id,pk,pkvalue);
						Document query = new Document(pk, pkvalue).append("_id", id);
						Document update = new Document(MongoUpdate.set.getOp(),new Document(kegNoName, keg));
//						mongoClient.getCollection(table).updateOne(query, update);
					}
					if (resultList != null && resultList.size() > maxLength) {
//						logger.info("_id={} {}={} kegNo={} arraySize={} > {}",id,pk,pkvalue,keg,resultList.size(),maxLength);
						check.getOverKegLength().add(resultList.size());
					}else{
						if (resultList!= null) {
							check.setArrayLength(resultList.size());
						}
					}
			}
		}catch(MongoQueryException e){
			logger.error(String.format("pagesize=%d,skip=%d", pageSize, skip),e);
		}catch (Exception e) {
			logger.error(String.format("pagesize=%d,skip=%d", pageSize, skip),e);
		}
		return resultSize;
	}
	
	public String buildKey(String table,String pk, String pkValue,String arrayField){
		return table+"::"+pk+"::"+pkValue + "::"+arrayField;
	}
	
	private void refreshRedis(String table, int kegNo,String pkValue,String field, int arrayLength){
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String fieldKey = MongoDaoImpl.getArraySizeField(table, field);
		String kegField = MongoDaoImpl.getKegNoField(table,field);
		jedis.hset(redisKey, fieldKey, String.valueOf(arrayLength));
		String redisValue = jedis.hget(redisKey, fieldKey);
		jedis.hset(redisKey, kegField, String.valueOf(kegNo));
		logger.info("table={}, pkValue={}, field={},arrayLength={} ,redisValue={}",table,pkValue,field,arrayLength,redisValue);
//		String fieldOldKey = MongoDaoImpl.getArraySizeField(field);
//		jedis.hdel(redisKey, fieldOldKey);
	}

}
