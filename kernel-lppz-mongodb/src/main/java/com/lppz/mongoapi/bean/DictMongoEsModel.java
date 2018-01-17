package com.lppz.mongoapi.bean;

import java.util.Map;

import org.bson.Document;

public class DictMongoEsModel {

	private String indexName;//es索引名称
	private String surffixFormat;//索引后缀时间格式
	private String type;//索引type，dto类全名
	private Map<String,String> mongoEsMap;//mongo与es字段映射
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getSurffixFormat() {
		return surffixFormat;
	}
	public void setSurffixFormat(String surffixFormat) {
		this.surffixFormat = surffixFormat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getMongoEsMap() {
		return mongoEsMap;
	}
	public void setMongoEsMap(Map<String, String> mongoEsMap) {
		this.mongoEsMap = mongoEsMap;
	}
	
	public static DictMongoEsModel build(Document document){
		if (document == null) {
			return null;
		}
		DictMongoEsModel dictMongoEsModel = new DictMongoEsModel();
		dictMongoEsModel.setIndexName(document.get("indexName", String.class));
		dictMongoEsModel.setSurffixFormat(document.get("surffixFormat", String.class));
		dictMongoEsModel.setType(document.get("type", String.class));
		dictMongoEsModel.setMongoEsMap(document.get("mongoEsMap", Map.class));
		return dictMongoEsModel;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DictMongoEsModel [indexName=");
		builder.append(indexName);
		builder.append(", surffixFormat=");
		builder.append(surffixFormat);
		builder.append(", type=");
		builder.append(type);
		builder.append(", mongoEsMap=");
		builder.append(mongoEsMap);
		builder.append("]");
		return builder.toString();
	}
}
