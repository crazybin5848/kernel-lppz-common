package com.lppz.util.rocketmq;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.lppz.core.exceptions.BaseLppzBizException;
import com.lppz.util.rocketmq.disruptor.BaseRocketHandler;
import com.lppz.util.rocketmq.exception.RocketMqBizException;
import com.lppz.util.rocketmq.listener.LppzMessageDisruptorListenerConcurrency;
import com.lppz.util.rocketmq.listener.LppzMessageDisruptorListenerOrderly;
import com.lppz.util.rocketmq.listener.LppzMessageListenerConcurrency;
import com.lppz.util.rocketmq.listener.LppzMessageListenerOrderly;

public class RocketMqConsumerTest {
//	String nameSrv="192.168.37.243:9876;192.168.37.242:9876;192.168.37.247:9876";
	String nameSrv="10.6.30.109:9876";	
	
	private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumerTest.class);
	private String tag = "JunitTest";
	private String topic = "JunitTest";
	private String consumerGroup = "JunitTestConsumerGroup";
	/**
	 * 无序单个消费
	 */
	@Test
	public void testStartConcurrency() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(false);
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setConsumeMinThread(100);
		param.setConsumeMaxThread(200);
		param.setTopic(topic);
		LppzMessageListenerConcurrency<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageListenerConcurrency<LppzOrder,List<LppzOrder>,Object>(){

			@Override
			protected Object doBiz(List<LppzOrder> msgs) throws BaseLppzBizException {
				for(LppzOrder order:msgs){
					String key = order.getOrderId();
					String keySource = "orderid";
					String module = "订单审核";
					logger.info(order.toString());
					if(!validate(order)){
						//进入rollbackBiz()方法处理，不重发消息
						throw RocketMqBizException.build(new Exception("审核异常"),msgs);
						//设置延迟消费级别，系统会发送一条延迟消息到当前topic，等待下次消费
//						throw RocketMqBizException.build(new Exception("审核异常"),msgs,RocketMqDelayEnums.OneMin.getLevel());
					}
					
					try{
						doSomething(order);
					}catch(Exception e){
						//会发送异常信息到LppzBizException的topic最终存入ES，可根据key查询异常信息，各业务节点最好定义自己的异常类继承BaseLppzBizException
						//这样各节点可以根据异常类型和所需的属性做异常处理比如事务补偿
						throw new BaseLppzBizException(key, keySource, module, e);
					}
				}
				return null;
			}

			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
				for(LppzOrder order:rollBackParam){
					logger.error("rockback:"+order);
				}
			}
		};
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
		mlc.setProducer(producer);
//		mlc.setJedisCluster(ClusterUtils.getJedisCluster());
		RocketMqConsumer.getInstance().startConcurrency(param, mlc);
		while(true){
			
		}
	}
	
	protected void doSomething(LppzOrder order) {
		// TODO Auto-generated method stub
		
	}

	protected boolean validate(LppzOrder order) {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * 无序单个消费
	 */
	@Test
	public void testStartBatchConcurrency() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(true);
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setConsumeMinThread(200);
		param.setConsumeMaxThread(500);
		param.setTopic(topic);
		LppzMessageListenerConcurrency<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageListenerConcurrency<LppzOrder,List<LppzOrder>,Object>(){

			@Override
			protected Object doBiz(List<LppzOrder> msgs) throws BaseLppzBizException {
				logger.info("batch msg size {}", msgs.size());
				for(LppzOrder order:msgs){
					String key = order.getOrderId();
					String keySource = "orderid";
					String module = "订单审核";
					logger.debug(order.toString());
					if(!validate(order)){
						//进入rollbackBiz()方法处理，不重发消息
						throw RocketMqBizException.build(new Exception("审核异常"),msgs);
						//设置延迟消费级别，系统会发送一条延迟消息到当前topic，等待下次消费
//						throw RocketMqBizException.build(new Exception("审核异常"),msgs,RocketMqDelayEnums.OneMin.getLevel());
					}
					
					try{
						doSomething(order);
					}catch(Exception e){
						//会发送异常信息到LppzBizException的topic最终存入ES，可根据key查询异常信息，各业务节点最好定义自己的异常类继承BaseLppzBizException
						//这样各节点可以根据异常类型和所需的属性做异常处理比如事务补偿
						throw new BaseLppzBizException(key, keySource, module, e);
					}
				}
				return null;
			}

			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
				for(LppzOrder order:rollBackParam){
					logger.error("rockback:"+order);
				}
			}
		};
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
		mlc.setProducer(producer);
		RocketMqConsumer.getInstance().startConcurrency(param, mlc);
		while(true){
			
		}
	}

	/**
	 * 无序disruptor批量消费
	 */
	@Test
	public void testStartDisruptorConcurrency() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setTopic(topic);
		BaseRocketHandler<LppzOrder,List<LppzOrder>,Object> handler=new BaseRocketHandler<LppzOrder,List<LppzOrder>,Object>(){
			@Override
			protected Object doBiz(List<LppzOrder> msgs)
					throws RocketMqBizException {
				logger.info(Thread.currentThread().getName()+":"+msgs.size());
				return msgs;
			}

			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
			}
			
		};
		LppzMessageDisruptorListenerConcurrency<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageDisruptorListenerConcurrency<LppzOrder,List<LppzOrder>,Object>(handler,1000,3000);
		RocketMqConsumer.getInstance().startDisruptorConcurrency(param, mlc);
		while(true){
			
		}

	}

	@Test
	public void testStartOrderly() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setTopic(topic);
		param.setConsumeBatch(false);
		final AtomicInteger count = new AtomicInteger(0);
		final AtomicInteger countList = new AtomicInteger(0);
		LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object>(){

			@Override
			protected Object doBiz(List<LppzOrder> msgs) {
				count.addAndGet(msgs.size());
				countList.addAndGet(1);
				logger.info("orderly batch msg get size {} list count {} msg count {}  thread id :{}"
						, msgs.size(), countList.get(), count.get(), Thread.currentThread().getId());
				for(LppzOrder order:msgs){
					logger.debug(order.toString());
				}
				if (count.get() > 1) {
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						logger.error(e.toString(),e);
					}
				}
				logger.info("sleep over");
				return null;
//				throw RocketMqBizException.build(new Exception("test exception"), msgs , RocketMqDelayEnums.FiveSecond.getLevel());
//				throw RocketMqBizException.build(new Exception("test exception"),msgs);
			}

			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
				for(LppzOrder order:rollBackParam){
					logger.error("rockback:"+order);
				}
			}
		};
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
		mlc.setProducer(producer);
		RocketMqConsumer.getInstance().startOrderly(param, mlc);
		while(true){
		}
	}
	@Test
	public void testStartBatchOrderly() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(true);
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setTopic(topic);
		final AtomicInteger count = new AtomicInteger(0);
		final AtomicInteger countList = new AtomicInteger(0);
		LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object>(){
			
			@Override
			protected Object doBiz(List<LppzOrder> msgs) {
				count.addAndGet(msgs.size());
				countList.addAndGet(1);
				logger.info("orderly batch msg get size {} list count {} msg count {}", msgs.size(), countList.get(), count.get());
				for(LppzOrder order:msgs){
					logger.debug(order.toString());
				}
				return null;
//				throw RocketMqBizException.build(new Exception("test exception"), msgs , RocketMqDelayEnums.FiveSecond.getLevel());
//				throw RocketMqBizException.build(new Exception("test exception"),msgs);
			}
			
			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
				for(LppzOrder order:rollBackParam){
					logger.error("rockback:"+order);
				}
			}
		};
		String producerGroup="test1Group";
		String namesrvAddr=nameSrv;
		String prodInstanceName="prodInstance";
		DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
		mlc.setProducer(producer);
		RocketMqConsumer.getInstance().startOrderly(param, mlc);
		while(true){
		}
	}

	@Test
	public void testStartDisruptorOrderly() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumerGroup(consumerGroup);
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setTopic(topic);
		BaseRocketHandler<LppzOrder,List<LppzOrder>,Object> handler=new BaseRocketHandler<LppzOrder,List<LppzOrder>,Object>(){
			@Override
			protected Object doBiz(List<LppzOrder> msgs)
					throws RocketMqBizException {
				for(LppzOrder order:msgs){
					logger.info(order.getShardId()+"" + order.getOrderId());
				}
				logger.info(Thread.currentThread().getName()+":"+msgs.size());
				return msgs;
			}

			@Override
			protected void rollBackBiz(List<LppzOrder> rollBackParam) {
			}
			
		};
		LppzMessageDisruptorListenerOrderly<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageDisruptorListenerOrderly<LppzOrder,List<LppzOrder>,Object>(handler,1000,1000);
		RocketMqConsumer.getInstance().startDisruptorOrderly(param, mlc);
		while(true){
			
		}
	}
}