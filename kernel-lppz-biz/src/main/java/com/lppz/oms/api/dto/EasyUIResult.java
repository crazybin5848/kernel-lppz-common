

/*
 * [y] hybris Core+ Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */

package com.lppz.oms.api.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import java.util.ArrayList;
import java.lang.Object;
import java.util.List;
import java.io.Serializable;
public class EasyUIResult implements Serializable
{

	public final static long serialVersionUID = -1723675601L;

	private int total;

	private boolean success;

	private String msg;

	private Object info;

	private List<Object> rows = new ArrayList();

	
	public EasyUIResult(){}

	public EasyUIResult(final int total)
	{	
		this.total = total;
	}
	
	public String getId()
	{
		return "";
	}



	/**
	* gets 
	*
	* @returns int
	*/
	public int getTotal()
	{
		return total;
	}

	/**
	* sets 
	*
	*/
	public void setTotal(int total)
	{
		this.total = total;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getSuccess()
	{
		return success;
	}

	/**
	* sets 
	*
	*/
	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getMsg()
	{
		return msg;
	}

	/**
	* sets 
	*
	*/
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	* gets 
	*
	* @returns Object
	*/
	public Object getInfo()
	{
		return info;
	}

	/**
	* sets 
	*
	*/
	public void setInfo(Object info)
	{
		this.info = info;
	}

	/**
	* gets 
	*
	* @returns List<Object>
	*/
	public List<Object> getRows()
	{
		return rows;
	}

	/**
	* sets 
	*
	*/
	public void setRows(List<Object> rows)
	{
		this.rows = rows;
	}


	@Override
	public String toString()
	{
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

}

