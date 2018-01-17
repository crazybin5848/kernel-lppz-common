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
 * 订单导入方式枚举类型
 *
 * @author licheng
 */
public enum CImportMethod
{
	/**
	 * 手动.
	 */
	MANUAL("Manual", "手动导入"),
	/**
	 * 临时表到正式表.
	 */
	ORDER("Order", ""),
	/**
	 * 系统.
	 */
	SYSTEM("System", "系统抓单");

	private String code;
	private String name;

	private CImportMethod(final String code, final String name)
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
