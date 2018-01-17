package com.lpp.util.test.disruptor;

import java.util.ArrayList;
import java.util.List;

import com.lmax.disruptor.EventHandler;

/** 
 */public class LongEventHandler implements EventHandler<LongEvent> {
	 List<Long> ll=new ArrayList<Long>();
	 @Override 
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception { 
        ll.add(longEvent.getValue());
		 if(ll.size()>=30000){
			System.out.println("size:"+l+"b:"+b);
			ll.clear();
			Thread.sleep(3000);
        }
//        System.out.println(Thread.currentThread().getId());
    } 
} 