package com.lppz.dubbo.monitor.disruptor;

import com.alibaba.dubbo.common.URL;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class DubboInvokeEventProducerWithTranslator { 
    //一个translator可以看做一个事件初始化器，publicEvent方法会调用它
    //填充Event
    private static final EventTranslatorOneArg<DubboInvokeEvent, URL> TRANSLATOR = 
            new EventTranslatorOneArg<DubboInvokeEvent, URL>() { 
                public void translateTo(DubboInvokeEvent event, long sequence, URL bb) { 
                    event.setStatistics(bb);
                } 
            };
    private final RingBuffer<DubboInvokeEvent> ringBuffer;
    public DubboInvokeEventProducerWithTranslator(RingBuffer<DubboInvokeEvent> ringBuffer) { 
        this.ringBuffer = ringBuffer; 
    } 
 
    public void onData(URL bb) { 
        ringBuffer.publishEvent(TRANSLATOR, bb); 
    } 
} 