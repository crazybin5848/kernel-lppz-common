package com.lppz.elasticsearch.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.elasticsearch.EsModel;
import com.lppz.util.disruptor.BaseErrorHandler;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;

public class BaseEsLogEventCRUD2Sender extends BaseEvent2Sender<String>{

	private BaseEsLogEventCRUD2Sender(int size,long timeStep,BaseErrorHandler<EsModel> errorHandler){
		BaseEventFactory<String> factory = new BaseEventFactory<String>(){
			@Override
			public BaseEvent<String> newInstance() {
				return new BaseEvent<String>();
			}
		};
		int bufferSize = super.bufferSize / 2;
		BaseCRUDEsLogEventHandler beeh=new BaseCRUDEsLogEventHandler();
		beeh.size=size;
		Disruptor<BaseEvent<String>> disruptor = new Disruptor<BaseEvent<String>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new SleepingWaitStrategy());
		disruptor
		.handleEventsWith(new BaseEsLogEventHandler[] {beeh});
		beeh.setHandler(new BaseEsHandler().buildErrorHandler(errorHandler));
		disruptor.start();
		RingBuffer<BaseEvent<String>> ringBuffer = disruptor.getRingBuffer();
		super.producer=new BaseEventProducerWith2Translator<String>(
				ringBuffer);
		super.loopSend(timeStep);
		super.baseList=beeh.getBaseList();
	}
	
	public static BaseEsLogEventCRUD2Sender create(int size,long timeStep){
		return new BaseEsLogEventCRUD2Sender(size,timeStep,null);
	}
	
	public static BaseEsLogEventCRUD2Sender create(int size,long timeStep,BaseErrorHandler<EsModel> errorHandler){
		return new BaseEsLogEventCRUD2Sender(size,timeStep,errorHandler);
	}
	
}