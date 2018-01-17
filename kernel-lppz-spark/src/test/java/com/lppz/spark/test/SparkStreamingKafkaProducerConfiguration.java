package com.lppz.spark.test;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.spark.scala.streaming.DapLog;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

public class SparkStreamingKafkaProducerConfiguration {
	
private static final Logger LOG = LoggerFactory.getLogger(SparkStreamingKafkaProducerConfiguration.class);
	
	public static SparkStreamingProducer createProducer(String kafkaBrokerPath) throws IOException {
		BaseKafkaProducer<DapLog> kafkaProducer = new SparkStreamingProducer();
		try {
			if(StringUtils.isNotBlank(kafkaBrokerPath)){				
				SparkStreamingProducer.kafkaProducerConfig = kafkaProducer.initConfig(kafkaBrokerPath);
			}
			kafkaProducer.setProducer_type(KafkaConstant.ASYNC);
			kafkaProducer.setProduce_serilize_class(KafkaConstant.STRINGENCODER);
			kafkaProducer.init();
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		return (SparkStreamingProducer) kafkaProducer;
	}
}

