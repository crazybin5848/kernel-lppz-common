package com.lppz.configuration.jedis;

public class JedisSentinelYamlBean extends JedisBaseYamlBean{
	private String masterName;

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JedisSentinelYamlBean [masterName=");
		builder.append(masterName);
		builder.append("]");
		builder.append(super.toString());
		return builder.toString();
	}

}
