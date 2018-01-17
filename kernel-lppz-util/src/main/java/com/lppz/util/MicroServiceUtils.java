package com.lppz.util;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.util.http.BaseHttpClientsComponent;
import com.lppz.util.http.FutureWrapper;
import com.lppz.util.http.enums.HttpMethodEnum;

public class MicroServiceUtils{

	private static Logger logger = LoggerFactory.getLogger(MicroServiceUtils.class);
	
	private static BaseHttpClientsComponent httpComponent=new BaseHttpClientsComponent();

	public static void shutdown(boolean boo,String url) {
		try {
			HttpResponse hrPost = doHttpStop(boo,url);
			if(hrPost==null)
				return;
			logger.info(new StringBuilder(hrPost.getStatusLine().toString())
					.toString()+" server begin shutdown...");
			while(true){
				try {
					hrPost = doHttpStop(false,url);
					if(hrPost==null){
						logger.info("server has been shutdown");
						break;
					}
				} catch (Exception e) {
					break;
				}
				logger.info(new StringBuilder(hrPost.getStatusLine().toString())
						.toString()+" server is shutdowning...");
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static HttpResponse doHttpStop(boolean boo,String url) throws InterruptedException, ExecutionException {
		HttpRequestBase httpPost = httpComponent.createReqBase(url,HttpMethodEnum.POST);
		StringEntity s = new StringEntity(JSON.toJSONString(boo), "UTF-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		((HttpPost) httpPost).setEntity(s);
		FutureWrapper fw;
		try {
			fw = httpComponent.doHttpSyncExec(httpPost);
		} catch (IOException e) {
			return null;
		}
		HttpResponse hrPost = fw == null ? null : fw.getResp();
		return hrPost;
	}
}
