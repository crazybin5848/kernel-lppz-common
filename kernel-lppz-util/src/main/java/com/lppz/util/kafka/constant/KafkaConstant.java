package com.lppz.util.kafka.constant;

/**
 *
 */
public class KafkaConstant
{
	public static final String STRINGENCODER="kafka.serializer.StringEncoder";
	public static final String BYTEENCODER="kafka.serializer.DefaultEncoder";
	public static final String STRING="String";
	public static final String BYTE="Byte";
	public static final String SYNC="sync";
	public static final String ASYNC="async";
	public static class Producer
	{
		// 指定kafka节点列表
		public static final String metadata_broker_list = "metadata.broker.list";
		// producer接收消息ack的时机
		public static final String serializer_class = "serializer.class";
		public static final String partitioner_class = "partitioner.class";
		// 同步还是异步发送消息，默认“sync”表同步，"async"表异步。
		public static final String request_required_acks = "request.required.acks";
		public static final String producer_type = "producer.type";
		public static final String batch_num_messages = "batch.num.messages";
		public static final String queue_buffering_max_ms = "queue.buffering.max.ms";
		public static final String queue_enqueue_timeout_ms = "queue.enqueue.timeout.ms";
	}

	public static class Consumer
	{
		public static final String zookeeper_connect = "zookeeper.connect";
		public static final String zookeeper_session_timeout_ms = "zookeeper.session.timeout.ms";
		public static final String zookeeper_connection_timeout_ms = "zookeeper.connection.timeout.ms";
		public static final String rebalance_backoff_ms = "rebalance.backoff.ms";
		public static final String zookeeper_sync_time_ms = "zookeeper.sync.time.ms";
		public static final String auto_commit_interval_ms = "auto.commit.interval.ms";
		public static final String topic_patition_num = "topic.patition.num";
//		public static final String consume_serializer_class = "consume.serializer.class";
	}
}
