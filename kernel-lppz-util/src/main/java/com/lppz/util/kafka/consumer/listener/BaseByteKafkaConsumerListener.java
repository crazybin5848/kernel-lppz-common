package com.lppz.util.kafka.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lppz.util.kryo.KryoUtil;

/**
 *
 */
public abstract class BaseByteKafkaConsumerListener<T> implements
		ByteKafkaConsumerListener<T> {
	private static final Logger logger = LoggerFactory
			.getLogger(BaseByteKafkaConsumerListener.class);

	@Override
	public void onMessage(final byte[] msg, Class<T> clazz) {
		try {
			T t = null;
			if (clazz == byte[].class)
				t = (T) msg;
			else
				t = KryoUtil.kyroDeSeriLize(msg, clazz);
			doMsg(t);
		} catch (final Exception e) {
			logger.error("consume error :" + msg, e);
		}
	}

	protected abstract void doMsg(T t);

}
