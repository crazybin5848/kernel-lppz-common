/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.util.thread;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ShardingByIpAndPortComponent 
{
	private static final Logger logger = LoggerFactory.getLogger(ShardingByIpAndPortComponent.class);
	private Map<String,String> clusterMap=new HashMap<String,String>();
	
	private String nodeList;
	
	public String getNodeList() {
		return nodeList;
	}
	public void setNodeList(String nodeList) {
		logger.info("setNodeList:{}",nodeList);
		this.nodeList = nodeList;
	}
	private String node;
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		logger.info("setNode:{}",node);
		this.node = node;
	}
	public Map<String, String> getClusterMap() {
		return clusterMap;
	}
	public void setClusterMap(Map<String, String> clusterMap) {
		this.clusterMap = clusterMap;
	}

	public void init() throws Exception {
		if(nodeList==null||nodeList.isEmpty()){
			this.node="-1";
			return;
		}
		String[] arr=nodeList.split(",");
		for(int i=0;i<arr.length; i++){
			clusterMap.put(arr[i],Integer.toString(i));
		}
		logger.info("xxxxxxxxxxxxxxxxxx {}",clusterMap);
	    this.node=checkNode();
	}
	
	private String checkNode()
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs;
		try {
			objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
			        Query.match(Query.attr("protocol"), Query.value("*")));
		    String hostname = InetAddress.getLocalHost().getHostName();
		    InetAddress[] addresses = InetAddress.getAllByName(hostname);
		    for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
		        ObjectName obj = i.next();
		        String port = obj.getKeyProperty("port");
		        for (InetAddress addr : addresses) {
		            String ip=addr.getHostAddress();
					if (ip.equalsIgnoreCase("127.0.0.1") || ip.equalsIgnoreCase("localhost"))
						continue;
					
					if (clusterMap.containsKey(ip+":"+port)){
						return clusterMap.get(ip+":"+port);
					}
		        }
		    }
		} catch (Exception  e) {
			logger.error(e.getMessage(),e);
		}
		return "-1";
	}
	
}
