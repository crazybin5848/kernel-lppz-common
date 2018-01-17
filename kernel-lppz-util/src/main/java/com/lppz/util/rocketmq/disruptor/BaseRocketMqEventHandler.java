package com.lppz.util.rocketmq.disruptor;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;

public class BaseRocketMqEventHandler<T,R,U> extends BaseEventHandler<MessageExt,BaseEvent<MessageExt>>  {
//	private static Logger logger = Logger.getLogger(BaseRocketMqEventHandler.class);
	protected int size;
	public BaseRocketMqEventHandler(){}
	public BaseRocketMqEventHandler(int size,BaseRocketHandler<T,R,U> handler){
		this.size=size;
		super.handler=handler;
	}
	
	@Override
	public void onEvent(BaseEvent<MessageExt> longEvent, long l, boolean b)
			throws Exception {
		MessageExt value = longEvent.getValue();
		if(value==null){
			super.onEvent(longEvent, l, b);
			return;
		}
		this.baseList.add(value);
		if (!baseList.isEmpty()
				&& baseList.size() >= size) {
			this.handler.handle(baseList);
		}
	}
}