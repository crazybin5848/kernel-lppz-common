package com.lppz.dubbo.edb.model;

import java.util.List;

import com.lppz.oms.exception.BaseException;

public class EdbRestModel {
	
	private BaseException baseException;
	
	private List list;

	/**
	 * @return the baseException
	 */
	public BaseException getBaseException() {
		return baseException;
	}

	/**
	 * @param baseException the baseException to set
	 */
	public void setBaseException(BaseException baseException) {
		this.baseException = baseException;
	}

	/**
	 * @return the list
	 */
	public List getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List list) {
		this.list = list;
	}
}
