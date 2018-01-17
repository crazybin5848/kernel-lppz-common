/*
 * [y] hybris Core+ Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package com.lppz.dubbo.edb.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lppz.dubbo.edb.enums.CDealType;


/**
 *
 * Generated automatically
 *
 * @author: dto-generator, [y] hybris Platform
 */
public class EdbLog implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4778029870972906891L;

	private int id;

	private String method;

	private String request;

	private String response;

	private int isDeal;

	private Date requestTime;

	private Date modifiedTime;

	private String dealOper;

	private String operType;
	
	//日志类型：0edb，1有赞
	private String logType;
	//外部单号
	private String outOrderId;
	//物流商编码
	private String logisticCode;
	//快递单号
	private String trackingId;
	//发货时间
	private String deliveryTime;
	//请求是否成功
	private String isSuccess;
	//店铺外部编码
	private String outStoreCode;
	//错误信息
	private String msg;
	
	private String storeCode;
	
	public EdbLog()
	{
	}

	public static EdbLog buildEdbLog(final Map<String, Object> result)
	{
		final EdbLog edbLog = new EdbLog();
		edbLog.setId((Integer) result.get("id"));
		edbLog.setIsDeal((Integer) result.get("isdeal"));
		edbLog.setMethod((String) result.get("method"));
		edbLog.setRequest((String) result.get("request"));
		edbLog.setRequestTime((Date) result.get("requesttime"));
		edbLog.setResponse((String) result.get("response"));
		edbLog.setModifiedTime((Date) result.get("modifiedtime"));
		edbLog.setDealOper((String) result.get("dealoper"));
		final String operType = (String) result.get("opertype");
		edbLog.setStoreCode(result.get("storecode")==null?"":(String) result.get("storecode"));
		if (StringUtils.isNotBlank(operType))
		{
			edbLog.setOperType(CDealType.valueOf(operType).getName());
		}
		return edbLog;
	}

	public static List<EdbLog> buildEdbLogs(final List<Map<String, Object>> results)
	{
		final List<EdbLog> edbLogs = new ArrayList<EdbLog>();
		if (results != null)
		{
			for (final Map<String, Object> result : results)
			{
				edbLogs.add(buildEdbLog(result));
			}
		}
		return edbLogs;
	}
	
	public int getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final int id)
	{
		this.id = id;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getMethod()
	{
		return method;
	}

	/**
	 * sets
	 *
	 */
	public void setMethod(final String method)
	{
		this.method = method;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getRequest()
	{
		return request;
	}

	/**
	 * sets
	 *
	 */
	public void setRequest(final String request)
	{
		this.request = request;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * sets
	 *
	 */
	public void setResponse(final String response)
	{
		this.response = response;
	}

	/**
	 * gets
	 *
	 * @returns Boolean
	 */
	public int getIsDeal()
	{
		return isDeal;
	}

	/**
	 * sets
	 *
	 */
	public void setIsDeal(final int isDeal)
	{
		this.isDeal = isDeal;
	}

	/**
	 * gets
	 *
	 * @returns Date
	 */
	public Date getRequestTime()
	{
		return requestTime;
	}

	/**
	 * sets
	 *
	 */
	public void setRequestTime(final Date requestTime)
	{
		this.requestTime = requestTime;
	}

	/**
	 * @return the modifiedTime
	 */
	public Date getModifiedTime()
	{
		return modifiedTime;
	}

	/**
	 * @param modifiedTime the modifiedTime to set
	 */
	public void setModifiedTime(final Date modifiedTime)
	{
		this.modifiedTime = modifiedTime;
	}

	/**
	 * @return the dealOper
	 */
	public String getDealOper()
	{
		return dealOper;
	}

	/**
	 * @param dealOper the dealOper to set
	 */
	public void setDealOper(final String dealOper)
	{
		this.dealOper = dealOper;
	}

	/**
	 * @return the operType
	 */
	public String getOperType()
	{
		return operType;
	}

	/**
	 * @param operType the operType to set
	 */
	public void setOperType(final String operType)
	{
		this.operType = operType;
	}
	
	public String getOperTypeValue(){
		if (StringUtils.isNotBlank(operType))
		{
			return CDealType.valueOf(operType).getName();
		}
		return null;
	}
	
	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getLogisticCode() {
		return logisticCode;
	}

	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getOutStoreCode() {
		return outStoreCode;
	}

	public void setOutStoreCode(String outStoreCode) {
		this.outStoreCode = outStoreCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	@Override
	public EdbLog clone() {
		EdbLog edbLog = new EdbLog();
		setDealOper(this.dealOper);
		setDeliveryTime(this.deliveryTime);
		setIsDeal(this.isDeal);
		setIsSuccess(this.isSuccess);
		setLogisticCode(this.logisticCode);
		setLogType(this.logType);
		setMethod(this.method);
		setModifiedTime(this.modifiedTime);
		setMsg(this.msg);
		setOperType(this.operType);
		setOutOrderId(this.outOrderId);
		setOutStoreCode(this.outStoreCode);
		setRequest(this.request);
		setResponse(this.response);
		setStoreCode(this.storeCode);
		setTrackingId(this.trackingId);
		return edbLog;
	}
}
