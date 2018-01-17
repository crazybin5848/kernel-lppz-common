package com.lppz.util.kafka;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class KafkaTopicKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4707277079166980933L;
	private String topic;
	private String key;

	public KafkaTopicKey(){}
	public KafkaTopicKey(String topic,String key){
		this.topic=topic;
		this.key=key;
	}
	public String buildJsonString(){
		return JSON.toJSONString(this);
	}
	
	public static KafkaTopicKey  deBuildJson(String jsonString){
		return JSON.parseObject(jsonString, KafkaTopicKey.class);
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getKey() {
		return key;
	}
	
	public byte[] buildKeyB() {
		if(key==null)
			return null;
		return key.getBytes();
	}

	public void setKey(String key) {
		this.key = key;
	}
}
