package com.lppz.dubbo.monitor.disruptor;

import com.lmax.disruptor.EventFactory;

public class DubboInvokeEventFactory implements EventFactory<DubboInvokeEvent> { 
    @Override 
    public DubboInvokeEvent newInstance() { 
        return new DubboInvokeEvent(); 
    } 
} 