package com.lppz.jstorm;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;

import com.alibaba.jstorm.utils.JStormUtils;
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class LppzTopologyBuilder {
	public final static String TOPOLOGY_SPOUT_PARALLELISM_HINT = "spout.parallel";
	public final static String TOPOLOGY_BOLT_PARALLELISM_HINT = "bolt.parallel";
	
	public TopologyBuilder build(Map conf){
		TopologyBuilder builder=new TopologyBuilder();
		fillBuilder(builder,conf);
		int workerNum = JStormUtils.parseInt(conf.get(Config.TOPOLOGY_WORKERS),
				1);
		conf.put(Config.TOPOLOGY_WORKERS, workerNum);
		return builder;
	}
	
	protected abstract void fillBuilder(TopologyBuilder builder,Map conf);
}
