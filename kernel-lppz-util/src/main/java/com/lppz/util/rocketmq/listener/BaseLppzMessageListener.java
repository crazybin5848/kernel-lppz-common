package com.lppz.util.rocketmq.listener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.set.SetParams;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.lppz.core.exceptions.BaseLppzBizException;
import com.lppz.util.rocketmq.ProducerParam;
import com.lppz.util.rocketmq.RocketMqProducer;
import com.lppz.util.rocketmq.RocketMqUtil;
import com.lppz.util.rocketmq.enums.OtherRocketMqProducerGroup;
import com.lppz.util.rocketmq.enums.OtherRocketMqTopic;
import com.lppz.util.rocketmq.enums.RocketMqProducerTypeEnums;
import com.lppz.util.rocketmq.exception.RocketMqBizException;
import com.lppz.util.rocketmq.exception.RocketMqBizResult;

public abstract class BaseLppzMessageListener<T,R,U> {
	private static final Logger logger = LoggerFactory.getLogger(BaseLppzMessageListener.class);
	protected JedisCluster jedisCluster;
	protected DefaultMQProducer producer;
	private String nameSrv;
	public static boolean continueConsume = true;
	public DefaultMQProducer getProducer() {
		return producer;
	}
	public void setProducer(DefaultMQProducer producer) {
		nameSrv = producer.getNamesrvAddr();
		this.producer = producer;
	}
	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}
	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	protected abstract U doBiz(List<T> msgs) throws RocketMqBizException,BaseLppzBizException;
	protected abstract void rollBackBiz(R rollBackParam) throws BaseLppzBizException;
	protected void reSendBizMsgOrderly(List<MessageExt> listMsg,int level,String topic,String tag){
		if(producer!=null){
			for(MessageExt msg:listMsg){
				try {
					int retryCount=msg.getProperty(RocketMqUtil.MSGRETRYCOUNT)==null?0:Integer.parseInt(msg.getProperty(RocketMqUtil.MSGRETRYCOUNT));
					if(retryCount<=3)
					producer.send(RocketMqUtil.buildMsg(msg,level,topic,tag), new MessageQueueSelector() {
					    @Override
					    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					        Integer id = (Integer) arg;
					        return mqs.get(id);
					    }
					}, msg.getQueueId());
				} catch (MQClientException | RemotingException
						| MQBrokerException | InterruptedException e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
	}
	protected void reSendBizMsgConcurrenly(List<MessageExt> listMsg,int level,String topic,String tag){
		if(producer!=null){
			for(MessageExt msg:listMsg){
				try {
					int retryCount=msg.getProperty(RocketMqUtil.MSGRETRYCOUNT)==null?0:Integer.parseInt(msg.getProperty(RocketMqUtil.MSGRETRYCOUNT));
					if(retryCount<=3)
					producer.send(RocketMqUtil.buildMsg(msg,level,topic,tag));
				} catch (MQClientException | RemotingException
						| MQBrokerException | InterruptedException e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected U doHandleMsgList(List<MessageExt> msgs) {
		List<T> list=new ArrayList<T>(msgs.size());
		double consumTime = 0D;
		for(MessageExt msg:msgs){
			consumTime = computeConsumTime(msg);
			logger.debug("consume cost time {} s",consumTime);
			T t=RocketMqUtil.getMsgBody(msg);
			if(t!=null){
				String idempotencyId=msg.getProperty(RocketMqUtil.IDEMPOTENCY);
				if(StringUtils.isNotBlank(idempotencyId)){
					if(jedisCluster!=null){
						String ok;
						try {
							ok = jedisCluster.set(idempotencyId, "", SetParams.setParams().ex(1800).nx());
							logger.debug("consumer msgid {} msgKey {} redis result {}",idempotencyId,msg.getKeys() , ok);
							if("ok".equalsIgnoreCase(ok)){
								list.add(t);
								continue;
							}
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						}
					}else{
						logger.debug("consumer jedisCluster is null all consumer");
						list.add(t);
					}
				}else{
					logger.debug("consumer idempotencyId is null all consumer");
					list.add(t);					
				}
			}
		}
		if (list.isEmpty()) {
			return null;
		}
		try {
			return doBiz(list);
		} catch (RocketMqBizException e) {
			logger.error("concurrency msg doBiz fail:"+e.getMessage(),e);
//			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			RocketMqBizResult<R> rmbz=e.getBizResult();
			try {
				R rollBackParam=rmbz.getR();
				rollBackBiz(rollBackParam);
			}catch (BaseLppzBizException e1) {
				try {
					sendBizExceptionMsgDisruptorConcurrenly(e1);
				} catch (InterruptedException e2) {
					logger.error("rollback send biz exception msg fail:"+e1.getMessage(),e2);
				}
			} catch (Exception e1) {
				logger.error("concurrency msg biz rollback fail:"+e.getMessage(),e1);
//				sendBizMsg2TransctionCompensate();
			}
			finally{
				//默认业务异常不重发消息，但是延迟消息涉及到全局锁失效造成重复消息问题
				if(rmbz.getLevel() > 0){
					if(RocketMqProducerTypeEnums.CONCURRENCY.getDes().equals(msgs.get(0).getProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE))){
						reSendBizMsgConcurrenly(msgs,rmbz.getLevel(),rmbz.getTopic(),rmbz.getTag());
					}else if(RocketMqProducerTypeEnums.ORDERLY.getDes().equals(msgs.get(0).getProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE))){
						reSendBizMsgOrderly(msgs,rmbz.getLevel(),rmbz.getTopic(),rmbz.getTag());
					}else{
						logger.warn("rocketmq重发消息失败，未配置重发消息类型");
					}
				}
			}
		} catch (BaseLppzBizException e) {
			try {
				sendBizExceptionMsgDisruptorConcurrenly(e);
			} catch (InterruptedException e1) {
				logger.error("send biz exception msg fail:"+e1.getMessage(),e1);
			}
		}catch (Exception e) {
			logger.error("do biz exception :"+e.getMessage(),e);
		}
		return null;
	}
	
	private double computeConsumTime(MessageExt msg) {
		double consumTime = 0D;
		try {
			consumTime = (Calendar.getInstance().getTimeInMillis()-msg.getStoreTimestamp())/1000D;
		} catch (Exception e) {
			logger.error("computeConsumTime exception :"+e.getMessage(),e);
		}
		return consumTime;
	}
	/**
	 * 把业务异常发送到指定Topic
	 * @param exception
	 * @throws InterruptedException
	 */
	public void sendBizExceptionMsgDisruptorConcurrenly(BaseLppzBizException exception) throws InterruptedException {
		if (exception != null && exception.getCompensationMsgMap() != null 
				&& StringUtils.isNotBlank(exception.getKey())) {
			String producerGroup=OtherRocketMqProducerGroup.LPPZBIZEXCEOTION.getId();
			String prodInstanceName="prodInstance";
			DefaultMQProducer producer=RocketMqProducer.getInstance().buildProducer(producerGroup, nameSrv, prodInstanceName);
			ProducerParam<BaseLppzBizException> param=new ProducerParam<BaseLppzBizException>();
			param.setClazz(BaseLppzBizException.class);
			param.setKey(exception.getKey());
			param.setBody(exception);
			param.setTag(StringUtils.isBlank(exception.getModule())?"bizException":exception.getModule());
			param.setTopic(OtherRocketMqTopic.LPPZBIZEXCEOTION.getId());
			RocketMqProducer.getInstance().sendMsgConcurrenly(producer, param);
		}
	}
	public String getNameSrv() {
		return nameSrv;
	}
}