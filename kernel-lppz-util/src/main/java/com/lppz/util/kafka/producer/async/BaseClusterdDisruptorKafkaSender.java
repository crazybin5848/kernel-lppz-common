package com.lppz.util.kafka.producer.async;

import java.util.HashMap;
import java.util.Map;

import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;

public class BaseClusterdDisruptorKafkaSender<U> implements AsyncKafkaMessageProducer<U> {
	
	@SuppressWarnings("rawtypes")
	public void destory() {
		for(AsyncKafkaMessageProducer<U> tmpSender:sendMap.values()){
			BaseEvent2Sender sender=(BaseEvent2Sender)tmpSender;
			sender.destory();
		}
	}

	private Map<Integer, AsyncKafkaMessageProducer<U>> sendMap = new HashMap<Integer, AsyncKafkaMessageProducer<U>>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BaseClusterdDisruptorKafkaSender build(BaseEvent2SenderFactory factory,int... size){
		int tt=Math.min(10, Runtime.getRuntime().availableProcessors()*2);
		return new BaseClusterdDisruptorKafkaSender(size==null||size.length==0?tt:size[0],factory);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private BaseClusterdDisruptorKafkaSender(int size,
			BaseEvent2SenderFactory<U> factory) {
		for (int i = 0; i < size; i++) {
			sendMap.put(i, (AsyncKafkaMessageProducer)factory.build());
		}
	}

	@Override
	public void pushCache(String topic, U jmsg) {
		if (sendMap.isEmpty())
		throw new IllegalStateException("sendMap can not be null");
		int i = java.util.concurrent.ThreadLocalRandom.current().nextInt(
			sendMap.size());
		sendMap.get(i).pushCache(topic, jmsg);
	}
	
	public void pushCacheOrderly(String key,String topic, U jmsg) {
		if (sendMap.isEmpty())
			throw new IllegalStateException("sendMap can not be null");
		if(key==null)
			pushCache(topic, jmsg);
		int i=Math.abs(key.hashCode())%sendMap.size();
		sendMap.get(i).pushCache(topic, jmsg);
	}
}