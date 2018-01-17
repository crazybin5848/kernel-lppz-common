package com.lppz.util.disruptor.sender;

import java.util.HashMap;
import java.util.Map;

public class BaseClusterdDisruptorSender<U> extends BaseEvent2Sender<U> implements IDal2ndSender<U>{
	
	@Override
	public void destory() {
		for(BaseEvent2Sender<U> sender:sendMap.values()){
			sender.destory();
		}
	}

	private Map<Integer, BaseEvent2Sender<U>> sendMap = new HashMap<Integer, BaseEvent2Sender<U>>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BaseClusterdDisruptorSender build(BaseEvent2SenderFactory factory,int... size){
		int tt=Math.min(5, Runtime.getRuntime().availableProcessors()*2);
		return new BaseClusterdDisruptorSender(size==null||size.length==0?tt:size[0],factory);
	}
	
	private BaseClusterdDisruptorSender(int size,
			BaseEvent2SenderFactory<U> factory) {
		for (int i = 0; i < size; i++) {
			sendMap.put(i, factory.build());
		}
	}

	@Override
	public void sendMsg(U dto, String... params) {
		if (sendMap.isEmpty())
			throw new IllegalStateException("sendMap can not be null");
		int i = java.util.concurrent.ThreadLocalRandom.current().nextInt(
				sendMap.size());
		sendMap.get(i).sendMsg(dto, params);
	}
	
	//ordered send msg by the same key
	public void sendMsgOrderly(U dto,Object key,String... params) {
		if (sendMap.isEmpty())
			throw new IllegalStateException("sendMap can not be null");
		if(key==null)
			sendMsg(dto, params);
		int i=Math.abs(key.hashCode())%sendMap.size();
		sendMap.get(i).sendMsg(dto, params);
	}
}