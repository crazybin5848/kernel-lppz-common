package com.lppz.elasticsearch.client;

import java.net.InetAddress;
import java.util.List;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.lppz.elasticsearch.client.pool.EsClientPool;
import com.lppz.elasticsearch.client.pool.EsHostAndPort;


public class EsTransportClientFactory implements PooledObjectFactory<TransportClient> {
	private List<EsHostAndPort> hostAndPortList;
	private Settings settings; 
	EsClientPool esClientPool;
	
//	public EsTransportClientFactory(String clusterName,List<EsHostAndPort> hostAndPortList){
//		this.hostAndPortList=hostAndPortList;
//		settings = Settings.settingsBuilder().put("cluster.name", clusterName)
//		        .put("TransportClient.transport.sniff", true).build();
//	}
	
	public EsTransportClientFactory(String clusterName,List<EsHostAndPort> hostAndPortList,int threadPoolSize){
		this.hostAndPortList=hostAndPortList;
		settings = Settings.settingsBuilder().put("cluster.name", clusterName)
				.put("TransportClient.transport.sniff", true).put(org.elasticsearch.common.util.concurrent.EsExecutors.PROCESSORS,threadPoolSize).build();
	}
	public void setEsClientPool(EsClientPool esClientPool) {
		esClientPool.setSettings(settings);
		this.esClientPool = esClientPool;
	}
	@Override
	public PooledObject<TransportClient> makeObject() throws Exception {
		TransportClient transportClient = TransportClient.builder().settings(settings).build();
		for(EsHostAndPort hostAndPort:hostAndPortList){
			transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostAndPort.getHost()), hostAndPort.getPort()));
		}
		return new DefaultPooledObject<TransportClient>(transportClient);
	}
	@Override
	public void destroyObject(PooledObject<TransportClient> pooledTransportClient)
			throws Exception {
		final TransportClient transportClient = pooledTransportClient.getObject();
		if(isOpen(transportClient)){
			if(transportClient!=null)
			transportClient.close();
		}
	}
	private boolean isOpen(TransportClient transportClient) {
		return esClientPool.isOpen(transportClient);
	}
	
	@Override
	public boolean validateObject(PooledObject<TransportClient> pooledTransportClient) {
		final TransportClient transportClient = pooledTransportClient.getObject();
		return isOpen(transportClient);
	}
	
	@Override
	public void activateObject(PooledObject<TransportClient> pooledTransportClient)
			throws Exception {
	}
	@Override
	public void passivateObject(PooledObject<TransportClient> paramPooledObject)
			throws Exception {
	}
  
}