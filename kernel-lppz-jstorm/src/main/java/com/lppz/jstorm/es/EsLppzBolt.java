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
package com.lppz.jstorm.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.support.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.elasticsearch.EsClientUtil;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.jstorm.JstormLppzUtil;

@SuppressWarnings("rawtypes")
public abstract class EsLppzBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 5720810158625748042L;
	private static final Logger LOG = LoggerFactory.getLogger(EsLppzBolt.class);
	protected Map conf;
	private transient AbstractClient esClient;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		if (LppzEsComponent.getInstance().getClient() != null) {
			esClient = LppzEsComponent.getInstance().getClient();
			return;
		}
		String esYamlConfPath = (String) conf
				.get(JstormLppzUtil.ESCLUSTERPATH);
		try {
			if (StringUtils.isNotBlank(esYamlConfPath)) {
				if (new File(esYamlConfPath).exists()) {
					esClient = EsClientUtil
							.buildPoolClientProxy(new FileInputStream(
									esYamlConfPath));
				}
			} else {
				InputStream in = EsLppzBolt.class
						.getResourceAsStream("/META-INF/es-cluster.yaml");
				if (in != null && in.available() > 0) {
					esClient = EsClientUtil.buildPoolClientProxy(in);
					in.close();
				}
			}
			LppzEsComponent.getInstance().setClient(esClient);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.info("Successfully do prepare");
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			doExec(input, collector, esClient);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new FailedException(e.getMessage(), e);
		}

	}

	protected abstract void doExec(Tuple input, BasicOutputCollector collector,
			AbstractClient esClient) throws Exception;

	@Override
	public void cleanup() {
		if (esClient != null)
			esClient.close();
		LOG.info("Successfully do cleanup");
	}
}
