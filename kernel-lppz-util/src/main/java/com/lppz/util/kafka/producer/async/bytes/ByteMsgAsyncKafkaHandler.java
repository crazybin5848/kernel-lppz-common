package com.lppz.util.kafka.producer.async.bytes;


import kafka.producer.KeyedMessage;

import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;
import com.lppz.util.kafka.KafkaTopicKey;
import com.lppz.util.kafka.producer.async.BaseMsgAsyncKafkaHandler;

public class ByteMsgAsyncKafkaHandler extends
		BaseEventHandler<KeyedMessage<byte[], byte[]>, BaseEvent<byte[]>> {
//	private static final Logger logger = LoggerFactory
//			.getLogger(StringMsgAsyncKafkaHandler.class);

	@Override
	public void onEvent(BaseEvent<byte[]> msgEvent, long l, boolean b)
			throws Exception {
		byte[] value = msgEvent.getValue();
		String param=msgEvent.getParam();
		KafkaTopicKey tt=KafkaTopicKey.deBuildJson(param);
		if (value==null||tt==null) {
			super.onEvent(msgEvent, l, b);
			return;
		}
		baseList.add(new KeyedMessage<byte[], byte[]>(tt.getTopic(),tt.buildKeyB(),value));
		if (baseList.size() >= BaseMsgAsyncKafkaHandler.batch_num_messages) {
			handler.handle(baseList);
		}
	}
}