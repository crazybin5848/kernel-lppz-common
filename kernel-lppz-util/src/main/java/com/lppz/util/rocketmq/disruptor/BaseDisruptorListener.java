package com.lppz.util.rocketmq.disruptor;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.util.disruptor.sender.BaseClusterdDisruptorSender;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;

public class BaseDisruptorListener<T, R,U> {
	protected int size;
	protected long timeStep;
	private int threadNum=10;
	protected BaseClusterdDisruptorSender<MessageExt> sender;

	@SuppressWarnings("unchecked")
	public void buildSender(final BaseRocketHandler<T, R,U> handler) {
		sender = BaseClusterdDisruptorSender.build(new BaseEvent2SenderFactory<MessageExt>() {

			@Override
			public BaseEvent2Sender<MessageExt> build() {
				return (BaseEvent2Sender<MessageExt>) BaseRocketMqEvent2Sender.create(handler, size, timeStep);
			}
		},threadNum);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(long timeStep) {
		this.timeStep = timeStep;
	}

	public BaseClusterdDisruptorSender<MessageExt> getSender() {
		return sender;
	}

	public void setSender(BaseClusterdDisruptorSender<MessageExt> sender) {
		this.sender = sender;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
}
