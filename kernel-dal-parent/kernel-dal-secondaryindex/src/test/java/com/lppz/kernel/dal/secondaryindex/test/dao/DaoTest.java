//package com.lppz.kernel.dal.secondaryindex.test.dao;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/base-spring-test.xml"})
//public class DaoTest {
//	
//	@Resource
//	private LpMemberDao dao;
//	
//	@Test
//	public void queryTest(){
//		Map<String, Object> params = new HashMap<>();
//		params.put("startNo", 0);
//		params.put("pageSize", 10);
//		while(true){
//			List<Map<String, Object>>  result = dao.selectLpMembers(params);
//			System.out.println(result);
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//}
