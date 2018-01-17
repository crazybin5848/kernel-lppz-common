package com.lppz.jstorm.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.lppz.util.http.BaseHttpClientsComponent;
@SuppressWarnings("rawtypes")
public abstract class RestLppzBolt extends BaseBasicBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4362338325776946959L;

	private static final Logger LOG = LoggerFactory
			.getLogger(RestLppzBolt.class);
	
	protected Map conf;
	private transient BaseHttpClientsComponent restClientComponent;
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.conf = stormConf;
		restClientComponent=new BaseHttpClientsComponent();
		try {
			restClientComponent.init();
		} catch (Exception e) {
		}
		LOG.info("Successfully do prepare");
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector)
	{
		restClientComponent.initHttpClient();
		try {
			doExec(input,collector,restClientComponent);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			restClientComponent.closeHttpClient();
			throw new FailedException(e.getMessage(),e);
		}
	}

	protected abstract void doExec(Tuple input, BasicOutputCollector collector,BaseHttpClientsComponent restClientComponent) throws Exception;

	@Override
	public void cleanup() {
		restClientComponent.closeHttpClient();
		LOG.info("Successfully do cleanup");
	}
}
