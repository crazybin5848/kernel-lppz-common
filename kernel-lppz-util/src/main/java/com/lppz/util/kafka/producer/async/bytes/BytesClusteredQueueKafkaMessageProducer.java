package com.lppz.util.kafka.producer.async.bytes;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.KafkaPropertiesUtils;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;
import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.producer.async.AsyncKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.BaseClusterdDisruptorKafkaSender;

public class BytesClusteredQueueKafkaMessageProducer extends BaseEvent2Sender<byte[]> implements AsyncKafkaMessageProducer<byte[]>{
	private static final Logger logger = LoggerFactory.getLogger(BytesClusteredQueueKafkaMessageProducer.class);
	@Override
	public void destory() {
		if(clusterSender!=null){
			clusterSender.destory();
		}
	}
	
	private static BytesClusteredQueueKafkaMessageProducer queueKafkaMessageCache = new BytesClusteredQueueKafkaMessageProducer();
	public static BytesClusteredQueueKafkaMessageProducer getInstance() {
		return queueKafkaMessageCache;
	}
	private BaseClusterdDisruptorKafkaSender<byte[]> clusterSender;
	
	private BytesClusteredQueueKafkaMessageProducer(){
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AsyncKafkaMessageProducer buildSender(int size){
		if(clusterSender==null)
		clusterSender=BaseClusterdDisruptorKafkaSender.build(new BaseEvent2SenderFactory<byte[]>(){
			@Override
			public BaseEvent2Sender<byte[]> build() {
				return new ByteQueueKafkaMessageProducer();
			}
		},size);
		return this;
	}

	@Override
	public void pushCache(String topic, byte[] jmsg) {
		KafkaTopicKey tt=KafkaTopicKey.deBuildJson(topic);
		if(StringUtils.isEmpty(tt.getKey())){
			clusterSender.pushCache(topic, jmsg);
//			logger.info(jmsg.toString()+" has send currently");
		}
		else{
//			logger.info("key:"+tt.getKey()+" has send Orderly");
			clusterSender.pushCacheOrderly(tt.getKey(), topic, jmsg);	
		}
	}
}