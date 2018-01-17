package com.lppz.mongoapi.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class MongoConfigMapBean implements Serializable{
	private static final long serialVersionUID = -237855705953552455L;
	
	Map<String, MongoConfigBean> map;

	public Map<String, MongoConfigBean> getMap() {
		return map;
	}

	public void setMap(Map<String, MongoConfigBean> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MongoConfigMapBean [map=");
		builder.append(map);
		builder.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		MongoConfigMapBean bean = new MongoConfigMapBean();
		
		Map<String, MongoConfigBean> map = new HashMap<>();
		MongoConfigBean appbean = new MongoConfigBean();
		appbean.setDb("lppzappsns");
		appbean.setConnectTimeout(25000);
		appbean.setMaxConnectionLifeTime(150000);
		appbean.setMaxWaitTime(300000);
		appbean.setPoolSize(200);
		appbean.setSocketTimeout(100000);
		Properties address = new Properties();
		address.setProperty("host", "192.168.37.242");
		address.setProperty("port", "50000");
		appbean.setServerAddress(Arrays.asList(address));
		appbean.setTables(Arrays.asList("AppDict","SnsActivity","SnsActivityResult","SnsActivityStatus","SnsBlog"
				,"SnsBlogTipRecord","SnsChannel","SnsConfig","SnsFilterWord","SnsGroup","SnsGroupMember","SnsGroupNotice","SnsGroupStatus"
				,"SnsImageLabel","SnsMessage","SnsPopupPageConfig","SnsSearchHotKey","SnsStatusComment","SnsStatusCommentLike","SnsStatusCommentReply","SnsStatusReadRecord"
				,"SnsTopic","SnsTopicJoinedUser","SnsTrend","SnsUser","SnsUserCollect","SnsUserFollowed","SnsUserFollowing","SnsUserLike"
				,"SnsUserStatus","deleted_snsActExtProChoice","deleted_snsBlogFunBtn","deleted_snsBlogReadRecord"
				,"deleted_snsBlogTipRecord","deleted_snsChannel","deleted_snsComment","deleted_snsCommentReplies","deleted_snsGroupNotice"
				,"deleted_snsGroupTimeline","deleted_snsMediaConfig","deleted_snsMessage","deleted_snsTipGift","deleted_snsUserComment","deleted_snsUserReceivedMsg"
				,"snsBlogReadRecord","snsStatus","snsUser","snsUserBlogOptLogs","snsUserCollect","snsUserComment"
				,"snsUserLike"));
		map.put("app", appbean);
		MongoConfigBean posbean = new MongoConfigBean();
		posbean.setDb("lppzappsns");
		posbean.setConnectTimeout(25000);
		posbean.setMaxConnectionLifeTime(150000);
		posbean.setMaxWaitTime(300000);
		posbean.setPoolSize(200);
		posbean.setSocketTimeout(100000);
		address = new Properties();
		address.setProperty("host", "192.168.37.242");
		address.setProperty("port", "50000");
		posbean.setServerAddress(Arrays.asList(address));
		posbean.setTables(Arrays.asList("SnsOrderComment","storeForPos","storeOrder"));
		map.put("pos", posbean);
		
		bean.setMap(map);
		System.out.println(new Yaml().dump(bean));
	}
}
