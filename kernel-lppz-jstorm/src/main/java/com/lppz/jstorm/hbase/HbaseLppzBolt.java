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
package com.lppz.jstorm.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.hbase.client.HBaseDmlInterface;
import com.lppz.hbase.client.HBaseScanInterface;
import com.lppz.hbase.client.configuration.HbaseClientConfiguration;
import com.lppz.hbase.client.impl.HBaseDmlImpl;
import com.lppz.hbase.client.impl.HBaseScanImpl;
import com.lppz.hbase.client.util.HbasePropertiesUtils;
import com.lppz.jstorm.JstormLppzUtil;

@SuppressWarnings("rawtypes")
public abstract class HbaseLppzBolt extends BaseBasicBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3362338325776946959L;

	private static final Logger LOG = LoggerFactory
			.getLogger(HbaseLppzBolt.class);

	protected Map conf;
	private transient HBaseScanInterface hBaseScanInterface;
	private transient HBaseDmlInterface hBaseDmlInterface;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		String hbaseConfPath = (String) conf
				.get(JstormLppzUtil.HBASECONFPATH);
		try {
			if (StringUtils.isNotBlank(hbaseConfPath)) {
				if (new File(hbaseConfPath).exists()) {
					Properties props=new Properties();
					props.load(new FileInputStream(hbaseConfPath));
					HbasePropertiesUtils.setProp(props);
				}
			} 
			else{
				HbasePropertiesUtils.loadProperties(HbaseLppzBolt.class);
			}
			HbaseClientConfiguration.getInstance().init();
			hBaseScanInterface=new HBaseScanImpl();
			hBaseDmlInterface=new HBaseDmlImpl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info("Successfully do prepare");
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			doExec(input, collector, hBaseScanInterface, hBaseDmlInterface);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new FailedException(e.getMessage(), e);
		}
	}

	protected abstract void doExec(Tuple input, BasicOutputCollector collector,
			HBaseScanInterface hBaseScanInterface, HBaseDmlInterface hBaseDmlInterface) throws Exception;

	@Override
	public void cleanup() {
		LOG.info("Successfully do cleanup");
	}
}
