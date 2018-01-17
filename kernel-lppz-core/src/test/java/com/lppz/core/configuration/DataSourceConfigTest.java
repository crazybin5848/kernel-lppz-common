//package com.lppz.core.configuration;
//
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.lppz.core.dao.d1.Test123Dao;
//import com.lppz.core.dao.d2.Test456Dao;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/kernel-core-test.xml" })
//public class DataSourceConfigTest {
//	@Autowired 
//	private Test123Dao test123Dao;
//	@Autowired 
//	private Test456Dao test456Dao;
//	@Test
//	public void testDataSource() {
//		List<Map> list1=test123Dao.selectFuck();
//		List<Map> list2=test456Dao.selectFuck();
//		for(Map map:list1){
//		
//		}
//	}
//
//}
