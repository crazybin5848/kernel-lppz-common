package com.lppz.util.kafka.producer.async.bytes;

import java.util.List;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.apache.log4j.Logger;

import com.lppz.util.disruptor.BaseHandler;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;

public class ByteDisruptorHandler extends BaseMsgAsyncKafkaHandler implements BaseHandler<KeyedMessage<byte[], byte[]>>{
	private static final Logger logger = Logger
			.getLogger(ByteDisruptorHandler.class);
	
	private Producer<byte[], byte[]> producer;
	
	@Override
	public void handle(List<KeyedMessage<byte[], byte[]>> list) {
			try {
				if(producer==null){
					producer = BaseKafkaProducer
							.getByteProducer().pollProducer();
				}
				producer.send((List<KeyedMessage<byte[], byte[]>>) list);
				logger.info(list.size()+" has send succ!");
				list.clear();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return;
			}
	}
}