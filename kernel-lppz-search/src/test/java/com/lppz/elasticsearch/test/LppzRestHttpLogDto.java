package com.lppz.elasticsearch.test;


import java.io.Serializable;

public class LppzRestHttpLogDto implements Serializable {

	public String getRequesthttpHeader() {
		return requesthttpHeader;
	}
	public void setRequesthttpHeader(String requesthttpHeader) {
		this.requesthttpHeader = requesthttpHeader;
	}
	public String getResponsehttpHeader() {
		return responsehttpHeader;
	}
	public void setResponsehttpHeader(String responsehttpHeader) {
		this.responsehttpHeader = responsehttpHeader;
	}
	public String getRemoteClientAddr() {
		return remoteClientAddr;
	}
	public void setRemoteClientAddr(String remoteClientAddr) {
		this.remoteClientAddr = remoteClientAddr;
	}
	public String getHostUri() {
		return hostUri;
	}
	public void setHostUri(String hostUri) {
		this.hostUri = hostUri;
	}
	public String getResponsebody() {
		return responsebody;
	}
	public void setResponsebody(String responsebody) {
		this.responsebody = responsebody;
	}
	public String getRequestbody() {
		return requestbody;
	}
	public void setRequestbody(String requestbody) {
		this.requestbody = requestbody;
	}
	private static final long serialVersionUID = 7060258538492398209L;
	private String requesthttpHeader; 
	private String responsehttpHeader; 
	private String remoteClientAddr; 
	private String hostUri; 
	private String responsebody;
	private String requestbody;
	private String httpMethodType;
	private String classType;
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getHttpMethodType() {
		return httpMethodType;
	}
	public void setHttpMethodType(String httpMethodType) {
		this.httpMethodType = httpMethodType;
	}
}
