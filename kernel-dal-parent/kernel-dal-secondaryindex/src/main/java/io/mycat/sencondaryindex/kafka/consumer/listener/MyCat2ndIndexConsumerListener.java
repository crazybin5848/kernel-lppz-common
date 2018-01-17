/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package io.mycat.sencondaryindex.kafka.consumer.listener;

import io.mycat.sencondaryindex.kafka.producer.KafkaErrorDBHandler;
import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;
import io.mycat.sencondaryindex.model.SchemaConfig;
import io.mycat.sencondaryindex.model.TableConfig;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.core.datasource.MycatClusterDynamicDataSource;
import com.lppz.elasticsearch.disruptor.BaseEsLogEventCRUD2Sender;
import com.lppz.util.disruptor.sender.BaseClusterdDisruptorSender;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;
import com.lppz.util.kafka.consumer.listener.BaseByteKafkaConsumerListener;

/**
 * 消费者实现类
 *
 */
@Component("myCat2ndIndexConsumerListener")
public class MyCat2ndIndexConsumerListener extends BaseByteKafkaConsumerListener<Dal2ndIdxKafkaModel> implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(MyCat2ndIndexConsumerListener.class);
	
	public static final String IDXTABLENAMEESPREFIX = "idx-mycat-secondaryidx-";
	private BaseClusterdDisruptorSender<String> logSender;
	
	@Value("${es.batch.size}")
	private int esBatchSize;
	
	@Value("${es.batch.time.ms}")
	private long esBatchTimeMs;
	
	@Value("${es.batch.thread}")
	private int esBatchThread;
	
	@Value("${es.retry.count}")
	private int reTryCount=1;
	
	@Value("${es.is.log}")
	private boolean isLogInfo;
	
	@Resource 
	private DynamicDataSource ds;
	
	CuratorFramework client;
	SchemaConfig sc;
	private void initZkSchema() throws Exception{
		MycatClusterDynamicDataSource dds=(MycatClusterDynamicDataSource)ds.getTargetDataSources().get("master-member-service");
		client = CuratorFrameworkFactory.builder()
				.connectString(dds.getZkServerAddr())
				.sessionTimeoutMs(5000)
				.connectionTimeoutMs(20000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 5))
				.build();
		client.start();
		String data=new String(client.getData().forPath("/mycat/" + dds.getZkMycatClusterName() + "/schema/schema"),"UTF-8");
		JSONArray jsonArray = JSONObject.parseArray(data);
		JSONObject jsono=(JSONObject) jsonArray.get(0);
		JSONArray array=(JSONArray)jsono.get("table");
		sc=buildSc(array);
		sc.setName(jsono.getString("name").toLowerCase());
		}
	
	
	private SchemaConfig buildSc(JSONArray array) {
		SchemaConfig sc=new SchemaConfig();
		for(Object o:array){
			buildTcNormal(sc, o);
			JSONObject jo = (JSONObject)o;
			recursePut(jo,sc);
		}
		return sc;
	}


	private void buildTcNormal(SchemaConfig sc, Object o) {
		JSONObject jo=(JSONObject)o;
		TableConfig tc=new TableConfig();
		tc.setType(jo.getString("type"));
		tc.setName(jo.getString("name"));
		tc.setPrimaryKey(jo.getString("primaryKey"));
		tc.setSecondaryIndexKeys(jo.getString("secondaryIndexKeys"));
		tc.setJoinKey(jo.getString("joinKey"));
		tc.setRoot_id(jo.getString("rootParentKey"));
		if(!"global".equals(tc.getType()))
		sc.put(tc.getName(), tc);
	}


	private void recursePut(JSONObject jo, SchemaConfig sc) {
		JSONArray ja=jo.getJSONArray("childTable");
		if(ja!=null){
			for(Object o:ja){
				buildTcNormal(sc, o);
				JSONObject jjo = (JSONObject)o;
				recursePut(jjo,sc);
			}
		}
	}


	@Override
	protected void doMsg(Dal2ndIdxKafkaModel t) {
		if(StringUtils.isNotBlank(t.getEsId()))
		logSender.sendMsgOrderly(t.getDto(), t.getEsId(), t.getParam());
		else
		logSender.sendMsg(t.getDto(), t.getParam());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		initZkSchema();
		logSender=BaseClusterdDisruptorSender.build(new BaseEvent2SenderFactory<String>(){
			@Override
			public BaseEvent2Sender<String> build() {
				KafkaErrorDBHandler handler=new KafkaErrorDBHandler(ds,sc);
				handler.setLogInfo(isLogInfo);
				handler.setReTryCount(reTryCount);
				return BaseEsLogEventCRUD2Sender.create(esBatchSize, esBatchTimeMs,handler);
			}
		},esBatchThread);
	}
	
	public BaseClusterdDisruptorSender<String> getLogSender() {
		return logSender;
	}

	public int getEsBatchSize() {
		return esBatchSize;
	}

	public void setEsBatchSize(int esBatchSize) {
		this.esBatchSize = esBatchSize;
	}

	public long getEsBatchTimeMs() {
		return esBatchTimeMs;
	}

	public void setEsBatchTimeMs(long esBatchTimeMs) {
		this.esBatchTimeMs = esBatchTimeMs;
	}

	public int getEsBatchThread() {
		return esBatchThread;
	}

	public void setEsBatchThread(int esBatchThread) {
		this.esBatchThread = esBatchThread;
	}
}