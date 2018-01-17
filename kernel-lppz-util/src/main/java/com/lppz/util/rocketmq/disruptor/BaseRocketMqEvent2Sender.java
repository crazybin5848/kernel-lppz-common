package com.lppz.util.rocketmq.disruptor;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
public class BaseRocketMqEvent2Sender<T,R,U> extends BaseEvent2Sender<MessageExt>{
	@SuppressWarnings("unchecked")
	private BaseRocketMqEvent2Sender(BaseRocketHandler<T,R,U> handler,int size,long timeStep){
		BaseEventFactory<MessageExt> factory = new BaseEventFactory<MessageExt>(){
			@Override
			public BaseEvent<MessageExt> newInstance() {
				return new BaseEvent<MessageExt>();
			}
		};
		int bufferSize = (int) (super.bufferSize * 0.5);
		BaseRocketMqEventHandler<T,R,U> beeh=new BaseRocketMqEventHandler<T,R,U>(size,handler);
		Disruptor<BaseEvent<MessageExt>> disruptor = new Disruptor<BaseEvent<MessageExt>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new SleepingWaitStrategy());
		disruptor
		.handleEventsWith(new BaseRocketMqEventHandler[] {beeh});
		disruptor.start();
		RingBuffer<BaseEvent<MessageExt>> ringBuffer = disruptor.getRingBuffer();
		super.producer=new BaseEventProducerWith2Translator<MessageExt>(
				ringBuffer);
		super.loopSend(timeStep);
		super.baseList=beeh.getBaseList();
	}
	
	public static <T,R,U> BaseRocketMqEvent2Sender<T,R,U> create(BaseRocketHandler<T,R,U> handler,int size,long timeStep){
		return new BaseRocketMqEvent2Sender<T,R,U>(handler,size,timeStep);
	}
}