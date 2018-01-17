package com.lppz.configuration.hbase;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lppz.hbase.client.HBaseDmlInterface;
import com.lppz.hbase.client.HBaseScanInterface;
import com.lppz.hbase.client.configuration.HbaseClientConfiguration;
import com.lppz.hbase.client.exception.HbaseConfigException;
import com.lppz.hbase.client.impl.HBaseDmlImpl;
import com.lppz.hbase.client.impl.HBaseScanImpl;

@Configuration
public class HbaseConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(HbaseConfiguration.class);

	@Bean(name = "hBaseDml")
	public HBaseDmlInterface hBaseDmlClient() throws IOException {
		HBaseDmlInterface hBaseDml = null;
		try {
			init();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return hBaseDml;
		}

		return new HBaseDmlImpl();
	}

	@Bean(name = "hBaseScan")
	public HBaseScanInterface hBaseScanClient() throws IOException {
		HBaseScanInterface hBaseScan = null;
		try {
			init();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return hBaseScan;
		}
		return new HBaseScanImpl();
	}

	private void init() throws IOException, HbaseConfigException {
		HbaseClientConfiguration.getInstance().init();
	}
}
