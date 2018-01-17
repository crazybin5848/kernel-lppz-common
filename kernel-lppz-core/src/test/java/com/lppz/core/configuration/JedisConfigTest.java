package com.lppz.core.configuration;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/kernel-core-test.xml" })
public class JedisConfigTest {
	
	@Resource
	private JedisCluster jedisCluster;
	
	@Resource
	private Jedis jedisSentinelProxy;

	@Test
	public void test(){
		jedisCluster.set("fuck", "1");
	}
	
	@Test
	public void test2(){
		jedisSentinelProxy.set("fuck", "2");
		System.out.println(jedisSentinelProxy.get("fuck"));
	}
}
