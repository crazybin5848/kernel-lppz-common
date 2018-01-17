package com.lppz.util.http.model;

import java.util.Map;

import com.lppz.util.http.enums.PlatformFetchEnum;
import com.lppz.util.http.enums.PlatformPushEnum;

public class EdEPModel {
	private boolean isUpperCase = false;
	private String encodeString;
	private Map<String,String> pageNo;
	private Map<String,String> pageSize;
	private Map<String,String> secretKey;
	private PlatformPushEnum pushEnum;
	private PlatformFetchEnum fetchEnum;
	public boolean isUpperCase() {
		return isUpperCase;
	}
	public void setUpperCase(boolean isUpperCase) {
		this.isUpperCase = isUpperCase;
	}
	
	public String getEncodeString() {
		return encodeString;
	}
	public void setEncodeString(String encodeString) {
		this.encodeString = encodeString;
	}
	public Map<String, String> getPageNo() {
		return pageNo;
	}
	public void setPageNo(Map<String, String> pageNo) {
		this.pageNo = pageNo;
	}
	public Map<String, String> getPageSize() {
		return pageSize;
	}
	public void setPageSize(Map<String, String> pageSize) {
		this.pageSize = pageSize;
	}
	public Map<String, String> getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(Map<String, String> secretKey) {
		this.secretKey = secretKey;
	}
	public PlatformPushEnum getPushEnum() {
		return pushEnum;
	}
	public void setPushEnum(PlatformPushEnum pushEnum) {
		this.pushEnum = pushEnum;
	}
	public PlatformFetchEnum getFetchEnum() {
		return fetchEnum;
	}
	public void setFetchEnum(PlatformFetchEnum fetchEnum) {
		this.fetchEnum = fetchEnum;
	}
	
	
	
}
