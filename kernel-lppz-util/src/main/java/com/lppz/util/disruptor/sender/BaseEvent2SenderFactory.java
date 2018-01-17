package com.lppz.util.disruptor.sender;

public interface BaseEvent2SenderFactory<U> {
	public BaseEvent2Sender<U> build();
}
