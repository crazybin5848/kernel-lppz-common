/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.aspect.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lppz.core.annotation.DalDsRW;
import com.lppz.core.annotation.DalDsRW.DalDB;
import com.lppz.core.aspect.BaseDataSourceAspect;
import com.lppz.core.aspect.IDalDataSourceAspect;
import com.lppz.core.datasource.DynamicDataSourceHolder;
import com.lppz.core.util.DalUtil;


/**
 * @author zoubin
 * 
 */
@Component("dalDataSourceRWAspect")
public class DalDataSourceRWAspect extends BaseDataSourceAspect implements IDalDataSourceAspect
{
	private static final Logger logger = LoggerFactory.getLogger(DalDataSourceRWAspect.class);


	/**
	 */
	@Override
	public void setInterlKey(final JoinPoint point)
	{
		try
		{
			final DalDsRW datasource = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(DalDsRW.class);
			if (datasource == null)
			{
				DynamicDataSourceHolder.setIntenal(DalUtil.generateRW(DalDB.Write));
				return;
			}
			DynamicDataSourceHolder.setIntenal(DalUtil.generateRW(datasource.value()));
			logger.debug("set datasource before do something");
		}
		catch (final Exception e)
		{
			logger.error("{}", e);
		}
	}




}
