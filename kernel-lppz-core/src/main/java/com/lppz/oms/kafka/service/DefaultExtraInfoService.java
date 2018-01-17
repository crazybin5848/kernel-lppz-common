/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.service;

import com.lppz.util.BizLogUtil;


/**
 * 默认的HostService实现类
 * 
 * 对于通过Hedwig暴露接口服务的应用可以直接使用
 *
 * @version 1.0
 */
public class DefaultExtraInfoService implements ExtraInfoService {

	/**
	 * 获取Client端IP
	 *
	 * @return String
	 */	
	@Override
	public String getRemoteAddress() {
		return BizLogUtil.getRemoteAddress();
	}

	/**
	 * 获取Server端IP
	 *
	 * @return String
	 */	
	@Override
	public String getLocalAddress() {
		return BizLogUtil.getLocalAddress();
	}

	/**
	 * 获取ref_code，如果返回null或""
	 * 默认取@refCodeIndex和@refCodeField的值
	 *
	 * @return String
	 */	
	@Override
	public String getRefCode() {
		return null;
	}

}
