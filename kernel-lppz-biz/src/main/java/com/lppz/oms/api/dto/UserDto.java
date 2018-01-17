
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

public class UserDto implements Serializable {

	public final static long serialVersionUID = -620057866L;

	private String authuserid;

	private String userid;

	private String username;

	private String userpwd;

	private String employeeno;
	
	private String parentuserid;

	private String email;

	private String status;

	private String creator;

	private String createtime;

	private String updator;

	private String updatetime;

	private String roleid;

	private String rolename;

	private String createtimestr;

	private String oldpwd;

	public UserDto() {
	}

	public String getId() {
		return this.authuserid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getAuthuserid() {
		return authuserid;
	}

	/**
	 * sets
	 *
	 */
	public void setAuthuserid(String authuserid) {
		this.authuserid = authuserid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * sets
	 *
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * sets
	 *
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getUserpwd() {
		return userpwd;
	}

	/**
	 * sets
	 *
	 */
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getEmployeeno() {
		return employeeno;
	}

	/**
	 * sets
	 *
	 */
	public void setEmployeeno(String employeeno) {
		this.employeeno = employeeno;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * sets
	 *
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * sets
	 *
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * sets
	 *
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getCreatetime() {
		return createtime;
	}

	/**
	 * sets
	 *
	 */
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getUpdator() {
		return updator;
	}

	/**
	 * sets
	 *
	 */
	public void setUpdator(String updator) {
		this.updator = updator;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * sets
	 *
	 */
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getRoleid() {
		return roleid;
	}

	/**
	 * sets
	 *
	 */
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getRolename() {
		return rolename;
	}

	/**
	 * sets
	 *
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getCreatetimestr() {
		return createtimestr;
	}

	/**
	 * sets
	 *
	 */
	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getOldpwd() {
		return oldpwd;
	}

	/**
	 * sets
	 *
	 */
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	@Override
	public String toString() {
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	public String getParentuserid() {
		return parentuserid;
	}

	public void setParentuserid(String parentuserid) {
		this.parentuserid = parentuserid;
	}
	

}
