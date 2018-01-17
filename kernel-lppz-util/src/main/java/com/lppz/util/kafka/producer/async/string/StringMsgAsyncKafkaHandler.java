package com.lppz.util.kafka.producer.async.string;

import kafka.producer.KeyedMessage;

import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;
import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;
public class StringMsgAsyncKafkaHandler extends
		BaseEventHandler<KeyedMessage<String, String>, BaseEvent<String>> {
//	private static final Logger logger = LoggerFactory
//			.getLogger(StringMsgAsyncKafkaHandler.class);

	@Override
	public void onEvent(BaseEvent<String> msgEvent, long l, boolean b)
			throws Exception {
		String value = msgEvent.getValue();
		String param=msgEvent.getParam();
		KafkaTopicKey tt=KafkaTopicKey.deBuildJson(param);
		if (null==value||null==tt) {
			super.onEvent(msgEvent, l, b);
			return;
		}
		baseList.add(new KeyedMessage<String, String>(tt.getTopic(),tt.getKey(),value));
		if (baseList.size() >= BaseMsgAsyncKafkaHandler.batch_num_messages) {
			handler.handle(baseList);
		}
	}
}