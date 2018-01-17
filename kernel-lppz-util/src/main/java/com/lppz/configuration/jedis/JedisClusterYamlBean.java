package com.lppz.configuration.jedis;

public class JedisClusterYamlBean extends JedisBaseYamlBean{
	private Integer maxRedirections;

	public Integer getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(Integer maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JedisClusterYamlBean [maxRedirections=");
		builder.append(maxRedirections);
		builder.append("]");
		builder.append(super.toString());
		return builder.toString();
	}
}
