package com.lppz.configuration.jedis;

import java.util.List;
import java.util.Properties;

public class JedisBaseYamlBean {
	protected JedisClusterPool jedisClusterPool;
	protected List<Properties> jedisClusterNode;
	protected Integer timeout;

	public JedisClusterPool getJedisClusterPool() {
		return jedisClusterPool;
	}

	public void setJedisClusterPool(JedisClusterPool jedisClusterPool) {
		this.jedisClusterPool = jedisClusterPool;
	}

	public List<Properties> getJedisClusterNode() {
		return jedisClusterNode;
	}

	public void setJedisClusterNode(List<Properties> jedisClusterNode) {
		this.jedisClusterNode = jedisClusterNode;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JedisBaseYamlBean [jedisClusterPool=");
		builder.append(jedisClusterPool);
		builder.append(", jedisClusterNode=");
		builder.append(jedisClusterNode);
		builder.append(", timeout=");
		builder.append(timeout);
		builder.append("]");
		return builder.toString();
	}
}
