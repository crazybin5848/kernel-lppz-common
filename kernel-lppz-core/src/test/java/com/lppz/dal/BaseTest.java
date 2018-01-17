package com.lppz.dal;


import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/kernel-dal-test.xml"})
public class BaseTest {
//	private static ClassPathXmlApplicationContext app;

	@Resource
	protected JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
//		app = new ClassPathXmlApplicationContext(
//				"classpath:InitJndi.xml");
//		DataSource ds = (DataSource) app.getBean("ds");
//		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
//		builder.bind("java:comp/env/jdbc/omsmasterds0", ds);
//		builder.bind("java:comp/env/jdbc/omsslaveds0", ds);
//		builder.activate();
	}
}