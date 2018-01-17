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
 * 订单临时表导入和创建订单错误信息类型
 * 主要用于CatchOrderErrMsg对象的errorType字段赋值
 *
 * @author licheng
 */
public enum CErrorType
{
	PARSECODE("ParseCode", "创建订单转码"), SHOPEXCEPTION("ShopException", "店铺异常"), PRODECTEXCEPTION("ProdectException", "商家编码异常"), AREAEXCEPTION(
			"AreaException", "地址异常"), CREATEVALIDATEEXCEPTION("CreateValidateException", "创建订单异常"), LOSEORDERID("LoseOrderId",
					"订单号缺失"), REPEATORDERID("RepeatOrderId", "订单号重复");

	private String code;
	private String name;

	private CErrorType(final String code, final String name)
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
