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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 *
 */
public class LppzSearchConstants
{
	public static final String SORT_DIRECTION = "order";
	public static final String SORT_COLUMN = "sort";
	public static final String PS = "rows";
	public static final String PN = "page";
	public static final String PREFIX = "_";
	public static final char PREFIX_CHAR = '_';
	private static final Set<String> RESERVED_NAMES = new HashSet();

	static
	{
		RESERVED_NAMES.add("order");
		RESERVED_NAMES.add("sort");
		RESERVED_NAMES.add("page");
		RESERVED_NAMES.add("rows");
	}

	public static Set<String> getReservedNames()

	{
		return Collections.unmodifiableSet(RESERVED_NAMES);
	}


	private LppzSearchConstants()
	{
	}
}
