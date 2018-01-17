package com.lppz.elasticsearch.client.pool;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.transport.TransportClientNodesService;
import org.elasticsearch.common.settings.Settings;

import com.lppz.elasticsearch.client.EsTransportClientFactory;

public class EsClientPool extends AbstractEsClientPool<TransportClient> {

	private Settings settings;
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public EsClientPool(String clusterName, List<EsHostAndPort> hostAndPortList,
			final GenericObjectPoolConfig poolConfig,int threadPoolSize) {
		super(poolConfig, new EsTransportClientFactory(clusterName,
				hostAndPortList,threadPoolSize));
		((EsTransportClientFactory) super.factory).setEsClientPool(this);
	}

	public boolean isOpen(TransportClient client) {
		try {
			TransportClientNodesService nodesService = getDynamicObj(
					TransportClient.class, "nodesService", client);
			boolean closed = getDynamicObj(TransportClientNodesService.class,
					"closed", nodesService);
			return !closed;
		} catch (SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void close(TransportClient client){
		if(isOpen(client)){
			returnResource(client);
		}else{
			returnBrokenResource(client);
		}
		
	}
	@SuppressWarnings("unchecked")
	private <T> T getDynamicObj(Class<?> clazz, String name, Object instance) {
		try {
			Field ff = clazz.getDeclaredField(name);
			ff.setAccessible(true);
			return ((T) ff.get(instance));
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
