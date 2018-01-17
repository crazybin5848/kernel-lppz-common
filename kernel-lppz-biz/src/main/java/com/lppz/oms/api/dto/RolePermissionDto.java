
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

import java.io.Serializable;

/**
 * 
 * Generated automatically
 * 
 * @author: dto-generator, [y] hybris Platform
 */
public class RolePermissionDto implements Serializable {

	public final static long serialVersionUID = -531789353L;

	private String authrolepermrelid;

	private String authroleid;

	private String permCode;

	private String creator;

	private String updator;

	private String createtime;

	private String updatetime;

	public RolePermissionDto() {
	}

	public String getAuthrolepermrelid() {
		return authrolepermrelid;
	}

	public void setAuthrolepermrelid(String authrolepermrelid) {
		this.authrolepermrelid = authrolepermrelid;
	}

	public String getAuthroleid() {
		return authroleid;
	}

	public void setAuthroleid(String authroleid) {
		this.authroleid = authroleid;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
