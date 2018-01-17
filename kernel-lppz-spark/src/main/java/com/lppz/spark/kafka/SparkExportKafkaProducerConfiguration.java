package com.lppz.spark.kafka;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.bean.Spark2kafkaBean;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

public class SparkExportKafkaProducerConfiguration {
	
private static final Logger LOG = LoggerFactory.getLogger(SparkExportKafkaProducerConfiguration.class);
	
	public static SparkExportProducer createProducer(String kafkaBrokerPath) throws IOException {
		BaseKafkaProducer<Spark2kafkaBean> kafkaProducer=new SparkExportProducer();
		try {
			if(StringUtils.isNotBlank(kafkaBrokerPath)){				
				SparkExportProducer.kafkaProducerConfig = kafkaProducer.initConfig(kafkaBrokerPath);
			}
			kafkaProducer.setProducer_type(KafkaConstant.ASYNC);
			kafkaProducer.setProduce_serilize_class(KafkaConstant.BYTEENCODER);
			kafkaProducer.init();
//			KafkaDubboUtil.setLogSender((KafkaProducer<DubboInvokeDetail>) dubboProducer);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		return (SparkExportProducer) kafkaProducer;
	}
	
	public static SparkExportStringProducer createStringProducer(String kafkaBrokerPath) throws IOException {
		BaseKafkaProducer<String> kafkaProducer=new SparkExportStringProducer();
		try {
			if(StringUtils.isNotBlank(kafkaBrokerPath)){				
				SparkExportStringProducer.kafkaProducerConfig = kafkaProducer.initConfig(kafkaBrokerPath);
			}
			kafkaProducer.setProducer_type(KafkaConstant.ASYNC);
			kafkaProducer.setProduce_serilize_class(KafkaConstant.STRINGENCODER);
			kafkaProducer.init();
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		return (SparkExportStringProducer) kafkaProducer;
	}
}

