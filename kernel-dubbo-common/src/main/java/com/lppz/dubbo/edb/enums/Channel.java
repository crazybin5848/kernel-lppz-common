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

import java.util.HashMap;
import java.util.Map;


/**
 * add by zhaozhen
 * 店铺枚举类
 */

public enum Channel
{
	TAOBAO(1, "淘宝"), TMALL(2, "天猫"), MEITUAN(3, "美团"), NUOMI(4, "糯米网"), HANGOU(5, "汉购网"), WOMAI(6, "我买网"), WUSHANG(7, "武商网"), JUMEI(
			8, "聚美"), YIHAODIAN(9, "1号店"), JINGDONG(10, "京东"), lppz(11, "良品官网"), KOUDAITONG(12, "口袋通"), WEIBO(13, "微博"), ZAODIAN(14,
			"早点"), ZHIFUBAO(15, "支付宝");
	private int id;
	private String name;
	private static Map<Integer, Channel> Channel_maps = new HashMap<Integer, Channel>();
	public static Map<String, String> Channel_name_maps = new HashMap<String, String>();

	static
	{
		for (final Channel o : Channel.values())
		{
			Channel_maps.put(o.getId(), o);
			Channel_name_maps.put(o.name, o.name());
		}
	}

	/**
	 *
	 */
	private Channel(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final int id)
	{
		this.id = id;
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
