package com.lppz.util.rocketmq;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.lppz.util.rocketmq.listener.LppzMessageListenerConcurrency;
import com.lppz.util.rocketmq.listener.LppzMessageListenerOrderly;

public class SourcingMqConsumerTest {
//	String nameSrv="192.168.37.243:9876;192.168.37.242:9876;192.168.37.246:9876;192.168.37.247:9876";
	String nameSrv="192.168.37.243:9876;192.168.37.242:9876;192.168.37.246:9876;192.168.37.247:9876";
	private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumerTest.class);

	//串行,一次只拿一个
	@Test
	public void testStartConcurrency() {
		ConsumerParam param=new ConsumerParam();
		param.setConsumeBatch(false);
		param.setConsumerGroup("LppzApprovedConsumerGroup");
		param.setNamesrvAddr(nameSrv);
		param.setTag("*");
		param.setConsumeMinThread(200);
		param.setConsumeMaxThread(500);
		param.setTopic("LppzApproved");
		LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object> mlc=new LppzMessageListenerOrderly<LppzOrder,List<LppzOrder>,Object>(){

			@Override
			protected Object doBiz(List<LppzOrder> msgs) {
				for(LppzOrder order:msgs){
					logger.info(order.toString());
				}
				return null;
//				throw RocketMqBizException.build(new Exception("fuck"),msgs);
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
}
