package com.lppz.mongoapi;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.set.SetParams;

import com.lppz.mongoapi.dao.impl.MongoDaoImpl;

public class JedisTest extends SpringBaseTest {
	@Resource
	protected Jedis jedis;
	String userid = "1145796050";
	String field = "following";
	String key = null;
	
	@Test
	public void getKey(){
		String hgetKey = "app:::1116215274";
//		hgetKey = "1twtcd7725g1o:::20170725";
//		String rpopKey="app:::redisSyncKey";
//		System.out.println(jedis.smembers("app:::orders:::20170725"));
		System.out.println(hgetKey + " = "+jedis.hgetAll(hgetKey));
//		System.out.println(hgetKey + " = "+jedis.hget(hgetKey,"visitCount"));
//		System.out.println("1twtcd7725g1o:::20170725" + " = "+jedis.hget("1twtcd7725g1o:::20170725","visitCount"));
//		System.out.println(rpopKey + " = " + jedis.lpop(rpopKey));
	}
	@Test
	public void hset(){
		String hgetKey = "app:::1148258375";
		String field = "app:::SnsUserCollect:::collectedStatuses:::keg";
		System.out.println(hgetKey + " = "+jedis.hset(hgetKey, field, "31"));
	}
	@Ignore
	@Test
	public void saddTest(){
		String key = "saddTestKey";
		jedis.sadd(key, new String[]{"1","2","3","4","5","6"});
		System.out.println(jedis.smembers(key));
	}
	@Ignore
	@Test
	public void lpushTest(){
		String key = "lpushTestKey";
//		jedis.lpush(key, new String[]{"1","2","3","4","5","6"});
		for (int i = 0; i < 10; i++) {
			
			System.out.println(jedis.rpop(key));
		}
	}
	
	@Test
	public void testArrayLengthGet(){
		String pkValue = "1146017271";
		String key = "userId";
		String arrayField = "followed";
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String fieldKey = MongoDaoImpl.getArraySizeField(arrayField);
		String kegNoField = MongoDaoImpl.getKegNoField(arrayField);
		
		int arrayLength = getArrayLenth(pkValue, arrayField);
		int kegNo = getMaxKegNo(pkValue, arrayField);
		
		System.out.println("key="+key+",pkValue="+pkValue+",arrayField="+arrayField+",arrayLength="+arrayLength+",kegNo="+kegNo);
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getArrayLenth(String pkValue, String fieldName){
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String fieldKey = MongoDaoImpl.getArraySizeField(fieldName);
		String arrayLengthV = jedis.hget(redisKey, fieldKey);
		long arrayLength = StringUtils.isBlank(arrayLengthV)?0:Long.valueOf(arrayLengthV);
		return (int) arrayLength;
	}
	
	private int getMaxKegNo(String pkValue, String fieldName){
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String kegNoField = MongoDaoImpl.getKegNoField(fieldName);
		String kegNoV = jedis.hget(redisKey, kegNoField);
		long kegNo = StringUtils.isBlank(kegNoV)?1:Long.valueOf(kegNoV);
		return (int) kegNo;
	}
	
	@Test
	public void testGet(){
		key = "app:::SnsUser:::20170731";
		key = "app:::SnsUserBehaviourReward:::20170815";
//		key = String.format(MongoDaoImpl.keyFormat, userid);
//		System.out.println(jedis.hgetAll(key));
//		System.out.println(jedis.smembers(key));
//		System.out.println(jedis.hget(String.format(MongoDaoImpl.keyFormat, userid),String.format(MongoDaoImpl.keyFormat, field)));
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testhset(){
		System.out.println(jedis.hset(String.format(MongoDaoImpl.keyFormat, userid), String.format(MongoDaoImpl.keyFormat, "following"), "14"));
	}
	
	@Ignore
	@Test
	public void testDel(){
		String key = String.format(MongoDaoImpl.keyFormat, userid);
//		Map<String,String> map = jedis.hgetAll(key);
//		for (Entry<String, String> entry : map.entrySet()) {
//			System.out.println(jedis.hdel(key,entry.getKey()));
//		}
		System.out.println(jedis.hdel(key,"following"));
	}
	
	@Test
	public void test1(){
		String key = "tttttkey1";
		String value="xx";
		System.out.println("1="+jedis.set(key, value, SetParams.setParams().nx().ex(5)));
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("2="+jedis.set(key, value, SetParams.setParams().nx().ex(5)));
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("3="+jedis.get(key));
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("4="+jedis.get(key));
		
		
		
	}
}
