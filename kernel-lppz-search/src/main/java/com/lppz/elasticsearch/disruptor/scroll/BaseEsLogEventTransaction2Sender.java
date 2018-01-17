package com.lppz.elasticsearch.disruptor.scroll;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;

public class BaseEsLogEventTransaction2Sender extends BaseEvent2Sender<String>{

	private BaseEsLogEventTransaction2Sender(int size,long timeStep, PrepareBulk bulk){
		BaseEventFactory<String> factory = new BaseEventFactory<String>(){
			@Override
			public BaseEvent<String> newInstance() {
				return new BaseEvent<String>();
			}
		};
		int bufferSize = (int) (0.5*super.bufferSize);
		BaseEsScrollLogEventHandler beeh=new BaseEsScrollLogEventHandler();
		beeh.size=size;
		Disruptor<BaseEvent<String>> disruptor = new Disruptor<BaseEvent<String>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new BlockingWaitStrategy());
		disruptor.handleEventsWith(new BaseEsScrollLogEventHandler[] {beeh});
		beeh.setHandler(new ScrollEsHander(bulk));
		disruptor.start();
		RingBuffer<BaseEvent<String>> ringBuffer = disruptor.getRingBuffer();
		super.producer=new BaseEventProducerWith2Translator<String>(
				ringBuffer);
		super.loopSend(timeStep);
		super.baseList=beeh.getBaseList();
	}
	
	public static BaseEsLogEventTransaction2Sender create(int size,long timeStep,PrepareBulk bulk){
		return new BaseEsLogEventTransaction2Sender(size,timeStep,bulk);
	}
	
}