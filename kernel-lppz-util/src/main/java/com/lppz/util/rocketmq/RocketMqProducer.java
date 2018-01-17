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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.lppz.util.rocketmq.enums.RocketMqProducerTypeEnums;

public class RocketMqProducer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6756217809935335480L;
	private final Logger logger = LoggerFactory
			.getLogger(RocketMqProducer.class);

	private RocketMqProducer() {
	}

	private static RocketMqProducer instance = new RocketMqProducer();

	public static RocketMqProducer getInstance() {
		return instance;
	}

	private Map<String, DefaultMQProducer> map = new ConcurrentHashMap<String, DefaultMQProducer>();

	public DefaultMQProducer buildProducer(String producerGroup,
			String namesrvAddr, String prodInstanceName) {
		String key = producerGroup + "##" + namesrvAddr + "##"
				+ prodInstanceName;
		if (map.containsKey(key))
			return map.get(key);
		synchronized (this) {
			if (map.containsKey(key))
				return map.get(key);
			DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
			producer.setNamesrvAddr(namesrvAddr);
			producer.setInstanceName(prodInstanceName);
			try {
				producer.start();
				map.put(key, producer);
				return producer;
			} catch (MQClientException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	public void shutDownProducer(DefaultMQProducer producer) {
		if (producer == null)
			return;
		producer.shutdown();
		String key = producer.getProducerGroup() + "##"
				+ producer.getNamesrvAddr() + "##" + producer.getInstanceName();
		map.remove(key);
	}

	public <T> SendResult sendMsgConcurrenly(DefaultMQProducer producer,
			ProducerParam<T> param) {
		if (producer == null || param == null)
			return null;
		Message msg = param.buildMessage();
		msg.putUserProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE, RocketMqProducerTypeEnums.CONCURRENCY.getDes());
		try {
			return producer.send(msg);
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			logger.error(param + "::" + e.getMessage(), e);
		}
		return null;
	}
	
	public <T> SendResult sendMsgConcurrenly(DefaultMQProducer producer,
			Message msg) {
		if (producer == null || msg == null)
			return null;
		try {
			return producer.send(msg);
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			logger.error(msg + "::" + e.getMessage(), e);
		}
		return null;
	}

	public <T> SendResult sendMsgOrderly(DefaultMQProducer producer,
			ProducerParam<T> param, int ordershardId) {
		if (producer == null || param == null)
			return null;
		Message msg = param.buildMessage();
		msg.putUserProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE, RocketMqProducerTypeEnums.ORDERLY.getDes());
		try {
			SendResult sendResult = producer.send(msg,
					new MessageQueueSelector() {
						@Override
						public MessageQueue select(List<MessageQueue> mqs,
								Message msg, Object arg) {
							Integer id = (Integer) arg;
							int index = id % mqs.size();
							return mqs.get(index);
						}
					}, ordershardId);
			return sendResult;
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			logger.error(param + "::" + e.getMessage(), e);
		}
		return null;
	}
	
	public <T> SendResult sendMsgOrderly(DefaultMQProducer producer,
			Message msg, int ordershardId) {
		if (producer == null || msg == null)
			return null;
		try {
			SendResult sendResult = producer.send(msg,
					new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs,
						Message msg, Object arg) {
					Integer id = (Integer) arg;
					int index = id % mqs.size();
					return mqs.get(index);
				}
			}, ordershardId);
			return sendResult;
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			logger.error(msg + "::" + e.getMessage(), e);
		}
		return null;
	}
}