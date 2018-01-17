//package com.lppz.diamond.client.util;
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.lppz.diamond.client.PropertiesConfiguration;
//
//public class LoadPropUtils {
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(LoadPropUtils.class);
//
//	public static Properties loadPropeties() {
//		Properties props = new Properties();
//		InputStream propsStream;
//		propsStream = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("main.properties");
//		try {
//			props.load(propsStream);
//		} catch (IOException e) {
//			LOGGER.warn("获取配置文件失败", e);
//		}
//		return props;
//	}
//
//	public static void writePropeties(Properties props) {
//
//		String hostname = props.getProperty("hostname");
//		int port = Integer.valueOf(props.getProperty("port"));
//		String projcode = props.getProperty("projcode");
//		String profile = props.getProperty("profile");
//		String modules = props.getProperty("modules");
//		String[] moduleArr = modules.split(",");
//		for (String m : moduleArr) {
//			// PropertiesConfiguration config = new
//			// PropertiesConfiguration("localhost", 8283, "cloud-service",
//			// "development", "jdbc");
//			PropertiesConfiguration config = new PropertiesConfiguration(
//					hostname, port, projcode, profile, m);
//			// config.addConfigurationListener(new ConfigurationListenerTest());
//			System.out.println(config.getString("fuck"));
//			Properties outprops = new Properties();
//			outprops = config.getProperties();
//			try {
//				BufferedWriter writer = new BufferedWriter(
//						new OutputStreamWriter(
//								new FileOutputStream(
//										"F://lppz-diamond-server-1.1.0-SNAPSHOT//test.properties"),
//								"UTF8"));
//				outprops.store(writer, "test");
//			} catch (Exception e) {
//				e.printStackTrace();
//			  }
//		}
//
//	}
//
//	public static void main(String[] args) {
//
//		Properties props = new Properties();
//		props = LoadPropUtils.loadPropeties();
//
//		LoadPropUtils.writePropeties(props);
//
//		/*
//		 * Properties props = new Properties(); //PropertiesConfiguration config
//		 * = new PropertiesConfiguration("localhost", 8283, "cloud-service",
//		 * "development", "jdbc"); PropertiesConfiguration config = new
//		 * PropertiesConfiguration("localhost", 8283, "fuck", "development",
//		 * "dubbo"); config.addConfigurationListener(new
//		 * ConfigurationListenerTest());
//		 * System.out.println(config.getString("fuck")); props =
//		 * config.getProperties(); try { BufferedWriter writer = new
//		 * BufferedWriter(new OutputStreamWriter(new FileOutputStream(
//		 * "F://lppz-diamond-server-1.1.0-SNAPSHOT//test.properties"),"UTF8"));
//		 * props.store(writer, "test"); } catch (Exception e) {
//		 * e.printStackTrace(); }
//		 */
//
//	}
//
//}
