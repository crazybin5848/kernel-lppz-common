package com.lppz.mongoapi.bean;

import java.util.ArrayList;
import java.util.Set;

import org.bson.Document;

public class DictMongoRedisModel {
	
	private ArrayList<String> mongoInRredis;//mongo写redis字段集合
	//复合field处理key=statusId，field=commontId.likedCount，vaule=10
	private String mongoRedisMixSubPk;//hset复合field主键字段：commontId
	private String mongoRedisMixSubCloumn;//hset符合field实际字段：likedCount
	
	public ArrayList<String> getMongoInRredis() {
		return mongoInRredis;
	}

	public void setMongoInRredis(ArrayList<String> mongoInRredis) {
		this.mongoInRredis = mongoInRredis;
	}

	public String getMongoRedisMixSubPk() {
		return mongoRedisMixSubPk;
	}

	public void setMongoRedisMixSubPk(String mongoRedisMixSubPk) {
		this.mongoRedisMixSubPk = mongoRedisMixSubPk;
	}

	public String getMongoRedisMixSubCloumn() {
		return mongoRedisMixSubCloumn;
	}

	public void setMongoRedisMixSubCloumn(String mongoRedisMixSubCloumn) {
		this.mongoRedisMixSubCloumn = mongoRedisMixSubCloumn;
	}
	
	public static DictMongoRedisModel build(Document document){
		if (document == null) {
			return null;
		}
		DictMongoRedisModel dictMongoRedisModel = new DictMongoRedisModel();
		dictMongoRedisModel.setMongoRedisMixSubCloumn(document.get("mongoRedisMixSubCloumn", String.class));
		dictMongoRedisModel.setMongoRedisMixSubPk(document.get("mongoRedisMixSubPk", String.class));
		dictMongoRedisModel.setMongoInRredis(document.get("mongoInRredis", ArrayList.class));
		return dictMongoRedisModel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DictMongoRedisModel [mongoInRredis=");
		builder.append(mongoInRredis);
		builder.append(", mongoRedisMixSubPk=");
		builder.append(mongoRedisMixSubPk);
		builder.append(", mongoRedisMixSubCloumn=");
		builder.append(mongoRedisMixSubCloumn);
		builder.append("]");
		return builder.toString();
	}
}
