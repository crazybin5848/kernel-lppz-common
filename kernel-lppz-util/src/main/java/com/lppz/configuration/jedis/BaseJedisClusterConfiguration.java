package com.lppz.configuration.jedis;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.lppz.util.jedis.cluster.concurrent.OmsJedisCluster;

public abstract class BaseJedisClusterConfiguration extends PathMatchingResourcePatternResolver{
	private AtomicInteger reloadNum = new AtomicInteger();
	public JedisCluster getJedisCluster(InputStream jedisResInput) throws IOException {
		JedisCluster jedisCluster;
		JedisClusterYamlBean tmpjedisObj = (JedisClusterYamlBean) new Yaml()
				.load(jedisResInput);
		JedisPoolConfig jpoolConfig = getJedisPool(tmpjedisObj.getJedisClusterPool());
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		if (CollectionUtils.isEmpty(tmpjedisObj.getJedisClusterNode())) {
			throw new RuntimeException("jedis need a set of cluster nodes!!!");
		} else {
			for (Properties prop : tmpjedisObj.getJedisClusterNode()) {
				nodes.add(new HostAndPort((String) prop.get("host"),(Integer) prop.get("port")));
			}
		}
		Integer timeout = (tmpjedisObj.getTimeout() == null) ? 50000
				: (Integer) tmpjedisObj.getTimeout();
		Integer maxRedirections = (tmpjedisObj.getMaxRedirections() == null) ? 5
				: (Integer) tmpjedisObj.getMaxRedirections();
		jedisCluster = new OmsJedisCluster(nodes, timeout,
				maxRedirections, jpoolConfig);
		return buildProxyJedisCluster(nodes, timeout, maxRedirections, jpoolConfig, jedisCluster);
	}

	private JedisPoolConfig getJedisPool(JedisClusterPool tmpjedisObj) {
		String yaml = new Yaml().dumpAsMap(tmpjedisObj);
		JedisPoolConfig jpoolConfig = new Yaml().loadAs(yaml,
				JedisPoolConfig.class);
		if (jpoolConfig == null) {
			throw new RuntimeException("jedis need a poolConfig!!!");
		}
		return jpoolConfig;
	}
	
	public Jedis buildJedisSentinelProxy(InputStream inputStream) {
		JedisSentinelYamlBean tmpjedisObj = (JedisSentinelYamlBean) new Yaml().load(inputStream);
		JedisPoolConfig jpoolConfig = getJedisPool(tmpjedisObj.getJedisClusterPool());
		Set<String> nodes = new HashSet<String>();
		if (CollectionUtils.isEmpty(tmpjedisObj.getJedisClusterNode())) {
			throw new RuntimeException("jedis need a set of sentinel nodes!!!");
		} else {
			for (Properties prop : tmpjedisObj.getJedisClusterNode()) {
				nodes.add((String) prop.get("host")+":"+(Integer) prop.get("port"));
			}
		}
		Integer timeout = (tmpjedisObj.getTimeout() == null) ? 50000
				: (Integer) tmpjedisObj.getTimeout();
		JedisSentinelPool jesenpool=new JedisSentinelPool(tmpjedisObj.getMasterName(),nodes,jpoolConfig,timeout);
		return buildProxyRedis(jesenpool);
	}

	private Jedis buildProxyRedis(final JedisSentinelPool jesenpool) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Jedis.class);
		enhancer.setCallback(new MethodInterceptor(){
			public Object intercept(Object proxy, Method method, Object[] args,
					MethodProxy methodProxy) throws Throwable {
				Jedis jjdes=jesenpool.getResource();
				try{
					return method.invoke(jjdes,args);
				}finally{
					jjdes.close();
				}
			}
		});
		enhancer.setClassLoader(Jedis.class.getClassLoader());
		return (Jedis) enhancer.create();
	}
	
	private JedisCluster buildProxyJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout,
			int maxRedirections, GenericObjectPoolConfig poolConfig, final JedisCluster jedisCluster) {
		Class[] args = new Class[]{Set.class, Integer.class, Integer.class, GenericObjectPoolConfig.class};
		Object[] argsValue = new Object[]{jedisClusterNode, timeout, maxRedirections, poolConfig};
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(OmsJedisCluster.class);
		enhancer.setCallback(new MethodInterceptor(){
			public Object intercept(Object proxy, Method method, Object[] args,
					MethodProxy methodProxy) throws Throwable {
				try{
					return method.invoke(jedisCluster, args);
				}catch(InvocationTargetException e){
					 if (e.getCause() instanceof JedisConnectionException) {
						if (reloadNum.incrementAndGet() <= 5) {
							JedisCluster jedisCluster = reloadJedisCluster();
							return method.invoke(jedisCluster,args);
						}
					}
					throw e;
				}
			}
		});
		enhancer.setClassLoader(Jedis.class.getClassLoader());
		return (JedisCluster) enhancer.create(args, argsValue);
	}
	
	public abstract JedisCluster reloadJedisCluster();
}