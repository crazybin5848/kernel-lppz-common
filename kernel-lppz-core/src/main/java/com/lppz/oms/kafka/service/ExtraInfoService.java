/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.service;

/**
 * 供调用方自主实现业务方法，包括获取Client端IP、Server端IP、refCode
 *
 * @author luoyiqun@yihaodian.com
 * @version 1.0
 */
public interface ExtraInfoService {
	
	/**
	 * 获取Client端IP
	 *
	 * @return String
	 */	
	public String getRemoteAddress();

	/**
	 * 获取Server端IP
	 *
	 * @return String
	 */		
	public String getLocalAddress();
	
	/**
	 * 获取ref_code，如果返回null或""，以@refCodeIndex和@refCodeField的值为准
	 *
	 * @return String
	 */		
	public String getRefCode();
}
