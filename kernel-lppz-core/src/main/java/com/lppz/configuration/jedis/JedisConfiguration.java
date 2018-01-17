package com.lppz.configuration.jedis;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.lppz.util.jedis.cluster.JedisSequenceUtil;

@Configuration
public class JedisConfiguration extends BaseJedisClusterConfiguration implements ApplicationContextAware {
	private static final int LOAD_FIRST_JEDIS_CONFIG_ONLY = 0;
	private static final Logger LOG = LoggerFactory.getLogger(JedisConfiguration.class);
	private ConfigurableApplicationContext applicationContext;
	
	@Bean(name = "jedisCluster",destroyMethod="close")
	public JedisCluster jedisCluster() throws IOException {
		Resource[] resources = null;
		JedisCluster jedisCluster = null;
		try {
			resources = getResources("classpath*:/META-INF/jedis-cluster*.yaml");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return jedisCluster;
		}
		if(!ArrayUtils.isEmpty(resources)){
			Resource jedisRes = resources[LOAD_FIRST_JEDIS_CONFIG_ONLY];
			jedisCluster = getJedisCluster(jedisRes.getInputStream());
		}
//		if(jedisCluster!=null){
//			JedisSequenceUtil.getInstance().setJedisCluster((OmsJedisCluster) jedisCluster);
//		}
		return jedisCluster;
	}
	
	@Bean(name = "jedisSentinelProxy",destroyMethod="close")
	public Jedis jedisSentinelProxy() throws IOException {
		Resource[] resources = null;
		Jedis jedis=null;
		try {
			resources = getResources("classpath*:/META-INF/jedis-sentinel*.yaml");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return jedis;
		}
		if(!ArrayUtils.isEmpty(resources)){
			Resource jedisRes = resources[LOAD_FIRST_JEDIS_CONFIG_ONLY];
			jedis = buildJedisSentinelProxy(jedisRes.getInputStream());
		}
		if(jedis!=null){
			JedisSequenceUtil.getInstance().setJedis(jedis);
		}
		return jedis;
	}
	
	@Override
	public JedisCluster reloadJedisCluster() {
		JedisCluster cluster = null;
		String beanName = "jedisCluster";
		try {
			cluster = jedisCluster();
			
			DefaultListableBeanFactory  fcy = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();  
			fcy.destroySingleton(beanName);
	        fcy.registerSingleton(beanName, cluster);
			// TODO Auto-generated method stub
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return cluster;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}
