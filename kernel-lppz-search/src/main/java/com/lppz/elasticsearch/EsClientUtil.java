package com.lppz.elasticsearch;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.support.Headers;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import com.lppz.configuration.es.EsBaseYamlBean;
import com.lppz.elasticsearch.client.pool.EsClientPool;
import com.lppz.elasticsearch.client.pool.EsHostAndPort;

public class EsClientUtil extends PathMatchingResourcePatternResolver {
	public static AbstractClient buildPoolClientProxy(InputStream inputStream) {
		EsBaseYamlBean tmpjedisObj = (EsBaseYamlBean) new Yaml().load(inputStream);
		return buildPoolClientProxy(tmpjedisObj);
	}

	public static AbstractClient buildPoolClientProxy(EsBaseYamlBean tmpjedisObj) {
		String yaml = new Yaml().dumpAsMap(tmpjedisObj.getEsClusterPool());
		GenericObjectPoolConfig jpoolConfig = new Yaml().loadAs(yaml,
				GenericObjectPoolConfig.class);
		if (jpoolConfig == null) {
			throw new RuntimeException("es need a poolConfig!!!");
		}
		List<EsHostAndPort> hostAndPortList=new ArrayList<EsHostAndPort>();
		if (CollectionUtils.isEmpty(tmpjedisObj.getEsclusterNode())) {
			throw new RuntimeException("es need a set of sentinel nodes!!!");
		} else {
			for (Properties prop : tmpjedisObj.getEsclusterNode()) {
				hostAndPortList.add(new EsHostAndPort((String) prop.get("host"),(Integer) prop.get("port")));
			}
		}
		jpoolConfig.setMinIdle(10);
		EsClientPool esPool=new EsClientPool(tmpjedisObj.getClusterName(),hostAndPortList,jpoolConfig,tmpjedisObj.getThreadPoolSize());
		client=esPool.getResource();
		return buildEsProxy(esPool);
	}

	static TransportClient client=null;
	private static AbstractClient buildEsProxy(final EsClientPool esPool) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AbstractClient.class);
		enhancer.setCallback(new MethodInterceptor(){
			public Object intercept(Object proxy, Method method, Object[] args,
					MethodProxy methodProxy) throws Throwable {
				if("close".equalsIgnoreCase(method.getName())&&method.getParameterTypes().length==0){
					esPool.close();
					return null;
				}
//				TransportClient client=esPool.getResource();
				try{
					method.setAccessible(true);
					return method.invoke(client,args);
				}finally{
//					esPool.close(client);
				}
			}
		});
		enhancer.setClassLoader(AbstractClient.class.getClassLoader());
		final ThreadPool threadPool = new ThreadPool(esPool.getSettings());
		final Headers headers = new Headers(esPool.getSettings());
		return (AbstractClient) enhancer.create(new Class[]{Settings.class,ThreadPool.class,Headers.class},
				new Object[]{esPool.getSettings(),threadPool,headers});
	}
}
