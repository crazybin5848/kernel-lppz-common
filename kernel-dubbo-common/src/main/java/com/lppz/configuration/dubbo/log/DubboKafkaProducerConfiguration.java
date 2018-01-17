package com.lppz.configuration.dubbo.log;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.rpc.support.DubboInvokeDetail;
import com.alibaba.dubbo.rpc.support.KafkaDubboUtil;
import com.alibaba.dubbo.rpc.support.KafkaProducer;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

@Configuration
public class DubboKafkaProducerConfiguration  {
	private static final Logger LOG = LoggerFactory.getLogger(DubboKafkaProducerConfiguration.class);
	
	@SuppressWarnings("unchecked")
	@Bean(name = "dubboLoggerProducer")
	public DubboLoggerProducer dubboLoggerProducer() throws IOException {
		BaseKafkaProducer<DubboInvokeDetail> dubboProducer=new DubboLoggerProducer();
		try {
			dubboProducer.setProducer_type(KafkaConstant.ASYNC);
			dubboProducer.init();
			KafkaDubboUtil.setLogSender((KafkaProducer<DubboInvokeDetail>) dubboProducer);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		return (DubboLoggerProducer) dubboProducer;
	}

}
