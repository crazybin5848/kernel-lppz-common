package com.lppz.canal.exception;

public class SendMsgException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4064713726802058438L;

	public SendMsgException() {
	}

	public SendMsgException(String msg) {
		super(msg);
	}

	public SendMsgException(String msg, Throwable e) {
		super(msg, e);
	}

	public SendMsgException(Throwable throwable) {
		super(throwable);
	}

	protected SendMsgException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}
}
