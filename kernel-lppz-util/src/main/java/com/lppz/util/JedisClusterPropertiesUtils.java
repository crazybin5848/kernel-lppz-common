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
package com.lppz.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 *
 */
public class JedisClusterPropertiesUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(JedisClusterPropertiesUtils.class);

	public static String getKey(final String keystr,String...home)
	{
		String restr = null;
		try
		{
			Resource resource=null;
			if(home==null||home.length==0){
				resource = new ClassPathResource("jedis.properties");
			}
			else{
				resource= new FileSystemResource(home[0]+File.separator+"jedis.properties");
			}
			final Properties props = PropertiesLoaderUtils.loadProperties(resource);
			restr = props.getProperty(keystr);
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return restr;
	}
}
