package com.lppz.elasticsearch.exception;

public class EsClientConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7034678886667919446L;

	public EsClientConnectionException(String string, Exception e) {
		super(string, e);
	}

}
