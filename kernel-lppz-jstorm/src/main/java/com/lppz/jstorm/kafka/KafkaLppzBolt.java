/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lppz.jstorm.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.jstorm.JstormLppzUtil;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

@SuppressWarnings("rawtypes")
public abstract class KafkaLppzBolt<T> extends BaseBasicBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4565620394159805565L;
	private static final Logger LOG = LoggerFactory
			.getLogger(KafkaLppzBolt.class);
	protected Map conf;
	protected String _topic;
	protected String _producer_type=KafkaConstant.ASYNC;
	protected String _produce_serilize_class=KafkaConstant.STRINGENCODER;
	private transient BaseKafkaProducer<T> producer;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		final String kafkaBrokerPath = (String) conf
				.get(JstormLppzUtil.KAFKABROKERPATH);
			producer = new BaseKafkaProducer<T>() {

				@Override
				protected void resetTopic() {
					super.setTopic(_topic);
				}

				@Override
				public void init() throws Exception {
					super.setProducer_type(_producer_type);
					super.setProduce_serilize_class(_produce_serilize_class);
					kafkaProducerConfig=super.initConfig(kafkaBrokerPath);
					super.init();
				}
			};
			try {
				producer.init();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			LOG.info("Successfully do prepare");
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			doExec(input, collector,producer);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new FailedException(e.getMessage(), e);
		}

	}

	protected abstract void doExec(Tuple input, BasicOutputCollector collector, BaseKafkaProducer<T> producer) throws Exception;

	@Override
	public void cleanup() {
		if (producer != null)
			producer.close();
		LOG.info("Successfully do cleanup");
	}
}
