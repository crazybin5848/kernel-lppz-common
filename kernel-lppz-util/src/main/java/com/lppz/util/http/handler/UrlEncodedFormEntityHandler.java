package com.lppz.util.http.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.http.model.EdEPModel;

public class UrlEncodedFormEntityHandler extends BaseHttpHandler {
	private final Logger logger = LoggerFactory.getLogger(UrlEncodedFormEntityHandler.class);

	private UrlEncodedFormEntityHandler() {
	}

	private static UrlEncodedFormEntityHandler instance = new UrlEncodedFormEntityHandler();

	public static UrlEncodedFormEntityHandler getInstance() {
		return instance;
	}


	@Override
	protected void doHttpPost(HttpRequestBase httpBase, Map<String, String> body,EdEPModel edepModel) {
		HttpPost a = (HttpPost) httpBase;
		generateSign(body,edepModel);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String str : body.keySet())
			formparams.add(new BasicNameValuePair(str, body.get(str)));
		try {
			a.setEntity(new UrlEncodedFormEntity(formparams, CHARSET_UTF8));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
