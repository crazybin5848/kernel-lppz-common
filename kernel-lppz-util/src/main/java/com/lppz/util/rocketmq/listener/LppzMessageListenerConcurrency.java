package com.lppz.util.rocketmq.listener;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

public abstract class LppzMessageListenerConcurrency<T,R,U> extends BaseLppzMessageListener<T,R,U> implements MessageListenerConcurrently{
//	private static final Logger logger = LoggerFactory.getLogger(LppzMessageListenerConcurrency.class);
//	AtomicInteger ai=new AtomicInteger(0);
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeConcurrentlyContext context) {
//		System.out.println(Thread.currentThread().getName()+":msgsize:"+msgs.size()+"ai:"+ai.addAndGet(msgs.size()));
		if (BaseLppzMessageListener.continueConsume) {
			doHandleMsgList(msgs);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.RECONSUME_LATER;
	}
}