///*
// * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
// * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
// *
// * This code is free software;Designed and Developed mainly by many Chinese 
// * opensource volunteers. you can redistribute it and/or modify it under the 
// * terms of the GNU General Public License version 2 only, as published by the
// * Free Software Foundation.
// *
// * This code is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
// * version 2 for more details (a copy is included in the LICENSE file that
// * accompanied this code).
// *
// * You should have received a copy of the GNU General Public License version
// * 2 along with this work; if not, write to the Free Software Foundation,
// * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
// * 
// * Any questions about this component can be directed to it's project Web address 
// * https://code.google.com/p/opencloudb/.
// *
// */
//package com.lppz.dal;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.apache.log4j.helpers.LogLog;
//import org.opencloudb.config.model.SystemConfig;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//
//import redis.clients.jedis.JedisCluster;
//
///**
// * @author binzou
// */
//@Configuration
////@PropertySource("classpath:/redis.properties")
//
//@ComponentScan(basePackages = "com.lppz.dal")
//public class MycatStartup {
//	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
//	private static AnnotationConfigApplicationContext context;
//	private static JedisCluster jedisCluster;
//	static {
//		System.setProperty(SystemConfig.SYS_HOME, System.getProperty("user.dir"));
//		String home = SystemConfig.getHomePath();
//		if (home == null) {
//			System.out.println(SystemConfig.SYS_HOME + "  is not set.");
//			System.exit(-1);
//		}
//		context = new AnnotationConfigApplicationContext(MycatStartup.class);
//		jedisCluster=(JedisCluster)context.getBean("jedisCluster");
//	}
//	
//	@Bean
//	public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
//	
//	public static void main(String[] args) {
//		try {
//			// init
//			MycatServer server = MycatServer.getInstance();
//			server.beforeStart();
//
//			// startup
//			server.startup();
//			System.out.println("MyCAT Server startup successfully. see logs in logs/mycat.log");
//			while (true) {
//				Thread.sleep(300 * 1000);
//			}
//		} catch (Exception e) {
//			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//			LogLog.error(sdf.format(new Date()) + " startup error", e);
//			System.exit(-1);
//		}
//	}
//}