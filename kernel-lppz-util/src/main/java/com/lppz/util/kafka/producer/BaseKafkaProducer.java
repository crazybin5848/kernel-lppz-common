package com.lppz.util.kafka.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lppz.util.KafkaPropertiesUtils;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.partition.StringHashCodePartitioner;
import com.lppz.util.kafka.producer.async.AsyncKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;
import com.lppz.util.kafka.producer.async.bytes.BytesClusteredQueueKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.string.StringClusteredQueueKafkaMessageProducer;

@SuppressWarnings("rawtypes")
public abstract class BaseKafkaProducer<T> implements KafkaProducer<T> {
	public static ProducerProxy<String, String> getProducer() {
		return producer;
	}

	public static ProducerProxy<byte[], byte[]> getByteProducer() {
		return byteProducer;
	}

	private static final Logger logger = Logger
			.getLogger(BaseKafkaProducer.class);
	private String topic;
	protected String producer_type = KafkaConstant.SYNC;
	private String produce_serilize_class = KafkaConstant.STRINGENCODER;

	private static Map<String,AsyncKafkaMessageProducer> kafkaMessageCacheMap = new HashMap<String, AsyncKafkaMessageProducer>(
			2);

	public String getProducer_type() {
		return producer_type;
	}

	public void setProducer_type(String producer_type) {
		this.producer_type = producer_type;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	protected static ProducerProxy<String, String> producer;
	protected static ProducerProxy<byte[], byte[]> byteProducer;
	public static KafkaProducerConfig kafkaProducerConfig = null;
	private static ProducerConfig producerConfig = null;
	private static Properties props = null;

	protected Properties initProducer(KafkaProducerConfig kafkaProducerConfig,
			String... home) {
		final Properties props = new Properties();
		try {
			if (kafkaProducerConfig != null) {
				props.put(KafkaConstant.Producer.metadata_broker_list,
						kafkaProducerConfig.getMetadata_broker_list());
				props.put(KafkaConstant.Producer.request_required_acks,
						kafkaProducerConfig.getRequest_required_acks());
				props.put(KafkaConstant.Producer.queue_enqueue_timeout_ms, "-1");
			} else {
				try {
					props.put(KafkaConstant.Producer.metadata_broker_list,
							KafkaPropertiesUtils
									.getKey(KafkaConstant.Producer.metadata_broker_list,
											home));
					props.put(
							KafkaConstant.Producer.request_required_acks,
							KafkaPropertiesUtils
									.getKey(KafkaConstant.Producer.request_required_acks,
											home));
					props.put(KafkaConstant.Producer.queue_enqueue_timeout_ms,
							"-1");
				} catch (final IOException e) {
					logger.error("kafka未加载!");
					return props;
				}
			}
			props.put(KafkaConstant.Producer.producer_type, "sync");
		} catch (final Exception e) {
			throw e;
		} catch (final Error e) {
			throw e;
		}
		return props;
	}

	public String getProduce_serilize_class() {
		return produce_serilize_class;
	}

	public void setProduce_serilize_class(String produce_serilize_class) {
		this.produce_serilize_class = produce_serilize_class;
	}

	/**
	 * 发送管理事件
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean sendMsg(final T t,String... key) throws Exception {
		if (t == null) {
			return false;
		}
		try {
			resetTopic();
			String type = producer_type;
			Object o = generateMsg(t);
			AbstractKafkaProducer
					.getInstance()
					.sendMsg(
							type,
							KafkaConstant.BYTEENCODER
									.equals(produce_serilize_class) ? KafkaConstant.BYTE
									: KafkaConstant.STRING, o, topic,key);
			return true;
		} catch (final Exception e) {
			logger.error("send msg to jump mq exception:", e);
			throw e;
		} catch (final Error e) {
			logger.error("send msg to jump mq error:", e);
			throw e;
		}
	}

	
	protected abstract void resetTopic();

	protected Object generateMsg(T t) {
		return t;
	}

	public void init() throws Exception {
		if (producer == null && byteProducer == null) {
			props = initProducer(kafkaProducerConfig);
		}
		props.put(KafkaConstant.Producer.serializer_class,
				produce_serilize_class);
		props.put(KafkaConstant.Producer.partitioner_class, StringHashCodePartitioner.class.getName());
		if (KafkaConstant.STRINGENCODER.equals(this.produce_serilize_class)) {
			if (producer == null) {
				producerConfig = new ProducerConfig(props);
				producer = new ProducerProxy<String, String>(producerConfig,kafkaProducerConfig==null?1:kafkaProducerConfig.getKafkaClusterSize());
				AbstractKafkaProducer.getInstance().setProducer(producer);
			}
		} else if (KafkaConstant.BYTEENCODER
				.equals(this.produce_serilize_class)) {
			if (byteProducer == null) {
				producerConfig = new ProducerConfig(props);
				byteProducer = new ProducerProxy<byte[], byte[]>(producerConfig,kafkaProducerConfig==null?1:kafkaProducerConfig.getKafkaClusterSize());
				AbstractKafkaProducer.getInstance().setByteProducer(
						byteProducer);
			}
		}
		if (KafkaConstant.ASYNC.equals(producer_type)) {
			resetTopic();
			if (kafkaMessageCacheMap.isEmpty()) {
				kafkaMessageCacheMap.put(KafkaConstant.STRINGENCODER,
						StringClusteredQueueKafkaMessageProducer.getInstance().buildSender(kafkaProducerConfig==null?1:kafkaProducerConfig.getKafkaClusterSize()));
				kafkaMessageCacheMap.put(KafkaConstant.BYTEENCODER,
						BytesClusteredQueueKafkaMessageProducer.getInstance().buildSender(kafkaProducerConfig==null?1:kafkaProducerConfig.getKafkaClusterSize()));
				AbstractKafkaProducer.getInstance().setKafkaMessageCacheMap(
						kafkaMessageCacheMap);
			}
		}
	}

	public KafkaProducerConfig initConfig(String kafkaBrokerPath) {
		Properties props = null;
		if (StringUtils.isNotBlank(kafkaBrokerPath)) {
			Resource resource = new FileSystemResource(kafkaBrokerPath);
			try {
				props = PropertiesLoaderUtils.loadProperties(resource);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			InputStream input = getClass().getClassLoader()
					.getResourceAsStream("kafka.properties");
			props = new Properties();
			try {
				if (input != null && input.available() > 0) {
					props.load(input);
					input.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		KafkaProducerConfig config = new KafkaProducerConfig(props);
		if (StringUtils.isNotBlank(config.getBatch_num_messages()))
			BaseMsgAsyncKafkaHandler.batch_num_messages = Integer
					.parseInt(config.getBatch_num_messages());
		if (StringUtils.isNotBlank(config.getQueue_buffering_max_ms()))
			BaseMsgAsyncKafkaHandler.queue_buffering_max_ms = Long
					.parseLong(config.getQueue_buffering_max_ms());
		if (StringUtils.isNotBlank(props.getProperty("kafkaclustersize")))
			try {
				config.setKafkaClusterSize(Integer.parseInt(props.getProperty("kafkaclustersize")));
			} catch (NumberFormatException e) {
				config.setKafkaClusterSize(1);
			}
		BaseMsgAsyncKafkaHandler.isInit = true;
		return config;
	}

	public void close() {
		if (producer != null){
			producer.close();
			producer=null;
		}
		if (byteProducer != null){
			byteProducer.close();
			byteProducer=null;
		}
	}
	
	public void destroy(long... time) throws Exception{
		if(KafkaConstant.ASYNC.equals(producer_type)){
			Thread.sleep(time==null||time.length==0?3000:time[0]);
			AbstractKafkaProducer
			.getInstance().destroy(producer_type, produce_serilize_class);
		}
	}
}