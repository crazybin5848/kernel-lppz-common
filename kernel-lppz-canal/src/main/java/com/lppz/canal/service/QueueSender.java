package com.lppz.canal.service;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.lppz.util.rocketmq.ProducerParam;
import com.lppz.util.rocketmq.RocketMqProducer;

public class QueueSender<T> {
	
	private DefaultMQProducer producer;
	private Class<T> clazz;
	private String tag;
	private String topic;
	
	public QueueSender(DefaultMQProducer producer, Class<T> clazz, String topic, String tag) {
		this.producer = producer;
		this.clazz = clazz;
		this.topic = topic;
		this.tag = tag;
	}
	
	public boolean sendMsg(T t, String key, int mainId){
		ProducerParam<T> param=new ProducerParam<T>();
    	param.setClazz(clazz);
    	param.setKey(key);
    	param.setBody(t);
    	param.setTag(StringUtils.isBlank(tag)?topic:tag);
    	param.setTopic(topic);
    	SendResult result = RocketMqProducer.getInstance().sendMsgOrderly(producer, param, mainId);
    	if (SendStatus.SEND_OK.equals(result.getSendStatus())) {
			return true;
		}else{
			return false;
		}
	}

}
