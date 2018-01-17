package com.lppz.util.logback;

import com.lppz.oms.kafka.constant.Topic;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.KafkaProducer;

public class LogBackLoggerProducer extends BaseKafkaProducer<LogBackKafkaVo> implements KafkaProducer<LogBackKafkaVo> {

	@Override
	protected void resetTopic() {
		setTopic(Topic.LOGBACKLOG);
	}

	@Override
	protected Object generateMsg(LogBackKafkaVo t) {
		return t;
	}
}
