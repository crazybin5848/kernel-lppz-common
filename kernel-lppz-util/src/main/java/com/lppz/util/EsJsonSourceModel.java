package com.lppz.util;

public class EsJsonSourceModel {
	protected Integer shardId;
	protected Integer status=0;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getShardId() {
		return shardId;
	}

	public void setShardId(Integer shardId) {
		this.shardId = shardId;
	}
}
