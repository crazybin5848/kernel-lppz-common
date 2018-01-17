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
package io.mycat;

import io.mycat.config.loader.zkprocess.comm.ZkConfig;
import io.mycat.config.loader.zkprocess.comm.ZkParamCfg;
import io.mycat.config.loader.zkprocess.xmltozk.XmltoZkMain;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;

import java.util.Date;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mycat
 */
public final class MycatShutdown {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MycatShutdown.class);
	private static boolean isShutdown;

	public static boolean isShutdown() {
		return isShutdown;
	}

	public static void main(String[] args) {
		LOGGER.info(new Date() + ",server shutdown!");
        String custerName = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_CLUSTERID);
		String myid = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_MYID);
        CuratorFramework zkConn = XmltoZkMain.buildConnection(ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_URL));
        String path="/mycat/" + custerName + "/line/"+myid;
		try {
			String dd =	new String(zkConn.getData().forPath(path));
			String[] tmpDD=dd.split(":");
			StringBuilder sb=new StringBuilder("");
			for(int i=0;i<tmpDD.length;i++){
					if(i==tmpDD.length-1){
						sb.append("0");
					}
					else
					sb.append(tmpDD[i]).append(":");
			}
			zkConn.setData().forPath(path, sb.toString().getBytes());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		finally{
//			if(zkConn!=null)
//			zkConn.close();
		}
	}

	public static void shutdown() {
		isShutdown=true;
		CatSecondaryEsIndexHandler.getInstance().rollBackAll();
		CatSecondaryEsIndexHandler.getInstance().getLogSender().destory();
		LOGGER.info("successfully shutdown mycat");
		System.exit(0);
	}
}