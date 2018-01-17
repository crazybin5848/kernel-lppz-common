package com.lppz.mongoapi.disruptor;

import java.util.Map;

import org.bson.Document;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;

public class MongoBatchInsertSender extends BaseEvent2Sender<Map<String,Document>>{
	private MongoBatchInsertSender(MongoBathInsertHandler handler,int size,long timeStep){
		BaseEventFactory<Map<String,Document>> factory = new BaseEventFactory<Map<String,Document>>(){
			@Override
			public BaseEvent<Map<String,Document>> newInstance() {
				return new BaseEvent<Map<String,Document>>();
			}
		};
		int bufferSize = super.bufferSize / 2;
		MongoBatchInserEventHandler beeh=new MongoBatchInserEventHandler(size,handler);
		Disruptor<BaseEvent<Map<String,Document>>> disruptor = new Disruptor<BaseEvent<Map<String,Document>>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new SleepingWaitStrategy());
		disruptor.handleEventsWith(new MongoBatchInserEventHandler[] {beeh});
		disruptor.start();
		RingBuffer<BaseEvent<Map<String,Document>>> ringBuffer = disruptor.getRingBuffer();
		super.producer=new BaseEventProducerWith2Translator<Map<String,Document>>(
				ringBuffer);
		super.loopSend(timeStep);
		super.baseList=beeh.getBaseList();
	}
	
	public static MongoBatchInsertSender create(MongoBathInsertHandler handler,int size,long timeStep){
		return new MongoBatchInsertSender(handler,size,timeStep);
	}
}
