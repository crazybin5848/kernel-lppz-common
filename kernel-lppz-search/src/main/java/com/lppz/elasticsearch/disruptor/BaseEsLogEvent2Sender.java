package com.lppz.elasticsearch.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;

public class BaseEsLogEvent2Sender extends BaseEvent2Sender<String>{
	private BaseEsLogEvent2Sender(String idxName,String type,int size,long timeStep,String idxSurffix,boolean isShard){
		BaseEventFactory<String> factory = new BaseEventFactory<String>(){
			@Override
			public BaseEvent<String> newInstance() {
				return new BaseEvent<String>();
			}
		};
		int bufferSize = super.bufferSize / 2;
		BaseEsLogEventHandler beeh=new BaseEsLogEventHandler(idxName,type,size,idxSurffix);
		if(isShard){
			beeh.setHandler(new BaseEsShardHandler());
		}
		Disruptor<BaseEvent<String>> disruptor = new Disruptor<BaseEvent<String>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new SleepingWaitStrategy());
		disruptor
		.handleEventsWith(new BaseEsLogEventHandler[] {beeh});
		disruptor.start();
		RingBuffer<BaseEvent<String>> ringBuffer = disruptor.getRingBuffer();
		super.producer=new BaseEventProducerWith2Translator<String>(
				ringBuffer);
		super.loopSend(timeStep);
		super.baseList=beeh.getBaseList();
	}
	
	public static BaseEsLogEvent2Sender create(String idxName,String type,boolean isShard){
		return new BaseEsLogEvent2Sender(idxName,type,5000,1000,null,isShard);
	}
	
	public static BaseEsLogEvent2Sender create(String idxName,String type,String idxSurffix,boolean isShard){
		return new BaseEsLogEvent2Sender(idxName,type,5000,1000,idxSurffix,isShard);
	}
	
	public static BaseEsLogEvent2Sender create(String idxName,String type,int size,long timeStep,String idxSurffix,boolean isShard){
		return new BaseEsLogEvent2Sender(idxName,type,size,timeStep,idxSurffix,isShard);
	}
}