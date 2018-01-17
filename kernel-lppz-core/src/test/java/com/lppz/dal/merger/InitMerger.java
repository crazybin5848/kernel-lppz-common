package com.lppz.dal.merger;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/oms-kernal-spring-test.xml" })
public class InitMerger {

	@Resource
	JdbcTemplate edbjdbcTemplate;

	@Test
	public void initMergerHashcode() {
		boolean flag = true;
		while (flag) {
			flag = upadateHashCode();
		}

	}

	public Boolean upadateHashCode() {
		List<Map<String, Object>> list = edbjdbcTemplate.queryForList("SELECT COUNT(1) aaaa,hashcode hashcode FROM `busi_merge_order_pool_data` GROUP BY hashcode HAVING COUNT(1)>11");
		if (list != null && list.size() > 0) {
			for (Map<String, Object> m : list) {
				
				Integer hashcode =  (Integer) m.get("hashcode");
				
				Long limit = (Long) m.get("aaaa");
				limit= limit - (int)(Math.random()*10)-2;
				List<Map<String, Object>> listor = edbjdbcTemplate.queryForList("select orderid from busi_merge_order_pool_data where hashcode ='"+hashcode+"' LIMIT "+ (limit - 10) );
				for (Map<String, Object> orderids : listor){
					String orderid = (String) orderids.get("orderid");
					int newhashcode =  (int)(Math.random() * 18000) + 450757119;
					edbjdbcTemplate.update("UPDATE busi_merge_order_pool_data SET hashcode ='" + newhashcode + "' where orderid = '"+orderid+"'");
				}
				
//				edbjdbcTemplate.update("UPDATE busi_merge_order_pool_data SET hashcode ='" + newhashcode + "' where hashcode ='"+hashcode+"' LIMIT " + (limit - 10) );
			}
			System.out.println(">>>修改hashcode...size:"+list.size());
			return true;
		} else {
			System.out.println(">>>修改hashcode完毕...");
			return false;
		}
	}
}
