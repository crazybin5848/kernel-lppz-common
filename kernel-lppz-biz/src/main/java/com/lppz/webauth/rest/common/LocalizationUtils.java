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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 */
public class LocalizationUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(LocalizationUtils.class);
	private static final Properties properties = new Properties();
	static
	{
		try
		{
			properties.load(new InputStreamReader(LocalizationUtils.class.getResourceAsStream("/localization.properties"), "UTF-8"));
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		}
	}

	public static String getMessage(final String key)
	{
		final String value = properties.getProperty(key);
		if (StringUtils.isBlank(value))
		{
			return key;
		}
		return value;
	}

	public static String getMessage(final Object key)
	{
		return getMessage(key.toString());
	}
}
