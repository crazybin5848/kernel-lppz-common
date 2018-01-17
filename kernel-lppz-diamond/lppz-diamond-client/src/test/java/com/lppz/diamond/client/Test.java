package com.lppz.diamond.client;
import javax.naming.ConfigurationException;

public class Test {

	/**
	 * @param args
	 * @throws ConfigurationException
	 */
	public static void main(String[] args) throws Exception {
			PropertiesConfiguration config = new PropertiesConfiguration("10.6.24.11", 8283, "kernel-lppz-cachenew", "development", "dubbo.properties",false);
			config.addConfigurationListener(new ConfigurationListenerTest());
			System.out.println(config.getString("dubbo.serialization"));
	
			System.out.println("------------------");
			/*
			PropertiesConfiguration config2 = new PropertiesConfiguration("127.0.0.1", 8283, "kernel-lppz-cachenew", "development", "dubbo.properties",false);
			config2.addConfigurationListener(new ConfigurationListenerTest());
			System.out.println(config2.getString("dubbo.serialization"));
			*/
	}
}