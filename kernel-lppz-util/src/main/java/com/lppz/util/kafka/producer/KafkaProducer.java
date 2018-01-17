package com.lppz.util.kafka.producer;

public interface KafkaProducer<T> {
	public boolean sendMsg(final T t,String... key) throws Exception;
	public void destroy(long... time) throws Exception;
}
