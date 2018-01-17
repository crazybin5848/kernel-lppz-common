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
package com.lppz.core.util;

import java.util.List;

import com.lppz.core.annotation.DalDsRW;
import com.lppz.core.annotation.HashKeyListDs;
import com.lppz.core.annotation.HashKeyListInserDs;



/**
 *
 */
public interface IDalCommonCallInterFace
{
	@DalDsRW
	@HashKeyListDs
	public Object batchInsertOrUpdate(@HashKeyListInserDs List<String> routeKeys, Object... args) throws Exception;

}
