package com.lpp.util.test.disruptor;
public class LongEvent { 
    private long value;
    public long getValue() { 
        return value; 
    } 
 
    public void setValue(long value) { 
        this.value = value; 
    } 
    
    public static void main(String[] args) {
    	int bufferSize = 1024*100;
    	System.out.println(Integer.bitCount(bufferSize));
	}
} 