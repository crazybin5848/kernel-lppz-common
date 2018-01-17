package com.lppz.util.rocketmq;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.Message;
import com.lppz.util.kryo.KryoUtil;
import com.lppz.util.rocketmq.enums.RocketMqDelayEnums;

public class ProducerParam<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 436813283670088281L;
	private final Logger logger = LoggerFactory
			.getLogger(ProducerParam.class);
	private String topic;
	private String tag;
	private String key;
	private String idempotencyid;
	private Class<T> clazz;
	private T body;
	private RocketMqDelayEnums delayTimes;
	private boolean isCanRepeat = true;
	private boolean isSendJsonBody;
	
	public Message buildMessage(){
		byte[] bb=null;
		try {
//			filterBody(body);
			bb=KryoUtil.kyroSeriLize(body, -1);
			Message msg = new Message(topic,tag,key,bb);
			msg.putUserProperty(RocketMqUtil.MSGCLASSTYPE, clazz.getName());
			msg.putUserProperty(RocketMqUtil.IDEMPOTENCY, StringUtils.isBlank(idempotencyid)?Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()),36):idempotencyid);
			if(isSendJsonBody){				
				msg.putUserProperty(RocketMqUtil.MSGJSONBODY, JSON.toJSONString(body));
			}
			int currentCount=msg.getProperty(RocketMqUtil.MSGRETRYCOUNT)==null?0:Integer.parseInt(msg.getProperty(RocketMqUtil.MSGRETRYCOUNT));
			msg.putUserProperty(RocketMqUtil.MSGRETRYCOUNT,String.valueOf(currentCount+1));
			msg.putUserProperty(RocketMqUtil.ISCANREPEAT, String.valueOf(isCanRepeat));
			if(delayTimes != null){
				msg.setDelayTimeLevel(delayTimes.getLevel());
			}
			return msg;
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
//	protected abstract void filterBody(T body);

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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Class<T> getClazz() {
		return clazz;
	}
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}

	public String getIdempotencyid() {
		return idempotencyid;
	}

	public void setIdempotencyid(String idempotencyid) {
		this.idempotencyid = idempotencyid;
	}

	public RocketMqDelayEnums getDelayTimes() {
		return delayTimes;
	}

	public void setDelayTimes(RocketMqDelayEnums delayTimes) {
		this.delayTimes = delayTimes;
	}

	public boolean isCanRepeat() {
		return isCanRepeat;
	}

	public void setCanRepeat(boolean isCanRepeat) {
		this.isCanRepeat = isCanRepeat;
	}

	public boolean isSendJsonBody() {
		return isSendJsonBody;
	}

	public void setSendJsonBody(boolean isSendJsonBody) {
		this.isSendJsonBody = isSendJsonBody;
	}
}