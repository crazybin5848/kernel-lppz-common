package com.lppz.dal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class IndexServiceImplTest {
	@Transactional
	public void insertTest(JdbcTemplate jdbcTemplate) {
		/**
		 * 	lp_member
			lp_member_address
			lp_member_capital_account
			lp_member_change_details
			lp_member_extend
			lp_member_log
			lp_member_rela_accounts
		 */
		String memberNo = null;
		String partnerNo = null;
		String cardNo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		for(int i=1;i<=3;i++){
			memberNo = String.valueOf(i);
			partnerNo = memberNo;
			cardNo = memberNo;
			time = sdf.format(new Date());
			List<String> sqlList=new ArrayList<String>();
			sqlList.add("INSERT INTO lp_member(partner_no,card_no,member_no,telphone,source_type,member_account,member_pwd,password_encoding,birthday,create_time,create_user,edit_time,edit_user,register_store,recommender,member_status,sync_crm_status,sync_crm_time,member_type,member_level,zzannual_amt,zztier_count)"
					+ " VALUES("+partnerNo+","+cardNo+","+memberNo+",'1338888882"+i+"','1','user"+i+"','111111','UTF-8','2010-10-01','"+time+"','"+time+"','system','1001','1020','1','1','"+time+"','1','1','1','1')");	
			sqlList.add("INSERT INTO lp_member_address(id,member_no,partner_no,consignee,gender,position,longitude,dimensionality,map_type,address,cell_phone,province_code,province_name,city_code,city_name,district_code,district_name,create_time,edit_time,is_default,post_code)"
					+ " VALUES("+i+",'"+memberNo+"','"+partnerNo+"','路人"+i+"','1','',0,0,0,,'发展大道"+(i+1)+"号','','','','','','','','"+time+"','"+time+"','1','')");	
			sqlList.add("INSERT INTO lp_member_capital_account(id,partner_no,member_no,card_no,level_type,all_scores,credit_scores,behavior_scores,all_card_balance,card_balance,donation_amount,card_status)"
					+ " VALUES("+i+",'"+partnerNo+"','"+memberNo+"','"+cardNo+"','1',20,10,10,0,0,0,'0')");	
			sqlList.add("INSERT INTO lp_member_change_details(id,member_no,partner_no,change_type,member_type,credit_scores,pk_crm,store_code,outorder_code,order_code,line_id,txn_reason,remark,create_time,integration_time,integration_end_time,status)"
					+ " VALUES("+i+",'"+memberNo+"','"+partnerNo+"',1,1,10,'','','','','','','','"+time+"','"+time+"','"+time+"',0)");	
			sqlList.add("INSERT INTO lp_member_extend(id,member_no,partner_no,member_name,member_nickname,gender,educationcd,occupation,marital_status,income,id_type,id_number,header_imgpath,create_time,delete_flag,register_ip,register_terminal,terminal_type,email)"
					+ " VALUES("+i+",'"+memberNo+"','"+partnerNo+"','路人"+i+"','路人','1','','','','','','','','"+time+"',0,'','','','')");	
			sqlList.add("INSERT INTO lp_member_log(id,member_no,partner_no,chanel_code,request_ip,request_time,request_type,request_content,request_header)"
					+ " VALUES("+i+",'"+memberNo+"','"+partnerNo+"','','','','','','')");	
			sqlList.add("INSERT INTO lp_member_rela_accounts(id,member_no,partner_no,chanel_code,outer_user_id,outer_unionid,create_time,delete_flag,outIdType)"
					+ " VALUES("+i+",'"+memberNo+"','"+partnerNo+"','','','','"+time+"',0,'')");	
			for(String sql:sqlList)
				System.out.println(sql);
			jdbcTemplate.batchUpdate(sqlList.toArray(new String[0]));
		}
		System.out.println("----------end------------");
	}
	
	@Transactional
	public void updateTelephoneTest(JdbcTemplate jdbcTemplate, String telephone, String memberNo) {
		String sql="update lp_member set telphone='"+telephone+"' where member_no='"+memberNo+"'";
		jdbcTemplate.update(sql);
	}
	
	
	public void updateChannelCode(JdbcTemplate jdbcTemplate, String id, String channelCode){
		String sql="update lp_member_rela_accounts set channel_code='"+channelCode+"' where id='"+id+"'";
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
