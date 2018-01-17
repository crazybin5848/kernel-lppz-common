package com.lppz.spark.bean.jedis;

public class JedisClusterYamlBean extends JedisBaseYamlBean{
	private Integer maxRedirections;

	public Integer getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(Integer maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

}
