package com.lppz.jstorm.es;

import java.io.Serializable;

import com.alibaba.jstorm.batch.BatchId;

public class EsAckObj implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2781454113265414809L;
	private String index;
	private String type;
	private String id;
	private BatchId batchId;
	
	public EsAckObj(){}
	public EsAckObj(String index,String type,String id,BatchId batchId){
		this.index=index;
		this.type=type;
		this.id=id;
		this.batchId=batchId;
	}
	
	public BatchId getBatchId() {
		return batchId;
	}
	public void setBatchId(BatchId batchId) {
		this.batchId = batchId;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return new StringBuilder("index:").append(index).append(",type:")
				.append(type).append(",id:").append(id).append(",batchId:").append(batchId).toString();
	}
}
