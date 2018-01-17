package com.lppz.configuration.biz.producer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.lppz.oms.kafka.constant.Topic;
import com.lppz.oms.kafka.dto.BizLogDto;
import com.lppz.util.kafka.producer.BaseKafkaProducer;
@Component("lppzExProducer")
public class LppzExProducer extends BaseKafkaProducer<BizLogDto> implements InitializingBean{

	@Override
	protected void resetTopic() {
		setTopic(Topic.EXCPETION_TOPIC);
	}


	@Override
	protected Object generateMsg(BizLogDto t) {
		return t;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		super.init();
	}
}
