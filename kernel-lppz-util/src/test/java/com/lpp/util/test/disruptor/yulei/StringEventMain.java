package com.lpp.util.test.disruptor.yulei;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class StringEventMain {

	public static void main(String[] args) {
		Executor executor = Executors.newCachedThreadPool();
		
		StringEventFactory factory = new StringEventFactory();
		
		int bufferSize = 1024;
		
		Disruptor<StringEvent> disruptor=new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());
		
		disruptor.handleEventsWith(new StringEventHandler[]{new StringEventHandler()});
		
		disruptor.start();
		
		RingBuffer<StringEvent> ringBuffer = disruptor.getRingBuffer();
		
		StringEventProducer producer = new StringEventProducer(ringBuffer); 
		
		StringEventProducer producer2 = new StringEventProducer(ringBuffer); 
		 
        String bb = "fuck";
        for (; true; ) { 
        	for(int i=0;i<1000;i++){
        		executor.execute(new FuckThread(bb,i%2==0?producer:producer2));
        	}
        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
	}
	
	private static class FuckThread implements Runnable{
		
		private String msg;
		
		private StringEventProducer producer;
		
		public FuckThread(String msg,StringEventProducer producer){
			this.msg=msg;
			this.producer=producer;
		}

		@Override
		public void run() {
			producer.onData(msg);
		}
		
	}

}
