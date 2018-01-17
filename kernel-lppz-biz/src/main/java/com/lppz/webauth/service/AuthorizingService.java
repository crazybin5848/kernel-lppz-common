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
package com.lppz.webauth.service;

import java.util.List;

import com.lppz.oms.api.dto.PermissionDto;

/**
 *
 */
public interface AuthorizingService
{
	public boolean hasPermission(String perm);
	
	public List<PermissionDto> getPermTree();
}
