package com.lppz.spark.kafka;

import com.lppz.bean.Spark2kafkaBean;
import com.lppz.oms.kafka.constant.Topic;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
import com.lppz.util.kafka.producer.KafkaProducer;

public class SparkExportProducer extends BaseKafkaProducer<Spark2kafkaBean> implements KafkaProducer<Spark2kafkaBean>{

	@Override
	protected void resetTopic() {
		setTopic(Topic.SPARKEXPORT);
	}
	
	@Override
	protected Object generateMsg(Spark2kafkaBean t) {
			return t;
	}
}
