package com.lppz.util.kafka.producer.async.string;

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

public class StringQueueKafkaMessageProducer extends BaseEvent2Sender<String> implements AsyncKafkaMessageProducer<String> {
	private static final Logger logger = LoggerFactory.getLogger(StringQueueKafkaMessageProducer.class);
//	private static StringQueueKafkaMessageProducer queueKafkaMessageCache = new StringQueueKafkaMessageProducer();
//	public static StringQueueKafkaMessageProducer getInstance() {
//		return queueKafkaMessageCache;
//	}

	public StringQueueKafkaMessageProducer() {
		BaseEventFactory<String> factory = new BaseEventFactory<String>(){
			@Override
			public BaseEvent<String> newInstance() {
				return new BaseEvent<String>();
			}
		};
		int bufferSize = super.bufferSize;
		Disruptor<BaseEvent<String>> disruptor = new Disruptor<BaseEvent<String>>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
				new SleepingWaitStrategy());
		StringMsgAsyncKafkaHandler beh=new StringMsgAsyncKafkaHandler();
		StringDisruptorHandler handler=new StringDisruptorHandler();
		handler.init();
		beh.setHandler(handler);
		disruptor.handleEventsWith(new StringMsgAsyncKafkaHandler[] {beh});
		disruptor.start();
		RingBuffer<BaseEvent<String>> ringBuffer = disruptor.getRingBuffer();
		producer = new BaseEventProducerWith2Translator<String>(ringBuffer);
		super.loopSend(BaseMsgAsyncKafkaHandler.queue_buffering_max_ms);
		super.baseList=beh.getBaseList();
	}

	@Override
	public void pushCache(final String topic, final String jmsg) {
		try {
			producer.onData(jmsg, topic);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
}