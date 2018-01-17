package com.lppz.elasticsearch;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.support.Headers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;

import com.lppz.elasticsearch.client.pool.EsClientPool;

public class EsTransportClient extends AbstractClient {

	private EsClientPool esPool;
	
	public EsTransportClient(Settings settings, ThreadPool threadPool,
			Headers headers) {
		super(settings, threadPool, headers);
	}
	
	public EsTransportClient(EsClientPool esPool,Settings settings, ThreadPool threadPool,
			Headers headers) {
		this(settings, threadPool, headers);
		setEsPool(esPool);
	}

	@Override
	protected <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> void doExecute(
			Action<Request, Response, RequestBuilder> action, Request request,
			ActionListener<Response> listener) {
	}

	@Override
	public void close() {
		if(esPool != null){
			esPool.close();
		}
	}

	public EsClientPool getEsPool() {
		return esPool;
	}

	public void setEsPool(EsClientPool esPool) {
		this.esPool = esPool;
	}
}