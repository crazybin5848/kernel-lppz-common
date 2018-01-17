/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.dubbo.edb.enums;

/**
 * 临时表创建订单异常处理类型
 * 主要用于标记异常处理类型
 *
 * @author licheng
 */
public enum CDealType
{
	/**
	 * 创建.
	 */
	CREATE("Create", "创建"),
	/**
	 * 作废.
	 */
	CANCEL("Cancel", "作废"),

	/**
	 * 重试.
	 */
	RETRY("retry", "重试");

	private String code;
	private String name;

	private CDealType(final String code, final String name)
	{
		this.code = code;
		this.name = name;
	}


	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}


}
