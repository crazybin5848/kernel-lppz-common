package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.lppz.mongoapi.dao.MongoDao;
import com.lppz.mongoapi.dao.impl.MongoDaoImpl;

public class PushPullMongoApiTest extends SpringBaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(PushPullMongoApiTest.class);
	
	@Resource
	private MongoDao mongoDao;
	
	@Resource
	private Jedis jedis;
	
	private final ConcurrentHashMap<String, String> map= new ConcurrentHashMap<>();
	
	private String table = "SnsUserFollowed";
	private String arrayField = "followed";
	private String pk = "userId";
	private String pkValue = "111222333";
	
	@Test
	public void pushTest(){
		List<Object> documents=new ArrayList<Object>();
		for(int j=0;j<501;j++){
			long count = jedis.incr("testUserId");
			Document obj=new Document().
//					append("userId","111"+count).
					append("name", "na"+count).
					append("followedCount", "1").append("state", 1).
					append("createdTime", System.currentTimeMillis()).append("isFriend", 0);
			documents.add(obj);
		}
		
		mongoDao.push(table, pk, pkValue, arrayField, documents);
		
	}
	
	@Test
	public void pushMutilTest(){
		ExecutorService threadPool = Executors.newFixedThreadPool(50);
		for(int j=0;j<53000;j++){
			threadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					List<Object> documents=new ArrayList<Object>();
					long count = jedis.incr("testUserId");
					String arrayUserId = "111"+count;
					map.put(pkValue, arrayUserId);
					Document obj=new Document().
							append("userId",arrayUserId).
							append("name", "na"+count).
							append("followedCount", "1").append("state", 1).
							append("createdTime", System.currentTimeMillis()).append("isFriend", 0);
					documents.add(obj);
					mongoDao.push(table, pk, pkValue, arrayField, documents);
				}
			});
		}
		
		try {
			Thread.sleep(10000000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 边push边pull
	 * pull根据push后的数据进程
	 */
	@Test
	public void pushAndPullMutilTest(){
		pushMutilTest();
		pullMutilTest();
		
		try {
			Thread.sleep(2000000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pullMutilTest(){
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		for(int j=0;j<53;j++){
			threadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					for (Entry<String, String> entry : map.entrySet()) {
						logger.info("-------------map size={} key={} value={}",map.size(),entry.getKey(),entry.getValue());
						mongoDao.pull(table, pk, entry.getKey(), arrayField+".userId", entry.getValue());
						break;
					}
				}
			});
		}
		
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void pullTest(){
		String arraySubField="userId";
		String arraySubValue="1112941";
		
		mongoDao.pull(table, pk, pkValue, arrayField+"."+arraySubField, arraySubValue);
		
		try {
			Thread.sleep(100000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Ignore
	@Test
	public void resetRedisArrayInfo(){
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String fieldKey = MongoDaoImpl.getArraySizeField(table,arrayField);
		String kegNoField = MongoDaoImpl.getKegNoField(arrayField);
		System.out.println("fieldsize="+jedis.hset(redisKey, fieldKey, "0"));
		System.out.println("kegNo="+jedis.hset(redisKey, kegNoField, "0"));
	}
	
	@Test
	public void getRedisArrayInfo(){
		String redisKey = MongoDaoImpl.getRedisKey(pkValue);
		String fieldKey = MongoDaoImpl.getArraySizeField(table,arrayField);
		String kegNoField = MongoDaoImpl.getKegNoField(arrayField);
		System.out.println("fieldsize="+jedis.hget(redisKey, fieldKey));
		System.out.println("kegNo="+jedis.hget(redisKey, kegNoField));
	}
	
	@Test
	public void saddTest(){
		//redis key=SnsUserFollowed:userId:111222333:followed.userId,keg=98
		String key = "SnsUserFollowed:userId:111222333:followed.userId";
		String keg="98";
		System.out.println("sadd line:"+jedis.sadd(MongoDaoImpl.fixKegJedisQueue.getBytes(), key.getBytes()));
		System.out.println("sadd keg:"+jedis.sadd(key.getBytes(), keg.getBytes()));
	}
	
	@Test
	public void smembersTest(){
		String key = "SnsUserFollowed:userId:111222333:followed.userId";
		Set<byte[]> kegs = jedis.smembers(key.getBytes());
		 StringBuilder sb = new StringBuilder();
		 for (byte[] bytes : kegs) {
			 sb.append(new String(bytes)).append(",");
		}
		System.out.println("smembers:"+sb.toString());
		if (sb.length() > 0) {
			byte[] keg = jedis.spop(key.getBytes());
			System.out.println("keg="+new String(keg));
		}
	}
	
	
}
