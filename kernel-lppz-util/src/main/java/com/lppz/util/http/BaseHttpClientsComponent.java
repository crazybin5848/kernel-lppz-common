package com.lppz.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.LppzPropertiesUtils;
import com.lppz.util.http.enums.HttpMethodEnum;

public class BaseHttpClientsComponent {

	private static final Logger logger = LoggerFactory.getLogger(BaseHttpClientsComponent.class);

	private int httpClientTimeout = 300000;// 超时时间默认为5分钟

	protected Map<String, String> paramsMap;

	protected Integer saveThreadsHold = 10;

	protected Integer saveMaxResults = 5000;

	protected Integer fetchThreadsHold = 20;

	protected Integer fetchMaxResults = 5000;

	protected int httpClientTotal = 5000;

//	protected ExecutorService httpExecutor = Executors.newCachedThreadPool();

	public HttpRequestBase createReqBase(String baseUrl,HttpMethodEnum hmenum,String... format) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Role", "admin");
		headers.put("X-tenantId", "single");
//		headers.put("Content-Encoding", "gzip");
		headers.put("Content-Type", format==null||format.length==0?"application/json":format[0]);
		HttpRequestBase post = null;
		try {
			post = HttpMethodEnum.GET.equals(hmenum)?new HttpGet(baseUrl):new HttpPost(baseUrl);
			for (Entry<String, String> h : headers.entrySet()) {
				post.setHeader((String) h.getKey(), (String) h.getValue());
			}
		} catch (Exception e) {
			logger.error("http post create failed" + e.getMessage());
		}
		return post;
	}

	protected static ThreadLocal<CloseableHttpAsyncClient> httpAsyncClient=new ThreadLocal<CloseableHttpAsyncClient>();

	public void initHttpClient() {
		if (httpAsyncClient.get() == null) {
			ConnectingIOReactor ioReactor;
			try {
				ioReactor = new DefaultConnectingIOReactor();
				PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpClientTimeout)
						.setConnectTimeout(httpClientTimeout).build();

				cm.setMaxTotal(httpClientTotal);
				cm.setDefaultMaxPerRoute(httpClientTotal);
				httpAsyncClient.set(HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig)
						.setConnectionManager(cm).build());
				httpAsyncClient.get().start();
			} catch (IOReactorException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void closeHttpClient() {
		try {
			if(httpAsyncClient.get()!=null)
			httpAsyncClient.get().close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}finally{
			httpAsyncClient.remove();
			httpAsyncClient.set(null);
		}
	}
	
	public void init() {
		try {
			String httpClientTimeoutStr = LppzPropertiesUtils.getKey("httpClientTimeout");
			String httpClientTotalStr = LppzPropertiesUtils.getKey("httpClientTotal");
			if (StringUtils.isNotBlank(httpClientTimeoutStr)) {
				httpClientTimeout = Integer.valueOf(httpClientTimeoutStr);
			}
			if (StringUtils.isNotBlank(httpClientTotalStr)) {
				httpClientTotal = Integer.valueOf(httpClientTotalStr);
			}
			if (StringUtils.isNotBlank(LppzPropertiesUtils.getKey("saveThreadsHold"))) {
				saveThreadsHold = Integer.valueOf(LppzPropertiesUtils.getKey("saveThreadsHold"));
			}
			if (StringUtils.isNotBlank(LppzPropertiesUtils.getKey("saveMaxResults"))) {
				saveMaxResults = Integer.valueOf(LppzPropertiesUtils.getKey("saveMaxResults"));
			}
			if (StringUtils.isNotBlank(LppzPropertiesUtils.getKey("fetchThreadsHold"))) {
				fetchThreadsHold = Integer.valueOf(LppzPropertiesUtils.getKey("fetchThreadsHold"));
			}
			if (StringUtils.isNotBlank(LppzPropertiesUtils.getKey("fetchMaxResults"))) {
				fetchMaxResults = Integer.valueOf(LppzPropertiesUtils.getKey("fetchMaxResults"));
			}
		} catch (Exception e) {
		}
	}
	
//	protected FutureWrapper doHttpMultiPost(
//			final HttpRequestBase httpPost, final AtomicInteger atmCount,
//			final int size) {
//		return new FutureWrapper(httpExecutor.submit(new Callable<Future<HttpResponse>>() {
//			@Override
//			public Future<HttpResponse> call() throws Exception {
//				return doHttpPost(httpPost, atmCount, size).getFh();
//			}
//		}),null);
//	}
	public FutureWrapper doHttpSyncExec(final HttpRequestBase httpReqBase) throws IOException{
		CloseableHttpClient cc=HttpClients.createDefault();
		try {
			return new FutureWrapper(cc.execute(httpReqBase));
		} catch (IOException e) {
			throw e;
		}finally{
			if(cc!=null)
				try {
					cc.close();
				} catch (IOException e) {
				}
		}
	}
	
	public FutureWrapper doHttpExec(final HttpRequestBase httpReqBase,
			final AtomicInteger atmCount, final int size,final AtomicInteger... retryCount){
		return doHttpExec(httpReqBase, atmCount, size,null,retryCount);
	}
	
	private FutureWrapper doHttpExec(final HttpRequestBase httpReqBase,
			final AtomicInteger atmCount, final int size,CloseableHttpResponse resp,final AtomicInteger... retryCount) {
		if(retryCount!=null&&retryCount.length>0&&retryCount[0].get()==-1){
			throw new HttpLppzRetryException("retry httpclient fail!:>"+3);
		}
		if(resp!=null){
			return new FutureWrapper(resp);
		}
		return new FutureWrapper(null,httpAsyncClient.get().execute(httpReqBase,
				new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response) {
						atomicAdd(atmCount, size);
						logger.debug(httpReqBase.getRequestLine() + "->"
								+ response.getStatusLine());
					}

					public void failed(final Exception ex) {
						logger.error(httpReqBase.getRequestLine() + "->"
								+ ex);
						reTrySendTmpOrder2Oms(httpReqBase,atmCount,size,retryCount==null||retryCount.length==0?null:retryCount[0],HttpClients.createDefault());
					}

					public void cancelled() {
						logger.error(httpReqBase.getRequestLine() + "->"
								+ " cancelled");
						reTrySendTmpOrder2Oms(httpReqBase,atmCount,size,retryCount==null||retryCount.length==0?null:retryCount[0],HttpClients.createDefault());
						}
				}));
	}
	
	private void reTrySendTmpOrder2Oms(final HttpRequestBase httpPost,final AtomicInteger atmCount, final int size, AtomicInteger retryCount,CloseableHttpClient client){
		if(retryCount==null)
			retryCount=new AtomicInteger(0);
		logger.debug("errorRetrycount:" + "->"
				+ retryCount.get());
		if (retryCount.addAndGet(1) > 5) {
			atomicAdd(atmCount, size);
			httpPost.releaseConnection();
			BaseHttpClientsComponent.this.closeHttpClient();
			try {
				client.close();
			} catch (IOException e) {
			}
			retryCount.set(-1);
			doHttpExec(httpPost,atmCount,size,retryCount);
		} 
		try {
			CloseableHttpResponse resp=client.execute(httpPost);
			doHttpExec(httpPost,atmCount,size,resp,retryCount);
		} catch (IOException e) {
			reTrySendTmpOrder2Oms(httpPost,atmCount,size,retryCount,client);
		}
	}
	
	private void atomicAdd(
			final AtomicInteger atmCount, final int size) {
		if (atmCount != null)
			atmCount.getAndAdd(size);
	}
}
