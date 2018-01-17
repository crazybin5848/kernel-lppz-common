/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package com.lppz.mycat.monitor;



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.mycat.monitor.trigger.MycatNodeMonitorTrigger;
import com.lppz.mycat.monitor.utils.ZkConfig;

/**
 * @author mycat
 */
public final class MycatMonitorStartup {
	private static final Logger logger = LoggerFactory.getLogger(MycatMonitorStartup.class);
    public static void main(String[] args) {
    	final MycatNodeMonitorTrigger trigger = new MycatNodeMonitorTrigger();
    	ScheduledExecutorService excuter = Executors.newSingleThreadScheduledExecutor();
    	String zkUrl = ZkConfig.getInstance().getZkURL();
    	final CuratorFramework zkConn = ZkConfig.getInstance().buildConnection(zkUrl);
		
    	excuter.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				try {
					logger.info("start monitor mycatNodes:\r\n");
					trigger.checkMycatNodes(zkConn);
				} catch (Exception e) {
					logger.error("mycat 监控异常", e);
				}
				
			}
		},1, 10, TimeUnit.SECONDS);
    }
}
