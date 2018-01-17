package com.lppz.util.rocketmq.exception;
@SuppressWarnings("rawtypes")
public class RocketMqBizException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363457941540687624L;
	
	private RocketMqBizResult bizResult;
	
	public RocketMqBizException(Exception e) {
		super(e);
	}
	public RocketMqBizException() {
		super();
	}
	
	public static <R> RocketMqBizException build(Exception ex,R r,String topic,String tag,int level){
		RocketMqBizException e=new RocketMqBizException(ex);
		RocketMqBizResult<R> bizResult=new RocketMqBizResult<R>();
		bizResult.setR(r);
		bizResult.setLevel(level);
		bizResult.setTopic(topic);
		bizResult.setTag(tag);
		e.setBizResult(bizResult);
		return e;
	}
	
	public static <R> RocketMqBizException build(Exception ex,R r,int level){
		return build(ex, r, null,null, level);
	}
	public static <R> RocketMqBizException build(Exception ex,R r){
		return build(ex, r, null,null,0);
	}
	
	public RocketMqBizResult getBizResult() {
		return bizResult;
	}
	public void setBizResult(RocketMqBizResult bizResult) {
		this.bizResult = bizResult;
	}
}
