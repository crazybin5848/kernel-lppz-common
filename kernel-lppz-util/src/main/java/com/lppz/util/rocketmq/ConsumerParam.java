package com.lppz.util.rocketmq;

import java.io.Serializable;

public class ConsumerParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 336813283670088281L;
	private String namesrvAddr;
	private String consumerGroup;
	private String topic;
	private String tag;
	private boolean consumeBatch;
	private Integer consumeBatchMaxNum=128;
	private int consumeMinThread=1;
	private int consumeMaxThread=10;

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isConsumeBatch() {
		return consumeBatch;
	}

	public void setConsumeBatch(boolean consumeBatch) {
		this.consumeBatch = consumeBatch;
	}

	public int getConsumeMinThread() {
		return consumeMinThread;
	}

	public void setConsumeMinThread(int consumeMinThread) {
		this.consumeMinThread = consumeMinThread;
	}

	public int getConsumeMaxThread() {
		return consumeMaxThread;
	}

	public void setConsumeMaxThread(int consumeMaxThread) {
		this.consumeMaxThread = consumeMaxThread;
	}

	public Integer getConsumeBatchMaxNum() {
		return consumeBatchMaxNum;
	}

	public void setConsumeBatchMaxNum(Integer consumeBatchMaxNum) {
		this.consumeBatchMaxNum = consumeBatchMaxNum;
	}
}