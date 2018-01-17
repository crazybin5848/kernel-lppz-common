package com.lppz.util.kafka.producer;

import java.io.IOException;

import kafka.producer.KeyedMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.async.bytes.ByteQueueKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.bytes.BytesClusteredQueueKafkaMessageProducer;
import com.lppz.util.kryo.KryoUtil;

public class ByteKafkaProducer extends AbstractKafkaProducer{
	private static final Logger logger = LoggerFactory
			.getLogger(ByteKafkaProducer.class);
	private static ByteKafkaProducer byteKafkaProducer=new ByteKafkaProducer();
	public static ByteKafkaProducer getInstance(){
		return byteKafkaProducer;
	}
	private ByteKafkaProducer(){
	}
	
	@Override
	protected void sendMsg(String type,Object o,String topic,String... key) throws IOException{
		byte[] jjmsg;
		try {
			jjmsg = KryoUtil.kyroSeriLize(o, -1);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		if (KafkaConstant.SYNC.equals(type)) {
			final KeyedMessage<byte[], byte[]> msg = new KeyedMessage<byte[], byte[]>(
					topic,buildByteKey(key),jjmsg);
			byteProducer.send(msg);
		} else if (KafkaConstant.ASYNC.equals(type)) {
			((BytesClusteredQueueKafkaMessageProducer)kafkaMessageCacheMap.get(KafkaConstant.BYTEENCODER)).pushCache(new KafkaTopicKey(topic,buildKey(key)).buildJsonString(), jjmsg);
		}
		logger.debug("successfully send message [{}]", jjmsg);
	}
	
	public void destroy(String type,String serializerType){
		if (KafkaConstant.ASYNC.equals(type)){
			logger.info("successfully sended all msgs in this jvm!");
			if(kafkaMessageCacheMap!=null&&kafkaMessageCacheMap.get(KafkaConstant.BYTEENCODER)!=null)
			((BytesClusteredQueueKafkaMessageProducer)kafkaMessageCacheMap.get(KafkaConstant.BYTEENCODER)).destory();
		}
	}
}
