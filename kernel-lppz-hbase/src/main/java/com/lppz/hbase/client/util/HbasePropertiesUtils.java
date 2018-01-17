package com.lppz.hbase.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbasePropertiesUtils {
	private static final Logger LOG = LoggerFactory
			.getLogger(HbasePropertiesUtils.class);

	private static Properties prop;

	static {
		loadProperties(null);
	}

	public static void setProp(Properties prop) {
		HbasePropertiesUtils.prop = prop;
	}

	public static String getKey(final String keystr) throws IOException {
		return prop == null ? null : prop.getProperty(keystr);
	}

	public static void loadProperties(Class<?> clazz) {
		InputStream in = (clazz == null ? HbasePropertiesUtils.class : clazz)
				.getResourceAsStream("/META-INF/hbase.properties");
		try {
			if (in != null) {
				prop = new Properties();
				prop.load(in);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}
}