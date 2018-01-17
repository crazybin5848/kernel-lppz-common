package com.lppz.util.kafka.producer.async.string;

import org.apache.commons.lang3.StringUtils;

import com.lppz.util.KafkaPropertiesUtils;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;
import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.producer.async.AsyncKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.BaseClusterdDisruptorKafkaSender;

public class StringClusteredQueueKafkaMessageProducer extends BaseEvent2Sender<String> implements AsyncKafkaMessageProducer<String>{
	@Override
	public void destory() {
		if(clusterSender!=null){
			clusterSender.destory();
		}
	}
	
	private static StringClusteredQueueKafkaMessageProducer queueKafkaMessageCache = new StringClusteredQueueKafkaMessageProducer();
	public static StringClusteredQueueKafkaMessageProducer getInstance() {
		return queueKafkaMessageCache;
	}
	private BaseClusterdDisruptorKafkaSender<String> clusterSender;
	
	
	private StringClusteredQueueKafkaMessageProducer(){
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AsyncKafkaMessageProducer buildSender(int size){
		if(clusterSender==null)
		clusterSender=BaseClusterdDisruptorKafkaSender.build(new BaseEvent2SenderFactory<String>(){
			@Override
			public BaseEvent2Sender<String> build() {
				return new StringQueueKafkaMessageProducer();
			}
		},size);
		return this;
	}
	
	@Override
	public void pushCache(String topic, String jmsg) {
		KafkaTopicKey tt=KafkaTopicKey.deBuildJson(topic);
		if(StringUtils.isEmpty(tt.getKey()))
			clusterSender.pushCache(topic, jmsg);
		else
			clusterSender.pushCacheOrderly(tt.getKey(), topic, jmsg);	
	}
}