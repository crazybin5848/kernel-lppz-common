package com.lppz.util.rocketmq.exception;
public class RocketMqNoSenderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363457941540687624L;
	
	public RocketMqNoSenderException(Exception e) {
		super(e);
	}
	public RocketMqNoSenderException() {
		super();
	}
	public RocketMqNoSenderException(String msg) {
		super(msg);
	}
}