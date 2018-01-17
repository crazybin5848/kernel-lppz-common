package com.lppz.jstorm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.TopologyAssignException;
import backtype.storm.topology.TopologyBuilder;

import com.alibaba.jstorm.utils.LoadConf;
@SuppressWarnings({"rawtypes","unchecked"})
public class JstormLppzUtil {

	public static final String JEDISCLUSTERPATH = "jedis.cluster.storm.path";
	public static final String JEDISSENTINELPATH = "jedis.sentinel.storm.path";
	public static final String ESCLUSTERPATH = "es.cluster.storm.path";
	public static final String KAFKABROKERPATH = "kafka.broker.storm.path";
	public static final String JDBCDATASOURCEPATH = "jdbc.datasource.storm.path";
	public static final String HBASECONFPATH = "hbase.conf.storm.path";
	public static final String COMMIT = "storm.batch.commit";

	public static Map LoadConf(String arg) {
		Map conf = new HashMap<Object, Object>();
		if (arg.endsWith("yaml")) {
			conf = LoadConf.LoadYaml(arg);
		} else {
			conf = LoadConf.LoadProperty(arg);
		}
		return conf;
	}

	public static Map LoadConf(InputStream in) {
		Map conf = new HashMap<Object, Object>();
		try {
			if (in != null && in.available() > 0) {
				conf = LoadConf.LoadYaml(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		return conf;
	}

	public static boolean local_mode(Map conf) {
		String mode = (String) conf.get(Config.STORM_CLUSTER_MODE);
		if (mode != null) {
			if (mode.equals("local")) {
				return true;
			}
		}

		return false;
	}

	
	public static void SetRemoteTopology(String name, TopologyBuilder builder,
			Map conf) throws AlreadyAliveException, InvalidTopologyException,
			TopologyAssignException {
		String streamName = (String) conf.get(Config.TOPOLOGY_NAME);
		if (streamName == null) {
			streamName = name;
		}
		conf.put(Config.STORM_CLUSTER_MODE, "distributed");
		StormSubmitter.submitTopology(streamName, conf,
				builder.createTopology());
	}

	public static void SetLocalTopology(String name, TopologyBuilder builder,
			Map conf) throws Exception {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("KafkaServiceTopology", conf,
				builder.createTopology());

		// Thread.sleep(60000);
		// cluster.killTopology("KafkaServiceTopology");
		// cluster.shutdown();
	}

	public static void buildTopoLogy(String[] args, LppzTopologyBuilder builder,
			String yamlPath, Class<?> topologyClazz) throws Exception,
			AlreadyAliveException, InvalidTopologyException,
			TopologyAssignException {
		Map conf = null;
		if (args.length > 0) {
			conf = LoadConf(args[0]);
		} else {
			InputStream in = topologyClazz.getResourceAsStream(yamlPath);
			conf = LoadConf(in);
		}
		TopologyBuilder tb=builder.build(conf);
		if (JstormLppzUtil.local_mode(conf)) {
			SetLocalTopology(topologyClazz.getName(), tb, conf);
		} else {
			SetRemoteTopology(topologyClazz.getName(), tb, conf);
		}
	}

	public static void SetDPRCTopology() throws AlreadyAliveException,
			InvalidTopologyException, TopologyAssignException {
		// LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder(
		// "exclamation");
		//
		// builder.addBolt(new TotalCount(), 3);
		//
		// Config conf = new Config();
		//
		// conf.setNumWorkers(3);
		// StormSubmitter.submitTopology("rpc", conf,
		// builder.createRemoteTopology());
		System.out
				.println("Please refer to com.alipay.dw.jstorm.example.drpc.ReachTopology");
	}
}
