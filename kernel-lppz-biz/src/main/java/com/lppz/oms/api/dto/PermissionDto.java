
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

import javax.xml.bind.annotation.XmlID;

public class PermissionDto implements Serializable {

	public final static long serialVersionUID = -1675330391L;

	private String authpermid;

	private String permcode;

	private String menuname;

	private String menuurl;

	private String parentcode;

	private String permtype;

	private String permindex;

	public PermissionDto() {
	}

	public String getId() {
		return this.authpermid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getAuthpermid() {
		return authpermid;
	}

	/**
	 * sets
	 *
	 */
	public void setAuthpermid(String authpermid) {
		this.authpermid = authpermid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getPermcode() {
		return permcode;
	}

	/**
	 * sets
	 *
	 */
	public void setPermcode(String permcode) {
		this.permcode = permcode;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getMenuname() {
		return menuname;
	}

	/**
	 * sets
	 *
	 */
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getMenuurl() {
		return menuurl;
	}

	/**
	 * sets
	 *
	 */
	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getParentcode() {
		return parentcode;
	}

	/**
	 * sets
	 *
	 */
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getPermtype() {
		return permtype;
	}

	/**
	 * sets
	 *
	 */
	public void setPermtype(String permtype) {
		this.permtype = permtype;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getPermindex() {
		return permindex;
	}

	/**
	 * sets
	 *
	 */
	public void setPermindex(String permindex) {
		this.permindex = permindex;
	}

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

}
