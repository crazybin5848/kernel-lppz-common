package com.lppz.canal.exception;

public class YamlLoadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4064713726802058438L;

	public YamlLoadException() {
	}

	public YamlLoadException(String msg) {
		super(msg);
	}

	public YamlLoadException(String msg, Throwable e) {
		super(msg, e);
	}

	public YamlLoadException(Throwable throwable) {
		super(throwable);
	}

	protected YamlLoadException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}
}
