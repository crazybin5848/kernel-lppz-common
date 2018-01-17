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

import java.util.Collection;
import com.lppz.oms.api.dto.EasyUIResult;


/**
 *
 */
public class EasyUIUtils
{
	public static EasyUIResult createResult(final int total, final boolean isSuccess, final Object basicInfo, final Collection rows)
	{
		final EasyUIResult result = new EasyUIResult(total);
		result.setSuccess(isSuccess);
		if (basicInfo != null)
		{
			result.setInfo(basicInfo);
		}
		if (rows != null)
		{
			result.getRows().addAll(rows);
		}
		return result;
	}

	public static EasyUIResult getEasyUIResult(final int total, final boolean isSuccess)
	{
		final EasyUIResult result = new EasyUIResult(total);
		result.setSuccess(isSuccess);
		return result;
	}

}
