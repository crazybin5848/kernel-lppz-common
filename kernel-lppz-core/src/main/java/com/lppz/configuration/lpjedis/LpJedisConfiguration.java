package com.lppz.configuration.lpjedis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lppz.configuration.jedis.BaseJedisClusterConfiguration;

import redis.clients.jedis.JedisCluster;
import redis.clients.lppz.config.LpJedisInit;
import redis.clients.lppz.jedis.LpJedis;
import redis.clients.zkprocess.zk2local.Zk2Local;

@Configuration
public class LpJedisConfiguration extends BaseJedisClusterConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(LpJedisConfiguration.class);
	
	@Bean(name = "lpJedis",destroyMethod="close")
	public LpJedis jedisSentinelProxy() throws IOException {
		LpJedis jedis=null;
		try {
			Zk2Local.loadZktoFile();
			jedis = LpJedisInit.getInstance().getJedis();
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return jedis;
		}
		return jedis;
	}
	
	@Override
	public JedisCluster reloadJedisCluster() {
		// TODO Auto-generated method stub
		return null;
	}
}
