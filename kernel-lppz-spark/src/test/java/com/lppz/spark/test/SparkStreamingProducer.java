package com.lppz.spark.test;

import com.lppz.spark.scala.streaming.DapLog;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.KafkaProducer;

public class SparkStreamingProducer extends BaseKafkaProducer<DapLog> implements KafkaProducer<DapLog>{

	@Override
	protected void resetTopic() {
		setTopic("fuckStreaming");
	}
	
	@Override
	protected Object generateMsg(DapLog t) {
			return t;
	}
}
