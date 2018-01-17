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
 * 平台接口日志类型
 * 主要用于EdbLog表的logType字段赋值
 *
 * @author licheng
 */
public enum CEdbLogIsSuccessType
{
	FAIL("0", "失败"), SUCCESS("1", "成功"),NOTEXCUTE("2", "未执行");

	private String code;
	private String name;

	private CEdbLogIsSuccessType(final String code, final String name)
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
