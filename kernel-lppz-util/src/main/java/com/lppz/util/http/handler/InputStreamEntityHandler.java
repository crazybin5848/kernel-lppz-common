package com.lppz.util.http.handler;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.http.model.EdEPModel;

public class InputStreamEntityHandler extends BaseHttpHandler {
	private final Logger logger = LoggerFactory.getLogger(InputStreamEntityHandler.class);
	private InputStreamEntityHandler() {
	}

	private static InputStreamEntityHandler instance = new InputStreamEntityHandler();

	public static InputStreamEntityHandler getInstance() {
		return instance;
	}

	@Override
	protected void doHttpPost(HttpRequestBase httpBase, Map<String, String> body,EdEPModel edepModel) {
		HttpPost a=(HttpPost) httpBase ;
		try {
			String jsonBody=body.get("body");
			a.setEntity(new InputStreamEntity(new ByteArrayInputStream(buildJsonBody(jsonBody,edepModel).getBytes(BaseHttpHandler.CHARSET_UTF8))));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void  buildHeaders(HttpRequestBase httpBase,Map<String, String> headers,Map<String, String> body,EdEPModel edepModel ){
		if(null==edepModel||null==edepModel.getSecretKey()||null==headers||null==body)
			return;
		for(String key:edepModel.getSecretKey().keySet()){
			String baseStr = null;
			try {
				String jsonBody=body.get("body");			
				baseStr = new String(Base64.encodeBase64(buildJsonBody(jsonBody,edepModel).getBytes(CHARSET_UTF8)));
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
			StringBuffer signStr = new StringBuffer();	
			signStr.append(edepModel.getSecretKey().get(key));
			signStr.append(edepModel.getEncodeString());
			signStr.append(baseStr);
			headers.put(key, DigestUtils.md5Hex(signStr.toString()));			
		}
		super.buildHeaders(httpBase, headers, body, edepModel);
	}
	
	
}
