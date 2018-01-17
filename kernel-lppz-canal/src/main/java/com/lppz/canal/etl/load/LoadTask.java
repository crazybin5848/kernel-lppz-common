package com.lppz.canal.etl.load;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.consumer.AbstractConsumer;
import com.lppz.canal.enums.EtlEnums;
import com.lppz.canal.service.QueueSenderService;
import com.lppz.util.rocketmq.enums.OtherRocketMqConsumerGroup;
import com.lppz.util.rocketmq.enums.OtherRocketMqTopic;
import com.lppz.util.rocketmq.enums.RocketMqDelayEnums;
import com.lppz.util.rocketmq.exception.RocketMqBizException;

/**
 * 从rocketmq消费消息后根据数据处理类型转换成hbase api处理对象集合，写入hbase
 * 1、初始化hbase连接池
 * 2、启动消费线程，获取mq消息
 * 3、根据操纵类型分组
 * 4、根据分组分别调用hbase api
 * @author licheng
 *
 */
@Component
public class LoadTask extends AbstractConsumer<ChangeDataBean, ChangeDataBean, Object> implements DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadTask.class);

	@Value("${rocketmq.nameserver}")
	private String nameSrv;
	private String tag = EtlEnums.LOAD.getName();
	@Value("${rocketmq.consumer.batchSize:1000}")
	private int batchSize;
	@Value("${rocketmq.consumer.batchTimeStep:1000}")
	private int batchTimeStep;
	@Value("${rocketmq.consumer.minTreadNum:1}")
	private int consumerMinTreadNum;
	@Value("${rocketmq.consumer.maxTreadNum:10}")
	private int consumerMaxTreadNum;
	private String topic = OtherRocketMqTopic.CANCALMSG.getId();
	private String consumerGroup = OtherRocketMqConsumerGroup.CANCALMSG.getId();
	@Value("${task.load.isRun:false}")
	private boolean isRun;
	@Resource
	private JedisCluster jedisCluster;
	
	@Resource
	private QueueSenderService queueSenderService;

	
	@PostConstruct
	public void startTask(){
		if (!isRun) {
			return;
		}
		startConsume();
	}
	
	@Override
	public List<ChangeDataBean> validate(List<ChangeDataBean> t) {
		return t;
	}
	
	@Override
	public Object dealMsg(List<ChangeDataBean> records) {
		List<ChangeDataBean> insertList = new ArrayList<>();
		List<ChangeDataBean> updateList = new ArrayList<>();
		List<ChangeDataBean> deleteList = new ArrayList<>();
		List<ChangeDataBean> failList = new ArrayList<>();
		for (ChangeDataBean model : records) {
			switch (model.getOperEnums()) {
			case INSERT:
				insertList.add(model);
				break;
			case UPDATE:
				updateList.add(model);
				break;
			case DELETE:
				deleteList.add(model);
				break;
			default:
				break;
			}
		}
		
		insertBatch(insertList);
		
		updateBatch(updateList);
		
		deleteBatch(deleteList);
		
		if(!failList.isEmpty()){
			throw RocketMqBizException.build(new Exception("消息校验失败"), failList, getTopic(), getTag(), RocketMqDelayEnums.OneSecond.getLevel());
		}
		return null;
	}

	private void deleteBatch(List<ChangeDataBean> deleteList) {
		if (CollectionUtils.isNotEmpty(deleteList)) {
			// TODO Auto-generated method stub
		}
	}

	private void updateBatch(List<ChangeDataBean> updateList) {
		if (CollectionUtils.isNotEmpty(updateList)) {
			// TODO Auto-generated method stub
		}
	}

	private void insertBatch(List<ChangeDataBean> insertList) {
		if (CollectionUtils.isNotEmpty(insertList)) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void dealRollBack(List<ChangeDataBean> s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getNameSrv() {
		return nameSrv;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public String getConsumerGroup() {
		return consumerGroup;
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public int getBatchTimeStep() {
		return batchTimeStep;
	}

	@Override
	public int getConsumerMinTreadNum() {
		return consumerMinTreadNum;
	}

	@Override
	public int getConsumerMaxThreadNum() {
		return consumerMaxTreadNum;
	}	

}
