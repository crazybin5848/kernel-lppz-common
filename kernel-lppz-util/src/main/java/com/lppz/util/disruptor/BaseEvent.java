package com.lppz.util.disruptor;

public class BaseEvent<T> { 
    protected T value;
    protected String param;
    public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public T getValue() { 
        return value; 
    } 
 
    public void setValue(T value) { 
        this.value = value; 
    } 
} 