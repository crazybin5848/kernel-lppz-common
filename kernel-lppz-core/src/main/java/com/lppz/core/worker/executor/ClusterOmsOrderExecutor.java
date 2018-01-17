///*
// * [y] hybris Platform
// * 
// * Copyright (c) 2000-2015 hybris AG
// * All rights reserved.
// * 
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// */
//package com.lppz.core.worker.executor;
//
//import java.lang.management.ManagementFactory;
//import java.net.InetAddress;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.management.MBeanServer;
//import javax.management.ObjectName;
//import javax.management.Query;
//
//import org.activiti.engine.impl.jobexecutor.JobExecutor;
//import org.activiti.engine.impl.persistence.entity.JobEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//
//import com.lppz.core.activiti.jobexecutor.runnable.ClusterAcquireJobsRunnable;
//import com.lppz.core.activiti.jobexecutor.runnable.ClusterExecuteJobsRunnable;
//
//
///**
// *
// */
//public class ClusterOmsOrderExecutor implements InitializingBean
//{
//	private static final Logger logger = LoggerFactory.getLogger(ClusterOmsOrderExecutor.class);
//	private Map<String,String> clusterMap=new HashMap<String,String>();
//	
//	private String nodeList;
//	
//	public String getNodeList() {
//		return nodeList;
//	}
//	public void setNodeList(String nodeList) {
//		this.nodeList = nodeList;
//	}
//	private String node;
//	
//	public String getNode() {
//		return node;
//	}
//	public void setNode(String node) {
//		this.node = node;
//	}
//	public Map<String, String> getClusterMap() {
//		return clusterMap;
//	}
//	public void setClusterMap(Map<String, String> clusterMap) {
//		this.clusterMap = clusterMap;
//	}
//	public void setJobBatchSize(int jobBatchSize) {
//		this.jobBatchSize = jobBatchSize;
//	}
//	private int jobBatchSize=1;
//	
//	private final ExecutorService pool = Executors.newCachedThreadPool();
//
//	public AtomicInteger executingJobCounter = new AtomicInteger(0);
//	
//	private ClusterAcquireJobsRunnable clusterAcquireJobsRunnable;
//	
//	private int concurrentThread=1;
//	public int getConcurrentThread() {
//		return concurrentThread;
//	}
//	public void setConcurrentThread(int concurrentThread) {
//		this.concurrentThread = concurrentThread;
//	}
//
//	/**
//	 * @return the jobBatchSize
//	 */
//	public int getJobBatchSize()
//	{
//		return jobBatchSize;
//	}
//
//	public void start() {
//		if(!"-1".equals(this.node)){
//			super.start();
//		}
//	  }
//	
//	@Override
//	protected void startExecutingJobs()
//	{
//		logger.info("start job acquisition job");
//		startJobAcquisitionThread();
//	}
//
//	@Override
//	protected void stopExecutingJobs()
//	{
//		logger.info("stop job acquisition job");
//		clusterAcquireJobsRunnable.stop();
//		stopJobAcquisitionThread();
//	}
//	
//	public void executeJobEntities(final List<JobEntity> jobEntities)
//	{
//		try
//		{		
//			pool.execute(new ClusterExecuteJobsRunnable(this, jobEntities));
//		}
//		catch (final RejectedExecutionException e)
//		{
//			//rejectedJobsHandler.jobsRejected(this, jobIds);		
//		}
//	}
//	
//
//	@Override
//	protected void startJobAcquisitionThread()
//	{
//		if (jobAcquisitionThread == null)
//		{
//			clusterAcquireJobsRunnable=new ClusterAcquireJobsRunnable(this);
//			jobAcquisitionThread = new Thread(clusterAcquireJobsRunnable);
//			jobAcquisitionThread.start();
//		}
//	}
//	
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		String[] arr=nodeList.split(",");
//		
//		for(int i=0;i<arr.length; i++){
//			clusterMap.put(arr[i],Integer.toString(i));
//		}
//		
//	    this.node=checkNode();
//	    
//	}
//	
//	private String checkNode()
//	{
//		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//		Set<ObjectName> objs;
//		try {
//			objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
//			        Query.match(Query.attr("protocol"), Query.value("*")));
//		    String hostname = InetAddress.getLocalHost().getHostName();
//		    InetAddress[] addresses = InetAddress.getAllByName(hostname);
//		    for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
//		        ObjectName obj = i.next();
//		        String port = obj.getKeyProperty("port");
//		        for (InetAddress addr : addresses) {
//		            String ip=addr.getHostAddress();
//					
//					if (ip.equalsIgnoreCase("127.0.0.1") || ip.equalsIgnoreCase("localhost"))
//						continue;
//					
//					if (clusterMap.containsKey(ip+":"+port)){
//						return clusterMap.get(ip+":"+port);
//					}
//		        }
//		    }
//		} catch (Exception  e) {
//			logger.error(e.getMessage(),e);
//		}
//		return "-1";
//	}
//	
//}
