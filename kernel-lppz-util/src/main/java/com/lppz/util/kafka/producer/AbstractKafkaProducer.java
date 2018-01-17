package com.lppz.util.kafka.producer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kafka.javaapi.producer.Producer;

import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.async.AsyncKafkaMessageProducer;
@SuppressWarnings("rawtypes")
public class AbstractKafkaProducer {
	protected static ProducerProxy<String, String> producer;
	protected static ProducerProxy<byte[], byte[]> byteProducer;
	protected static Map<String,AsyncKafkaMessageProducer> kafkaMessageCacheMap = new HashMap<String,AsyncKafkaMessageProducer>(2);
	static Map<String,AbstractKafkaProducer> mapProducer=new HashMap<String,AbstractKafkaProducer>(2);
	private static AbstractKafkaProducer abstractKafkaProducer=new AbstractKafkaProducer();
	public static AbstractKafkaProducer getInstance(){
		return abstractKafkaProducer;
	}
	protected AbstractKafkaProducer(){
		if(!mapProducer.isEmpty())
			return;
		mapProducer.put(KafkaConstant.STRING, StringKafkaProducer.getInstance());
		mapProducer.put(KafkaConstant.BYTE, ByteKafkaProducer.getInstance());
	}
	public void setProducer(ProducerProxy<String, String> producer) {
		AbstractKafkaProducer.producer = producer;
	}
	public void setByteProducer(ProducerProxy<byte[], byte[]> byteProducer) {
		AbstractKafkaProducer.byteProducer = byteProducer;
	}
	public void sendMsg(String prodtype,String serializerType,Object msg,String topic,String... key) throws IOException{
		mapProducer.get(serializerType).sendMsg(prodtype,msg,topic,key);
	}
	
	protected void sendMsg(String prodtype,Object msg,String topic,String... key) throws IOException{
	}
	public void setKafkaMessageCacheMap(
			Map<String, AsyncKafkaMessageProducer> kafkaMessageCacheMap) {
		AbstractKafkaProducer.kafkaMessageCacheMap = kafkaMessageCacheMap;
	}
	
	public void destroy(String type,String serializerType){
		if (KafkaConstant.ASYNC.equals(type)){
			if(mapProducer!=null&&mapProducer.get(serializerType)!=null)
			mapProducer.get(serializerType).destroy(type, serializerType);
		}
	}
	
	public String buildKey(String...key){
		if(key!=null&&key.length>0)
			return key[0];
		return null;
	}
	
	public byte[] buildByteKey(String...key){
		if(key!=null&&key.length>0)
			return key[0].getBytes();
		return null;
	}
}
