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
package com.lppz.jstorm.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.yaml.snakeyaml.Yaml;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.jstorm.JstormLppzUtil;

@SuppressWarnings("rawtypes")
public abstract class JdbcTemplateLppzBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 5720810158625748042L;
	private static final Logger LOG = LoggerFactory
			.getLogger(JdbcTemplateLppzBolt.class);
	protected Map conf;
	private transient DynamicDataSource ds;
	private transient Map<String,StormTransactionTemplate> mapTT;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		initDs();
		mapTT=new HashMap<String,StormTransactionTemplate>();
		for(Object dds:ds.getTargetDataSources().keySet()){
			DataSource dss=(DataSource)ds.getTargetDataSources().get(dds);
			PlatformTransactionManager tx=new DataSourceTransactionManager(dss);
			StormTransactionTemplate tt=new StormTransactionTemplate(new JdbcTemplate(dss),tx);  
			mapTT.put((String)dds, tt);
		}
		LOG.info("Successfully do prepare");
	}

	@SuppressWarnings("unchecked")
	private void initDs() {
		Map<Object, Object> targetDataSources=null;
		String dsYamlConfPath = (String) conf
				.get(JstormLppzUtil.JDBCDATASOURCEPATH);
		try {
			if (StringUtils.isNotBlank(dsYamlConfPath)) {
				if (new File(dsYamlConfPath).exists()) {
					targetDataSources = (Map<Object, Object>) new Yaml()
					.load(new FileSystemResource(dsYamlConfPath)
					.getInputStream());
				} 
			}
			else {
				InputStream in=JdbcTemplateLppzBolt.class.getResourceAsStream("/META-INF/ds-cluster.yaml");
				if(in!=null&&in.available()>0){
					targetDataSources = (Map<Object, Object>) new Yaml().load(in);
					in.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LppzBasicDataSource lb = (LppzBasicDataSource) targetDataSources
				.values().iterator().next();
		ds = new DynamicDataSource(lb, targetDataSources);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		if(input.getSourceStreamId().equals(JstormLppzUtil.COMMIT)){
			doCommit(input, collector,mapTT);
			return;
		}
		try {
			doExec(input, collector,mapTT);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new FailedException(e.getMessage(), e);
		}
	}
	protected abstract void doCommit(Tuple input, BasicOutputCollector collector,
			Map<String, StormTransactionTemplate> mapTT);
	protected abstract void doExec(Tuple input, BasicOutputCollector collector, Map<String, StormTransactionTemplate> mapTT) throws Exception;
//	{
//		final String x=(String) input.getValue(0);
//		final StormTransactionTemplate stt=mapTT.get("fuck");
//		stt.getJt().query(psc, rse)
//		new StormDBTemplate(stt).doIntrans(new JdbcHandler() {
//			@Override
//			public void handleInTrans(JdbcTemplate jt) {
//				jt.update(x);
//				jt.batchUpdate(sql);
//			}
//		});
//	}

	@Override
	public void cleanup() {
		if (ds != null)
			try {
				ds.destory();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		LOG.info("Successfully do cleanup");
	}
}
