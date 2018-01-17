package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.disruptor.BaseEsLogEvent2Sender;
import com.lppz.elasticsearch.result.SearchResult;

public class EsImportTest extends SpringBaseTest {

	private static final Logger logger = LoggerFactory.getLogger(EsImportTest.class);
	
	@Test
	public void es2ES() {
		String[] index = new String[]{"member_es_log-2017"};
		String[] types = new String[]{"com.lppz.member.api.pojo.MemberLogTemplate"};
		int scrollSize = 10000;
		int timeMillis = 10000;
		
		LppzEsComponent.getInstance().scrollSearch(index, types, null, scrollSize, timeMillis, new PrepareBulk() {
			
			BaseEsLogEvent2Sender sender = BaseEsLogEvent2Sender.create("member-es-log-", "com.lppz.member.api.pojo.MemberLogTemplate", "2016", false);
			
			@Override
			public void bulk(List<SearchResult> listRes) {
				logger.info("bulk size {}",listRes.size());
				List<String> esModelList = buildInsertList(listRes);
				for (String msg : esModelList) {
					sender.sendMsg(msg);
				}
			}
		});
		
		while (true) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> buildInsertList(List<SearchResult> listRes) {
		List<String> models = new ArrayList<String>();
		for (SearchResult result : listRes) {
			models.add(JSON.toJSONString(result.getSource()));
		}
		return models;
	}
	
}
