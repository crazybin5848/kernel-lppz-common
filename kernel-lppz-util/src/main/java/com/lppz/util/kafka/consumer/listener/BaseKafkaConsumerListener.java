package com.lppz.util.kafka.consumer.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public abstract class BaseKafkaConsumerListener<T> implements KafkaConsumerListener<T>
{
	private static final Logger logger = LoggerFactory.getLogger(BaseKafkaConsumerListener.class);

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(final String msg,Class<T> clazz)
	{
		try
		{   T t=null;
			if(clazz==String.class)
				t=(T)msg;
			else
			 t= JSON.parseObject(msg, clazz);
			doMsg(t);
		}

		catch (final Exception e)
		{
			logger.error("consume error :" + msg, e);
		}

	}

	protected abstract void doMsg(T t) ;
	
}
