package com.lppz.canal.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.lppz.util.rocketmq.ConsumerParam;
import com.lppz.util.rocketmq.RocketMqConsumer;
import com.lppz.util.rocketmq.disruptor.BaseRocketHandler;
import com.lppz.util.rocketmq.listener.LppzMessageDisruptorListenerOrderly;

/**
 * rocketmq消费端基类，封装了consumer构造和消费消息处理
 * 暴露consume初始化参数抽象方法和业务处理方法
 * @author licheng
 *
 * @param <T> 消费mq消息实体类型
 * @param <S> 异常回滚获取的消息实体类型
 * @param <U> 业务处理返回的结果类型
 */
public abstract class AbstractConsumer<T,S, U> extends PathMatchingResourcePatternResolver  implements DisposableBean, ApplicationListener<ContextRefreshedEvent>{
	private static final Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);
	private DefaultMQPushConsumer consumer;
	@Override
	public void destroy() throws Exception {
		RocketMqConsumer.getInstance().shutDownConsumer(consumer);
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		startConsume();
	}
	
	public void startConsume(){
		logger.info("Consumer start");
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(false);
		param.setConsumerGroup(getConsumerGroup());
		param.setNamesrvAddr(getNameSrv());
		param.setTag(getTag());
		param.setConsumeMinThread(getConsumerMinTreadNum());
		param.setConsumeMaxThread(getConsumerMaxThreadNum());
		param.setTopic(getTopic());
		BaseRocketHandler<T,List<S>,U> handler=new BaseRocketHandler<T,List<S>,U>(){
			@Override
			protected U doBiz(List<T> msgs) {
				return dealMsg(validate(msgs));
			}

			@Override
			protected void rollBackBiz(List<S> rollBackParam) {
				dealRollBack(rollBackParam);
			}
			
		};
		LppzMessageDisruptorListenerOrderly<T,List<S>,U> mlc=new LppzMessageDisruptorListenerOrderly<T,List<S>,U>(handler,getBatchSize(),getBatchTimeStep());
		consumer = RocketMqConsumer.getInstance().startDisruptorOrderly(param, mlc);
		logger.info("excpConsumer end");
	}


	public abstract String getNameSrv();
	public abstract String getTag();
	public abstract String getConsumerGroup();
	public abstract String getTopic();
	public abstract int getBatchSize();
	public abstract int getBatchTimeStep();
	public abstract int getConsumerMinTreadNum();
	public abstract int getConsumerMaxThreadNum();
	public abstract List<T> validate(List<T> t);
	public abstract U dealMsg(List<T> t);
	public abstract void dealRollBack(List<S> s);
	
}
