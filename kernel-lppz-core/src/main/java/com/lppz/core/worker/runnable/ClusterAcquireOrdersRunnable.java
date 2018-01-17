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
//package com.lppz.core.worker.runnable;
//
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.activiti.engine.ActivitiOptimisticLockingException;
//import org.activiti.engine.impl.interceptor.CommandExecutor;
//import org.activiti.engine.impl.persistence.entity.JobEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.lppz.core.activiti.orderExecutor.ClusterorderExecutor;
//import com.lppz.core.activiti.orderExecutor.cmd.ClusterAcquireJobsCmd;
//import com.lppz.core.activiti.orderExecutor.entity.ClusterAcquiredJobs;
//import com.lppz.core.worker.executor.ClusterOmsOrderExecutor;
//
//
///**
// *
// */
//public class ClusterAcquireOrdersRunnable implements Runnable
//{
//	private static Logger log = LoggerFactory.getLogger(ClusterAcquireOrdersRunnable.class);
//
//	protected final ClusterOmsOrderExecutor orderExecutor;
//
//	protected volatile boolean isInterrupted = false;
//
//	public ClusterAcquireOrdersRunnable(final ClusterOmsOrderExecutor orderExecutor)
//	{
//		this.orderExecutor = orderExecutor;
//	}
//	
//	protected int format(int tempOrderSize, int pageSize) {
//		return (int) Math.ceil(Float.parseFloat(String.valueOf(tempOrderSize)) / pageSize);
//	}
//	
//	@Override
//	public synchronized void run()
//	{
//		log.info("{} starting to acquire orders", orderExecutor.getName());
//
//		final CommandExecutor commandExecutor = orderExecutor.getCommandExecutor();
//
//		while (!isInterrupted)
//		{
//			try
//			{
//				final ClusterAcquiredJobs acquiredJobs = commandExecutor.execute(new ClusterAcquireJobsCmd(orderExecutor));
//				
//				// if all jobs were executed
////				millisToWait = orderExecutor.getWaitTimeInMillis();
//				final int jobsAcquired = acquiredJobs.getJobsList().size();
//				List<JobEntity> list=acquiredJobs.getJobsList();
//				if(list==null||list.isEmpty()){
//					orderExecutor.executingJobCounter=new AtomicInteger(0);
//					continue;
//				}
//				int batchSize=orderExecutor.getJobBatchSize();
//				int concurrentThread=orderExecutor.getConcurrentThread();
//				int loopsize=concurrentThread*batchSize;
//				int loop=format(list.size(), loopsize);
//				for(int i=0;i<loop;)
//				{
//					List<JobEntity> sublist=list.subList((i++)*loopsize,i*loopsize>list.size()?list.size():i*loopsize);
//					int threadNo=sublist.size()<loopsize?format(sublist.size(), batchSize):concurrentThread;
//					for(int j=0;j<threadNo;){
//						orderExecutor.executeJobEntities(sublist.subList((j++)*batchSize,
//								j*batchSize>sublist.size()?sublist.size():j*batchSize));
//					}
//				}
//
//				// check child thread, if child thread not end then parent thread wait, else leave
//				while (true)
//				{
//					Thread.sleep(1000);
//					synchronized (this) {
//						if (orderExecutor.executingJobCounter.get() >= jobsAcquired) {//log
//							orderExecutor.executingJobCounter = new AtomicInteger(
//									0);
//							break;
//							//log
//						}
//					}
//				}
////				if (jobsAcquired==0)
////				{
////					isJobAdded = false;
////					// check if the next timer should fire before the normal sleep time is over
////					final Date duedate = new Date(ClockUtil.getCurrentTime().getTime() + millisToWait);
////					final List<TimerEntity> nextTimers = commandExecutor.execute(new GetUnlockedTimersByDuedateCmd(duedate, new Page(
////							0, 1)));
////
////					if (!nextTimers.isEmpty())
////					{
////						final long millisTillNextTimer = nextTimers.get(0).getDuedate().getTime()
////								- ClockUtil.getCurrentTime().getTime();
////						if (millisTillNextTimer < millisToWait)
////						{
////							millisToWait = millisTillNextTimer;
////						}
////					}
////
////				}
////				else
////				{
////					millisToWait = 0;
////				}
//
//			}
//			catch (final ActivitiOptimisticLockingException optimisticLockingException)
//			{
//				// See http://jira.codehaus.org/browse/ACT-1390
//				if (log.isDebugEnabled())
//				{
//					log.debug(
//							"Optimistic locking exception during job acquisition. If you have multiple job executors running against the same database, "
//									+ "this exception means that this thread tried to acquire a job, which already was acquired by another job executor acquisition thread."
//									+ "This is expected behavior in a clustered environment. "
//									+ "You can ignore this message if you indeed have multiple job executor acquisition threads running against the same database. "
//									+ "Exception message: {}", optimisticLockingException.getMessage());
//				}
//			}
//			catch (final Throwable e)
//			{
//				log.error("exception during job acquisition: {}", e.getMessage(), e);
//				millisToWait *= waitIncreaseFactor;
//				if (millisToWait > maxWait)
//				{
//					millisToWait = maxWait;
//				}
//				else if (millisToWait == 0)
//				{
//					millisToWait = orderExecutor.getWaitTimeInMillis();
//				}
//			}
//
////			if ((millisToWait > 0) && (!isJobAdded))
////			{
////				try
////				{
////					if (log.isDebugEnabled())
////					{
////						log.debug("job acquisition thread sleeping for {} millis", millisToWait);
////					}
////					synchronized (MONITOR)
////					{
////						if (!isInterrupted)
////						{
////							isWaiting.set(true);
////							MONITOR.wait(millisToWait);
////						}
////					}
////
////					if (log.isDebugEnabled())
////					{
////						log.debug("job acquisition thread woke up");
////					}
////				}
////				catch (final InterruptedException e)
////				{
////					if (log.isDebugEnabled())
////					{
////						log.debug("job acquisition wait interrupted");
////					}
////				}
////				finally
////				{
////					isWaiting.set(false);
////				}
////			}
//		}
//
//		log.info("{} stopped job acquisition", orderExecutor.getName());
//	}
//
//	public void stop()
//	{
//		synchronized (MONITOR)
//		{
//			isInterrupted = true;
//			if (isWaiting.compareAndSet(true, false))
//			{
//				MONITOR.notifyAll();
//			}
//		}
//	}
//
//	public void jobWasAdded()
//	{
//		isJobAdded = true;
//		if (isWaiting.compareAndSet(true, false))
//		{
//			// ensures we only notify once
//			// I am OK with the race condition
//			synchronized (MONITOR)
//			{
//				MONITOR.notifyAll();
//			}
//		}
//	}
//
//
//	public long getMillisToWait()
//	{
//		return millisToWait;
//	}
//
//	public void setMillisToWait(final long millisToWait)
//	{
//		this.millisToWait = millisToWait;
//	}
//
//	public float getWaitIncreaseFactor()
//	{
//		return waitIncreaseFactor;
//	}
//
//	public void setWaitIncreaseFactor(final float waitIncreaseFactor)
//	{
//		this.waitIncreaseFactor = waitIncreaseFactor;
//	}
//
//	public long getMaxWait()
//	{
//		return maxWait;
//	}
//
//	public void setMaxWait(final long maxWait)
//	{
//		this.maxWait = maxWait;
//	}
//}
