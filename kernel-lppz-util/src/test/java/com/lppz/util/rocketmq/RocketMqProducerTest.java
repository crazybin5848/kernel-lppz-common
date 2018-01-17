package com.lppz.util.rocketmq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.lppz.util.rocketmq.enums.RocketMqDelayEnums;

public class RocketMqProducerTest {
	ExecutorService executor = Executors.newFixedThreadPool(200);
//	String nameSrv="192.168.37.243:9876;192.168.37.242:9876;192.168.37.247:9876";
//	String nameSrv="192.168.19.187:9876;192.168.19.188:9876;192.168.19.189:9876";
	String nameSrv="10.6.30.109:9876";
	private String tag = "JunitTest";
	private String topic = "JunitTest";
	
	@Test
	public void testSendMsgConcurrenly() throws InterruptedException {
		final AtomicInteger ai=new AtomicInteger(0);
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		final DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
//		send(producer);
		int size=10000;
		for(int i=0;i<size;i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					ai.addAndGet(1);
					send(producer);
				}
			});
		}
		while(true){
			if(ai.get()==size)
			{
				executor.shutdown();
				break;
			}
			Thread.sleep(100);
		}
	}
	
	private void send(final DefaultMQProducer producer) {
		ProducerParam<LppzOrder> param=new ProducerParam<LppzOrder>();
		LppzOrder order=new LppzOrder();
		order.setOrderId("FUCKccc");
		order.setPhone("13789889999");
		order.setShardId(123);
		order.setUser("fuck");
		param.setClazz(LppzOrder.class);
		param.setKey(order.getOrderId());
		param.setBody(order);
		param.setTag(tag);
		param.setTopic(topic);
		RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
	}
	
	@Test
	public void testSendMsgDisruptorConcurrenly() throws InterruptedException {
		int size=50000;
		for(int i=0;i<size;i++){
			String producerGroup="test1Group";
			String namesrvAddr=nameSrv;
			String prodInstanceName="prodInstance";
			DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
			ProducerParam<LppzOrder> param=new ProducerParam<LppzOrder>();
			LppzOrder order=new LppzOrder();
			order.setOrderId("orderid001");
			order.setPhone("13789889999");
			order.setShardId(123);
			order.setUser("user1");
			param.setClazz(LppzOrder.class);
			param.setKey(order.getOrderId());
			param.setBody(order);
			param.setTag(tag);
			param.setTopic(topic);
			RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
		}
	}

	@Test
	public void testSendMsgOrderly() {
		int size=50000;
		for(int i=0;i<size;i++){
			String producerGroup="test2Group";
			String namesrvAddr=nameSrv;
			String prodInstanceName="prodInstance";
			DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
			ProducerParam<LppzOrder> param=new ProducerParam<LppzOrder>();
			LppzOrder order=new LppzOrder();
			order.setOrderId("orderi002");
			order.setPhone("13789889999");
//			order.setShardId(Math.abs((int)UUID.randomUUID().getLeastSignificantBits()));
			order.setShardId(i);
			order.setUser("useri");
			param.setClazz(LppzOrder.class);
			param.setKey(order.getOrderId());
			param.setBody(order);
			param.setTag(tag);
			param.setTopic(topic);
			RocketMqProducer.getInstance().sendMsgOrderly(producer, param, order.getShardId());
		}
	}
	
	@Test
	public void testSendMsgDisruptorOrderly() {
		int size=50000;
		for(int i=0;i<size;i++){
			String producerGroup="test2Group";
			String namesrvAddr=nameSrv;
			String prodInstanceName="prodInstance";
			DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
			ProducerParam<LppzOrder> param=new ProducerParam<LppzOrder>();
			LppzOrder order=new LppzOrder();
			order.setOrderId("orderid" + i);
			order.setPhone("13789889999");
//			order.setShardId(Math.abs((int)UUID.randomUUID().getLeastSignificantBits()));
			order.setShardId(i);
			order.setUser("user");
			param.setClazz(LppzOrder.class);
			param.setKey(order.getOrderId());
			param.setBody(order);
			param.setTag(tag);
			param.setTopic(topic);
			RocketMqProducer.getInstance().sendMsgOrderly(producer, param, order.getShardId());
		}
	}
	
	@Test
	public void testSendMsgOrderlyDelay() {
		int size=1;
		for(int i=0;i<size;i++){
			String producerGroup="test2Group";
			String namesrvAddr=nameSrv;
			String prodInstanceName="prodInstance";
			DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
			ProducerParam<LppzOrder> param=new ProducerParam<LppzOrder>();
			LppzOrder order=new LppzOrder();
			order.setOrderId("FUCKccc" + i);
			order.setPhone("13789889499");
//			order.setShardId(Math.abs((int)UUID.randomUUID().getLeastSignificantBits()));
			order.setShardId(i);
			order.setUser("fuck");
			param.setClazz(LppzOrder.class);
			param.setKey(order.getOrderId());
			param.setBody(order);
			param.setTag(tag);
			param.setTopic(topic);
//			param.setDelayTimes(RocketMqDelayEnums.OneMin);
			RocketMqProducer.getInstance().sendMsgOrderly(producer, param, order.getShardId());
		}
	}
}