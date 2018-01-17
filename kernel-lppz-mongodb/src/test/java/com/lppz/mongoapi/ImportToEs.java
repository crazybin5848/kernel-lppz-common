package com.lppz.mongoapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import kafka.utils.Json;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.disruptor.BaseEsLogEvent2Sender;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.result.SearchAllResult;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mobile.model.es.ESSnsStatus;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.dao.MongoDao;
import com.lppz.mongoapi.util.EsIndexUtils;

public class ImportToEs extends SpringBaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(ImportToEs.class);
	int mongoPageSize = 70000;
	@Resource
	private MongoDao mongoDao;
	
	private java.util.Set<String> userIDSet = new HashSet<>();

	
	@Test
	public void mongoToEs() throws InterruptedException{
		int i =10;
		String[] tables = new String[]{
//				"ESSnsUser"
//				,
//				"ESSnsTopic"
//				,
//				"ESSnsActivity"
//				,
//				"ESSnsGroup"
//				,
//				"ESSnsStatus"
//				,
//				"ESSnsComment"
				};
		for (String table : tables) {
			mongoToES(table);
			Thread.sleep(10000L);
		}
		
		while(i-->0){
			Thread.sleep(2000L);
		}
	}
	
	@Test
	public void appmongoToEs() throws InterruptedException{
		int i =10;
		boolean isUpdate = false;
		String[] tables = new String[]{
//				"SnsUser"
//				,
//				"SnsTopic"
//				,
//				"SnsActivity"
//				,
//				"SnsGroup"
//				,
				"snsStatus"
//				,
//				"SnsStatusComment"
		};
		for (String table : tables) {
			appMongoToES(table, isUpdate);
			Thread.sleep(10000L);
		}
		
		while(i-->0){
			Thread.sleep(2000L);
		}
	}
	
	public void mongoToES(String estable) throws InterruptedException{
		Document query =new Document();
//		query.append("userId", "1156129273");
		List<Document> resultList = null;
		resultList = mongoDao.findMongo(estable, query, null, null, 0, mongoPageSize);
//		resultList = new ArrayList<>();
		inertEsOfTable(estable,resultList);
	}
	
	private void appMongoToES(String table, boolean ... isUpdate) throws InterruptedException{
		Document query =buildQuery();
		List<Document> resultList = null;
		resultList = mongoDao.findMongo(table, query, null, null, 0, mongoPageSize);
//		resultList = new ArrayList<>();
		inertEsOfAppTable(table,resultList, isUpdate);
	}
	
	private Document buildQuery(){
		Document query =new Document();
//		List<String> ids = new ArrayList<>();
//		ids.add("1152594119");
//		ids.add("1156129359");
//		ids.add("1156129399");
//		ids.add("1156129391");
//		ids.add("1156129407");
//		ids.add("1135235528");
//		ids.add("1156129447");
//		ids.add("1156129471");
//		ids.add("1156129473");
//		ids.add("1156129477");
//		ids.add("1156129479");
//		ids.add("1156129485");
//		ids.add("1156129489");
//		ids.add("1156129563");
//		ids.add("1156129601");
//		ids.add("1156129619");
//		ids.add("1156129623");
//		ids.add("1105567730");
//		ids.add("1156129659");
//		ids.add("1152766823");
//		ids.add("1156129725");
//		ids.add("1156118845");
//		query.append("userId", new Document("$in", ids));
//		query.append("statusId", "a0ea0609-e9bf-445b-9a64-fa0ccabcfe5c");
		return query;
	}
	
	private void inertEsOfAppTable(String table, List<Document> resultList, boolean ... isUpdate) throws InterruptedException {
		Map<String,String> esJsonMap = null;
		String pk = null;
		if ("SnsUser".equals(table)) {
			pk = "userId";
			esJsonMap = buildUser(resultList);
		}else if("SnsTopic".equals(table)){
			pk = "topicId";
			esJsonMap = buildTopic(resultList);
		}else if("SnsActivity".equals(table)){
			pk = "activityId";
			esJsonMap = buildActivity(resultList);
		}else if("SnsGroup".equals(table)){
			pk = "groupId";
			esJsonMap = buildGroup(resultList);
		}else if("snsStatus".equals(table)){
			pk = "statusId";
			esJsonMap = buildStatus(resultList);
		}else if("SnsStatusComment".equals(table)){
			pk = "commentId";
			esJsonMap = buildComment(resultList);
		}
		
		if (esJsonMap != null) {
//			List<String> failIds = failids();
//			for (Entry<String, String> entry : esJsonMap.entrySet()) {
//				logger.debug("{}={{}}",entry.getKey(), entry.getValue());
////				if (failIds.indexOf(entry.getKey()) > -1) {
////					logger.info("{}  {}",entry.getKey(), entry.getValue());
////				}
//				insertEs(table, entry.getValue(), entry.getKey(), isUpdate);
//			}
			logger.info("over set size {}",userIDSet.size());
			userIDSet.clear();
			List<String> difIds = compareIds(table, pk);
			logger.info("{} 未导入es的id size {} {}",table,difIds.size(),difIds);
		}
	}
	private List<String> failids(){
		String s = "52bd680c-3462-4138-9e93-aafcab0e6fed,1im2ckhyljzz0,9b2dc23c-f0c4-4487-b62d-d32400575484,4930f76a-17da-4c13-945a-96760a5e04d6,4aebdca5-7677-4bd0-aab0-695f157c2672,cd9af69c-5ce8-4d60-8381-b6b9876c32ec";
		return Arrays.asList(s.split(","));
	}
	private void inertEsOfTable(String estable, List<Document> resultList) throws InterruptedException {
		String table = estable.substring(2);
		Map<String,String> esJsonMap = null;
		String pk = null;
		if ("SnsUser".equals(table)) {
			pk = "userId";
			esJsonMap = buildUser(resultList);
		}else if("SnsTopic".equals(table)){
			pk = "topicId";
			esJsonMap = buildTopic(resultList);
		}else if("SnsActivity".equals(table)){
			pk = "activityId";
			esJsonMap = buildActivity(resultList);
		}else if("SnsGroup".equals(table)){
			pk = "groupId";
			esJsonMap = buildGroup(resultList);
		}else if("SnsStatus".equals(table)){
			pk = "statusId";
			esJsonMap = buildStatus(resultList);
		}else if("SnsComment".equals(table)){
			table = "SnsStatusComment";
			pk = "commentId";
			esJsonMap = buildComment(resultList);
		}

		if (esJsonMap != null) {
//			for (Entry<String, String> entry : esJsonMap.entrySet()) {
//				insertEs(table, entry.getValue(), entry.getKey());
//			}
			logger.info("over set size {}",userIDSet.size());
			userIDSet.clear();
			List<String> difIds = compareIds(table, pk);
			logger.info("{} 未导入es的id size {} {}",table,difIds.size(),difIds);
		}
	}
	
	private Map<String, String> buildComment(final List<Document> resultList) {
		final Map<String, String> map = new HashMap<>();
		ExecutorService pool = Executors.newFixedThreadPool(10);
		final AtomicInteger count = new AtomicInteger();
		final AtomicInteger count1 = new AtomicInteger();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					String id = null;
					int start = index*5000;
					int end = (index+1)*5000;
					if (end > resultList.size()) {
						end = resultList.size();
					}
					List<Document> dlist = resultList.subList(start, end);
					logger.info("{}   {}  {}",start, end, dlist.size());
					for (Document document : dlist) {
						count1.incrementAndGet();
						JSONObject json = new JSONObject();
						id = document.getString("commentId");
						json.put("id", id);
						Long value = getTime(document.get("createdTime"));
						if (value != null ) {
							json.put("createdTime", value);
						}
						json.put("state", document.get("state"));
						json.put("commentOnStatusId", document.get("commentOnStatusId"));
						json.put("likedCount", document.get("likedCount"));
						json.put("commentId", document.get("commentId"));
						json.put("publishedByUserId", documentToString(document.get("publishedBy")));
						json.put("content", document.get("content"));
						json.put("repliedCount", document.get("repliedCount"));
						json.put("isRecommmended", document.get("isRecommmended"));
						json.put("weight", document.get("weight"));
						json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
						json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
						json.put("sharingToQQCount", document.get("sharingToQQCount"));
						value = getTime(document.get("recommendEndTime"));
						if (value != null ) {
							json.put("recommendEndTime", value);
						}
						value = getTime(document.get("recommendStartTime"));
						if (value != null ) {
							json.put("recommendStartTime", value);
						}
						
						getReidsValue(id,json);
						map.put(id, JSON.toJSONString(json));
					}
					count.incrementAndGet();
				}
			});
		}
		while (count.get() < 10) {
			logger.info("---{}",count1.get());
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return map;
	}

	private void getReidsValue(String id, JSONObject json) {
		Map<String,String> map = mongoDao.findCache(id, null, null, null);
		if (map != null) {
			for (Entry<String, String> entry : map.entrySet()) {
				logger.debug("redis value {}, {}",entry.getKey(), entry.getValue());
				if (!"_id".equals(entry.getKey())) {
					if(entry.getKey().indexOf(".") > -1){
						String[] array = entry.getKey().split("\\.");
						json.put(array[array.length-1], entry.getValue());
					}else if(!entry.getKey().contains(":::")){
						json.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> buildStatus(List<Document> resultList, boolean ... isUpdate) {
		Map<String, String> map = new HashMap<>();
		String id = null;
		boolean update = false;
		if (isUpdate != null && isUpdate.length > 0) {
			update = isUpdate[0];
		}
		for (Document document : resultList) {
			JSONObject json = new JSONObject();
			id = document.getString("statusId");
			if (!update) {
				Long value = getTime(document.get("createdTime"));
				if (value != null ) {
					json.put("createdTime", value);
				}
				json.put("collectedCount", document.get("collectedCount"));
				json.put("location", documentToString(document.get("location")));
				json.put("readCount", document.get("readCount"));
				json.put("state", document.get("state"));
				json.put("contentType", document.get("contentType"));
				json.put("type", document.get("type"));
				json.put("statusId", document.get("statusId"));
				json.put("activityId", document.get("activityId"));
				json.put("tipAmount", document.get("tipAmount"));
				json.put("name", document.get("name"));
				json.put("blogTitle", document.get("blogTitle"));
				json.put("tipCount", document.get("tipCount"));
				json.put("fromPlatform", document.get("fromPlatform"));
				json.put("commentCount", document.get("commentCount"));
				json.put("likedCount", document.get("likedCount"));
				String content = document.getString("content");
				if (content != null && content.length()>100) {
					content = content.substring(0,100);
				}
				json.put("content", content);
				json.put("isRecommmended", document.get("isRecommmended"));
				value = getTime(document.get("recommendEndTime"));
				if (value != null ) {
					json.put("recommendEndTime", value);
				}
				value = getTime(document.get("recommendStartTime"));
				if (value != null ) {
					json.put("recommendStartTime", value);
				}
				json.put("weight", document.get("weight"));
				json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
				json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
				json.put("sharingToQQCount", document.get("sharingToQQCount"));
			}
			json.put("publishedByUserId", getId((Document)document.get("publishedBy"),"userId"));
			json.put("channelIds", buildIds((List<Object>)document.get("channels"),"channelId"));
			json.put("publishToGroupIds", buildIds((List<Object>)document.get("publishToGroups"),"groupId"));
			json.put("topicIds", buildIds((List<Object>)document.get("topics"),"topicId"));
			if (map.get(id) == null) {
//				getReidsValue(id, json);
				map.put(id, JSON.toJSONString(json));
			}else{
				logger.info("status 重复 statusId={} body={}",id,json);
			}
		}
		return map;
	}
	
	private String getId(Document document, String key) {
		if (document != null) {
			return document.getString(key);
		}
		return null;
	}
	
	private String buildIds(List<Object> array, String key) {
		StringBuilder sb = new StringBuilder();
		String id = null;
		try {
			if (CollectionUtils.isNotEmpty(array)) {
				for (Object obj : array) {
					id = ((Document)obj).getString(key);
					if (StringUtils.isNotBlank(id)) {
						sb.append(id).append(",");
					}
				}
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length()-1);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return sb.toString();
	}

	private String documentToString(Object ojb){
		return ojb==null?"":ojb.toString().replaceAll("Document|[\\{\\}]", "");
	}

	private Map<String, String> buildGroup(List<Document> resultList) {
		Map<String, String> map = new HashMap<>();
		String id = null;
		for (Document document : resultList) {
			JSONObject json = new JSONObject();
			id = document.getString("groupId");
			json.put("createdTime", document.get("createdTime"));
			json.put("state", document.get("state"));
			json.put("memberCount", document.get("memberCount"));
			json.put("groupId", document.get("groupId"));
			json.put("name", document.get("name"));
			json.put("authType", document.get("authType"));
			json.put("adminByUserId", document.get("adminByUserId"));
			json.put("blockedReason", document.get("blockedReason"));
			json.put("statusCount", document.get("statusCount"));
			json.put("summery", document.get("summery"));
			json.put("isRecommmended", document.get("isRecommmended"));
			Long value = getTime(document.get("recommendEndTime"));
			if (value != null ) {
				json.put("recommendEndTime", value);
			}
			value = getTime(document.get("recommendStartTime"));
			if (value != null ) {
				json.put("recommendStartTime", value);
			}
			json.put("weight", document.get("weight"));
			json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
			json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
			json.put("sharingToQQCount", document.get("sharingToQQCount"));
			map.put(id, JSON.toJSONString(json));
		}
		return map;
	}

	private Map<String, String> buildActivity(List<Document> resultList) {
		Map<String, String> map = new HashMap<>();
		String id = null;
		for (Document document : resultList) {
			JSONObject json = new JSONObject();
			id = document.getString("activityId");
			json.put("hasEnrolledCount", document.get("hasEnrolledCount"));
			json.put("storeIds", documentToString(document.get("storeIds")));
			json.put("location", document.get("location"));
			json.put("state", document.get("state"));
			json.put("commentCount", document.get("commentCount"));
			json.put("type", document.get("type"));
			Long value = getTime(document.get("createdTime"));
			if (value != null ) {
				json.put("createdTime", value);
			}
			value = getTime(document.get("enrollStartTime"));
			if (value != null ) {
				json.put("enrollStartTime", value);
			}
			value = getTime(document.get("enrollEndTime"));
			if (value != null ) {
				json.put("enrollEndTime", value);
			}
			json.put("activityId", document.get("activityId"));
			json.put("storeNames", documentToString(document.get("storeNames")));
			json.put("title", document.get("title"));
			json.put("subType", document.get("subType"));
			value = getTime(document.get("activityEndTime"));
			if (value != null ) {
				json.put("activityEndTime", value);
			}
			value = getTime(document.get("activityStartTime"));
			if (value != null ) {
				json.put("activityStartTime", value);
			}
			json.put("isRecommmended", document.get("isRecommmended"));
			value = getTime(document.get("recommendEndTime"));
			if (value != null ) {
				json.put("recommendEndTime", value);
			}
			value = getTime(document.get("recommendStartTime"));
			if (value != null ) {
				json.put("recommendStartTime", value);
			}
			json.put("weight", document.get("weight"));
			json.put("sharingToQQCount", document.get("sharingToQQCount"));
			json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
			json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
			getReidsValue(id, json);
			map.put(id, JSON.toJSONString(json));
		}
		return map;
	}

	private Map<String, String> buildTopic(List<Document> resultList) {
		Map<String, String> map = new HashMap<>();
		String id = null;
		for (Document document : resultList) {
			JSONObject json = new JSONObject();
			id = document.getString("topicId");
			json.put("createdTime", document.get("createdTime"));
			json.put("readCount", document.get("readCount"));
			json.put("joinedStatusCount", document.get("joinedStatusCount"));
			json.put("joinedUserCount", document.get("joinedUserCount"));
			json.put("topicId", document.get("topicId"));
			json.put("commentCount", document.get("commentCount"));
			json.put("isBlocked", document.get("isBlocked"));
			json.put("createdByUserId", document.get("createdByUserId"));
			json.put("name", document.get("name"));
			Long value = getTime(document.get("lastJoinedStatusTime"));
			if (value != null ) {
				json.put("lastJoinedStatusTime", value);
			}
			json.put("blockedReason", document.get("blockedReason"));
			json.put("weight", document.get("weight"));
			json.put("isRecommmended", document.get("isRecommmended"));
			value = getTime(document.get("recommendEndTime"));
			if (value != null ) {
				json.put("recommendEndTime", value);
			}
			value = getTime(document.get("recommendStartTime"));
			if (value != null ) {
				json.put("recommendStartTime", value);
			}
			json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
			json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
			json.put("sharingToQQCount", document.get("sharingToQQCount"));
			map.put(id, JSON.toJSONString(json));
		}
		return map;
	}
	
	private Long getTime(Object value){
		if (value != null ) {
			if (value instanceof Double && ((Double) value).doubleValue() !=0) {
				return ((Double) value).longValue();
			}else if(value instanceof Long && ((Long) value).longValue() !=0){
				return ((Long) value).longValue();
			}
		}
		return null;
	}

	private Map<String, String> buildUser(List<Document> resultList) {
		Map<String, String> map = new HashMap<>();
		String id = null;
		Long createTime = null;
		Long createTime2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Document document : resultList) {
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			id = document.getString("userId");
			json.put("collectCount", document.get("collectCount"));
			json.put("followingCount", document.get("followingCount"));
			createTime = (Long) document.get("createdTime");
			json.put("createdTime", createTime);
			json.put("readCount", document.get("readCount"));
			json.put("state", document.get("state"));
			json.put("avatarImage", document.get("avatarImage"));
			json.put("belongToStoreId", document.get("belongToStoreId"));
			json.put("userId", document.get("userId"));
			json.put("name", document.get("name"));
			json.put("tipAmountCount", document.get("tipAmountCount"));
			json.put("tipBlogCount", document.get("tipBlogCount"));
			json.put("roleType", document.get("roleType"));
			json.put("followedCount", document.get("followedCount"));
			json.put("note", document.get("note"));
			json.put("summery", document.get("summery"));
			json.put("loginName", document.get("loginName"));
			json.put("publishedCount", document.get("publishedCount"));
			json.put("weight", document.get("weight"));
			json.put("sharingToWechatCount", document.get("sharingToWechatCount"));
			json.put("sharingToWeboCount", document.get("sharingToWeboCount"));
			json.put("sharingToQQCount", document.get("sharingToQQCount"));
			json.put("isRecommmended", document.get("isRecommmended"));
			Long value = getTime(document.get("recommendEndTime"));
			if (value != null ) {
				json.put("recommendEndTime", value);
			}
			value = getTime(document.get("recommendStartTime"));
			if (value != null ) {
				json.put("recommendStartTime", value);
			}
			if (StringUtils.isBlank(id)) {
				logger.info("userid 为空  {} \r\n",JSON.toJSONString(json));
				continue;
			}
			if (map.get(id) == null) {
				map.put(id, JSON.toJSONString(json));
			}else{
				createTime2 = JSON.parseObject(map.get(id)).getLong("createdTime");
				logger.info("重复userid {} 创建时间1： {} 创建时间2：{} {} \r\n", id, sdf.format(new Date(createTime)), sdf.format(new Date(createTime2)),JSON.toJSONString(json));
			}
		}
		return map;
	}

	private void insertEs(String table, String json,String id, boolean ... isUpdate){
		userIDSet.add(id);
//		logger.info("size : {}", userIDSet.size());
		mongoDao.saveInEs(table, json, id, null, isUpdate);
//		logger.info("o ");
	}
	
	@Test
	public void CheckUserInEs(){
		String tableName = "SnsUser";
		final List<String> ids = new ArrayList<>();
		int scrollSize = 50000;
		int timeMillis = 10000;
		final DictModel dict = mongoDao.getDictModel(tableName);
		final String indexName = dict.getEsModel().getIndexName();
		final String type = dict.getEsModel().getType();
		for (int i = 1; i < 6; i++) {
			String index = indexName+i;
			LppzEsComponent.getInstance().scrollSearch(new String[]{index}, new String[]{type}, null, scrollSize, timeMillis, new PrepareBulk() {
				
				@Override
				public void bulk(List<SearchResult> listRes) {
					logger.debug("bulk size {}",listRes.size());
					List<EsModel> esModelList = buildInsertList(listRes);
					for (EsModel model : esModelList) {
						String index = model.getIndex();
						JSONObject esuser = JSON.parseObject((String)model.getJsonSource());
						if (esuser.getString("userId") == null) {
							logger.info("{},{}",index,esuser);
						}
						String id = model.getId().substring(1);
						ids.add(id);
					}
				}
			});
		}
		int x=10;
		while (--x > 0) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("es {} size {}",tableName,ids.size());
	}

	public List<String> findEs(String tableName){
		final List<String> ids = new ArrayList<>();
		int scrollSize = 50000;
		int timeMillis = 10000;
		final DictModel dict = mongoDao.getDictModel(tableName);
		final String indexName = dict.getEsModel().getIndexName();
		final String type = dict.getEsModel().getType();
		for (int i = 1; i < 6; i++) {
			String index = indexName+i;
			LppzEsComponent.getInstance().scrollSearch(new String[]{index}, new String[]{type}, null, scrollSize, timeMillis, new PrepareBulk() {
				
				@Override
				public void bulk(List<SearchResult> listRes) {
					logger.debug("bulk size {}",listRes.size());
					List<EsModel> esModelList = buildInsertList(listRes);
					for (EsModel model : esModelList) {
						String id = model.getId().substring(1);
						ids.add(id);
					}
				}
			});
		}
		int x=10;
		while (--x > 0) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("es {} size {}",tableName,ids.size());
		return ids;
	}
	
	public List<String> compareIds(String tableName, String pk) throws InterruptedException{
		Thread.sleep(10000L);
		List<String> esIds = findEs(tableName);
		if ("SnsStatus".equals(tableName)) {
			tableName = "snsStatus";
		}
		List<String> mongoIds = findMongo(tableName, pk);
		
		List<String> dif = new ArrayList<>();
		for (String id : mongoIds) {
			if (esIds.indexOf(id)==-1) {
				dif.add(id);
			}
		}
		List<String> notInMongo = new ArrayList<>();
		for (String esId : esIds) {
			if (mongoIds.indexOf(esId)==-1) {
				notInMongo.add(esId);
			}
		}
		logger.info("{} not in mongo {}",tableName,notInMongo);
		return dif;
	}
	
	public List<String> findMongo(String tableName, String pk){
		List<String> ids = new ArrayList<>();
		Document query = new Document();
		List<Document> resultList = mongoDao.findMongo(tableName, query, null, null, 0, mongoPageSize);
		for (Document d : resultList ) {
			ids.add(d.getString(pk));
		}
		logger.info("mongo {} size {}",tableName,ids.size());
		return ids;
	}
	
	protected String buildId(String id) {
		try {
			
			int hashCode = Math.abs(id.hashCode());
			return hashCode % 6 + id;
		} catch (Exception e) {
			e.toString();
		}
		return id;
	}
	public List<EsModel> buildInsertList(List<SearchResult> listRes) {
		EsModel model = null;
		List<EsModel> models = new ArrayList<EsModel>();
		for (SearchResult result : listRes) {
			model = new EsModel<>(result.getIndex(), result.getType(), result.getId(), JSON.toJSONString(result.getSource()), EsDMlEnum.Insert);
			models.add(model);
		}
		return models;
	}
	
	
//	@Test
	public void esToEs(){
		String tableName = "SnsUser";
		String[] index = new String[]{"sns-user"};
		String[] types = new String[]{"com.lppz.mobile.model.sns.MSnsUser"};
		int scrollSize = 13000;
		int timeMillis = 10000;
		final DictModel dict = mongoDao.getDictModel(tableName);
		final String indexName = dict.getEsModel().getIndexName();
		final String type = dict.getEsModel().getType();
//		BaseEsLogEvent2Sender sender = BaseEsLogEvent2Sender.create(indexName, type,"2016",false);
		LppzEsComponent.getInstance().scrollSearch(index, types, null, scrollSize, timeMillis, new PrepareBulk() {
			
			@Override
			public void bulk(List<SearchResult> listRes) {
				logger.info("bulk size {}",listRes.size());
				List<EsModel> esModelList = buildInsertList(listRes);
				for (EsModel model : esModelList) {
					String surffix = EsIndexUtils.getSurffixByMode(model.getId(), EsIndexUtils.APP_SEARCH_MOD);
					try {
						LppzEsComponent.getInstance().insert(indexName+surffix, type, buildId(model.getId()), model.getJsonSource());
					} catch (Exception e) {
						logger.error(String.format("写入es异常 异常数据：%d", model),e);
					}
				}
			}
		});
		
		while (true) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
