/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.core.datasource.DynamicDataSourceHolder;



/**
 * @author zoubin
 * 
 */
public class BaseDataSourceAspect
{
	private static final Logger logger = LoggerFactory.getLogger(BaseDataSourceAspect.class);

	protected void after()
	{
		DynamicDataSourceHolder.remove();
		DynamicDataSourceHolder.removeIntenal();
		logger.debug("remove datasource after done");
	}

}
