package com.lppz.oms.kafka.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExceptionLogDto extends MyPageDto{
	private String id;
	
	private String logName;
	
	private String logDesc;
	
	private Date createTime;
	
	private String ip;
	
	private String stackTrace;
	
	private List<Map<String,Object>> exceptionParam;
	
	private String ExceptionParamStr;
	
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
	     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return  format.format(createTime);
	}
	
	public void setCreateTime(Date createTime) {
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
	public List<Map<String, Object>> getExceptionParam() {
		return exceptionParam;
	}
	public void setExceptionParam(List<Map<String, Object>> exceptionParam) {
		this.exceptionParam = exceptionParam;
	}
	public int getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(int sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getExceptionParamStr() {
		return this.exceptionParam.toString();
	}
	public void setExceptionParamStr(String exceptionParamStr) {
		ExceptionParamStr = exceptionParamStr;
	}
	
}
