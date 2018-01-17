package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.query.BoolSearchQuery;
import com.lppz.elasticsearch.query.Operator;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.RangeItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.query.fielditem.WildCardKvItem;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.elasticsearch.search.SortBy;
import com.lppz.mobile.model.es.ESSnsStatus;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.dao.MongoDao;
import com.lppz.oms.kafka.dto.OrderLogDto;

public class BaseLppzMongoApiTest extends SpringBaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(BaseLppzMongoApiTest.class);
	
	String table = "orders";
	@Resource
	private MongoDao mongoDao;
	
	@Test
	public void insertTest(){
		Executor pool = Executors.newFixedThreadPool(100);
		final AtomicInteger count = new AtomicInteger();
		int size =200;
		for (int i = 0; i < size; i++) {
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					Document document = getDocument(count.get());
					mongoDao.insert(table, document);
					count.incrementAndGet();
				}
			});
		}
		while (size > count.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(100000000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void pushTest(){
		String userid="1e06eqfrvrdqt";
		List<Object> documents=new ArrayList<Object>();
		for(int j=0;j<50;j++){
			Document obj=new Document("a",j).append("fuck", "fuck").
					append("ads", "qqq").append("231", 456);
			documents.add(obj);
		}
		mongoDao.push(table, "userid", userid, "toUid", documents);
		
	}
	
	@Test
	public void pushTest2(){
		table = "SnsUserFollowed";
		String userid="1117801409";
		List<Object> documents=new ArrayList<Object>();
		for(int j=0;j<5;j++){
			documents.add("a"+j);
		}
		mongoDao.push(table, "userId", userid, "simpleArray", documents);
	}
	
	@Test
	public void pullTest(){
		String userid="1e06eqfrvrdqt";
		mongoDao.pull(table, "userid", userid, "toUid.a",3);
//		mongoDao.pull("", "commentId", "7213803f-8523-4a21-af6f-2369e205a05a", "likedUsers.userId","3101344336");
		
	}
	
	@Test
	public void appPullTest(){
		String userid="1102066401";
		String tableName = "SnsUserFollowing";
		mongoDao.pull(tableName, "userId", userid, "following.userId","1146017268");
	}
	
	@Test
	public void updateOneTest(){
		String userid="1twtcd7725g1o";
		Document query = new Document("userid", userid);
		mongoDao.update(table, query, new Document("timeout_status", "1").append("service_fee", "0").append("pre_status", "0"),true,false);
	}
	
	@Test
	public void updateManyTest(){
		String userid="1twtcd7725g1o";
		Document query = new Document("userid", userid);
		mongoDao.update(table, query, new Document("timeout_status", "2").append("service_fee", "1").append("pre_status", "1"),true,true);
	}
	
	@Test
	public void incr(){
		String users = "1151607088,1150315658,1116867684,1148470055,1150507973,1149985547,1105487457,1150514905,1145155692,1132141867,1148307015,1151245708,1148706559,1149421621,1111762571,1100367082,1101015325,1149734975,1150315662,1111561247,1150542082,1100417133,1148216245,1122340215,1148562690,1150326598,1150153743,1148891998,1150564078,1140535742,1149863953,1103195985,1151421956,1150192875,1150542076,1150600990,1151608156,1101884180,1100117012,1151492790,1144897845,1150511929,1150576576,1149588063,1149385237,1150511923,1148450940,1137536757,1149385233,1151245726,1149946333,1149734951,1150542068,1150634130,1148897764,1151379330,1150511931,1150386737,1118496495,1148463649,1150537218,1105102544,1150673490,1151662004,1148629081,1100669611,1150673492,1148897766,1150281883,1150505235,1131509449,1118643825,1103971234,1149259606,1151019174,1114954082,1133702798,1150629724,1133484208,1149365095,1149601431,1148629056,1100286509,1150414527,1116563209,1150183195,1148874006,1151630167,1151647207,1119487087,1110726858,1150629730,1150250979,1150064867,1151245750,1150993988,1150486677";
		String[] userArray = users.split(",");
//		String userid="1119674178";
//		userid="1111851791";
		String pk = "userId";
		table = "SnsUser";
		for (String userid : userArray) {
			mongoDao.incr(table, pk, userid, "tipBlogCount",1);
			mongoDao.incr(table, pk, userid, "tipAmountCount",1);
			mongoDao.incr(table, pk, userid, "publishedCount",1);
			mongoDao.incr(table, pk, userid, "followedCount",1);
			mongoDao.incr(table, pk, userid, "collectCount",1);
			mongoDao.incr(table, pk, userid, "followedCount",1);
		}
		
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void incr2(){
		String id = "757ed2ab-2266-4113-933d-5aa122b4ce89";
//		String userid="1119674178";
//		userid="1111851791";
		String pk = "statusId";
		table = "SnsStatus";
		mongoDao.incr(table, pk, id, "collectedCount",1);
		try {
			Thread.sleep(1500000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getincr(){
		table = "SnsUserFollowing";
		System.out.println(mongoDao.getIncrValue("1119674178", "following"));
	}
	
	@Test
	public void find(){
		table = "SnsUser";
		Document query = new Document();
		query.append("userId", "1146020198");
		List<String> fieldNames = new ArrayList<>();
//		fieldNames.add("order_id");
//		fieldNames.add("toUid.a");
		List<String> excludefieldNames = new ArrayList<>();
		List<Document> result = mongoDao.findMongo(table, query, fieldNames, null, 0 ,2);
		Map<String,Object> tmpmap = null;
		for (Document document : result) {
//			Order order = mapping(document);
//			System.out.println(order);
		}
		logger.info("result:{}",result);
		logger.info("result size :{}",result.size());
		
	}
	
	@Test
	public void findCache(){
		String userid="1149904839";
		System.out.println(mongoDao.findCache(userid, null, null, null));
	}
	
	@Test
	public void saveCache(){
		String userid="1149904839";
		Map<String, Map<String, String>> userRedisMap = new HashMap<String, Map<String, String>>();
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("userid",userid);
		m.put("username", "");
		m.put("avatarimage", "");
		m.put("user_collect", String.valueOf(1));
		m.put("user_like", String.valueOf(2));
		//m.put(RedisKeys.USER_TIP, String.valueOf(getUserCollectCount(mSnsUser.getUserId())));
		m.put("user_flowing", String.valueOf(3));
		m.put("user_flowed", String.valueOf(3));
		//m.put(RedisKeys.USER_PUBLISHED, String.valueOf(getUserCollectCount(mSnsUser.getUserId())));
		userRedisMap.put(userid, m);
		
		mongoDao.saveInRedis(userRedisMap);
	}
	
	@Test
	public void pagingArray(){
		//SnsUserFollowing,userId,1140309144,2,20
		int pageNo = 34;
		int pageSize = 30;
		List<String> resultFields = new ArrayList<>();
//		resultFields.add("order_id");
		resultFields.add("userId");
//		resultFields.add("toUid.a");
//		resultFields = null;
		List<Document> result = mongoDao.pagingArrays("SnsUserFollowing", "userId", "1140309144", "following", resultFields, 2, 20, true);
//		List<Document> result = mongoDao.pagingArrays(table, "userid", "1twtcd7725g1o", "toUid",resultFields, pageNo, pageSize, true);
		System.out.println(result);
		System.out.println(result.size());
	}
	
	@Test
	public void delete(){
		Document filter = new Document("userid", "1twtcd7725g1o").append("kegNo", 7);
		mongoDao.delete(table, filter);
	}

	private Document getDocument(int i) {
		Document doc = new Document();
		doc.put("userid",
				Long.toString(Math.abs(UUID.randomUUID()
						.getMostSignificantBits()), 36));
		doc.put("order_id", i + "orderid");
		doc.put("company_id", 505 + i);
		doc.put("user_id", 180225429 + i);
		doc.put("fetcher_id", 59803 + i);
		doc.put("fetch_schedule_begin", new Date());
		doc.put("fetch_schedule_end", new Date());
		doc.put("sender_id", 59803 + i);
		doc.put("mail_no", "000000");
		doc.put("mail_type", "301");
		doc.put("order_code", "LP10012700003959" + i);
		doc.put("order_status", 30);
		doc.put("prev_order_id", 0);
		doc.put("trade_id", 2010012706189794L + i);
		doc.put("goods_remark", "");
		doc.put("receiver_name", " 凯撒");
		doc.put("receiver_wangwang_id", "sanglin01");
		doc.put("receiver_mobile_phone", "13021525841");
		doc.put("receiver_zip_code", "650045");
		doc.put("receiver_telephone", "13868117135");
		doc.put("receiver_county_id", 350102);
		doc.put("receiver_address",
				"福建省^^^福州市^^^鼓楼区^^^的萨芬萨芬萨芬的12号");
		doc.put("gmt_create", new Date());
		doc.put("gmt_modified", new Date());
		doc.put("status_reason", "");
		doc.put("logis_type", 0);
		doc.put("seller_wangwang_id", "tbtest943" + i);
		doc.put("seller_send_confirm", 0);
		doc.put("shipping", 2);
		doc.put("company_code", "");
		doc.put("taobao_trade_id", "2232358300" + i);
		doc.put("options", 2);
		doc.put("shipping2", 0);
		doc.put("order_source", 0);
		doc.put("status_date", new Date());
		doc.put("timeout_status", 2);
		doc.put("feature", "ip=127.0.0.1;SFID=");
		doc.put("service_fee", 0);
		doc.put("seller_store_id", "23100");
		doc.put("items_value", 23100);
		doc.put("pre_status", 0);
		doc.put("ticket_id", "");
		doc.put("tfs_url", "T1DoBbXctCXXXXXXXX");
		return doc;
	}
	
	
	
	@Test
	public void esSearchAppUserTest(){
		String idxName = "sns-user-*";
		String [] types = new String[]{"com.lppz.mobile.model.es.ESSnsUser"};
		
		int pageSize = 100;
		int page = 1;
		SearchQuery sq=buildUserSq();
		SearchCondition sc = new SearchCondition(sq,idxName,types);
		sc.setOffset(pageSize * (page-1));
		sc.setSize(pageSize);
		List<SortBy> sortList=Lists.newArrayList();
		SortBy sort=new SortBy("createdTime", SortOrder.DESC);
		sortList.add(sort);
//		sc.setSortList(sortList);
		Map<String,Object> resultMap = mongoDao.searchInEs(sc);
		if(resultMap.get("success")!=null && (Boolean)resultMap.get("success")){
			List<SearchResult> resultList = (List<SearchResult>) resultMap.get("rows");
			long total = (long) resultMap.get("total");
			if (CollectionUtils.isNotEmpty(resultList)) {
				List<Object> logs = new ArrayList<>();
				for (SearchResult result : resultList) {
					logs.add(result.getSource());
					System.out.println(result.getIndex() + " " + JSON.toJSONString(result.getSource()));
				}
				System.out.println("查询结果："+logs);
				System.out.println("查询结果："+logs.size());
			}
			System.out.println("查询总数："+total);
		}else{
			System.out.println("查询失败，失败消息："+resultMap.get("message"));
		}
	}
	@Test
	public void esSearchAppTest(){
		String idxName = "sns-topic-20170804";
		String [] types = new String[]{"com.lppz.mobile.model.es.ESSnsTopic"};
		
		int pageSize = 100;
		int page = 1;
		SearchQuery sq=buildTopicSq();
		SearchCondition sc = new SearchCondition(sq,idxName,types);
		sc.setOffset(pageSize * (page-1));
		sc.setSize(pageSize);
		List<SortBy> sortList=Lists.newArrayList();
		SortBy sort=new SortBy("createdTime", SortOrder.DESC);
		sortList.add(sort);
//		sc.setSortList(sortList);
		Map<String,Object> resultMap = mongoDao.searchInEs(sc);
		if(resultMap.get("success")!=null && (Boolean)resultMap.get("success")){
			List<SearchResult> resultList = (List<SearchResult>) resultMap.get("rows");
			long total = (long) resultMap.get("total");
			if (CollectionUtils.isNotEmpty(resultList)) {
				List<Object> logs = new ArrayList<>();
				for (SearchResult result : resultList) {
					logs.add(result.getSource());
				}
				System.out.println("查询结果："+logs);
				System.out.println("查询结果："+logs.size());
			}
			System.out.println("查询总数："+total);
		}else{
			System.out.println("查询失败，失败消息："+resultMap.get("message"));
		}
	}
	@Test
	public void esSearchAppStatusTest(){
		String idxName = "sns-status-*";
		String [] types = new String[]{"com.lppz.mobile.model.es.ESSnsStatus"};
		
		int pageSize = 10;
		int page = 1;
		SearchQuery sq=buildTopicSq();
		SearchCondition sc = new SearchCondition(sq,idxName,types);
		sc.setOffset(pageSize * (page-1));
		sc.setSize(pageSize);
		List<SortBy> sortList=Lists.newArrayList();
		sortList.add(new SortBy("isRecommmended", SortOrder.DESC));
		sortList.add(new SortBy("weight", SortOrder.DESC));
		sortList.add(new SortBy("createdTime", SortOrder.DESC));
		sc.setSortList(sortList);
		Map<String,Object> resultMap = mongoDao.searchInEs(sc);
		if(resultMap.get("success")!=null && (Boolean)resultMap.get("success")){
			List<SearchResult> resultList = (List<SearchResult>) resultMap.get("rows");
			long total = (long) resultMap.get("total");
			if (CollectionUtils.isNotEmpty(resultList)) {
				List<Object> logs = new ArrayList<>();
				for (SearchResult result : resultList) {
					logs.add(result.getSource());
					ESSnsStatus s = (ESSnsStatus) result.getSource();
					System.out.println("查询结果："+s.getIsRecommmended() + " " + s.getWeight() + " " + s.getCreatedTime());
				}
				System.out.println("查询结果："+logs.size());
			}
			System.out.println("查询总数："+total);
		}else{
			System.out.println("查询失败，失败消息："+resultMap.get("message"));
		}
	}
	
	private SearchQuery buildUserSq() {
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
//		String name = "*您咯咯*";
		SearchQuery csq=new SearchQuery();
		List<FieldItem> fieldItemList=Lists.newArrayList();
		csq.setFieldItemList(fieldItemList);
//		
		TermKvItem item = new TermKvItem("userId", "1151864989");
		fieldItemList.add(item);
//		
		searchQueryList.add(csq);
		sq.setSearchQueryList(searchQueryList);
		return sq;
	}
	
	private SearchQuery buildTopicSq() {
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
//		String name = "*您咯咯*";
//		SearchQuery csq=new SearchQuery();
//		List<FieldItem> fieldItemList=Lists.newArrayList();
//		csq.setFieldItemList(fieldItemList);
//		
//		WildCardKvItem item = new WildCardKvItem("name", name);
//		fieldItemList.add(item);
//		
//		searchQueryList.add(csq);
		sq.setSearchQueryList(searchQueryList);
		return sq;
	}

	@Test
	public void esSearchTest(){
		String idxName = "orderlog-*";
		String [] types = new String[]{OrderLogDto.class.getName()};
		
		int pageSize = 10;
		int page = 1;
		SearchQuery sq=buildOrderLogSq();
		SearchCondition sc = new SearchCondition(sq,idxName,types);
		sc.setOffset(pageSize * (page-1));
		sc.setSize(pageSize);
		List<SortBy> sortList=Lists.newArrayList();
		SortBy sort=new SortBy("operateTime", SortOrder.DESC);
		sortList.add(sort);
		sc.setSortList(sortList);
		Map<String,Object> resultMap = mongoDao.searchInEs(sc);
		if(resultMap.get("success")!=null && (Boolean)resultMap.get("success")){
			List<SearchResult> resultList = (List<SearchResult>) resultMap.get("rows");
			long total = (long) resultMap.get("total");
			if (CollectionUtils.isNotEmpty(resultList)) {
				List<OrderLogDto> logs = new ArrayList<>();
				for (SearchResult result : resultList) {
					logs.add((OrderLogDto)result.getSource());
				}
				System.out.println("查询结果："+logs);
				System.out.println("查询结果："+logs.size());
			}
			System.out.println("查询总数："+total);
		}else{
			System.out.println("查询失败，失败消息："+resultMap.get("message"));
		}
	}
	
	private SearchQuery buildOrderLogSq(){
		String orderId = "";
		String appName = "order-update";
		String DEFAULT_APP_NAME="order_claim";
		String returnCode = "";
		String outOrderId = "";
		String startTime = "";
		String endTime = "";
		String remark = "";
//		startTime = "2017-05-20 12:00:00";
//		endTime = "2017-07-22 12:00:00";
//		orderId = "JD170613057538052,JD170613057535858";
		remark = "订单";
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
		
		sq.setSearchQueryList(searchQueryList);
		
		if(StringUtils.isNotEmpty(orderId)){
			String[] orderIdArray=orderId.split(",");
			
			BoolSearchQuery bsq=new BoolSearchQuery(Operator.OR);
			
			
			for(String orderid:orderIdArray){
				bsq.addFileItem(new TermKvItem("orderId", orderid));
			}
			searchQueryList.add(bsq);
		}
		
		//或者
		if(StringUtils.isNotEmpty(appName)){
			
			BoolSearchQuery bsq=new BoolSearchQuery(Operator.OR);
			
			bsq.addFileItem(new TermKvItem("appName", appName));
			bsq.addFileItem(new TermKvItem("appName", DEFAULT_APP_NAME));
			
			searchQueryList.add(bsq);
		}
		
		if(StringUtils.isNotBlank(returnCode)){
			SearchQuery csq=new SearchQuery();
			List<FieldItem> fieldItemList=Lists.newArrayList();
			csq.setFieldItemList(fieldItemList);
			
			fieldItemList.add(new TermKvItem("returnCode", returnCode));
			
			searchQueryList.add(csq);
		}
		
		if(StringUtils.isNotEmpty(outOrderId)){
			SearchQuery csq=new SearchQuery();
			List<FieldItem> fieldItemList=Lists.newArrayList();
			csq.setFieldItemList(fieldItemList);
			
			fieldItemList.add(new TermKvItem("outOrderId", outOrderId));
			
			searchQueryList.add(csq);
		}
		
		//范围查询
		if (StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)) {
			SearchQuery csq=new SearchQuery();
			List<FieldItem> fieldItemList=Lists.newArrayList();
			csq.setFieldItemList(fieldItemList);
			
			RangeItem rangeItem = new RangeItem();
			rangeItem.setTermField("operateTime");
			rangeItem.setGtStr(startTime);
			rangeItem.setLtStr(endTime);
			fieldItemList.add(rangeItem);
			
			searchQueryList.add(csq);
			
		}
		//模糊匹配
		if (StringUtils.isNotBlank(remark)) {
			SearchQuery csq=new SearchQuery();
			List<FieldItem> fieldItemList=Lists.newArrayList();
			csq.setFieldItemList(fieldItemList);
			
			WildCardKvItem item = new WildCardKvItem("remark", remark);
			fieldItemList.add(item);
			
			searchQueryList.add(csq);
			
		}
		return sq;
	}
	
	@Test
	public void saveInEsTest() throws ClassNotFoundException{
//		String indexName = "sns-comment-";
//		String indexName = "SnsComment";
		String indexName = "snsStatus";
		String id = "0771becb-8cb6-41fa-a741-b90248eeb45a";
		JSONObject json = new JSONObject();
//		json.put("commentId", id);
		json.put("statusId", id);
		json.put("contentType", 2);
		mongoDao.saveInEs(indexName, json.toJSONString(), id, null,true);
		try {
			Thread.sleep(250000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveInEsMutilTest(){
		final String indexName = "orderlog-";
		final OrderLogDto dto = new OrderLogDto();
		dto.setAppName("order-update");
		dto.setChangeMoney("");
		String orderid = "xxxx00001";
		dto.setOrderId(orderid);
		dto.setOperateTime("2017-07-21 12:00:00");
		dto.setOutOrderId("yyyy000001");
		dto.setRemark("订单日志测试remark，模糊匹配分词处理等等还有什么悬崖");
		final String id = orderid;
		ExecutorService pool = Executors.newFixedThreadPool(500);
		for (int i = 0; i < 100; i++) {
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					mongoDao.saveInEs(indexName, JSON.toJSONString(dto), id, dto.getClass());
				}
			});
		}
		try {
			Thread.sleep(2500L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void initAppDict(){
		String table = "AppDict";
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		
		esModel.append("indexName", "status-");
		esModel.append("type", "com.lppz.app.Status");
		esModel.append("surffixFormat", "yyyy-MM-dd");
		esModel.append("mongoEsMap", new Document("statusId", "statusId"));
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
//		dicModel.append("table", "status");
		dicModel.append("table", "orders");
		dicModel.append("dayLiving", true);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		
		mongoDao.insert(table, dicModel);
		
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateAppDict(){
		String table="orders";
		Document query = new Document("table", table);
		mongoDao.update(table, query, new Document("dayLiving", true),true,false);
	}
	
	@Test
	public void getDict(){
		DictModel model = mongoDao.getDictModel("status");
		if (model != null && model.getEsModel() != null) {
			Map<String,String> map = model.getEsModel().getMongoEsMap();
			System.out.println(map.keySet());
			
		}
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteInEs(){
		String table = "SnsTopic";
		String type = "com.lppz.mobile.model.es.ESSnsTopic";
		String ids = "911ebdfe-3001-443b-8713-3f2ffe2dc69d";
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			mongoDao.deleteInEs(table, id);
		}
	}
	
	@Test
	public void getInEs(){
		try {
			String index = "app-hy";
			String type = "bq";
			String id = "AWBJgV2wLakX2xSV5yqP";
			SearchResult result = LppzEsComponent.getInstance().searchById(index, type, id);
			logger.info("result={}",result);
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	
}
