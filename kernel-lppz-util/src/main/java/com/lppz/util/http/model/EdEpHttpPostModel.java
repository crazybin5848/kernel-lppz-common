package com.lppz.util.http.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;

import com.lppz.util.http.enums.HttpEntityEnum;
import com.lppz.util.http.handler.BaseHttpHandler;
import com.lppz.util.http.handler.InputStreamEntityHandler;
import com.lppz.util.http.handler.MultipartEntityHandler;
import com.lppz.util.http.handler.UrlEncodedFormEntityHandler;

public class EdEpHttpPostModel implements Cloneable{
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	private Map<String,String> headers;

	private EdEPModel edepModel;
	private static Map<HttpEntityEnum,BaseHttpHandler> mapHandler=new HashMap<HttpEntityEnum,BaseHttpHandler>(2);
	static {
		mapHandler.put(HttpEntityEnum.InputStreamEntityHandler, InputStreamEntityHandler.getInstance());
		mapHandler.put(HttpEntityEnum.UrlEncodedFormEntityHandler, UrlEncodedFormEntityHandler.getInstance());
		mapHandler.put(HttpEntityEnum.MultipartEntityHandler, MultipartEntityHandler.getInstance());
	}
	public HttpEntityEnum getEntityEnum() {
		return entityEnum;
	}
	public void setEntityEnum(HttpEntityEnum entityEnum) {
		this.entityEnum = entityEnum;
	}
	private Map<String,String> body;
	private HttpEntityEnum entityEnum=HttpEntityEnum.UrlEncodedFormEntityHandler;
	
	public void buildHttpRequestBase(HttpRequestBase httpBase){
		mapHandler.get(entityEnum).buildHttpRequestBase(httpBase, headers, body,edepModel);
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Map<String, String> getBody() {
		return body;
	}
	public void setBody(Map<String, String> body) {
		this.body = body;
	}
	public EdEPModel getEdepModel() {
		return edepModel;
	}
	public void setEdepModel(EdEPModel edepModel) {
		this.edepModel = edepModel;
	}
	
}
