package com.lppz.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class ServiceCoreImplTest {
	@Transactional
	public void insertTest(final JdbcTemplate jdbcTemplate) {
		List<String> sqlList=new ArrayList<String>();
		for(int i=1;i<=1000000;i++)
		sqlList.add("INSERT INTO customer(id,name,fuck,sex) VALUES("+i+",'zbb"+i+"','fuck"+i+"',"+ThreadLocalRandom.current().nextInt(10)+")");	
//			sqlList.add("INSERT INTO customer_addr(id,customer_id,addr) VALUES("+i+","+i+",'addr"+i+"')");	
//			for(int j=(i-1)*10+1;j<=i*10;j++){
//				sqlList.add("INSERT INTO orders(id,customer_id,state,outorderid) VALUES("+j+","+i+",'state"+ThreadLocalRandom.current().nextInt(10)+"','outoox"+ThreadLocalRandom.current().nextInt(10)+"')");	
//				for(int k=(j-1)*5+1;k<=j*5;k++){
//					sqlList.add("INSERT INTO order_items(id,order_id,linestate,sku,root_id) VALUES("+k+","+j+",'llsxx1','sku111',"+i+")");	
//				}
//			}
//			if(i==8)
//				throw new RuntimeException("fuck");
		System.out.println();
	}
	
	@Transactional
	public void insertSingleTest(final JdbcTemplate jdbcTemplate,final List<String> sqlList) {
//		String sql="INSERT INTO customer(id,name,fuck,sex) VALUES(?,?,?,?)";	
//		String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
//		String sql1="INSERT INTO customer_addr(id,customer_id,addr,crdate) VALUES("+8+","+4+",'addr"+1+"','"+date+"')";
//		final List<String> sqlList=new ArrayList<String>();
//		for(int i=0;i<20000;i++)
//			sqlList.add("INSERT INTO orders(id,customer_id,state,outorderid,createdate) VALUES("+i+","+i+",'state"+ThreadLocalRandom.current().nextInt(10)+"','outoox"+ThreadLocalRandom.current().nextInt(10)+"','"+date+"')");	
		jdbcTemplate.batchUpdate(sqlList.toArray(new String[0]));
	}
	
	@Transactional
	public void updateTest(JdbcTemplate jdbcTemplate) {
//		String sql="update orders set fuck='fuck123' where createdate = '2017-03-15 04:55:13'";
		String sql="update orders set outorderid='outooxx123' where fuck='fuck123'";
		jdbcTemplate.update(sql);
	}
	
	@Transactional
	public void deleteTest(JdbcTemplate jdbcTemplate) {
//		String sql="update orders set fuck='fuck123' where createdate = '2017-03-15 04:55:13'";
		String sql="delete from orders where createdate between '2017-03-17 11:17:26' and '2017-03-17 11:17:45'";
		jdbcTemplate.update(sql);
	}
	
	public void selectTest(JdbcTemplate jdbcTemplate) {
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
