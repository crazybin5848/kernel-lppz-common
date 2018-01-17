package com.lppz.util.kafka.producer.async;


public interface AsyncKafkaMessageProducer<T> {
	
	public void pushCache(final String topic, final T jmsg);
}
