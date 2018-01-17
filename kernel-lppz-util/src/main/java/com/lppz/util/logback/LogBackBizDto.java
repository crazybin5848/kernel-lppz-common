package com.lppz.util.logback;

import java.io.Serializable;

public class LogBackBizDto<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1059782743178685238L;
	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public LogBackBizEsVo getBizEs() {
		return bizEs;
	}

	public void setBizEs(LogBackBizEsVo bizEs) {
		this.bizEs = bizEs;
	}

	private T t;
	private LogBackBizEsVo bizEs;

}
