package com.lppz.core.configuration;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.lppz.jedis.LpJedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/kernel-core-test.xml" })
public class LpJedisConfigTest {
	
	@Resource
	private LpJedis lpJedis;
	
	@Test
	public void testSomeOp(){
		String key = "fuck";
		TestShardingFunction shard = new TestShardingFunction(key);
		
		System.out.println("set : " +lpJedis.set(shard,key, "1"));
		
		System.out.println("incr : " + lpJedis.incr(shard, key));
		
		System.out.println("get : " + lpJedis.get(shard, key));
		
		System.out.println("del : " + lpJedis.del(shard, key));
	}
	
}
