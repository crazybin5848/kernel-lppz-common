package com.lppz.dal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;

public class DalCoreImplTest extends BaseTest {

	@Resource
	protected ServiceCoreImplTest serviceCoreImplTest;
	
	@Test
	public void insertTest() {
		long start=System.currentTimeMillis();
		final AtomicInteger ai=new AtomicInteger(0);
		int size=50;
		List<String> sqlList=new ArrayList<String>();
		for(int i=1;i<=1;i++)
		sqlList.add("INSERT INTO customer(id,name,fuck,sex) VALUES("+i+",'zbb"+i+"','fuck"+i+"',"+ThreadLocalRandom.current().nextInt(10)+")");	
		
//		for(int i=0;i<size;i++){
//			final List<String> subList=sqlList.subList(i*2000, (i+1)*2000);
//			new Thread(new Runnable(){
//
//				@Override
//				public void run() {
//					try {
//						serviceCoreImplTest.insertSingleTest(jdbcTemplate,subList);
//					} catch (DataAccessException e) {
//						e.printStackTrace();
//					}
//					finally{
//						ai.getAndIncrement();
//					}
//
//				}
//			}).start();
//		}
//		while(true){
//			if(ai.get()==size){
//				System.out.println(System.currentTimeMillis()-start);
//				break;
//			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//			}
//		}
		serviceCoreImplTest.insertSingleTest(jdbcTemplate,sqlList);
		System.out.println();
	}
	
	@Test
	public void insertSingleTest() {
		String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
//		String sql1="INSERT INTO customer_addr(id,customer_id,addr,crdate) VALUES("+8+","+4+",'addr"+1+"','"+date+"')";
		final List<String> sqlList=new ArrayList<String>();
		for(int i=0;i<300000;i++)
			sqlList.add("INSERT INTO orders(id,customer_id,state,outorderid,createdate) VALUES("+i+","+i+",'state"+ThreadLocalRandom.current().nextInt(10)+"','outoox"+ThreadLocalRandom.current().nextInt(10)+"','"+date+"')");	
		long start=System.currentTimeMillis();
		final AtomicInteger ai=new AtomicInteger(0);
		for(int i=0;i<300;i++){
			final List<String> subList=sqlList.subList(i*1000, (i+1)*1000);
			new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						serviceCoreImplTest.insertSingleTest(jdbcTemplate,subList);
//						jdbcTemplate.batchUpdate(subList.toArray(new String[0]));
					} catch (DataAccessException e) {
						e.printStackTrace();
					}
					finally{
						ai.getAndIncrement();
					}

				}
			}).start();
		}
		while(true){
			if(ai.get()==300){
				System.out.println(System.currentTimeMillis()-start);
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		System.out.println();
	}
	
	@Test
	public void updateTest() {
		serviceCoreImplTest.updateTest(jdbcTemplate);
		System.out.println();
	}
	
	@Test
	public void deleteTest() {
		serviceCoreImplTest.deleteTest(jdbcTemplate);
		System.out.println();
	}
	
	@Test
	public void selectTest() {
		//select * from fuck3 where name ='bin7' order by uid desc,ctime desc limit 182720,15;
			String sql="select * from fuck1 where name ='bin7' order by uid desc,ctime desc limit 1000,15";
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			for(Map<String,Object> map:list){
				for(String key:map.keySet()){
					System.out.print(key+":"+map.get(key)+"...");
				}
				System.out.println();
			}
		}
}
