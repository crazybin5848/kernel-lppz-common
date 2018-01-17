package com.lppz.util.kafka.partition;

import java.io.UnsupportedEncodingException;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class StringHashCodePartitioner implements Partitioner {

	public StringHashCodePartitioner(VerifiableProperties props){
		
	}
	@Override
	public int partition(Object obj, int partNum) {
		int partition = 0;
		if(null == obj){
			return 0;
		}
		if(obj instanceof String){
			String key = (String) obj;
			partition = Math.abs(key.hashCode())%partNum;
		}
		else if(obj instanceof byte[]){
			try {
				String key = new String((((byte[]) obj)),"UTF-8");
				partition = Math.abs(key.hashCode())%partNum;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return 0;
			}
		}
		else{
			partition = Math.abs(obj.toString().hashCode())% partNum;
		}
		return partition;
	}
}