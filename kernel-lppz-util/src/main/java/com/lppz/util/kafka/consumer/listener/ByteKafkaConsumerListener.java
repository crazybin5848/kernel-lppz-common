package com.lppz.util.kafka.consumer.listener;

public interface ByteKafkaConsumerListener<T> 
{
	public void onMessage(byte[] msg,Class<T> clazz);
}
