/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lppz.util.rocketmq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.lppz.util.rocketmq.listener.LppzMessageDisruptorListenerConcurrency;
import com.lppz.util.rocketmq.listener.LppzMessageDisruptorListenerOrderly;
import com.lppz.util.rocketmq.listener.LppzMessageListenerConcurrency;
import com.lppz.util.rocketmq.listener.LppzMessageListenerOrderly;

public class RocketMqConsumer {
	private final Logger logger = LoggerFactory
			.getLogger(RocketMqConsumer.class);
	private static RocketMqConsumer instance = new RocketMqConsumer();

	private RocketMqConsumer() {
	}

	public static RocketMqConsumer getInstance() {
		return instance;
	}

	public <T,R,U> DefaultMQPushConsumer startConcurrency(ConsumerParam param,
			LppzMessageListenerConcurrency<T,R,U> mlc) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		setConsumerParam(param, consumer);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		return consumer;
	}
	
	public <T,R,U> DefaultMQPushConsumer startDisruptorConcurrency(ConsumerParam param,
			LppzMessageDisruptorListenerConcurrency<T,R,U> mlc) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		param.setConsumeBatch(false);
		setConsumerParam(param, consumer);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		return consumer;
	}

	public void setConsumerParam(ConsumerParam param,
			DefaultMQPushConsumer consumer) {
		consumer.setNamesrvAddr(param.getNamesrvAddr());
		consumer.setConsumeThreadMin(param.getConsumeMinThread());
		consumer.setConsumeThreadMax(param.getConsumeMaxThread());
		if(param.isConsumeBatch()){
			consumer.setPullInterval(param.getConsumeBatchMaxNum());
			consumer.setConsumeMessageBatchMaxSize(param.getConsumeBatchMaxNum());
		}
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
	}
	
	public <T,R,U> DefaultMQPushConsumer startOrderly(ConsumerParam param,
			LppzMessageListenerOrderly<T,R,U> mlc) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		setConsumerParam(param, consumer);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		return consumer;
	}
	
	public <T,R,U> DefaultMQPushConsumer startDisruptorOrderly(ConsumerParam param,
			LppzMessageDisruptorListenerOrderly<T,R,U> mlc) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		param.setConsumeBatch(false);
		setConsumerParam(param, consumer);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		return consumer;
	}
	
	public void shutDownConsumer(DefaultMQPushConsumer consumer) {
		if(consumer==null)
			return;
		logger.info("Consumer shutdown...");
		consumer.shutdown();
	}
}