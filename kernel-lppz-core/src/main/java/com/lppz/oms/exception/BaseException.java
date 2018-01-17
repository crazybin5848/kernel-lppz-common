package com.lppz.oms.exception;

import java.util.List;
import java.util.Map;

public class BaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String,Object> params ;
	
	private String desc;
	
	private List<BaseException> baseExceptions;
	
	private Boolean isMain =false;
	
	private Boolean isThrow =false;
	
	public BaseException(){
		
	}

	public BaseException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public BaseException(final String message)
	{
		super(message);
	}

	public BaseException(final Throwable cause)
	{
		super(cause);
	}
	public Map<String, Object> getParams() {
		return params;
	}

	public String getDesc() {
		return desc;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<BaseException> getBaseExceptions() {
		return baseExceptions;
	}

	public void setBaseExceptions(List<BaseException> baseExceptions) {
		this.baseExceptions = baseExceptions;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Boolean getIsThrow() {
		return isThrow;
	}

	public void setIsThrow(Boolean isThrow) {
		this.isThrow = isThrow;
	}
}
