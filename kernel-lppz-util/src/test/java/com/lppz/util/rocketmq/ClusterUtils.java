package com.lppz.util.rocketmq;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class ClusterUtils {

	public static JedisCluster getJedisCluster() {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("192.168.37.243", 7000));
		jedisClusterNodes.add(new HostAndPort("192.168.37.245", 7000));
		jedisClusterNodes.add(new HostAndPort("192.168.37.247", 7000));
		// 3个master 节点
		return new JedisCluster(jedisClusterNodes);
	}

}
