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
package com.lppz.core.exceptions;



/**
 *
 */
public class DalReturnTypeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DalReturnTypeException()
	{
	}

	public DalReturnTypeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public DalReturnTypeException(final String message)
	{
		super(message);
	}

	public DalReturnTypeException(final Throwable cause)
	{
		super(cause);
	}
}
