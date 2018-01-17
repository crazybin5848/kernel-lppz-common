package com.lppz.mongoapi.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.elasticsearch.action.bulk.BulkItemResponse.Failure;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.disruptor.BaseEsLogEventCRUD2Sender;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.bean.DictMongoRedisModel;
import com.lppz.mongoapi.bean.MongoPullFixArrayModel;
import com.lppz.mongoapi.bean.MongoPullFixModel;
import com.lppz.mongoapi.bean.SyncRedisToEsModel;
import com.lppz.mongoapi.client.LppzMongoClientMulti;
import com.lppz.mongoapi.disruptor.MongoBatchInsertSender;
import com.lppz.mongoapi.disruptor.MongoBathInsertHandler;
import com.lppz.mongoapi.enums.EsOperationEnums;
import com.lppz.mongoapi.util.EsIndexUtils;
import com.lppz.util.curator.comm.NotiflyService;
import com.lppz.util.curator.zookeeper.process.ZkMultLoader;
import com.lppz.util.disruptor.BaseErrorHandler;
import com.lppz.util.disruptor.sender.BaseClusterdDisruptorSender;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

@SuppressWarnings("unchecked")
public abstract class BaseMongoDao extends ZkMultLoader implements NotiflyService, InitializingBean{
	
	private static final Logger logger = LoggerFactory.getLogger(BaseMongoDao.class);
	public static final String keyFormat = "app:::%s";
//	public static final String cacheKeyFormat = "appcache:::%s";
	public static final String userDayBehaveKeyFormat = "app:::%s:::%s";
	public static final String defaultKeyFormat = "%s:::%s";
	public static final String filedFormatKeg = "app:::%s:::keg";
	public static final String filedFormatKegNew = "app:::%s:::%s:::keg";
	public static final String filedFormatArraySize = "app:::%s:::size";
	public static final String kegNoKey = "kegNo";
	public static final String redisSyncKey = "app:::redisSyncKey";
	public static final String totalUnreadCountKey = "msg_unReadCount";
	public static final String categoryUnReadCountKeyFormat = "msg_%s_unReadCount";
	public static final String fixKegJedisQueue = "fixKegJedisQueue";
	
	protected BaseClusterdDisruptorSender<String> incrSyncSender = null;
	
	protected BaseClusterdDisruptorSender<Map<String,Document>> sender;
	
	protected Map<String,DictModel> dictMap = new HashMap<>();
	
	@Value("${mongo.insert.batch.size:1000}")
	private int size;
	@Value("${mongo.insert.batch.timeStep:100}")
	private int timeStep;
	@Value("${mongo.insert.batch.threadNum:5}")
	private int threadNum;
	@Value("${mongo.push.array.max.length:1000}")
	protected int mongoArrayMaxLength;
	@Value("${es.agg.row.max.limit:1000000}")
	private int esAggRowMaxLimit;
	@Value("${rocketmq.nameSrvAddress}")
	protected String nameSrvAddress;
	@Value("${app.dictTableName:AppDict}")
	private String dictTable;
	@Value("${mongo.pull.fix.delay.second:20}")
	private int arrayFixDelaySecond;
	
	@Value("${mongo.pushpull.isASyncPull:true}")
	protected boolean isASyncPull;
	@Value("${mongo.pushpull.timeLimit:5000}")
	private long timeLimit;
	
	@Resource(name="lppzMongoClientMulti")
	protected LppzMongoClientMulti mongoClient;
	
	@Resource
	protected Jedis jedis;
	
	public abstract List<Document> findMongo(String table, Document query, List<String> fieldNames, Document sort, Integer skip, Integer limit);
	public abstract void push(String table, String pk, String pkValue, String arrayField, List<Object> documents);

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	private void init(){
		//初始化批量写mongodb的disruptor sender
		final MongoBathInsertHandler handler = new MongoBathInsertHandler(mongoClient);
		sender = BaseClusterdDisruptorSender.build(new BaseEvent2SenderFactory<Map<String,Document>>() {
			
			@Override
			public BaseEvent2Sender<Map<String,Document>> build() {
				return (BaseEvent2Sender<Map<String,Document>>) MongoBatchInsertSender.create(handler, size, timeStep);
			}
		},threadNum);
		
		//初始化redis同步es sender
		buildSyncSender();
		
		//初始化字典数据
		initDictMap();
		
		//启动从redis同步inc操作到es
		syncRedisToES();
		
		if (isASyncPull) {
			//启动定时补齐数组线程
			startFixArrayKeg();
		}
	}
	
	protected void initDictMap() {
		Document query = new Document();
		DictModel model = null;
		List<Document> resultList = findMongo(dictTable, query, null, null, null, null);
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (Document d : resultList) {
				model = DictModel.build(d);
				dictMap.put(model.getTable(), model);
			}
		}
	}
	
	protected void syncRedisToES() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				startRedisToEsThread();
			}
		}).start();
	}
	
	private void startRedisToEsThread() {
		Set<String> syncSet = new HashSet<>();
		long startTime = System.currentTimeMillis();
		String value = null;
		while (true) {
			value = jedis.lpop(redisSyncKey);
			if (StringUtils.isNotBlank(value)) {
				syncSet.add(value);
			}else{
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
				}
			}
			if (syncSet.size() >= 1000) {
				doSyncEs(syncSet);
				startTime = System.currentTimeMillis();
			}else if((System.currentTimeMillis() - startTime) >= 5000 && syncSet.size() > 0){
				doSyncEs(syncSet);
				startTime = System.currentTimeMillis();
			}
		}
		
	}

	private void buildSyncSender(){
		incrSyncSender=BaseClusterdDisruptorSender.build(new BaseEvent2SenderFactory<String>(){
	        @SuppressWarnings("rawtypes")
			@Override
	        public BaseEvent2Sender<String> build() {
	          BaseErrorHandler<EsModel> errorHandler=new BaseErrorHandler<EsModel>(){
	            @Override
	            public void handler(EsModel u) {
	            	logger.warn("es 写入异常 model={}",u);
	            	Failure f = u.getFailure();
	            	if (f != null) {
	            		try {
	            			if (f.getStatus().equals(RestStatus.NOT_FOUND)&& EsDMlEnum.Update.equals(u.getEnumType())) {
	            				logger.info("修改索引不存在，执行插入操作 model : {} ",u);
	            				LppzEsComponent.getInstance().insert(u.getIndex(), u.getType(), u.getId(), u.getJsonSource());
	            			}
						} catch (Exception e) {
							logger.error("重新插入es失败，msg="+u,e);
						}
					}
	            }

	            @Override
	            public int getRetryCount() {
	              return 0;
	            }

	            @Override
	            public boolean isLogInfo() {
	              return true;
	            }
	          };
	          return BaseEsLogEventCRUD2Sender.create(10000, 1000,errorHandler);
	        }
	      },10);
	}
	
	private void doSyncEs(Set<String> syncSet) {
		String esDtoClass = null;
		String index = null;
		String idxSurffix = null;
		for (String modelStr : syncSet) {
			SyncRedisToEsModel model = JSON.parseObject(modelStr, SyncRedisToEsModel.class);
			JSONObject json = new JSONObject();
			String table = model.getTable();
			String pkValue = model.getPkValue();
			String column = model.getColumn();
			DictModel dict = dictMap.get(table);
			if (dict != null) {
				esDtoClass = dict.getEsModel().getType();
				idxSurffix = EsIndexUtils.getSurffixByMode(pkValue, EsIndexUtils.APP_SEARCH_MOD);
//				idxSurffixFormat=dict.getEsModel().getSurffixFormat();
//				if (StringUtils.isNotBlank(idxSurffixFormat)) {
//					idxSurffix = new SimpleDateFormat(idxSurffixFormat).format(new Date());
//				}else{
//					idxSurffix = new SimpleDateFormat("yyyy").format(new Date());
//				}
				index = dict.getEsModel().getIndexName();
				String[] mixField = column.split("_");
				String redisValue = jedis.hget(getRedisKey(pkValue), column);
				//字典表匹配复合型的field属性
				if (mixField.length > 1) {
					DictMongoRedisModel redisModel = dict.getRedisModel();
					String mixPk = redisModel.getMongoRedisMixSubPk();
					String mixColumn = redisModel.getMongoRedisMixSubCloumn();
					if (StringUtils.isNotBlank(mixPk)
							&&StringUtils.isNotBlank(mixColumn)) {
						json.put(mixColumn, redisValue);
					}
				}else{
					json.put(model.getColumn(), redisValue);
				}
//				sender = getEsSender(index, esDtoClass, idxSurffix);
//				sender.sendMsg(json.toJSONString(), pkValue);
				String esId = EsIndexUtils.buildId(pkValue);
			    String params= getEsParams(index, esDtoClass, esId, idxSurffix, EsOperationEnums.Update);
				incrSyncSender.sendMsgOrderly(json.toJSONString(), esId, params);
				logger.debug("发送es"+json.toJSONString() + " " + pkValue);
			}
		}
		syncSet.clear();
	}
	
	
	protected String getEsParams(String index, String type, String id, String idxSurffix, EsOperationEnums operationEnums){
		BaseEsParamTypeEvent eventType=new BaseEsParamTypeEvent();
		eventType.setEsOperType(operationEnums.name());
		eventType.setIdxName(index);
		eventType.setTypeName(type);
		eventType.setId(id);
		eventType.setIdxCurrday(idxSurffix);
	   return JSON.toJSONString(eventType);
	}
	
	private void startFixArrayKeg() {
		ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
		scheduled.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				long start = System.currentTimeMillis();
//				doFixByRedis();
				doFixByRedisGet();
				float cost = (System.currentTimeMillis()-start)/1000f;
				if (cost > 1) {
					logger.warn("end doFixByRedis cost over 1s cost={}s",cost);
				}
			}

		},arrayFixDelaySecond, arrayFixDelaySecond, TimeUnit.SECONDS);
	}

	protected void doFixByRedisGet() {
		Map<byte[],byte[]> pullKeys = jedis.hgetAll(fixKegJedisQueue.getBytes());
		if (pullKeys == null ||pullKeys.isEmpty()) {
			logger.info("doFixByRedis over");
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("需要补齐的行有{}",hGetFromRedis(fixKegJedisQueue.getBytes()));
		}
		
		for (Entry<byte[], byte[]> entry : pullKeys.entrySet()) {
			doFixByRedisGet(entry.getKey(), Long.valueOf(new String(entry.getValue())), timeLimit);
		}
	}
	
	protected void doFixByRedisGet(byte[] field, long value, long timeLimit){
		long currentTime = System.currentTimeMillis() - value;
		if (currentTime > timeLimit) {
			if (logger.isDebugEnabled()) {
				logger.debug("doFixByRedisGet start to popAndFix field={},timeLimit={},time={}",new String(field),timeLimit,currentTime);
			}
			popAndFix(field);
			jedis.hdel(fixKegJedisQueue.getBytes(), field);
		}
	}
	
	protected void popAndFix(byte[] record){
		byte[] keg = null;
		String kegStr = null;
		String key = new String(record);
		if (logger.isDebugEnabled()) {
			logger.debug("开始补齐 key={}",key);
		}
		MongoPullFixModel fixModel = new MongoPullFixModel(record);
		if (logger.isDebugEnabled()) {			
			logger.debug("需要补齐的桶有{}",getMembersFromRedis(record));
		}
		while (true) {
			try {
				keg = jedis.spop(record);
				if (keg == null ||keg.length==0) {
					logger.info("key={} is empry",key);
					break;
				}
				kegStr = new String(keg);
				if (logger.isDebugEnabled()) {
					logger.debug("开始补齐 key={} keg={} ",key,kegStr);
				}
				int maxKeg = getMaxKegNo(fixModel.getTable(),fixModel.getPkValue(), fixModel.getFieldName());
				doFix(fixModel.getTable(), fixModel.getPk(), fixModel.getPkValue(), fixModel.getArrayField(), new Integer(new String(keg)), maxKeg);
			} catch (Exception e) {
				logger.error(String.format("补齐数组异常 redis key=%s,keg=%s", key,kegStr),e);
				break;
			}
		}
	}
	
	/**
	 * 遍历需要补位的桶，实现补位
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param arrayField
	 * @param kegNo
	 * @param maxKegNo
	 */
	protected void doFix(String table, String pk, String pkValue,
			String arrayField, Integer kegNo, int maxKegNo) {
		if (logger.isDebugEnabled()) {			
			logger.debug("doFix table={}, pk={}, pkValue={},arrayField={},kegNo={},maxKegNo={}",table,pk,pkValue,arrayField,kegNo,maxKegNo);
		}
		moveFristOfArray(table, pk, pkValue, arrayField, kegNo, maxKegNo);
	}
	
	/**
	 * 查询下一桶的第一个元素添加到当前桶，并删除下一桶中的此元素
	 * @param table
	 * @param pk
	 * @param pkValue
	 * @param arrayField
	 * @param kegNo
	 */
	private void moveFristOfArray(String table, String pk, String pkValue,
			String arrayField, int kegNo, int maxKegNo){
		if (logger.isDebugEnabled()) {			
			logger.debug("moveFristOfArray table={}, pk={}, pkValue={}, arrayField={}, kegNo={}",table,pk,pkValue,arrayField,kegNo);
		}
		String[] fields = arrayField.split("\\.");
		String field = fields[0];
		Bson filters = Filters.and(Filters.eq(pk, pkValue),Filters.eq(kegNoKey, kegNo));
		List<Document> currentKegArray =getArrayLength(table, field, filters);
		int currentKegArraySize = currentKegArray==null?0:currentKegArray.size();
		int fixLength = mongoArrayMaxLength - currentKegArraySize;
		List<Document> opList = null;
		UpdateResult result = null;
		//当前桶未满，从后向前补齐
		if (fixLength > 0) {
			//如果尾桶不足，从后往前补齐
			while (fixLength > 0) {
				//获取最后一个有数组集合的桶
				MongoPullFixArrayModel model = getLastKegArray(table, field, pk, pkValue, kegNo, maxKegNo, fixLength);
				try {
					if (model != null) {
						Bson nextFegfilters = Filters.and(Filters.eq(pk, pkValue),Filters.eq(kegNoKey, model.getKegNo()));
						List<Document> arrays = model.getArrays();
						if (arrays != null) {
							if (arrays.size() >= fixLength) {
								opList = arrays.subList(0, fixLength);
							}else{
								opList = arrays;
							}
							result = mongoClient.getCollection(table).updateOne(filters, Updates.pushEach(field, opList));
							result = mongoClient.getCollection(table).updateOne(nextFegfilters, Updates.pullAll(field, opList));
							fixLength -= opList.size();
						}
					}else{
						break;
					}
				} catch (Exception e) {
					logger.error(e.toString(),e);
				}
			}
			//当前桶长度超过限制，通过push方法在尾部添加
		}else if (fixLength < 0) {
			opList = currentKegArray.subList(mongoArrayMaxLength, currentKegArraySize);
			mongoClient.getCollection(table).updateOne(filters, Updates.pullAll(field, opList));
			push(table, pk, pkValue, arrayField, convertToObject(opList));
			//push会在redis中增加数组长度，这里push成功后减去重复++的长度
			subArrayLength(table, pk, pkValue, field,opList.size(),kegNo);
		}
	}
	
	protected void subArrayLength(String table, String pk, String pkValue,
			String field, int size, int kegNo) {
		String redisKey = getRedisKey(pkValue);
		String fieldKey = getArraySizeField(table, field);
//		String kegField = getKegNoField(table, field);
		//modifiedCount表示变更的行，如果有变更的行，认为全部成功，redis中数组长度减去values的长度
		long incrResult = jedis.hincrBy(redisKey, fieldKey, size*-1);
		if (incrResult <= 0) {
			if (kegNo == 1) {
				jedis.hset(redisKey, fieldKey, "0");
			}else{
				jedis.hset(redisKey, fieldKey, String.valueOf(mongoArrayMaxLength+incrResult));
//				jedis.hset(redisKey, kegField, String.valueOf(--kegNo));//TODO 并发下测试会不会出现不一致情况导致的桶号与行不一致
			}
		}
	}
	
	protected void hsetPullKegToRedis(List<Document> resultList, String table, String pk, String pkValue, String arrayField){
		Integer kegNo = null;
		String fixKegKey = null;
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (Document document : resultList) {
				kegNo = document.getInteger(kegNoKey, 1);
				fixKegKey = MongoPullFixModel.buildFixKegKey(table, pk,pkValue,arrayField);
				if (logger.isDebugEnabled()) {					
					logger.debug("hset fixKegKey={} fixKeg= {}",fixKegKey,kegNo);
				}
				//向被pull行集合中添加有变更的桶号信息
				jedis.sadd(fixKegKey.getBytes(), String.valueOf(kegNo).getBytes());
			}
			//向redis集合中添加被pull行标识，未避免桶添加redis和行被补齐线程先取出的时间差导致补齐线程获取不到桶pull数据，行数据添加放到桶添加后面
			jedis.hsetnx(fixKegJedisQueue.getBytes(), fixKegKey.getBytes(), String.valueOf(System.currentTimeMillis()).getBytes());
			doFixByRedisGet(fixKegKey.getBytes(), System.currentTimeMillis(), timeLimit);
		}else{
			logger.info("pull result is empty");
		}
	}

	private List<Object> convertToObject(List<Document> opList) {
		List<Object> list = new ArrayList<Object>();
		list.addAll(opList);
		return list;
	}

	/**
	 * 从后往前获取需要补齐的元素列表
	 * @param table
	 * @param field
	 * @param pk
	 * @param pkValue
	 * @param kegNo
	 * @param maxKeg
	 * @param fixLength
	 * @return
	 */
	private MongoPullFixArrayModel getLastKegArray(String table, String field, String pk, String pkValue, int kegNo, int maxKeg, int fixLength ){
		Map<String,Object> slice =  (Map<String, Object>) Projections.slice(field, 0, fixLength);
		Document projection = new Document(slice);
		//	projection.append(fields[0], 1);
		projection.append("_id", 0);
		projection.append(kegNoKey, 0);
		projection.append(pk, 0);
		Document document = null;
		do {
			Bson NextFegfilters = Filters.and(Filters.eq(pk, pkValue),Filters.eq(kegNoKey, maxKeg));
			MongoCursor<Document> curosr = mongoClient.getCollection(table).find(NextFegfilters).projection(projection).iterator();
			while (curosr.hasNext()) {
				document = curosr.next();
			}
			
			if (document != null&&!document.isEmpty()) {
				List<Document> tmpList = (List<Document>) document.get(field);
				if (CollectionUtils.isNotEmpty(tmpList)) {
					return new MongoPullFixArrayModel(maxKeg,tmpList);
				}
			}
		} while (kegNo < --maxKeg);
		
		return null;
	}
	
	private List<Document> getArrayLength(String table, String field, Bson filters){
		Document ducoment = null;
		MongoCursor<Document> curosr = mongoClient.getCollection(table).find(filters).iterator();
		while (curosr.hasNext()) {
			ducoment = curosr.next();
		}
		if (ducoment != null) {
			List<Document> arrays = (List<Document>) ducoment.get(field);
			return arrays;
		}
		
		return null;
	}

	public List<List<Object>> splitArray(List<Object> remainArray) {
		List<List<Object>> resultList = new ArrayList<>();
		if (remainArray == null || remainArray.size() == 0 || mongoArrayMaxLength < 1) {  
			return null;  
		}  
			  
		int size = remainArray.size();  
		int count = (size + mongoArrayMaxLength - 1) / mongoArrayMaxLength;  
		
		for (int i = 0; i < count; i++) {  
			List<Object> subList = remainArray.subList(i * mongoArrayMaxLength, ((i + 1) * mongoArrayMaxLength > size ? size : mongoArrayMaxLength * (i + 1)));  
			resultList.add(subList);  
		}  
			  
		return resultList;
	}
	
	protected String getMembersFromRedis(byte[] record) {
		 Set<byte[]> kegs = jedis.smembers(record);
		 StringBuilder sb = new StringBuilder();
		 for (byte[] bytes : kegs) {
			 sb.append(new String(bytes)).append(",");
		}
		return sb.toString();
	}
	
	protected String hGetFromRedis(byte[] record) {
		Map<byte[], byte[]> kegs = jedis.hgetAll(record);
		StringBuilder sb = new StringBuilder();
		for (Entry<byte[], byte[]> bytes : kegs.entrySet()) {
			sb.append(new String(bytes.getKey())).append("=").append(new String(bytes.getValue())).append(",");
		}
		return sb.toString();
	}
	protected int getArrayLenth(String table, String pkValue, String fieldName){
		String redisKey = getRedisKey(pkValue);
		String fieldKey = getArraySizeField(table,fieldName);
		String arrayLengthV = jedis.hget(redisKey, fieldKey);
		long arrayLength = StringUtils.isBlank(arrayLengthV)?0:Long.valueOf(arrayLengthV);
		return (int) arrayLength;
	}
	
	@Deprecated
	protected int getArrayLenth(String pkValue, String fieldName){
		String redisKey = getRedisKey(pkValue);
		String fieldKey = getArraySizeField(fieldName);
		String arrayLengthV = jedis.hget(redisKey, fieldKey);
		long arrayLength = StringUtils.isBlank(arrayLengthV)?0:Long.valueOf(arrayLengthV);
		return (int) arrayLength;
	}
	
	protected int getMaxKegNo(String table, String pkValue, String fieldName){
		String redisKey = getRedisKey(pkValue);
		String kegNoField = getKegNoField(table, fieldName);
		String kegNoV = jedis.hget(redisKey, kegNoField);
		long kegNo = StringUtils.isBlank(kegNoV)?1:Long.valueOf(kegNoV);
		return (int) kegNo;
	}
	
	@Deprecated
	protected int getMaxKegNo(String pkValue, String fieldName){
		String redisKey = getRedisKey(pkValue);
		String kegNoField = getKegNoField(fieldName);
		String kegNoV = jedis.hget(redisKey, kegNoField);
		long kegNo = StringUtils.isBlank(kegNoV)?1:Long.valueOf(kegNoV);
		return (int) kegNo;
	}
	
	protected int getTotalPage(long arrayLength){
		return (int)(arrayLength-1)/mongoArrayMaxLength + 1;
	}
	
	
	public static String getRedisKey(String key){
		return String.format(keyFormat, key);
	}
	
	@Deprecated
	public static String getKegNoField(String key){
		return String.format(filedFormatKeg, key);
	}
	public static String getKegNoField(String table,String key){
		return String.format(filedFormatKegNew, table, key);
	}
	
	public static String getArraySizeField(String table, String key){
		return String.format(userDayBehaveKeyFormat, table, key);
	}
	
	@Deprecated
	public static String getArraySizeField(String key){
		return String.format(keyFormat, key);
	}
}
