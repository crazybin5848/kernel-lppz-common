package com.lppz.dal;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

public class DalIndexTest extends BaseTest {

	@Resource
	protected IndexServiceImplTest service;
	
	@Test
	public void insertTest() {
		service.insertTest(jdbcTemplate);
	}
	
	@Test
	public void updateTelephoneTest() {
		String telephone = "13888999777";
		String memberNo = "1";
		service.updateTelephoneTest(jdbcTemplate, telephone, memberNo);
	}
	
	public void testUpdateChannelCode() {
		String channelCode = "1002";
		String id = "1";
		service.updateChannelCode(jdbcTemplate, channelCode, id);
	}
	
	@Test
	public void selectTest() {
		//select * from fuck3 where name ='bin7' order by uid desc,ctime desc limit 182720,15;
			String sql="select * from lp_member where telephone ='13888999777' order by member_no desc,createtime desc limit 1000,15";
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			for(Map<String,Object> map:list){
				for(String key:map.keySet()){
					System.out.print(key+":"+map.get(key)+"...");
				}
				System.out.println();
			}
		}
}
