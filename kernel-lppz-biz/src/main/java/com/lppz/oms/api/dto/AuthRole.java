
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

package com.lppz.oms.api.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;

/**
 * 
 * Generated automatically
 * 
 * @author: dto-generator, [y] hybris Platform
 */

public class AuthRole implements Serializable {

	public final static long serialVersionUID = -1359399423L;

	private String authRoleId;

	private String roleCode;

	private String roleName;

	private String roleLevel;

	private String roleDesc;

	private String enableFlg;

	private String creator;

	private String createTime;

	private String updator;

	private String updateTime;

	private String superRoleCode;

	private String permCodes;

	public String getAuthRoleId() {
		return authRoleId;
	}

	public void setAuthRoleId(String authRoleId) {
		this.authRoleId = authRoleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getEnableFlg() {
		return enableFlg;
	}

	public void setEnableFlg(String enableFlg) {
		this.enableFlg = enableFlg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getSuperRoleCode() {
		return superRoleCode;
	}

	public void setSuperRoleCode(String superRoleCode) {
		this.superRoleCode = superRoleCode;
	}

	public String getPermCodes() {
		return permCodes;
	}

	public void setPermCodes(String permCodes) {
		this.permCodes = permCodes;
	}

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

}
