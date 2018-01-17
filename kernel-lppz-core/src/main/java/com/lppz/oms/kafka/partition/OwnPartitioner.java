package com.lppz.oms.kafka.partition;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class OwnPartitioner implements Partitioner {

	public OwnPartitioner(VerifiableProperties props){
		
	}
	@Override
	public int partition(Object obj, int partNum) {
		int partition = 0;
		System.out.println(obj.toString()+"\t"+partNum);
		if(null == obj){
			return 0;
		}
		if(obj instanceof String){
			String key = (String) obj;
			partition = key.length()%partNum;
		}else{
			partition = obj.toString().length() % partNum;
		}
		return partition;
	}


}
