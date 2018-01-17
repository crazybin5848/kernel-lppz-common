package com.lppz.util.kafka.consumer;

public class KafkaConsumerConfig {
	private String batchNumMessages;
	private String producerType;
	private String topicPatitionNum;
	private String queueBufferingMaxMs;
	private String zookeeperSyncTimeMs;
	private String autoCommitIntervalMs;
	private String rebalanceBackoffMs;
	private String zookeeperSessionTimeoutMs;
	private String zookeeperConnectionTimeoutMs;
	private String zookeeperConnect;
	public String getBatchNumMessages() {
		return batchNumMessages;
	}
	public void setBatchNumMessages(String batchNumMessages) {
		this.batchNumMessages = batchNumMessages;
	}
	public String getProducerType() {
		return producerType;
	}
	public void setProducerType(String producerType) {
		this.producerType = producerType;
	}
	public String getQueueBufferingMaxMs() {
		return queueBufferingMaxMs;
	}
	public void setQueueBufferingMaxMs(String queueBufferingMaxMs) {
		this.queueBufferingMaxMs = queueBufferingMaxMs;
	}
	public String getZookeeperSyncTimeMs() {
		return zookeeperSyncTimeMs;
	}
	public void setZookeeperSyncTimeMs(String zookeeperSyncTimeMs) {
		this.zookeeperSyncTimeMs = zookeeperSyncTimeMs;
	}
	public String getAutoCommitIntervalMs() {
		return autoCommitIntervalMs;
	}
	public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
		this.autoCommitIntervalMs = autoCommitIntervalMs;
	}
	public String getRebalanceBackoffMs() {
		return rebalanceBackoffMs;
	}
	public void setRebalanceBackoffMs(String rebalanceBackoffMs) {
		this.rebalanceBackoffMs = rebalanceBackoffMs;
	}
	public String getZookeeperSessionTimeoutMs() {
		return zookeeperSessionTimeoutMs;
	}
	public void setZookeeperSessionTimeoutMs(String zookeeperSessionTimeoutMs) {
		this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
	}
	public String getZookeeperConnectionTimeoutMs() {
		return zookeeperConnectionTimeoutMs;
	}
	public void setZookeeperConnectionTimeoutMs(String zookeeperConnectionTimeoutMs) {
		this.zookeeperConnectionTimeoutMs = zookeeperConnectionTimeoutMs;
	}
	public String getZookeeperConnect() {
		return zookeeperConnect;
	}
	public void setZookeeperConnect(String zookeeperConnect) {
		this.zookeeperConnect = zookeeperConnect;
	}
	public String getTopicPatitionNum() {
		return topicPatitionNum;
	}
	public void setTopicPatitionNum(String topicPatitionNum) {
		this.topicPatitionNum = topicPatitionNum;
	}
	
	public KafkaConsumerConfig(String batchNumMessages, String producerType, String topicPatitionNum, String queueBufferingMaxMs, String zookeeperSyncTimeMs, String autoCommitIntervalMs,
			String rebalanceBackoffMs, String zookeeperSessionTimeoutMs, String zookeeperConnectionTimeoutMs, String zookeeperConnect) {
		super();
		this.batchNumMessages = batchNumMessages;
		this.producerType = producerType;
		this.topicPatitionNum = topicPatitionNum;
		this.queueBufferingMaxMs = queueBufferingMaxMs;
		this.zookeeperSyncTimeMs = zookeeperSyncTimeMs;
		this.autoCommitIntervalMs = autoCommitIntervalMs;
		this.rebalanceBackoffMs = rebalanceBackoffMs;
		this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
		this.zookeeperConnectionTimeoutMs = zookeeperConnectionTimeoutMs;
		this.zookeeperConnect = zookeeperConnect;
	}
	
	public KafkaConsumerConfig(){}
}
