package com.lpp.util.test.disruptor.yulei;

import java.io.Serializable;

public class StringEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2154451423819541774L;

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
