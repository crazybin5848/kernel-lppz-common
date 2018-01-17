package com.lppz.kernel.dal.secondaryindex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/kernel-lppz-dal-test.xml"})
public class SelectTest {
	
	private static Logger logger = LoggerFactory.getLogger(SelectTest.class);

	@Resource
	private JdbcTemplate jt;
	
	private static List<String> sqlList=new ArrayList<String>();
	
	static {
		sqlList.add("select * from lp_member where card_no='10167545'");
		sqlList.add("select * from lp_member where telphone='13971588566'");
		sqlList.add("SELECT MAX(partner_no) maxpartnerno,MAX(member_no) maxmemberno FROM lp_member");
		sqlList.add("select * from lp_member where member_account='19qdr1u9c00a01000'");
		sqlList.add("select * from lp_member where member_pwd='fe2cc10a6e8abcb54919b8109e63edfb' and telphone='13638644196'");
		sqlList.add("select * from lp_member where member_pwd='0de1def90938e07ff28dcd424af6b02c' and member_account='19qdr1u9u00h01000'");
		sqlList.add("select * from lp_member_address where member_no='1100005589'");
		sqlList.add("select * from lp_member_capital_account where member_no='1100006315'");
		sqlList.add("select * from lp_member_extend where member_no='1136740334'");
		sqlList.add("select * from lp_member_rela_accounts where member_no='1126881006'");
		sqlList.add("select * from lp_member_rela_accounts where outer_unionid='oEaF8xGbbfFziCye6bUS' and outIdType='ZCM0'");
		sqlList.add("select * from lp_member_change_details where member_no='1143250006' and create_time between '2017-04-01' and '2017-01-01' and member_type = '0'");
		sqlList.add("select count(1) from lp_marketgrouprelamembers where lpgroupid = '3000000005' and lpmemberno = '1100495207'");
	}
	
	private ConcurrentHashMap<String, Long> resultMap=new ConcurrentHashMap<>();
	
	private int threadNum=200;
	
	@Test
	public void test(){
		java.util.concurrent.ExecutorService tp=java.util.concurrent.Executors.newFixedThreadPool(threadNum);
		java.util.concurrent.atomic.AtomicInteger ai=new AtomicInteger();
		
		for(int i=0;i<threadNum;i++){
			tp.execute(new ExecQueryThread(ai,sqlList,jt));
		}
		while(ai.get() !=threadNum){
			try {
				logger.info("completed task count {}",ai.get());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
		tp.shutdown();
		
		for(Entry<String, Long> entry:resultMap.entrySet()){
			logger.info("thread id : {} ,query cost :{}",entry.getKey(),entry.getValue());
		}
		
	}
	

	public class ExecQueryThread implements Runnable {

		private AtomicInteger ai;
		private List<String> sqlList;
		private JdbcTemplate jt;
		
		public ExecQueryThread(AtomicInteger ai,List<String> sqlList,JdbcTemplate jt){
			this.ai=ai;
			this.sqlList=sqlList;
			this.jt=jt;
		}
		
		@Override
		public void run() {
			try{
				long start=0l;
				long end=0l;
				int i=0;
				for(String sql:sqlList){
					start=System.currentTimeMillis();
					jt.queryForList(sql);
					end=System.currentTimeMillis();
					
					long cost=end - start;
					
					resultMap.put(Thread.currentThread().getId()+""+i+" exec sql :"+sql, cost);
					i++;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}finally {
				ai.getAndIncrement();
			}
		}

	}

}
