package com.lppz.core.configuration;

import redis.clients.lppz.config.ShardingFunction;

public class TestShardingFunction implements ShardingFunction {
	
	private String key;
	
	public TestShardingFunction() {
	}
	
	public TestShardingFunction(String key) {
		this.key = key;
	}
	
	@Override
	public int getShardingIndex() {
		return Math.abs(key.hashCode());
	}

}
