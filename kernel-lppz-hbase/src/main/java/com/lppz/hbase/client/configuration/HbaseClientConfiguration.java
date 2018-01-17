package com.lppz.hbase.client.configuration;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.BaseClientUtil;
import org.apache.hadoop.hbase.client.HbaseClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lppz.configuration.es.EsBaseYamlBean;
import com.lppz.configuration.hbase.HBaseConfigYamlBean;
import com.lppz.hbase.client.exception.HbaseConfigException;

public class HbaseClientConfiguration extends BaseHbaseClientConfig{
	private static final Logger logger = LoggerFactory
			.getLogger(HbaseClientConfiguration.class);
	private static HbaseClientConfiguration hbaseClientConfiguration = new HbaseClientConfiguration();

	private ConcurrentMap<String, HbaseClientInterface> m = new ConcurrentHashMap<String, HbaseClientInterface>();

	private boolean isinit;

	public static HbaseClientConfiguration getInstance() {
		return hbaseClientConfiguration;
	}

	private HbaseClientConfiguration() {

	}

	private Configuration init(String clientPort, String quorum, Long timeout,
			Integer period, Long caching) throws IOException {
		Configuration conf = BaseClientUtil.initConf(clientPort, quorum, timeout, period,
				caching);
		//初始化操作，否则RecoverableZooKeeper对象为空，http post请求地址为null
		init(conf);
		initConf(conf);
		return conf;
	}

	public synchronized void init(String... yaml) throws IOException,
			HbaseConfigException {
		if (this.isinit)
			return;
		String yypath = yaml == null || yaml.length == 0 ? null : yaml[0];
		HBaseConfigYamlBean hbaseBean = BaseClientUtil.initHBaseConfig(yypath);
		if (hbaseBean == null)
			throw new HbaseConfigException("lack of hbaseYaml config File");
		String clientPort = hbaseBean.getZkClientPort();
		String quorum = hbaseBean.getZkQuorum();
		if (clientPort == null || quorum == null)
			throw new HbaseConfigException("config properties has error!");
		Long timeout = hbaseBean.getHbaseRpcTimeOut();
		Integer period = hbaseBean.getHbaseScanTimeoutPeriod();
		Long caching = hbaseBean.getHbaseClientScannerCaching();
		Configuration cfg=init(clientPort, quorum, timeout, period, caching);
		//上面一步已经初始化，这里注释掉
		//super.init(cfg);
		EsBaseYamlBean esBean=BaseClientUtil.getEsBeanFromZk(rz);
		hbaseBean.setEsBean(esBean);
		BaseClientUtil.initEs4HBase(hbaseBean);
		this.isinit = true;
	}

	public HbaseClientInterface client(
			Class<? extends HbaseClientInterface> clzz) {
		HbaseClientInterface client = m.get(clzz.getSimpleName());
		if (client == null) {
			if (clzz.isInterface() || Modifier.isAbstract(clzz.getModifiers())) {
				return null;
			}
			if (m.size() == 0) {
				try {
					hbaseClientConfiguration.init();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return null;
				}
			}
			try {
				m.put(clzz.getSimpleName(), clzz.newInstance());
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return m.get(clzz.getSimpleName());
	}
}