package io.mycat.sencondaryindex.kafka.producer;

import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

public class DalKafkaProducerConfiguration {
	
private static final Logger LOG = LoggerFactory.getLogger(DalKafkaProducerConfiguration.class);
	
	public static DalKafkaProducer createProducer(String kafkaBrokerPath) throws IOException {
		BaseKafkaProducer<Dal2ndIdxKafkaModel> kafkaProducer = new DalKafkaProducer();
		try {
			if(StringUtils.isNotBlank(kafkaBrokerPath)){				
				DalKafkaProducer.kafkaProducerConfig = kafkaProducer.initConfig(kafkaBrokerPath);
			}
			kafkaProducer.setProducer_type(KafkaConstant.ASYNC);
			kafkaProducer.setProduce_serilize_class(KafkaConstant.BYTEENCODER);
			kafkaProducer.init();
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		return (DalKafkaProducer) kafkaProducer;
	}
}

