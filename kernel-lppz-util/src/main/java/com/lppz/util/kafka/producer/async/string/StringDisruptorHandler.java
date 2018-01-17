package com.lppz.util.kafka.producer.async.string;

import java.util.List;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.apache.log4j.Logger;

import com.lppz.util.disruptor.BaseHandler;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;

public class StringDisruptorHandler extends BaseMsgAsyncKafkaHandler implements BaseHandler<KeyedMessage<String, String>>{
	private static final Logger logger = Logger
			.getLogger(StringDisruptorHandler.class);
	
	private Producer<String, String> producer;
	@Override
	public void handle(List<KeyedMessage<String, String>> list) {
			try {
				if(producer==null){
					producer = BaseKafkaProducer
							.getProducer().pollProducer();
				}
				producer.send((List<KeyedMessage<String, String>>) list);
				logger.info(list.size()+" has send succ!");
				list.clear();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return;
			}
	}
}
