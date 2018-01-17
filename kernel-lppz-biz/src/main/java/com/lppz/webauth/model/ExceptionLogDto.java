package com.lppz.webauth.model;


public class ExceptionLogDto {
	private String id;
	
	private String logName;
	
	private String logDesc;
	
	private String createTime;
	
	private String ip;
	
	private String stackTrace;
	
	private String exceptionParam;
	
	private int sendFlag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogDesc() {
		return logDesc;
	}
	
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
	
	public String getLogName() {
		return logName;
	}
	
	
	public void setLogName(String logName) {
		this.logName = logName;
	}
	
	public String getCreateTime() {
	    return  createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public String getExceptionParam() {
		return exceptionParam;
	}
	public void setExceptionParam(String exceptionParam) {
		this.exceptionParam = exceptionParam;
	}
	public int getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(int sendFlag) {
		this.sendFlag = sendFlag;
	}
	
}
