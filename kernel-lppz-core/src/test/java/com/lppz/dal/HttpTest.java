package com.lppz.dal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.lppz.util.http.handler.InputStreamEntityHandler;
import com.lppz.util.http.handler.UrlEncodedFormEntityHandler;

public class HttpTest {

	@Test
	public  void httpBuildTest() throws ClientProtocolException, IOException{
		InputStreamEntityHandler ish = InputStreamEntityHandler.getInstance();
		UrlEncodedFormEntityHandler u = UrlEncodedFormEntityHandler.getInstance();
		HttpClient client = new DefaultHttpClient();
		String serverUrl = "http://gw.api.taobao.com/router/rest";
//		String serverUrl = "http://gw.api.taobao.com/router/rest?&timestamp=2016-01-27 17:15:31&sign=A9F8E231A48AD08825DDFB0E5E8F4DE6&partner_id=top-apitools&v=2.0&q=aa&method=taobao.products.search&app_key=12129701&format=json&fields=product_id,cid,props,name,pic_url";
		HttpPost request = new HttpPost(serverUrl);
		Map<String,String> headers = new HashMap<String,String>();
		Map<String,String> body = new HashMap<String,String>();
		body.put("sign", "A9F8E231A48AD08825DDFB0E5E8F4DE6");
		body.put("timestamp", "2016-01-27 17:15:31");
		body.put("v", "2.0");
		body.put("app_key", "12129701");
		body.put("method", "taobao.products.search");
		body.put("partner_id", "top-apitools");
		body.put("format", "json");
		body.put("q", "aa");
		body.put("fields", "product_id,cid,props,name,pic_url");
//		ish.buildHttpRequestBase(request, headers, body);
//		HttpResponse response = client.execute(request);
//		System.out.println(EntityUtils.toString(response.getEntity()));
//		u.buildHttpRequestBase(request, headers, body);
//		HttpResponse uresponse = client.execute(request);
//		System.out.println(EntityUtils.toString(uresponse.getEntity()));
		HttpGet hg = new HttpGet(serverUrl);
//		ish.buildHttpRequestBase(hg, headers, body);
//		HttpResponse gr = client.execute(hg);
//		System.out.println(EntityUtils.toString(gr.getEntity()));
	}
}
