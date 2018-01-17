package com.lppz.util.disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

public class BaseEventProducerWith2Translator<U> { 
    //填充Event
    protected EventTranslatorTwoArg<BaseEvent<U>, U,String> TRANSLATOR = 
            new EventTranslatorTwoArg<BaseEvent<U>, U,String>() { 
                public void translateTo(BaseEvent<U> event, long sequence, U bb,String t) { 
                    event.setValue(bb);
                    event.setParam(t);
                } 
            };
    private final RingBuffer<BaseEvent<U>> ringBuffer;
    
    public BaseEventProducerWith2Translator(RingBuffer<BaseEvent<U>> ringBuffer) { 
        this.ringBuffer = ringBuffer; 
    } 
 
    public void onData(U bb,String t) { 
        ringBuffer.publishEvent(TRANSLATOR, bb,t); 
    } 
} 