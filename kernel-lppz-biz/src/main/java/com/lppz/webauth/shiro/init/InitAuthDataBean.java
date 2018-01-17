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
 *
 *
 */
package com.lppz.webauth.shiro.init;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.lppz.core.inittb.BaseInitTableBean;
import com.lppz.core.inittb.InitDBMySqlUtil;


/**
 *
 */
@Component("initAuthDataBean")
public class InitAuthDataBean extends BaseInitTableBean
{
	@Override
	@Resource(name="dataSource")
	public void setMultidataSource(DataSource multidataSource) {
		this.multidataSource = multidataSource;
	}
	
	@Override
	protected void build()
	{
		super.DB_TYPE = "mysql";
		super.initDbsqlUtil = InitDBMySqlUtil.getInstance();
		super.resourceName = "auth_data_init_" + DB_TYPE + ".sql";
		super.TBName="authuser";
		super.component="OMSAuth";
	}
}
