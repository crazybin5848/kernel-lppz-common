

/*
 * [y] hybris Core+ Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */

package com.lppz.dubbo.oms.pojo;

import java.io.Serializable;
/**
* 
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
public class StockSynStopQuery implements Serializable
{

	public final static long serialVersionUID = 1445201131L;

	private String orgnization;

	private String baseStore;

	private String orgnizationName;

	private String baseStoreName;

	private String productId;

	private String productCode;

	private String productName;

	private String stopTime;

	private String recoveryTime;

	
	public StockSynStopQuery(){}

	public String getId()
	{
		return this.productId;
	}
						
			

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrgnization()
	{
		return orgnization;
	}

	/**
	* sets 
	*
	*/
	public void setOrgnization(String orgnization)
	{
		this.orgnization = orgnization;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getBaseStore()
	{
		return baseStore;
	}

	/**
	* sets 
	*
	*/
	public void setBaseStore(String baseStore)
	{
		this.baseStore = baseStore;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrgnizationName()
	{
		return orgnizationName;
	}

	/**
	* sets 
	*
	*/
	public void setOrgnizationName(String orgnizationName)
	{
		this.orgnizationName = orgnizationName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getBaseStoreName()
	{
		return baseStoreName;
	}

	/**
	* sets 
	*
	*/
	public void setBaseStoreName(String baseStoreName)
	{
		this.baseStoreName = baseStoreName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getProductId()
	{
		return productId;
	}

	/**
	* sets 
	*
	*/
	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getProductCode()
	{
		return productCode;
	}

	/**
	* sets 
	*
	*/
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getProductName()
	{
		return productName;
	}

	/**
	* sets 
	*
	*/
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStopTime()
	{
		return stopTime;
	}

	/**
	* sets 
	*
	*/
	public void setStopTime(String stopTime)
	{
		this.stopTime = stopTime;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getRecoveryTime()
	{
		return recoveryTime;
	}

	/**
	* sets 
	*
	*/
	public void setRecoveryTime(String recoveryTime)
	{
		this.recoveryTime = recoveryTime;
	}
}

