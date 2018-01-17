package com.lppz.configuration.dubbo.log;

import com.alibaba.dubbo.rpc.support.DubboInvokeDetail;
import com.alibaba.dubbo.rpc.support.KafkaProducer;
import com.lppz.oms.kafka.constant.Topic;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

public class DubboLoggerProducer extends BaseKafkaProducer<DubboInvokeDetail> implements KafkaProducer<DubboInvokeDetail> {

	@Override
	protected void resetTopic() {
		setTopic(Topic.DUBBOLOG);
	}

	@Override
	protected Object generateMsg(DubboInvokeDetail t) {
		return t;
	}
}
