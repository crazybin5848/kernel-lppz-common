package com.lppz.spark.rocketmq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.lppz.bean.Spark2kafkaBean;
import com.lppz.util.rocketmq.ProducerParam;
import com.lppz.util.rocketmq.RocketMqProducer;
import com.lppz.util.rocketmq.enums.OtherRocketMqProducerGroup;
import com.lppz.util.rocketmq.enums.OtherRocketMqTopic;

public class SparkExportRocketMqProducer {
	private ExecutorService executor = Executors.newFixedThreadPool(200);
	private static final Logger logger = LoggerFactory.getLogger(SparkExportRocketMqProducer.class);
	private DefaultMQProducer producer;
	private String producerGroup;
	private String namesrvAddr;
	private String prodInstanceName = "prodInstance";
	private String topic;
	private String tag;
	private int threadNum;
	
	public SparkExportRocketMqProducer() {
	}
	
	public SparkExportRocketMqProducer(String namesrvAddr){
		this.producerGroup = OtherRocketMqProducerGroup.SPARKEXPORT.getId();
		this.namesrvAddr = namesrvAddr;
		this.topic = OtherRocketMqTopic.SPARKEXPORT.getId();
		this.tag = OtherRocketMqTopic.SPARKEXPORT.getId();
		this.threadNum = 200;
		producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
	}
	
	public SparkExportRocketMqProducer(String producerGroup, String namesrvAddr, String topic, String tag, int threadNum) {
		this.producerGroup = producerGroup;
		this.namesrvAddr = namesrvAddr;
		this.topic = topic;
		this.tag = tag;
		this.threadNum = threadNum;
		producer=RocketMqProducer.getInstance().buildProducer(producerGroup, namesrvAddr, prodInstanceName);
	}
	
	public DefaultMQProducer getProducer() {
		return producer;
	}

	public void setProducer(DefaultMQProducer producer) {
		this.producer = producer;
	}

	public String getProducerGroup() {
		return producerGroup;
	}

	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getProdInstanceName() {
		return prodInstanceName;
	}

	public void setProdInstanceName(String prodInstanceName) {
		this.prodInstanceName = prodInstanceName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public void sendMsgConcurrenly(String msg,String key){
		if(null==msg || "".equals(msg)){
			return;
		}
		try {
			ProducerParam<String> param=new ProducerParam<String>();
			param.setClazz(String.class);
			param.setKey(key);
			param.setBody(msg);
			param.setTag(tag);
			param.setTopic(topic);
			producer.setSendMsgTimeout(10000);
			RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMsgConcurrenly(List<Spark2kafkaBean> msgs) throws InterruptedException {
		logger.info("sendMsgConcurrenly msgs size {}",CollectionUtils.isEmpty(msgs)?0:msgs.size());
		if (CollectionUtils.isEmpty(msgs)) {
			return;
		}
		final AtomicInteger ai=new AtomicInteger(0);
		int pageNum = (msgs.size()-1)/threadNum + 1;
		
		for(int i=0;i<pageNum;i++){
			int startNum = i*threadNum;
			int endNum = (i+1)*threadNum;
			if (startNum>=msgs.size()) {
				break;
			}
			endNum = endNum > msgs.size()?msgs.size():endNum;
			final List<Spark2kafkaBean> tmpMsgs = msgs.subList(startNum, endNum);
			executor.execute(new Runnable(){
				@Override
				public void run() {
					List<String> ids = new ArrayList<String>();
					for(Spark2kafkaBean msg : tmpMsgs){
						String orderid = msg.getResultMap().get("orderid");
						ids.add(msg.getResultMap().get("id"));
						try {
							ProducerParam<Spark2kafkaBean> param=new ProducerParam<Spark2kafkaBean>();
							param.setClazz(Spark2kafkaBean.class);
							param.setKey(orderid);
							param.setBody(msg);
							param.setTag(tag);
							param.setTopic(topic);
							producer.setSendMsgTimeout(10000);
							RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
							logger.debug(orderid);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					logger.debug("ids:{}",ids);
					ai.addAndGet(1);
				}
			});
		}
		while(true){
			logger.debug("ai num = {}",ai.get());
			if(ai.get()==pageNum)
			{
				executor.shutdown();
				break;
			}
			Thread.sleep(100);
		}
	}

	public void close(){
		if (producer != null) {
			try {
				producer.shutdown();
			} catch (Exception e) {
				logger.error("shutsown producer exception ", e);
			}
		}
	}
}
