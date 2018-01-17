package com.lppz.util.disruptor;

import com.lmax.disruptor.EventFactory;

public abstract class BaseEventFactory<T> implements EventFactory<BaseEvent<T>> { 
     
    public abstract BaseEvent<T> newInstance();
} 