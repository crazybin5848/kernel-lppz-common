package com.lppz.util.http.handler;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.HttpEncodeUtils;
import com.lppz.util.http.model.EdEPModel;

public abstract class BaseHttpHandler {
	private final Logger logger = LoggerFactory.getLogger(BaseHttpHandler.class);
	protected static final String CHARSET_UTF8 = "UTF-8";
	private final static String COMMA=",";
	private final static String SEMICOLON=":";
	private final static String BRACKET="}";
	private final static String AMPERSAND="&";
	private final static String EQUALSSYMBOL="=";
	public void buildHttpRequestBase(HttpRequestBase httpBase, Map<String, String> headers, Map<String, String> body,EdEPModel edEPModel) {
		buildHeaders(httpBase, headers, body,edEPModel);
		if (body == null)
			return;
		if (httpBase instanceof HttpPost) {	
			doHttpPost(httpBase,body,edEPModel);
		}

		if (httpBase instanceof HttpGet) {
			buildHttpGetRequest((HttpGet) httpBase, headers, body,edEPModel);
		}
	}

	protected abstract void doHttpPost(HttpRequestBase httpBase,Map<String, String> body,EdEPModel edEPModel);
		

	protected void buildHttpGetRequest(HttpGet httpBase,Map<String,String> headers,Map<String,String> body,EdEPModel edEPModel){
		String url = httpBase.getURI().toString();
		String baseUrl = url.contains("?")?url.split("\\?")[0]:url;
		StringBuffer burl = new StringBuffer(baseUrl);
		if (!"?".equals(url.substring(url.length() - 1)) && body != null && body.size() != 0) {
			burl.append("?");
		}
		int i=0;
		try {
			for(String key:body.keySet()){
				if(i!=0)
					burl.append("&");
				burl.append(key).append("=").append(URLEncoder.encode(body.get(key), BaseHttpHandler.CHARSET_UTF8));
				i++;
			}
			if(null!=edEPModel.getFetchEnum().sign()){
				 String signk=edEPModel.getFetchEnum().sign();
				 String signV=HttpEncodeUtils.md5Signature(body,edEPModel.getSecretKey().get(signk),edEPModel.isUpperCase());
				 burl.append("&").append(signk).append("=").append(URLEncoder.encode(signV, BaseHttpHandler.CHARSET_UTF8));
			 }
			if(null!=edEPModel){
				injectPageParams(edEPModel.getPageNo(),burl);
				injectPageParams(edEPModel.getPageSize(),burl);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		httpBase.setURI(URI.create(burl.toString()));
	}
	protected  void buildHeaders(HttpRequestBase httpBase, Map<String, String> headers,Map<String, String> body,EdEPModel edepModel){
		if (headers != null) {
			for (String str : headers.keySet())
				httpBase.setHeader(str, headers.get(str));
		}
	}
	
	protected void generateSign(Map<String,String> body,EdEPModel edepModel){
		if(null!=edepModel&&null!=edepModel.getEncodeString()){
			String buildJsonBody=body.get(edepModel.getEncodeString());
			if(null!=edepModel.getPageNo()&&null!=edepModel.getPageSize()){
				removeBodyParms(body,edepModel.getPageNo(),buildJsonBody);	
				removeBodyParms(body,edepModel.getPageSize(),buildJsonBody);	
				body.put(edepModel.getEncodeString(),buildJsonBody(buildJsonBody,edepModel));
			}
		}		
		buildSignBody(body,edepModel);
	}
	
	protected String buildJsonBody(String body,EdEPModel edepModel){
		String[] pageNoName=getSingleMapKey(edepModel.getPageNo());
		String[] pageNoSize=getSingleMapKey(edepModel.getPageSize());
		return injectNumSize(body,pageNoName[0],pageNoSize[0],pageNoName[1],pageNoSize[1]);
	}
	
	private void buildSignBody(Map<String,String> body,EdEPModel edepModel){
		String signKey=null;
		String signValue=null;
		boolean isUpperCase=edepModel.isUpperCase();
		if(null!=edepModel.getSecretKey()){
			for(String key:edepModel.getSecretKey().keySet()){
				signKey=key;
				signValue=edepModel.getSecretKey().get(key);
			}
		}
		if(null!=edepModel.getPushEnum()){
			signKey=edepModel.getPushEnum().signKey();
			signValue=body.get(signKey);
			isUpperCase=edepModel.getPushEnum().isUpperCase();
		}
		body.remove(signKey);
		body.put(signKey, HttpEncodeUtils.md5Signature(body, signValue,isUpperCase));
	}

	private String injectNumSize(String body,String pageNoName,String pageSizeName,String pageNo,String pageSize){	
			String[] app=body.split(COMMA);
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<app.length;i++){
				String[] pagePrams=app[i].split(SEMICOLON);
				if(pagePrams[0].contains(pageNoName)&&!pagePrams[0].contains(pageSizeName)){					
					sb.append(pagePrams[0]+SEMICOLON+"\""+pageNo+"\"");
					sb.append(getComma(i,app.length,pagePrams[1]));	
					continue;
				}
				if(pagePrams[0].contains(pageSizeName)){
					sb.append(pagePrams[0]+SEMICOLON+"\""+pageSize+"\"");
					sb.append(getComma(i,app.length,pagePrams[1]));	
					continue;
				}
				sb.append(app[i]);
				sb.append(getComma(i,app.length,StringUtils.EMPTY));				
			}
			return sb.toString();
	}
	private String[] getSingleMapKey(Map<String,String> map){
		String[] result=new String[2];
		for(Map.Entry<String, String> entry : map.entrySet()){
			 result[0]=entry.getKey();
			 result[1]=entry.getValue();
		}
		return result;
	}
	private String getComma(int step,int size,String bracket){
		if(step!=size-1)
			return COMMA;
		return bracket.contains(BRACKET)?BRACKET:StringUtils.EMPTY;
	}
	
	private void removeBodyParms(Map<String,String> body,Map<String,String> maps,String encodeStr){
		for(String key:maps.keySet())
			if(encodeStr.contains(key))
				body.remove(key);	
	}
	
	private void injectPageParams(Map<String,String> map,StringBuffer sb){
		if(null==map) 
			return;
		for(String key:map.keySet())
			sb.append(AMPERSAND).append(key).append(EQUALSSYMBOL).append(map.get(key));					
	}
}
