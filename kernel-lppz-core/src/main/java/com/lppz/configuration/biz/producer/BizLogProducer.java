/*
 * Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work. Neither this
 * material nor any portion hereof may be copied or distributed without the
 * express written consent of Home Box Office, Inc. This material also contains
 * proprietary and confidential information of Home Box Office, Inc. and its
 * suppliers, and may not be used by or disclosed to any person, in whole or in
 * part, without the prior written consent of Yi Hao Dian, Inc.
 */
package com.lppz.configuration.biz.producer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.lppz.oms.kafka.constant.Topic;
import com.lppz.oms.kafka.dto.BizLogDto;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

/**
 * Bin
 */
@Component("bizLogProducer")
public class BizLogProducer extends BaseKafkaProducer<BizLogDto> implements InitializingBean{

	@Override
	protected void resetTopic() {
		setTopic(Topic.DEFAULT_TOPIC);
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
