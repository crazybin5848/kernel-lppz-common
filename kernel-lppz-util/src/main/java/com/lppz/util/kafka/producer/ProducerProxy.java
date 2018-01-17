package com.lppz.util.kafka.producer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.lppz.util.KafkaPropertiesUtils;

public class ProducerProxy<T1, T2> {
	public ProducerProxy(ProducerConfig producerConfig,int size) {
		for(int i=0;i<size;i++){
			Producer<T1, T2> producer=new Producer<T1, T2>(producerConfig);
			prodMap.put(i, producer);
			queue.offer(producer);
		}
	}

	private Map<Integer,Producer<T1, T2>> prodMap=new HashMap<Integer,Producer<T1, T2>>();
	private ConcurrentLinkedQueue<Producer<T1, T2>> queue=new ConcurrentLinkedQueue<Producer<T1, T2>>();
	public void send(List<KeyedMessage<T1, T2>> messages) {
		if (prodMap.isEmpty())
			throw new IllegalStateException("prodMap can not be null");
			int i = java.util.concurrent.ThreadLocalRandom.current().nextInt(
					prodMap.size());
			prodMap.get(i).send(messages);
	}
	public void close() {
		for(Producer<T1, T2> prod:prodMap.values())
			prod.close();
	}
	
	public void send(KeyedMessage<T1, T2> msg) {
		if (prodMap.isEmpty())
			throw new IllegalStateException("prodMap can not be null");
			int i = java.util.concurrent.ThreadLocalRandom.current().nextInt(
					prodMap.size());
			prodMap.get(i).send(msg);
	}
	public Producer<T1, T2> pollProducer() {
		return queue.poll();
	}
}