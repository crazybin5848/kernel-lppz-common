package com.lppz.canal.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.enums.EtlEnums;
import com.lppz.util.rocketmq.RocketMqProducer;
import com.lppz.util.rocketmq.enums.OtherRocketMqProducerGroup;
import com.lppz.util.rocketmq.enums.OtherRocketMqTopic;

public class SenderFactory {
	public static String nameSrv;
	
	private static Map<String, QueueSender> producers;
	
	static{
		initProducer();
	}
	
	private static void initProducer() {
		producers = new HashMap<>();
		producers.put(EtlEnums.ETRACT.getName(), buildSender(OtherRocketMqTopic.CANCALMSG.getId(),EtlEnums.ETRACT.getName(), ChangeDataBean.class));
		producers.put(EtlEnums.TRANSFORM.getName(), buildSender(OtherRocketMqTopic.CANCALMSG.getId(),EtlEnums.TRANSFORM.getName(), ChangeDataBean.class));
		producers.put(EtlEnums.LOAD.getName(), buildSender(OtherRocketMqTopic.CANCALMSG.getId(),EtlEnums.LOAD.getName(), ChangeDataBean.class));
	}

	private static QueueSender buildSender(String topic, String tag, Class clazz) {
		String prodInstance = "prodInstance";
		DefaultMQProducer producer = RocketMqProducer.getInstance().buildProducer(OtherRocketMqProducerGroup.CANCALMSG.getId(), nameSrv, prodInstance);
		return new QueueSender<>(producer, clazz, topic, tag);
	}

	public static boolean sendMsg(EtlEnums etlEnums, ChangeDataBean t, String key, int mainId){
		if (producers == null) {
			throw new RuntimeException("producer is not init");
		}
		
		return producers.get(etlEnums.getName()).sendMsg(t, key, mainId);
	}
}
