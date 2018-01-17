package io.mycat.sencondaryindex.kafka.producer;

import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;

import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.KafkaProducer;

public class DalKafkaProducer extends BaseKafkaProducer<Dal2ndIdxKafkaModel> implements KafkaProducer<Dal2ndIdxKafkaModel>{

	@Override
	protected void resetTopic() {
		setTopic("dalMycatSendaryTopic");
	}
	
	@Override
	protected Object generateMsg(Dal2ndIdxKafkaModel t) {
			return t;
	}
}
