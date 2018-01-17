package com.lppz.util.disruptor.sender;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.disruptor.BaseEventProducerWith2Translator;


public class BaseEvent2Sender<U> extends BaseEventLoopSender<U>{
	private static final Logger logger = LoggerFactory.getLogger(BaseEvent2Sender.class);
	protected int bufferSize=1024*256;
	protected BaseEventProducerWith2Translator<U> producer;
	@Override
	public void sendMsg(U dto, String... params) {
		producer.onData(dto,params==null||params.length==0?null:params[0]);		
	}
	
	@SuppressWarnings("rawtypes")
	protected List baseList;
	@Override
	public void destory(){
		if(baseList==null)
			super.destory();
		while(!baseList.isEmpty()){
			super.destory();
			try {
				logger.info("fuck: baselist has remain:"+baseList.size()+" for destroy");
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		scheduledExecutorService.shutdown();
	}
}

