package com.lppz.util.rocketmq.disruptor;

import java.util.List;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.util.disruptor.BaseHandler;
import com.lppz.util.rocketmq.listener.BaseLppzMessageListener;

public abstract class BaseRocketHandler<T, R,U> extends
		BaseLppzMessageListener<T, R,U> implements BaseHandler<MessageExt> {
	// private static Logger logger = Logger.getLogger(BaseRocketHandler.class);

	@Override
	public void handle(List<MessageExt> msgs) {
		// System.out.println(Thread.currentThread().getName()+":msgsize:"+msgs.size()+"ai:"+ai.addAndGet(msgs.size()));
		try {
			doHandleMsgList(msgs);
		} finally {
			msgs.clear();
		}
	}
}
