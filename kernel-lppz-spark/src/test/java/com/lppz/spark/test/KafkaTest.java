package com.lppz.spark.test;

import java.util.UUID;

import org.junit.Test;

import com.lppz.spark.scala.streaming.DapLog;

public class KafkaTest {

	@Test
	public void test(){
//		String path = "/home/licheng/workspace/kernel-lppz-common/kernel-lppz-spark/src/test/resources/kafka.properties";
		String path = "/Users/zoubin/Documents/scalawork/kernel-lppz-common/kernel-lppz-spark/src/test/resources/kafka.properties";
		try {
//			SparkExportProducer producer = SparkExportKafkaProducerConfiguration.createProducer(path);
//			producer.sendMsg("{test:true}");
			SparkStreamingProducer producer=SparkStreamingKafkaProducerConfiguration.createProducer(path);
			for(int i=0;i<100;i++){
				DapLog log=new DapLog(""+i,"192.168.2.1",UUID.randomUUID().toString());
				producer.sendMsg(log);
			}
			for(int i=0;i<77;i++){
				DapLog log=new DapLog(""+i,"192.168.2.2",UUID.randomUUID().toString());
				producer.sendMsg(log);
			}
			for(int i=1;i<88;i++){
				DapLog log=new DapLog(""+i,"192.168.2.3",UUID.randomUUID().toString());
				producer.sendMsg(log);
			}
			while(true){
				Thread.sleep(5000);
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}