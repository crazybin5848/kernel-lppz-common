package com.lppz.util.kafka.producer;

import kafka.producer.KeyedMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.async.string.StringClusteredQueueKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.string.StringQueueKafkaMessageProducer;

public class StringKafkaProducer extends AbstractKafkaProducer{
	private static final Logger logger = LoggerFactory
			.getLogger(StringKafkaProducer.class);
	private static StringKafkaProducer stringKafkaProducer=new StringKafkaProducer();
	public static StringKafkaProducer getInstance(){
		return stringKafkaProducer;
	}
	private StringKafkaProducer(){
	}
	
	protected void sendMsg(String type,Object o,String topic,String... key){
		String jjmsg = o instanceof String?(String)o:JSON.toJSONString(o);
		if (KafkaConstant.SYNC.equals(type)) {
			final KeyedMessage<String, String> msg = new KeyedMessage<String, String>(
					topic,buildKey(key),jjmsg);
			producer.send(msg);
		} else if (KafkaConstant.ASYNC.equals(type)) {
			((StringClusteredQueueKafkaMessageProducer)kafkaMessageCacheMap.get(KafkaConstant.STRINGENCODER)).pushCache(new KafkaTopicKey(topic,buildKey(key)).buildJsonString(), jjmsg);
		}
		logger.debug("successfully send message [{}]", jjmsg);
	}
	
	public void destroy(String type,String serializerType){
		if (KafkaConstant.ASYNC.equals(type)){
			logger.info("successfully sended all msgs in this jvm!");
			if(kafkaMessageCacheMap!=null&&kafkaMessageCacheMap.get(KafkaConstant.STRINGENCODER)!=null)
			((StringClusteredQueueKafkaMessageProducer)kafkaMessageCacheMap.get(KafkaConstant.STRINGENCODER)).destory();
		}
	}
}
