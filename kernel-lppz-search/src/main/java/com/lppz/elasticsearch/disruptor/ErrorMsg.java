package com.lppz.elasticsearch.disruptor;

import java.io.Serializable;

import org.elasticsearch.action.bulk.BulkItemResponse.Failure;

public class ErrorMsg implements Serializable{
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public String getFailurMsg() {
		return failurMsg;
	}
	public void setFailurMsg(String failurMsg) {
		this.failurMsg = failurMsg;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8442353484190715241L;
	private Integer retryCount=0;
	private String failurMsg;
	private Failure failure;
	
	public ErrorMsg(Integer retryCount,String failurMsg, Failure failure){
		this.failurMsg=failurMsg;
		this.retryCount=retryCount;
		this.failure=failure;
	}
	public Failure getFailure() {
		return failure;
	}
	public void setFailure(Failure failure) {
		this.failure = failure;
	}
}
