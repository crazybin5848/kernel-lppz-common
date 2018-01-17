package com.lppz.spark.kafka;

import com.lppz.oms.kafka.constant.Topic;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.KafkaProducer;

public class SparkExportStringProducer extends BaseKafkaProducer<String> implements KafkaProducer<String>{

	@Override
	protected void resetTopic() {
		setTopic(Topic.SPARKEXPORT);
	}

	@Override
	protected Object generateMsg(String t) {
			return t;
	}
}
