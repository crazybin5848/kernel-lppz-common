package com.lppz.util.kafka.producer.async.bytes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventFactory;
import com.lppz.util.disruptor.BaseEventProducerWith2Translator;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.kafka.producer.async.AsyncKafkaMessageProducer;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;

public class ByteQueueKafkaMessageProducer extends BaseEvent2Sender<byte[]> implements AsyncKafkaMessageProducer<byte[]> {
	private static final Logger logger = LoggerFactory.getLogger(ByteQueueKafkaMessageProducer.class);
//	private static ByteQueueKafkaMessageProducer queueKafkaMessageCache = new ByteQueueKafkaMessageProducer();
//	public static ByteQueueKafkaMessageProducer getInstance() {
//		return queueKafkaMessageCache;
//	}

	public ByteQueueKafkaMessageProducer() {
		BaseEventFactory<byte[]> factory = new BaseEventFactory<byte[]>(){
			@Override
			public BaseEvent<byte[]> newInstance() {
				return new BaseEvent<byte[]>();
			}
		};
		int bufferSize = super.bufferSize;
		Disruptor<BaseEvent<byte[]>> disruptor = new Disruptor<BaseEvent<byte[]>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
				new SleepingWaitStrategy());
		ByteMsgAsyncKafkaHandler beh=new ByteMsgAsyncKafkaHandler();
		ByteDisruptorHandler handler=new ByteDisruptorHandler();
		handler.init();
		beh.setHandler(handler);
		disruptor.handleEventsWith(new ByteMsgAsyncKafkaHandler[] {beh});
		disruptor.start();
		RingBuffer<BaseEvent<byte[]>> ringBuffer = disruptor.getRingBuffer();
		producer = new BaseEventProducerWith2Translator<byte[]>(ringBuffer);
		super.loopSend(BaseMsgAsyncKafkaHandler.queue_buffering_max_ms);
		super.baseList=beh.getBaseList();
	}

	@Override
	public void pushCache(final String topic, final byte[] jmsg) {
		try {
			producer.onData(jmsg, topic);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
}