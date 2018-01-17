package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.mongoapi.dao.MongoDao;

public class PushPullTest extends SpringBaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(PushPullTest.class);
	
	String table = "orders";
	@Resource
	private MongoDao mongoDao;
	
	@Test
	public void pushTest(){
		String userid="1twtcd7725g1o";
		List<Object> documents=new ArrayList<Object>();
		for(int j=0;j<5000;j++){
			Document obj=new Document("a",j).append("fuck", "fuck").
					append("ads", "qqq").append("231", 456);
			documents.add(obj);
		}
		mongoDao.push(table, "userid", userid, "toUid", documents);
		
	}
	
	@Test
	public void pushTest2(){
		String userid="1twtcd7725g1o";
		List<Object> documents=new ArrayList<Object>();
		for(int j=0;j<5000;j++){
			Document obj=new Document("a",j);
			documents.add(obj);
		}
		mongoDao.push(table, "userid", userid, "toUid", documents);
		
	}
	@Test
	public void pullTest(){
		String userid="1twtcd7725g1o";
		mongoDao.pull(table, "userid", userid, "toUid.a",4999);
//		mongoDao.pull("", "commentId", "7213803f-8523-4a21-af6f-2369e205a05a", "likedUsers.userId","3101344336");
		
	}
	
	@Test
	public void testgtriiger(){
		ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
		scheduled.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				logger.info("xxxxxxxxxxxxstart doFixByRedis");
			}
		}, 1, 5, TimeUnit.SECONDS);
		
		try {
			Thread.sleep(500000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
