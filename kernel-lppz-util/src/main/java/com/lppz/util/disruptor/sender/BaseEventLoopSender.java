package com.lppz.util.disruptor.sender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.util.DaemonThreadFactory;


public abstract class BaseEventLoopSender<U> {
	private static final Logger logger = LoggerFactory.getLogger(BaseEventLoopSender.class);
	static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3, DaemonThreadFactory.INSTANCE);
	ScheduledFuture<?> sendFuture;
	protected void loopSend(long timeStep){
		long monitorInterval = timeStep;
		sendFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                		sendMsg(null);
                } catch (Throwable t) { // 防御性容错
                    logger.error("Unexpected error occur at send statistic, cause: " + t.getMessage(), t);
                }
            }
        }, monitorInterval, monitorInterval, TimeUnit.MILLISECONDS);
	}
	
	public abstract void sendMsg(U dto,String... params);
	
	protected void destory(){
		sendMsg(null);
		logger.info("successfully sended all msg using send null param");
	}
}