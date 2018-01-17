package com.lpp.util.test.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain { 
    public static void main(String[] args) throws InterruptedException { 
    	ExecutorService executor = Executors.newFixedThreadPool(100);
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024*128;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize,DaemonThreadFactory.INSTANCE,ProducerType.MULTI,new YieldingWaitStrategy());
        // Connect the handler 
        disruptor.handleEventsWith(new LongEventHandler[]{new LongEventHandler()});
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer(); 
       final LongEventProducerWithTranslator producer=new LongEventProducerWithTranslator(ringBuffer);
        AtomicLong ai=new AtomicLong(0);
        for(;;){
        for (long l = 0; l<10000;l++) {
        	executor.execute(new DisruptorRunnable(ai,producer));
//        	System.out.println(l);
        } 
        Thread.sleep(500);
        }
//        while(true){
////        	System.out.println(ai.get());
//        	Thread.sleep(300);
//        	if(ai.get()==500){
//        		disruptor.shutdown();
//        		executor.shutdown();
//        		return;
//        	}
//        }
    } 
    
    private static class DisruptorRunnable implements Runnable{
    	private AtomicLong i;
    	LongEventProducerWithTranslator producer;
    	final ByteBuffer bb = ByteBuffer.allocate(8);
    	public DisruptorRunnable(AtomicLong i,LongEventProducerWithTranslator producer){
    		this.i=i;
    		this.producer=producer;
    	}
		@Override
		public void run() {
			long start=System.currentTimeMillis();
			Long x=i.getAndAdd(1);
			 bb.putLong(0,x); 
	         producer.onData(bb); 
//	         System.out.println(x);
	         long end=System.currentTimeMillis()-start;
	         if(end>10)
	         System.out.println(Thread.currentThread().getId()+"cost:"+end+"x:"+x);
		}
    }
} 