package com.lppz.jstorm.kafka;

import java.io.IOException;
import java.util.List;

import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.utils.Utils;

import com.alibaba.jstorm.kafka.KafkaMessageId;
import com.alibaba.jstorm.kafka.KafkaSpout;
import com.alibaba.jstorm.kafka.KafkaSpoutConfig;
import com.alibaba.jstorm.kafka.PartitionConsumer;

public class KafkaLppzSpout extends KafkaSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8732762990785959231L;
	/**
	 * 
	 */
	private static Logger LOG = LoggerFactory.getLogger(KafkaLppzSpout.class);

	public KafkaLppzSpout() {
	    super();
	}
	
	public KafkaLppzSpout(KafkaSpoutConfig config) {
		super(config);
	}

	@Override
	public void fail(Object msgId) {
		KafkaMessageId messageId = (KafkaMessageId)msgId;
		PartitionConsumer consumer = coordinator.getConsumer(messageId.getPartition());
		consumer.fail(messageId.getOffset());
		ByteBufferMessageSet msgs=null;
		try {
			msgs = consumer.getConsumer().fetchMessages(messageId.getPartition(), messageId.getOffset());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		for (MessageAndOffset toEmitMsg : msgs) {
			Iterable<List<Object>> tups = consumer.generateTuples(toEmitMsg.message());
            if (tups != null) {
                for (List<Object> tuple : tups) {
                    LOG.debug("emit message {}", new String(Utils.toByteArray(toEmitMsg.message().payload())));
                    collector.emit(tuple, new KafkaMessageId(messageId.getPartition(), toEmitMsg.offset()));
                }
            } else {
                ack(toEmitMsg.offset());
            }
        }
	}
}