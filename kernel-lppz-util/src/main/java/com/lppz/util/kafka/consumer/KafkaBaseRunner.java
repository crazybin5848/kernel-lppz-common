package com.lppz.util.kafka.consumer;

import java.nio.charset.Charset;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lppz.util.kafka.consumer.listener.KafkaConsumerListener;

public class KafkaBaseRunner<T> implements Runnable
{
	protected static final Log logger = LogFactory
			.getLog(BaseKafkaConsumer.class);
	private final KafkaStream<byte[], byte[]> partition;
	private final KafkaConsumerListener<T> listener;
	private final Class<T> clazz;
	public static volatile boolean needRun;
	public KafkaBaseRunner(final KafkaConsumerListener<T> listener, final KafkaStream<byte[], byte[]> partition,Class<T> clazz)
	{
		this.listener = listener;
		this.partition = partition;
		this.clazz = clazz;
	}

	@Override
	public void run()
	{
		final ConsumerIterator<byte[], byte[]> it = partition.iterator();
		while (it.hasNext())
		{
			if(needRun)
				return;
			final MessageAndMetadata<byte[], byte[]> item = it.next();
			try
			{
				if(item.message()!=null)
				listener.onMessage(new String(item.message(), Charset.forName("utf8")),clazz);
				else
				logger.warn("item msg is null:"+item);
			}
			catch (final Exception e)
			{
				logger.error(e.getMessage(),e);
			}
		}
	}
}