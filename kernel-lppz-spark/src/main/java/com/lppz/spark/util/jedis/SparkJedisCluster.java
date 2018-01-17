package com.lppz.spark.util.jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class SparkJedisCluster extends JedisCluster{
	private static final Logger LOG = LoggerFactory.getLogger(SparkJedisCluster.class);
	@Override
	public String set(String key, String value) {
		try {
			return super.set(key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			try {
				Thread.sleep(100);
				set(key, value);
			} catch (InterruptedException e1) {
			}
		}
		return null;
	}
	
	@Override
	public String set(byte[] key, byte[] value) {
		try {
			return super.set(key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			try {
				Thread.sleep(100);
				set(key, value);
			} catch (InterruptedException e1) {
			}
		}
		return null;
	}
	
	@Override
	public String get(String key) {
		try {
			return super.get(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			try {
				Thread.sleep(100);
				return get(key);
			} catch (InterruptedException e1) {
			}
		}
		return null;
	}
	
	@Override
	public byte[] get(byte[] key) {
		try {
			return super.get(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			try {
				Thread.sleep(100);
				return get(key);
			} catch (InterruptedException e1) {
			}
		}
		return null;
	}


	public SparkJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout,
			int maxRedirections, GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, maxRedirections, poolConfig);
		for(HostAndPort ha:jedisClusterNode){
			jedisGroup.add(new Jedis(ha.getHost(),ha.getPort()));
		}
	}
	
	private List<Jedis> jedisGroup=new ArrayList<Jedis>();

	public List<Jedis> getJedisGroup() {
		return jedisGroup;
	}
	
	public Set<String> keys(final String pattern) {
		Set<String> s = new HashSet<String>();
		for (Jedis j : jedisGroup) {
			Set<String> sj = null;
			try {
				sj = j.keys(pattern);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			if (sj != null)
				s.addAll(sj);
		}
		return s;
	}
}
