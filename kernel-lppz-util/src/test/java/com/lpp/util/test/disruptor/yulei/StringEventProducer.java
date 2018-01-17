package com.lpp.util.test.disruptor.yulei;

import com.lmax.disruptor.RingBuffer;

public class StringEventProducer {

	private final RingBuffer<StringEvent> ringBuffer;

	public StringEventProducer(RingBuffer<StringEvent> ringBuffer){
		this.ringBuffer=ringBuffer;
	}
	
	public void onData(String msg){
		long seq=ringBuffer.next();
		
		try{
			StringEvent event=ringBuffer.get(seq);
			
			event.setMsg(msg);
		}finally{
			ringBuffer.publish(seq);
		}
	}
}
