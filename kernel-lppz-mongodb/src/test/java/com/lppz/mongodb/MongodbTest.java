package com.lppz.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.lppz.mongoapi.SpringBaseTest;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
public class MongodbTest extends SpringBaseTest {
 
	@Resource
	protected Jedis jedis;
	public static final String redisKey = "testMongodbId";

	
	@Test
	public void insertTest(){
		try {
			initData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
 
 static ExecutorService executor = Executors.newFixedThreadPool(200);
 private static int maxWaitTime=300000;
 private static int socketTimeout=200000;
private static int maxConnectionLifeTime=300000;
private static int connectTimeout=60000;
 private final static int POOLSIZE = 500;
 @SuppressWarnings("deprecation")
private void initData() throws InterruptedException {
	 MongoClientOptions options = MongoClientOptions.builder()  
             .connectionsPerHost(POOLSIZE)  
             .maxWaitTime(maxWaitTime)  
             .socketTimeout(socketTimeout)  
             .maxConnectionLifeTime(maxConnectionLifeTime) 
             .connectTimeout(connectTimeout).build();  
	 final List<ServerAddress> seeds=new ArrayList<ServerAddress>();
//	 seeds.add(new ServerAddress("192.168.37.230:50000"));
//	 seeds.add(new ServerAddress("192.168.37.242:50000"));
	 seeds.add(new ServerAddress("192.168.37.245:50000"));
//	 seeds.add(new ServerAddress("192.168.37.246:50000"));
//	 seeds.add(new ServerAddress("192.168.37.247:50000"));
//	 seeds.add(new ServerAddress("10.8.102.168:50000"));
   final MongoClient db = new MongoClient(seeds,options);
   final DB mydb = db.getDB("lppzappsns");
   final DBCollection coll = mydb.getCollection("SnsUser");
   final AtomicInteger ai = new AtomicInteger(0);
   final AtomicInteger aj = new AtomicInteger(0);
   int size=200;
   long start = new Date().getTime();
   final Jedis jds = jedis;
   for(int j=0;j<size;j++){
	   executor.execute(new Runnable() {
		@Override
		public void run() {
			try {
				List<BasicDBObject> list=new ArrayList<BasicDBObject>();
				   for (long i = 1; i <= 100000; i++) {
				    BasicDBObject doc = new BasicDBObject();
//				    doc.put("userid", Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()),36));
				    long index = jds.incrBy(redisKey, 1);
//				    System.out.println("index="+index);
				    doc.put("userId", index);
				    doc.put("name", "name"+index);
				    list.add(doc);
				    coll.insert(list);
				    list.clear();
				   }
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				ai.incrementAndGet();
			}
		}
	});
   }
   while(true){
	   if(ai.get()==size){
		   db.close();
		   long endTime = new Date().getTime();
		   System.out.println(endTime - start);
		   return;
	   }
	   Thread.sleep(500);
	   System.out.println(aj.get());
   }
}  
}