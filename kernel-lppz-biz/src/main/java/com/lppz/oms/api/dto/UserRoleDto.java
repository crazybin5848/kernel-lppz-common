
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
public class UserRoleDto implements Serializable {

	public final static long serialVersionUID = -1230619887L;

	private String authuserrolerelid;

	private String authuserid;

	private String authroleid;

	private String creator;

	private String updator;

	private String createtime;

	private String updatetime;

	public String getAuthuserrolerelid() {
		return authuserrolerelid;
	}

	public void setAuthuserrolerelid(String authuserrolerelid) {
		this.authuserrolerelid = authuserrolerelid;
	}

	public String getAuthuserid() {
		return authuserid;
	}

	public void setAuthuserid(String authuserid) {
		this.authuserid = authuserid;
	}

	public String getAuthroleid() {
		return authroleid;
	}

	public void setAuthroleid(String authroleid) {
		this.authroleid = authroleid;
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
