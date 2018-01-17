package com.lppz.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoPushTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		initData();
	}

	private static int maxWaitTime = 300000;
	private static int socketTimeout = 200000;
	private static int maxConnectionLifeTime = 300000;
	private static int connectTimeout = 60000;
	private final static int POOLSIZE = 500;

	@SuppressWarnings("deprecation")
	private static void initData() throws InterruptedException {
		MongoClientOptions options = MongoClientOptions.builder()
				.connectionsPerHost(POOLSIZE).maxWaitTime(maxWaitTime)
				.socketTimeout(socketTimeout)
				.maxConnectionLifeTime(maxConnectionLifeTime)
				.connectTimeout(connectTimeout).build();
		final List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		// seeds.add(new ServerAddress("192.168.37.230:50000"));
		seeds.add(new ServerAddress("192.168.37.242:50000"));
		// seeds.add(new ServerAddress("192.168.37.245:50000"));
		// seeds.add(new ServerAddress("192.168.37.246:50000"));
		// seeds.add(new ServerAddress("192.168.37.247:50000"));
		final MongoClient db = new MongoClient(seeds, options);
		final DB mydb = db.getDB("test");
		final DBCollection coll = mydb.getCollection("orders");
		final AtomicInteger ai = new AtomicInteger(0);
		final AtomicInteger aj = new AtomicInteger(0);
		int size = 200;
		long start = new Date().getTime();
		List<BasicDBObject> list = new ArrayList<BasicDBObject>();
		int i=0;
//		BasicDBObject doc = new BasicDBObject();
//		doc.put("userid",
//				Long.toString(Math.abs(UUID.randomUUID()
//						.getMostSignificantBits()), 36));
//		doc.put("order_id", i + "orderid");
//		doc.put("company_id", 505 + i);
//		doc.put("user_id", 180225429 + i);
//		doc.put("fetcher_id", 59803 + i);
//		doc.put("fetch_schedule_begin", new Date());
//		doc.put("fetch_schedule_end", new Date());
//		doc.put("sender_id", 59803 + i);
//		doc.put("mail_no", "000000");
//		doc.put("mail_type", "301");
//		doc.put("order_code", "LP10012700003959" + i);
//		doc.put("order_status", 30);
//		doc.put("prev_order_id", 0);
//		doc.put("trade_id", 2010012706189794L + i);
//		doc.put("goods_remark", "");
//		doc.put("receiver_name", " 凯撒");
//		doc.put("receiver_wangwang_id", "sanglin01");
//		doc.put("receiver_mobile_phone", "13021525841");
//		doc.put("receiver_zip_code", "650045");
//		doc.put("receiver_telephone", "13868117135");
//		doc.put("receiver_county_id", 350102);
//		doc.put("receiver_address",
//				"福建省^^^福州市^^^鼓楼区^^^的萨芬萨芬萨芬的12号");
//		doc.put("gmt_create", new Date());
//		doc.put("gmt_modified", new Date());
//		doc.put("status_reason", "");
//		doc.put("logis_type", 0);
//		doc.put("seller_wangwang_id", "tbtest943" + i);
//		doc.put("seller_send_confirm", 0);
//		doc.put("shipping", 2);
//		doc.put("company_code", "");
//		doc.put("taobao_trade_id", "2232358300" + i);
//		doc.put("options", 2);
//		doc.put("shipping2", 0);
//		doc.put("order_source", 0);
//		doc.put("status_date", new Date());
//		doc.put("timeout_status", 2);
//		doc.put("feature", "ip=127.0.0.1;SFID=");
//		doc.put("service_fee", 0);
//		doc.put("seller_store_id", "23100");
//		doc.put("items_value", 23100);
//		doc.put("pre_status", 0);
//		doc.put("ticket_id", "");
//		doc.put("tfs_url", "T1DoBbXctCXXXXXXXX");
//		list.add(doc);
//		coll.insert(list);
		
//		List<BasicDBObject> ll=new ArrayList<BasicDBObject>();
//		for(int j=0;j<50000;j++){
//			BasicDBObject obj=new BasicDBObject("a",j).append("fuck", "fuck").
//					append("ads", "qqq").append("231", 456);
//			ll.add(obj);
//		}
//		BasicDBObject fromUid=new BasicDBObject("toUid",ll.toArray(new BasicDBObject[0]));
//		BasicDBObject update=new BasicDBObject("$pushAll",fromUid);
//		coll.update(new BasicDBObject("userid","a3venorzpiit"), update);
		
		for(int k=0;k<10;k++){
//			start=System.currentTimeMillis();
//		DBObject explainResult = coll.find(new BasicDBObject("userid","a3venorzpiit"),new BasicDBObject("toUid","{\"$slice\":[0,1]}")).explain();
//		System.out.println(explainResult);
			DBCursor cur=coll.find(new BasicDBObject("userid","a3venorzpiit"),new BasicDBObject("toUid",new BasicDBObject("$slice",new Integer[]{3,2})));
//			DBCursor cur=coll.find(new BasicDBObject("userid","a3venorzpiit"),new BasicDBObject("toUid",1));
			while(cur.hasNext()){  
			      DBObject document = cur.next(); 
			      BasicDBList bbl=(BasicDBList)document.get("fromUid");
			      if(bbl!=null)
			      System.out.println(bbl.size());
			      bbl=(BasicDBList)document.get("toUid");
			      if(bbl!=null)
			    	  System.out.println(bbl.size());
			      System.out.println(bbl);
			  } 
			System.out.println(System.currentTimeMillis()-start);
		}
	}
	
}