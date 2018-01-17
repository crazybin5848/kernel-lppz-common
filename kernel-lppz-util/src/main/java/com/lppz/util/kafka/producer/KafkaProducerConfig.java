package com.lppz.util.kafka.producer;

import java.util.Properties;

import com.lppz.util.kafka.constant.KafkaConstant;

public class KafkaProducerConfig {
	private String batch_num_messages;
	private String queue_buffering_max_ms;
	private String metadata_broker_list;
	private String request_required_acks;
	private int kafkaClusterSize = 1;

	public String getBatch_num_messages() {
		return batch_num_messages;
	}

	public void setBatch_num_messages(String batch_num_messages) {
		this.batch_num_messages = batch_num_messages;
	}

	public String getQueue_buffering_max_ms() {
		return queue_buffering_max_ms;
	}

	public void setQueue_buffering_max_ms(String queue_buffering_max_ms) {
		this.queue_buffering_max_ms = queue_buffering_max_ms;
	}

	public String getMetadata_broker_list() {
		return metadata_broker_list;
	}

	public void setMetadata_broker_list(String metadata_broker_list) {
		this.metadata_broker_list = metadata_broker_list;
	}

	public String getRequest_required_acks() {
		return request_required_acks;
	}

	public void setRequest_required_acks(String request_required_acks) {
		this.request_required_acks = request_required_acks;
	}

	public KafkaProducerConfig(String batch_num_messages,
			String queue_buffering_max_ms, String metadata_broker_list,
			String request_required_acks) {
		this.batch_num_messages = batch_num_messages;
		this.queue_buffering_max_ms = queue_buffering_max_ms;
		this.metadata_broker_list = metadata_broker_list;
		this.request_required_acks = request_required_acks;
	}

	public KafkaProducerConfig() {
	}

	public KafkaProducerConfig(Properties props) {
		this.batch_num_messages = props
				.getProperty(KafkaConstant.Producer.batch_num_messages);
		this.queue_buffering_max_ms = props
				.getProperty(KafkaConstant.Producer.queue_buffering_max_ms);
		this.metadata_broker_list = props
				.getProperty(KafkaConstant.Producer.metadata_broker_list);
		this.request_required_acks = props
				.getProperty(KafkaConstant.Producer.request_required_acks);
	}

	public int getKafkaClusterSize() {
		return kafkaClusterSize;
	}

	public void setKafkaClusterSize(int kafkaClusterSize) {
		if (kafkaClusterSize > Runtime.getRuntime().availableProcessors())
			this.kafkaClusterSize = Runtime.getRuntime().availableProcessors();
		else
			this.kafkaClusterSize = kafkaClusterSize;
	}
}