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
package com.lppz.webauth.rest.common;

import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 *
 */
public class AccJSONTools
{
	private static final Logger LOG = LoggerFactory.getLogger(AccJSONTools.class);

	public static String toJSONString(final String reqstr) throws Exception
	{
		LOG.info(">>> decoder : " + reqstr);
		// reqstr = reqstr.substring(5);
		// LOG.info(reqstr);
		// return URLDecoder.decode(reqstr.replace("%25", "%"), "UTF-8").replace("\\", "").replace("\"{",
		// "{").replace("}\"", "}");
		return URLDecoder.decode(reqstr, "UTF-8");
	}

	public static JSONObject toJSONObject(final String reqstr) throws Exception
	{
		final String tmpstr = toJSONString(reqstr);
		final JSONObject jobject = JSON.parseObject(tmpstr).getJSONObject("data");
		LOG.info(">>> JSONString : " + jobject.toJSONString());
		return jobject;
	}

}
