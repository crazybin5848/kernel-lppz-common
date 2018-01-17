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
package com.lppz.jstorm.jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.configuration.jedis.BaseJedisClusterConfiguration;
import com.lppz.jstorm.JstormLppzUtil;

@SuppressWarnings("rawtypes")
public abstract class JedisLppzBolt extends BaseBasicBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3362338325776946959L;

	private static final Logger LOG = LoggerFactory
			.getLogger(JedisLppzBolt.class);

	protected Map conf;
	private transient Jedis jedisSentinel;
	private transient JedisCluster jedisCluster;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		String jedisClusterYamlConfPath = (String) conf
				.get(JstormLppzUtil.JEDISCLUSTERPATH);
		String jedisSentinelYamlConfPath = (String) conf
				.get(JstormLppzUtil.JEDISSENTINELPATH);
//		try {
//			if (StringUtils.isNotBlank(jedisSentinelYamlConfPath)) {
//				if (new File(jedisSentinelYamlConfPath).exists()) {
//					jedisSentinel = BaseJedisClusterConfiguration
//							.buildJedisSentinelProxy(new FileInputStream(
//									jedisSentinelYamlConfPath));
//				}
//			} else {
//				InputStream input = JedisLppzBolt.class
//						.getResourceAsStream("/META-INF/jedis-sentinel.yaml");
//				if (input != null && input.available() > 0) {
//					jedisSentinel = BaseJedisClusterConfiguration
//							.buildJedisSentinelProxy(input);
//					input.close();
//				}
//			}
//			if (StringUtils.isNotBlank(jedisClusterYamlConfPath)) {
//				if (new File(jedisClusterYamlConfPath).exists()) {
//					jedisCluster = BaseJedisClusterConfiguration
//							.getJedisCluster(new FileInputStream(
//									jedisClusterYamlConfPath));
//				}
//			} else {
//				InputStream input = JedisLppzBolt.class
//						.getResourceAsStream("/META-INF/jedis-cluster.yaml");
//				if (input != null && input.available() > 0) {
//					jedisCluster = BaseJedisClusterConfiguration
//							.getJedisCluster(input);
//					input.close();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		LOG.info("Successfully do prepare");
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			doExec(input, collector, jedisCluster, jedisSentinel);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new FailedException(e.getMessage(), e);
		}
	}

	protected abstract void doExec(Tuple input, BasicOutputCollector collector,
			JedisCluster jedisCluster, Jedis jedisSentinel) throws Exception;

	@Override
	public void cleanup() {
		if (jedisCluster != null)
			jedisCluster.close();
		if (jedisSentinel != null)
			jedisSentinel.close();
		LOG.info("Successfully do cleanup");
	}
}
