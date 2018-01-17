package com.lppz.canal.model;

import java.util.ArrayList;
import java.util.List;

public class BatchMessageModel {
	
	private String key;
	private String shardingId;
	private String tag;
	private int retryNum;
	private List<TableModel> tables;
	
	public BatchMessageModel() {
		tables = new ArrayList<TableModel>();
	}
	
	public List<TableModel> getTables() {
		return tables;
	}
	public void setTables(List<TableModel> tables) {
		this.tables = tables;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getShardingId() {
		return shardingId;
	}

	public void setShardingId(String shardingId) {
		this.shardingId = shardingId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransactionModel [key=");
		builder.append(key);
		builder.append(", shardingId=");
		builder.append(shardingId);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", retryNum=");
		builder.append(retryNum);
		builder.append(", tables=");
		builder.append(tables);
		builder.append("]");
		return builder.toString();
	}
}
