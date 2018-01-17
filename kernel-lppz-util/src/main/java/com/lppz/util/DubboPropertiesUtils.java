package com.lppz.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 *
 */
public class DubboPropertiesUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(DubboPropertiesUtils.class);

	final static Resource resource = new ClassPathResource("/META-INF/dubbo.properties");

	public static Properties getObject()
	{
		Properties props = null;
		try
		{
			props = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return props;
	}

	public static String getKey(final String keystr)
	{
		String restr = null;
		try
		{
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
