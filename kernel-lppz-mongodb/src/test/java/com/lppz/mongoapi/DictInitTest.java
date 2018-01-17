package com.lppz.mongoapi;

import java.util.Arrays;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;

import com.lppz.mongoapi.dao.MongoDao;

public class DictInitTest extends SpringBaseTest{
	
	@Resource
	private MongoDao mongoDao;
	
	@Test
	public void DictInit(){
		String table = "AppDict";
		
		try {
			mongoDao.insert(table, buildUser());
			mongoDao.insert(table, buildGroup());
			mongoDao.insert(table, buildTopic());
			mongoDao.insert(table, buildStatus());
			mongoDao.insert(table, buildComment());
			mongoDao.insert(table, buildActivity());
			mongoDao.insert(table, buildSnsUserMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		mongoDao.insert(table, buildAutocomplete());
//		mongoDao.insert(table, buildHotcard());
//		mongoDao.insert(table, buildHotkey());
		
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Document buildGroup(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsGroup";
		
		esModel.append("indexName", "sns-group-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsGroup");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("adminByUserId", "adminByUserId");
		mongoEsMap.append("authType", "authType");
		mongoEsMap.append("blockedReason", "blockedReason");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("groupId", "groupId");
		mongoEsMap.append("memberCount", "memberCount");
		mongoEsMap.append("name", "name");
		mongoEsMap.append("state", "state");
		mongoEsMap.append("statusCount", "statusCount");
		mongoEsMap.append("summery", "summery");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	private Document buildSnsUserMessage(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsUserMessage";
		
		esModel.append("indexName", "sns-usermessage-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsUserMessage");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("userId", "userId");
		mongoEsMap.append("state", "state");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("category", "category");
		mongoEsMap.append("invalidateTime", "invalidateTime");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildUser(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsUser";
		
		esModel.append("indexName", "sns-user-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsUser");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("avatarImage", "avatarImage");
		mongoEsMap.append("belongToStoreId", "belongToStoreId");
		mongoEsMap.append("collectCount", "collectCount");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("followedCount", "followedCount");
		mongoEsMap.append("followingCount", "followingCount");
		mongoEsMap.append("loginName", "loginName");
		mongoEsMap.append("name", "name");
		mongoEsMap.append("note", "note");
		mongoEsMap.append("publishedCount", "publishedCount");
		mongoEsMap.append("roleType", "roleType");
		mongoEsMap.append("state", "state");
		mongoEsMap.append("summery", "summery");
		mongoEsMap.append("tipAmountCount", "tipAmountCount");
		mongoEsMap.append("tipBlogCount", "tipBlogCount");
		mongoEsMap.append("userId", "userId");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
		redisModel.append("mongoInRredis", Arrays.asList("name","avatarImage","followedCount"
				,"followingCount","publishedCount","tipAmountCount","tipBlogCount","collectCount"));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", true);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildTopic(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsTopic";
		
		esModel.append("indexName", "sns-topic-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsTopic");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("activity", "activity");
		mongoEsMap.append("blockedReason", "blockedReason");
		mongoEsMap.append("commentCount", "commentCount");
		mongoEsMap.append("createdBy", "createdBy");
		mongoEsMap.append("createdByUserId", "createdByUserId");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("isBlocked", "isBlocked");
		mongoEsMap.append("joinedStatusCount", "joinedStatusCount");
		mongoEsMap.append("joinedUserCount", "joinedUserCount");
		mongoEsMap.append("joinedUsers", "joinedUsers");
		mongoEsMap.append("lastJoinedStatusTime", "lastJoinedStatusTime");
		mongoEsMap.append("name", "name");
		mongoEsMap.append("readCount", "readCount");
		mongoEsMap.append("summery", "summery");
		mongoEsMap.append("topicCategory", "topicCategory");
		mongoEsMap.append("topicDetails", "topicDetails");
		mongoEsMap.append("topicId", "topicId");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildStatus(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsStatus";
		
		esModel.append("indexName", "sns-status-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsStatus");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("activityId", "activityId");
		mongoEsMap.append("blogTitle", "blogTitle");
		mongoEsMap.append("channelIds", "channelIds");
		mongoEsMap.append("collectedCount", "collectedCount");
		mongoEsMap.append("commentCount", "commentCount");
		mongoEsMap.append("content", "content");
		mongoEsMap.append("contentType", "contentType");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("fromPlatform", "fromPlatform");
		mongoEsMap.append("likedCount", "likedCount");
		mongoEsMap.append("location", "location");
		mongoEsMap.append("name", "name");
		mongoEsMap.append("publishedByUserId", "publishedByUserId");
		mongoEsMap.append("publishToGroupIds", "publishToGroupIds");
		mongoEsMap.append("readCount", "readCount");
		mongoEsMap.append("state", "state");
		mongoEsMap.append("statusId", "statusId");
		mongoEsMap.append("tipAmount", "tipAmount");
		mongoEsMap.append("tipCount", "tipCount");
		mongoEsMap.append("topicIds", "topicIds");
		mongoEsMap.append("type", "type");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("saveSync", true);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	
	private Document buildComment(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsStatusComment";
		
		esModel.append("indexName", "sns-comment-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsComment");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("commentId", "commentId");
		mongoEsMap.append("commentOnStatusId", "commentOnStatusId");
		mongoEsMap.append("content", "content");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("likedCount", "likedCount");
		mongoEsMap.append("publishedByUserId", "publishedByUserId");
		mongoEsMap.append("repliedCount", "repliedCount");
		mongoEsMap.append("state", "state");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
//		redisModel.append("mongoRedisMixSubPk", "commondId");
//		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
//		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildActivity(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "SnsActivity";
		
		esModel.append("indexName", "sns-activity-");
		esModel.append("type", "com.lppz.mobile.model.es.ESSnsActivity");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("activityEndTime", "activityEndTime");
		mongoEsMap.append("activityId", "activityId");
		mongoEsMap.append("activityStartTime", "activityStartTime");
		mongoEsMap.append("commentCount", "commentCount");
		mongoEsMap.append("createdTime", "createdTime");
		mongoEsMap.append("enrollEndTime", "enrollEndTime");
		mongoEsMap.append("enrollStartTime", "enrollStartTime");
		mongoEsMap.append("enrollStartTime", "enrollStartTime");
		mongoEsMap.append("location", "location");
		mongoEsMap.append("state", "state");
		mongoEsMap.append("storeIds", "storeIds");
		mongoEsMap.append("storeNames", "storeNames");
		mongoEsMap.append("subType", "subType");
		mongoEsMap.append("title", "title");
		mongoEsMap.append("type", "type");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildHotcard(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "snsHotcard";
		
		esModel.append("indexName", "sns-hotcard-");
		esModel.append("type", "");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("saveSync", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private Document buildHotkey(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "snsHotkey";
		
		esModel.append("indexName", "sns-hotkey-");
		esModel.append("type", "");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}

	
	private Document buildAutocomplete(){
		Document esModel = new Document();
		Document redisModel  = new Document();
		Document dicModel = new Document();
		String apptable = "snsAutocomplete";
		
		esModel.append("indexName", "sns-autocomplete-");
		esModel.append("type", "");
		esModel.append("surffixFormat", "yyyyMMdd");
		Document mongoEsMap = new Document("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		mongoEsMap.append("", "");
		appendItems(mongoEsMap);
		esModel.append("mongoEsMap", mongoEsMap);
		
//		redisModel.append("mongoInRredis", Arrays.asList("","",""));
		redisModel.append("mongoRedisMixSubPk", "commondId");
		redisModel.append("mongoRedisMixSubCloumn", "likedCount");
		
		dicModel.append("table", apptable);
		dicModel.append("dayLiving", false);
		dicModel.append("esModel", esModel);
		dicModel.append("redisModel", redisModel);
		
		return dicModel;
	}
	
	private void appendItems(Document mongoEsMap){
		mongoEsMap.append("isRecommmended", "isRecommmended");
		mongoEsMap.append("recommendEndTime", "recommendEndTime");
		mongoEsMap.append("recommendStartTime", "recommendStartTime");
		mongoEsMap.append("sharingToQQCount", "sharingToQQCount");
		mongoEsMap.append("sharingToWeboCount", "sharingToWeboCount");
		mongoEsMap.append("sharingToWechatCount", "sharingToWechatCount");
		mongoEsMap.append("weight", "weight");
	}
}
