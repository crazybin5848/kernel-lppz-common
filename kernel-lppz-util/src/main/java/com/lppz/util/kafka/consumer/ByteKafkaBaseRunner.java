package com.lppz.util.kafka.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lppz.util.kafka.consumer.listener.ByteKafkaConsumerListener;

public class ByteKafkaBaseRunner<T> implements Runnable
{
	protected static final Log logger = LogFactory
			.getLog(ByteKafkaBaseRunner.class);
	private final KafkaStream<byte[], byte[]> partition;
	private final ByteKafkaConsumerListener<T> listener;
	private final Class<T> clazz;
	public ByteKafkaBaseRunner(final ByteKafkaConsumerListener<T> listener, final KafkaStream<byte[], byte[]> partition,Class<T> clazz)
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
			final MessageAndMetadata<byte[], byte[]> item = it.next();
			try
			{
				listener.onMessage(item.message(),clazz);
			}
			catch (final Exception e)
			{
				logger.error(e.getMessage(),e);
			}
		}
	}
}