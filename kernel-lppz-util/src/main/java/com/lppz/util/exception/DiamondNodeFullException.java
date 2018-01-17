package com.lppz.util.exception;

public class DiamondNodeFullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 99963630286505667L;
	
	public DiamondNodeFullException() {
		super();
	}
	
	public DiamondNodeFullException(String msg){
		super(msg);
	}
	
	public DiamondNodeFullException(Throwable throwable){
		super(throwable);
	}
	
	public DiamondNodeFullException(String msg, Throwable throwable){
		super(msg, throwable);
	}
	

}
