package com.lppz.util.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class SimpleMqTest {
	String nameSrv="10.6.30.109:9876";	
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleMqTest.class);
	private String tag = "JunitTest";
	private String topic = "mqfuck";
	private String odrtopic = "mqfuckodr";
	private String consumerGroup = "JavaConsumerGroup";
	/**
	 * 无序单个消费
	 */
	@Test
	public void testStartConcurrency() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(true);
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setConsumeMinThread(100);
		param.setConsumeMaxThread(200);
		param.setTopic(topic);
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		RocketMqConsumer.getInstance().setConsumerParam(param, consumer);
		final AtomicInteger ai=new AtomicInteger(0);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			MessageListenerConcurrently mlc=new MessageListenerConcurrently(){
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					ai.getAndAdd(msgs.size());
//					for(MessageExt msg:msgs){
//						try {
//							logger.info(new String(msg.getBody(),"utf-8"));
//						} catch (UnsupportedEncodingException e) {
//							e.printStackTrace();
//						}
//					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			};
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			logger.info("cc:"+ai.get());
		}
	}
	
	@Test
	public void testStartOrderly() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(true);
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setConsumeMinThread(100);
		param.setConsumeMaxThread(200);
		param.setTopic(odrtopic);
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				param.getConsumerGroup());
		RocketMqConsumer.getInstance().setConsumerParam(param, consumer);
		final AtomicInteger ai=new AtomicInteger(0);
		try {
			consumer.subscribe(param.getTopic(), param.getTag());
			MessageListenerOrderly mlc=new MessageListenerOrderly(){
				@Override
				public ConsumeOrderlyStatus consumeMessage(
						List<MessageExt> msgs, ConsumeOrderlyContext context) {
					for(MessageExt msg:msgs){
						try {
							logger.info(new String(msg.getBody(),"utf-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					ai.getAndAdd(msgs.size());
					return ConsumeOrderlyStatus.SUCCESS;
				}
			};
			consumer.registerMessageListener(mlc);
			consumer.start();
			logger.info("Consumer Started.");
		} catch (MQClientException e) {
			logger.error(e.getMessage(),e);
		}
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
//			logger.info("cc:"+ai.get());
		}
	}
	
	@Test
	public void testSendMsg() {
//		ExecutorService executor = Executors.newFixedThreadPool(200);
		final AtomicInteger ai=new AtomicInteger(0);
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		final DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
//		send(producer);
		try {
			sendOdr(producer);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		int size=10000;
//		for(int i=0;i<size;i++){
//			executor.execute(new Runnable(){
//				@Override
//				public void run() {
//					try {
//						send(producer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}finally{
//						ai.addAndGet(1);
//					}
//				}
//			});
//		}
//		while(true){
//			if(ai.get()==size)
//			{
//				executor.shutdown();
//				break;
//			}
//			System.out.println("cc:"+ai.get());
//			Thread.sleep(100);
//		}
	}
	
	private void send(final DefaultMQProducer producer) throws Exception {
		Message msg = new Message(topic,tag,String.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits())),"大神哈哈fuck".getBytes());
		try {
			producer.send(msg);
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void sendOdr(final DefaultMQProducer producer) throws Exception {
		Message msg = new Message(odrtopic,tag,String.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits())),"大神哈哈fuck".getBytes());
		try {
			producer.send(msg, new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs,
						Message msg, Object arg) {
					Integer id = (Integer) arg;
					int index = id % mqs.size();
					return mqs.get(index);
				}
			}, 1);
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			e.printStackTrace();
			throw e;
		}
	}
}