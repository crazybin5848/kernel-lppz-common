package com.lppz.util.http;


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.exception.HttpFetchException;

public class FutureWrapper {
	private static final Logger LOG = LoggerFactory
			.getLogger(FutureWrapper.class);
	private Future<Future<HttpResponse>> ffh;
	private Future<HttpResponse> fh;
	private HttpResponse resp;
	public static final String CHARSET_UTF8 = "UTF-8";
	public FutureWrapper(CloseableHttpResponse resp) {
		this.resp=resp;
	}

	public HttpResponse getResp() {
		try {
			if(resp!=null)
				return resp;
			return ffh == null ? fh.get() : ffh.get().get();
		} catch (InterruptedException | ExecutionException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String getRespBody() throws HttpFetchException{
		return getRespBody(CHARSET_UTF8);
	}

	public FutureWrapper(Future<Future<HttpResponse>> ffh,
			Future<HttpResponse> fh) {
		this.ffh = ffh;
		this.fh = fh;
	}

	public Future<Future<HttpResponse>> getFfh() {
		return ffh;
	}

	public void setFfh(Future<Future<HttpResponse>> ffh) {
		this.ffh = ffh;
	}

	public Future<HttpResponse> getFh() {
		return fh;
	}

	public void setFh(Future<HttpResponse> fh) {
		this.fh = fh;
	}
	
	public String getRespBody(String charset) throws HttpFetchException{
		String body="";
		HttpResponse httpResponse = getResp();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK && null != httpResponse.getEntity()) {
			try {
				body = IOUtils.toString(httpResponse.getEntity().getContent(), StringUtils.isBlank(charset)?CHARSET_UTF8:charset);
			} catch (IllegalStateException | IOException e) {
				LOG.error(e.getMessage(), e);
				throw new HttpFetchException(e.getMessage(),e,httpResponse);
			}
		}
		else{
			throw new HttpFetchException("http fetch exception:"+statusCode,httpResponse);
		}
		return body==null?"":body;
	}
	private String baseId;
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	
}
