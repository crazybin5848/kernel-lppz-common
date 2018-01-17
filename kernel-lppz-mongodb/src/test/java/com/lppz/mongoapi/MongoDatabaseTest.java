package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import com.google.common.collect.Collections2;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class MongoDatabaseTest extends BaseTest{

	@Test
	public void insert() throws InterruptedException {
		MongoCollection<Document> coll = database.getCollection("orders");
		final AtomicInteger ai = new AtomicInteger(0);
		final AtomicInteger aj = new AtomicInteger(0);
		int size = 200;
		List<Document> list = new ArrayList<Document>();
		int i=0;
		Document doc = new Document();
		doc.put("userid",
				Long.toString(Math.abs(UUID.randomUUID()
						.getMostSignificantBits()), 36));
		doc.put("order_id", i + "orderid");
		doc.put("company_id", 505 + i);
		doc.put("user_id", 180225429 + i);
		doc.put("fetcher_id", 59803 + i);
		doc.put("fetch_schedule_begin", new Date());
		doc.put("fetch_schedule_end", new Date());
		doc.put("sender_id", 59803 + i);
		doc.put("mail_no", "000000");
		doc.put("mail_type", "301");
		doc.put("order_code", "LP10012700003959" + i);
		doc.put("order_status", 30);
		doc.put("prev_order_id", 0);
		doc.put("trade_id", 2010012706189794L + i);
		doc.put("goods_remark", "");
		doc.put("receiver_name", " 凯撒");
		doc.put("receiver_wangwang_id", "sanglin01");
		doc.put("receiver_mobile_phone", "13021525841");
		doc.put("receiver_zip_code", "650045");
		doc.put("receiver_telephone", "13868117135");
		doc.put("receiver_county_id", 350102);
		doc.put("receiver_address",
				"福建省^^^福州市^^^鼓楼区^^^的萨芬萨芬萨芬的12号");
		doc.put("gmt_create", new Date());
		doc.put("gmt_modified", new Date());
		doc.put("status_reason", "");
		doc.put("logis_type", 0);
		doc.put("seller_wangwang_id", "tbtest943" + i);
		doc.put("seller_send_confirm", 0);
		doc.put("shipping", 2);
		doc.put("company_code", "");
		doc.put("taobao_trade_id", "2232358300" + i);
		doc.put("options", 2);
		doc.put("shipping2", 0);
		doc.put("order_source", 0);
		doc.put("status_date", new Date());
		doc.put("timeout_status", 2);
		doc.put("feature", "ip=127.0.0.1;SFID=");
		doc.put("service_fee", 0);
		doc.put("seller_store_id", "23100");
		doc.put("items_value", 23100);
		doc.put("pre_status", 0);
		doc.put("ticket_id", "");
		doc.put("tfs_url", "T1DoBbXctCXXXXXXXX");
		System.out.println(doc.get("userid"));
		list.add(doc);
		coll.insertMany(list);
		
	}
	
	public void pushAll(){
		List<BasicDBObject> ll=new ArrayList<BasicDBObject>();
		MongoCollection<Document> coll = database.getCollection("orders");
		for(int j=0;j<50000;j++){
			BasicDBObject obj=new BasicDBObject("a",j).append("fuck", "fuck").
					append("ads", "qqq").append("231", 456);
			ll.add(obj);
		}
		Document fromUid=new Document("toUid",ll.toArray(new Document[0]));
		Document update=new Document("$pushAll",fromUid);
		coll.updateMany(new Document("userid","a3venorzpiit"), update);
	}
	
	@Test
	public void findInAppTest(){
		String table = null;
		table = "SnsUser";
		long start = new Date().getTime();
		MongoCollection<Document> coll = database.getCollection(table);
		for(int k=0;k<1;k++){
			start=System.currentTimeMillis();
			Document sort = new Document();
//		sort.append("order_code", -1).append("userid", 1);
			List<String> fieldNames = Arrays.asList("userId"/*,"order_code"*/);
			Bson filter = Filters.eq("user_id", 180225429);
			FindIterable<Document> fi = coll.find().projection(Projections.include(fieldNames));
			if (sort != null && !sort.isEmpty()) {
				fi.sort(sort);
			}
			MongoCursor<Document> cursor = fi.iterator();
			Set<String> userIds = new HashSet<>();
			while(cursor.hasNext()){
				Document document = cursor.next(); 
				userIds.add((String)document.get("userId"));
			}
			System.out.println(userIds.size());
			System.out.println(StringUtils.join(userIds, ","));
			System.out.println("cost : "+(System.currentTimeMillis()-start));
		}
	}
	@Test
	public void findTest(){
		String table = null;
//		table = "orders";
		table = "SnsUser";
		long start = new Date().getTime();
		MongoCollection<Document> coll = database.getCollection(table);
		for(int k=0;k<1;k++){
		start=System.currentTimeMillis();
		Document sort = new Document();
//		sort.append("order_code", -1).append("userid", 1);
//		MongoCursor<Document> cursor = coll.find(Filters.and(Filters.eq("userid", "a3venorzpiit"),Projections.slice("toUid", 2, 3))).iterator();
//		MongoCursor<Document> cursor = coll.find(Filters.and(Filters.eq("userid", "a3venorzpiit"),new BasicDBObject("toUid",new BasicDBObject("$slice",new Integer[]{3,2})))).iterator();
//		MongoCursor<Document> cursor = coll.find(Filters.and(new BasicDBObject("userid","a3venorzpiit"),new BasicDBObject("toUid",new BasicDBObject("$slice",new Integer[]{3,2})))).iterator();
//		MongoCursor<Document> cursor = coll.find(Filters.and(Filters.eq("userid", "1twtcd7725g1o"),Filters.eq("kegNo", 1))).projection(Projections.slice("toUid", 2, 3)).sort(sort).iterator();
//		MongoCursor<Document> cursor = coll.find(Filters.eq("user_id", 180225429)).sort(sort).iterator();
		List<String> fieldNames = Arrays.asList("userId"/*,"order_code"*/);
		Bson filter = Filters.eq("user_id", 180225429);
		filter = null;
		FindIterable<Document> fi = coll.find(filter).projection(Projections.include(fieldNames));
		if (sort != null && !sort.isEmpty()) {
			fi.sort(sort);
		}
		MongoCursor<Document> cursor = fi.iterator();
		int i = 0;
		while(cursor.hasNext()){
		      Document document = cursor.next(); 
		      List bbl=(List)document.get("fromUid");
		      if(bbl!=null)
		      System.out.println(i + " fromUid : "+bbl.size());
		      bbl=(List)document.get("toUid");
		      if(bbl!=null)
		    	  System.out.println(i + " toUid : "+bbl.size());
//		      System.out.println(document.get("toUid"));  
		      System.out.println("userid="+document.get("userid") + " order_code=" + document.getString("order_code"));
		  } 
		System.out.println("cost : "+(System.currentTimeMillis()-start));
	}
	}
	
	@Test
	public void sliceInclude(){
		int pageNo = 300;
		int pageSize = 5;
		List<String> resultFields = new ArrayList<>();
		resultFields.add("toUid");
		resultFields.add("userid");
		Iterator<DBObject> result = null;
		int [] slice = {pageNo,pageSize};
		result = db.getCollection("orders").find(new BasicDBObject("userid", "1twtcd7725g1o"), new BasicDBObject("toUid", new BasicDBObject("$slice", slice))
		.append("userid", 1).append("order_id", 1).append("_id", 0))
		.iterator();
//		result = db.getCollection("orders").find(new BasicDBObject("userid", "1twtcd7725g1o"), new BasicDBObject()
//		.append("userid", 1).append("order_id", 1))
//		.iterator();
		if (result.hasNext()) {			
			System.out.println(result.next());
		}else{
			System.out.println("no");
		}
	}
}