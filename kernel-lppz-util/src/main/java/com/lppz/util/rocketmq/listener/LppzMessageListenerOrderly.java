package com.lppz.util.rocketmq.listener;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;

public abstract class LppzMessageListenerOrderly<T,R,U> extends BaseLppzMessageListener<T,R,U> implements MessageListenerOrderly{
//	private static final Logger logger = LoggerFactory.getLogger(LppzMessageListenerOrderly.class);
	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeOrderlyContext context) {
		context.setAutoCommit(true);
		if (BaseLppzMessageListener.continueConsume) {
			doHandleMsgList(msgs);
			return ConsumeOrderlyStatus.SUCCESS;
		}
		return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
	}
}