package com.lppz.dubbo.oms.pojo;

import java.io.Serializable;

public class Delivery2Edb implements Serializable
{
	public final static long serialVersionUID = 2042789798L;

	private String outerStoreCode;

	private String outerOrderId;

	private String expressCode;

	private String trackingId;

	private String shipDate;

	private String orderId;

	private String storeCode;

	
	public Delivery2Edb(){}

	public Delivery2Edb(final String outerStoreCode, final String outerOrderId, final String expressCode, final String trackingId, final String shipDate){
		this.outerStoreCode = outerStoreCode;
		this.outerOrderId = outerOrderId;
		this.expressCode = expressCode;
		this.trackingId = trackingId;
		this.shipDate = shipDate;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOuterStoreCode()
	{
		return outerStoreCode;
	}

	/**
	* sets 
	*
	*/
	public void setOuterStoreCode(String outerStoreCode)
	{
		this.outerStoreCode = outerStoreCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOuterOrderId()
	{
		return outerOrderId;
	}

	/**
	* sets 
	*
	*/
	public void setOuterOrderId(String outerOrderId)
	{
		this.outerOrderId = outerOrderId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getExpressCode()
	{
		return expressCode;
	}

	/**
	* sets 
	*
	*/
	public void setExpressCode(String expressCode)
	{
		this.expressCode = expressCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTrackingId()
	{
		return trackingId;
	}

	/**
	* sets 
	*
	*/
	public void setTrackingId(String trackingId)
	{
		this.trackingId = trackingId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShipDate()
	{
		return shipDate;
	}

	/**
	* sets 
	*
	*/
	public void setShipDate(String shipDate)
	{
		this.shipDate = shipDate;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderId()
	{
		return orderId;
	}

	/**
	* sets 
	*
	*/
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStoreCode()
	{
		return storeCode;
	}

	/**
	* sets 
	*
	*/
	public void setStoreCode(String storeCode)
	{
		this.storeCode = storeCode;
	}

	@Override
	public String toString() {
		return "Delivery2Edb [outerStoreCode=" + outerStoreCode
				+ ", outerOrderId=" + outerOrderId + ", expressCode="
				+ expressCode + ", trackingId=" + trackingId + ", shipDate="
				+ shipDate + ", orderId=" + orderId + ", storeCode="
				+ storeCode + "]";
	}

}
