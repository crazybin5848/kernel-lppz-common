package com.lppz.util.rocketmq.listener;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.util.rocketmq.disruptor.BaseDisruptorListener;
import com.lppz.util.rocketmq.disruptor.BaseRocketHandler;
import com.lppz.util.rocketmq.exception.RocketMqNoSenderException;

public class LppzMessageDisruptorListenerConcurrency<T,R,U> extends BaseDisruptorListener<T,R,U> implements MessageListenerConcurrently{
//	private static final Logger logger = LoggerFactory.getLogger(LppzMessageDisruptorListenerConcurrency.class);
	public LppzMessageDisruptorListenerConcurrency(){
	}
	public LppzMessageDisruptorListenerConcurrency(BaseRocketHandler<T,R,U> handler,int size,long timeStep){
		this.size=size;
		this.timeStep=timeStep;
		super.buildSender(handler);
	}
	
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeConcurrentlyContext context) {
		if (BaseLppzMessageListener.continueConsume) {
			if(sender==null)
				throw new RocketMqNoSenderException("no sender in this listener");
			for(MessageExt msg:msgs)
				sender.sendMsg(msg);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}else{
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
	}
}