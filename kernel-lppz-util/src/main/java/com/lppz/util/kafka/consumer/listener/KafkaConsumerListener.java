package com.lppz.util.kafka.consumer.listener;

public interface KafkaConsumerListener<T>
{
	public void onMessage(String msg,Class<T> clazz);
}
