package com.lppz.spark.bean.jedis;

public class JedisSentinelYamlBean extends JedisBaseYamlBean{
	private String masterName;

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}


}
