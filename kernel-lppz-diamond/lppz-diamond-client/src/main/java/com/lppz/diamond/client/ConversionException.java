package com.lppz.diamond.client;

public class ConversionException extends ConfigurationRuntimeException {

	private static final long serialVersionUID = -5764816812711964004L;

	public ConversionException() {
		super();
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}
}