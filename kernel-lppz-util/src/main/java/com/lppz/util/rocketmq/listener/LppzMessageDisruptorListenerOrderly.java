package com.lppz.util.rocketmq.listener;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.util.rocketmq.disruptor.BaseDisruptorListener;
import com.lppz.util.rocketmq.disruptor.BaseRocketHandler;
import com.lppz.util.rocketmq.exception.RocketMqNoSenderException;

public class LppzMessageDisruptorListenerOrderly<T,R,U> extends BaseDisruptorListener<T,R,U> implements MessageListenerOrderly{
//	private static final Logger logger = LoggerFactory.getLogger(LppzMessageDisruptorListenerOrderly.class);
	public LppzMessageDisruptorListenerOrderly(){
	}
	public LppzMessageDisruptorListenerOrderly(BaseRocketHandler<T,R,U> handler,int size,long timeStep){
		this.size=size;
		this.timeStep=timeStep;
		super.buildSender(handler);
	}
	
	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeOrderlyContext context) {
		if (BaseLppzMessageListener.continueConsume) {
			if(sender==null)
				throw new RocketMqNoSenderException("no sender in this listener");
			context.setAutoCommit(true);
			for(MessageExt msg:msgs)
				sender.sendMsg(msg);
			return ConsumeOrderlyStatus.SUCCESS;
		}
		return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
	}
}