package com.lppz.util.kafka.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.KafkaPropertiesUtils;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.consumer.listener.ByteKafkaConsumerListener;
import com.lppz.util.kafka.consumer.listener.KafkaConsumerListener;

public abstract class BaseKafkaConsumer<T> 
{   
	protected KafkaConsumerListener<T> kafkaListener;
	protected ByteKafkaConsumerListener<T> bytekafkaListener;
	protected Class<T> clazz;
	public static ExecutorService pool = Executors.newCachedThreadPool();
	protected List<KafkaStream<byte[], byte[]>> streams;
	private String topic; // 消费的topic，需要注入
	private static final Logger logger = LoggerFactory.getLogger(BaseKafkaConsumer.class);
    private String groupId;
    private String topic_patition_num;
    private String consum_serilize_class=KafkaConstant.STRINGENCODER;
	public String getConsum_serilize_class() {
		return consum_serilize_class;
	}

	public void setConsum_serilize_class(String consum_serilize_class) {
		this.consum_serilize_class = consum_serilize_class;
	}

	public void setTopic_patition_num(String topic_patition_num) {
		this.topic_patition_num = topic_patition_num;
	}

	/**
	 * @return the topic
	 */
	public String getTopic()
	{
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(final String topic)
	{
		this.topic = topic;
	}



	protected Properties init(KafkaConsumerConfig kafkaConsumerConfig,String...home) throws IOException
	{
		final Properties props = new Properties();
		try
		{
			if(kafkaConsumerConfig!=null){
				props.put(KafkaConstant.Consumer.zookeeper_connect,
						kafkaConsumerConfig.getZookeeperConnect());
				props.put(KafkaConstant.Consumer.zookeeper_connection_timeout_ms,
						kafkaConsumerConfig.getZookeeperConnectionTimeoutMs());
				props.put(KafkaConstant.Consumer.zookeeper_session_timeout_ms,
						kafkaConsumerConfig.getZookeeperSessionTimeoutMs());
				props.put(KafkaConstant.Consumer.rebalance_backoff_ms,
						kafkaConsumerConfig.getRebalanceBackoffMs());
				props.put(KafkaConstant.Consumer.auto_commit_interval_ms,
						kafkaConsumerConfig.getAutoCommitIntervalMs());
				props.put(KafkaConstant.Consumer.zookeeper_sync_time_ms,
						kafkaConsumerConfig.getZookeeperSyncTimeMs());
			}else{
			try{
			props.put(KafkaConstant.Consumer.zookeeper_connect,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.zookeeper_connect,home));
			props.put(KafkaConstant.Consumer.zookeeper_connection_timeout_ms,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.zookeeper_connection_timeout_ms,home));
			props.put(KafkaConstant.Consumer.zookeeper_session_timeout_ms,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.zookeeper_session_timeout_ms,home));
			props.put(KafkaConstant.Consumer.rebalance_backoff_ms,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.rebalance_backoff_ms,home));
			props.put(KafkaConstant.Consumer.auto_commit_interval_ms,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.auto_commit_interval_ms,home));
			props.put(KafkaConstant.Consumer.zookeeper_sync_time_ms,
					KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.zookeeper_sync_time_ms,home));
			props.put("fetch.message.max.bytes", "10485760");
			}catch(final IOException e){
				logger.error("kafka未加载!");
				return null;
			}
			}
			if(getGroupId()==null)
			{
				props.setProperty("group.id", Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()),36));
			}else{
				props.setProperty("group.id", getGroupId());
			}
			if(topic_patition_num==null)
			topic_patition_num=kafkaConsumerConfig!=null?kafkaConsumerConfig.getTopicPatitionNum():KafkaPropertiesUtils.getKey(KafkaConstant.Consumer.topic_patition_num,home);
		}
		catch (final Exception e)
		{
			logger.error("start consumer error", e);
		}
		return props;
	}

	protected List<KafkaStream<byte[], byte[]>> initCreateConsumer(
			final Properties props) throws IOException {
		final ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
		final Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic,topic_patition_num==null?new Integer(1):Integer.parseInt(topic_patition_num));
		final Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
		final List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
		return streams;
	}

	public void setKafkaListener(KafkaConsumerListener<T> kafkaListener) {
		this.kafkaListener = kafkaListener;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param set group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void init(String...home) throws Exception {
		initTopic();
		initClazz();
		Properties props=init(initConfig(),home);
		streams=initCreateConsumer(props);
		for (final KafkaStream<byte[], byte[]> stream : streams)
		{
			if(KafkaConstant.BYTEENCODER.equals(consum_serilize_class)){
				pool.execute(new ByteKafkaBaseRunner<T>(bytekafkaListener,stream,clazz));
			}else{
				KafkaBaseRunner<T> kbr=new KafkaBaseRunner<T>(kafkaListener,stream,clazz);
				pool.execute(kbr);
			}
		}
	}

	protected abstract void initClazz();

	protected KafkaConsumerConfig initConfig(){
		return null;
	}
	
	protected abstract void initTopic();

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void setBytekafkaListener(ByteKafkaConsumerListener<T> bytekafkaListener) {
		this.bytekafkaListener = bytekafkaListener;
	}
}