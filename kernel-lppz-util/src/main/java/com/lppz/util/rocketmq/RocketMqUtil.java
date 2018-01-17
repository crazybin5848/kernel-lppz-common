package com.lppz.util.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.lppz.constant.RedisConstants;
import com.lppz.util.kryo.KryoUtil;

public class RocketMqUtil {
	public static final String MSGCLASSTYPE="msgclasstype";
	public static final String IDEMPOTENCY="idempotency";
	public static final String MSGJSONBODY="msgjsonbody";
	public static final String MSGRETRYCOUNT="msgretrycount";
	public static final String ISCANREPEAT = "isCanRepeat";
	public static final String ROCKETMQPRODUCERTYPE = "RocketMqType";
	private static final Logger logger = LoggerFactory
			.getLogger(RocketMqUtil.class);
	public static String getMsgBodyString(MessageExt msg) {
	try {
//		String msgJSON=msg.getProperty(MSGJSONBODY);
//		if(msgJSON!=null)
//			return msgJSON;
		byte[] body=msg.getBody();
		return new String(body,"UTF-8");
	} catch (UnsupportedEncodingException e) {
		logger.error(e.getMessage(),e);
	}
	return null;
}
	
	public static String getMsgClassString(MessageExt msg) {
		String msgJSON=msg.getProperty(MSGCLASSTYPE);
		if(msgJSON!=null){
			return msgJSON;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getMsgBody(MessageExt msg) {
		byte[] body=msg.getBody();
		try {
			Class<T> clazz=(Class<T>) Class.forName(msg.getProperty(MSGCLASSTYPE));
			return KryoUtil.kyroDeSeriLize(body,clazz);
		} catch (Exception e) {
			logger.error(e.getMessage()+" key="+msg.getKeys() + " topic="+msg.getTopic(),e);
		}
		return null;
	}
	
	public static Message buildMsg(MessageExt msg,int level,String topic,String tag) {
		if(msg==null)
			return null;
		Message msgext=new Message(topic==null?msg.getTopic():topic,tag==null?msg.getTags():tag,msg.getKeys(),msg.getBody());
		msgext.setDelayTimeLevel(level);
		msgext.putUserProperty(RocketMqUtil.MSGCLASSTYPE, msg.getProperty(RocketMqUtil.MSGCLASSTYPE));
		msgext.putUserProperty(RocketMqUtil.IDEMPOTENCY,Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()),36));
		int currentCount=msg.getProperty(RocketMqUtil.MSGRETRYCOUNT)==null?0:Integer.parseInt(msg.getProperty(RocketMqUtil.MSGRETRYCOUNT));
		msgext.putUserProperty(RocketMqUtil.MSGRETRYCOUNT,String.valueOf(currentCount+1));
		msgext.putUserProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE, msg.getProperty(RocketMqUtil.ROCKETMQPRODUCERTYPE));
		msgext.putUserProperty(RocketMqUtil.ISCANREPEAT, msg.getProperty(RocketMqUtil.ISCANREPEAT));
		String jsonMsgBody = msg.getProperty(RocketMqUtil.MSGJSONBODY);
		if(StringUtils.isNotBlank(jsonMsgBody)){
			msgext.putUserProperty(RocketMqUtil.MSGJSONBODY, jsonMsgBody);
		}
//		try {
//			msgext.putUserProperty(RocketMqUtil.MSGJSONBODY, JSON.toJSONString(KryoUtil.kyroDeSeriLize(msg.getBody(), Class.forName(msg.getProperty(RocketMqUtil.MSGCLASSTYPE)))));
//		} catch (Exception e) {
//			logger.error(e.getMessage(),e);
//		}
		return msgext;
	}
	
	public static int getExpireSeconds(MessageExt msg) {
		int second = 10;
		String expireSeconds = msg.getProperty(RedisConstants.EXPIRE_SECOND_KEY);
		if(StringUtils.isBlank(expireSeconds)){
			return second;
		}
		try {
			second = Integer.valueOf(expireSeconds);
		} catch (Exception e) {
			logger.error("获取系统锁定时间异常，默认锁定 " + second + "s",e);
		}
		return second;
	}
}
