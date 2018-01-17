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
 *
 *
 */
package com.lppz.webauth.shiro;

import java.util.List;

import org.apache.shiro.authz.SimpleAuthorizationInfo;

import com.lppz.oms.api.dto.PermissionDto;

/**
 *
 */
public class OmsAuthorizationInfo extends SimpleAuthorizationInfo
{
	protected List<PermissionDto> permissions;

	/**
	 * @return the permissions
	 */
	public List<PermissionDto> getPermissions()
	{
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<PermissionDto> permissions)
	{
		this.permissions = permissions;
	}
	
}
