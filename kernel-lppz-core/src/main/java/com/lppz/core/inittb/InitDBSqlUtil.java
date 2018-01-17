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
package com.lppz.core.inittb;

import java.io.InputStream;
import javax.sql.DataSource;

/**
 *
 */
public interface InitDBSqlUtil
{
	public static String[] JDBC_METADATA_TABLE_TYPES = {"TABLE"};
	public void executeSchemaResource(DataSource dataSource, String resourceName, InputStream inputStream,String component,String charactorSet);
	public boolean isTablePresent(DataSource dataSource,String tableName);
}
