package com.lppz.util.exception;

public class DiamondNodeNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 99963630286505667L;
	
	public DiamondNodeNotExistException() {
		super();
	}
	
	public DiamondNodeNotExistException(String msg){
		super(msg);
	}
	
	public DiamondNodeNotExistException(Throwable throwable){
		super(throwable);
	}
	
	public DiamondNodeNotExistException(String msg, Throwable throwable){
		super(msg, throwable);
	}
	

}
