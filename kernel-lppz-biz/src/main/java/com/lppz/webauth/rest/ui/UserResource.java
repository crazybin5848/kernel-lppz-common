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
 */
package com.lppz.webauth.rest.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.lppz.oms.api.dto.AuthRole;
import com.lppz.oms.api.dto.EasyUIResult;
import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.persist.AuthRoleMapper;
import com.lppz.oms.persist.EmployeeMapper;
import com.lppz.oms.service.auth.AuthRoleService;
import com.lppz.util.MD5;
import com.lppz.webauth.model.UserConfig;
import com.lppz.webauth.rest.common.EasyUIUtils;
import com.lppz.webauth.rest.common.PageBoundsUtils;
import com.lppz.webauth.shiro.utils.SessionUtil;

/**
 *
 */
@Controller
@Consumes({ "application/json" })
@Produces({ "application/json" })
@Path("/ui/user")
public class UserResource {
	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	@Resource
	private EmployeeMapper employeeMapper;
	@Autowired
	private AuthRoleService authRoleService;
	@Resource
	private AuthRoleMapper authRoleMapper;
	
	
	public EmployeeMapper getEmployeeMapper() {
		return employeeMapper;
	}

	public void setEmployeeMapper(EmployeeMapper employeeMapper) {
		this.employeeMapper = employeeMapper;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Transactional
	public EasyUIResult getAllUsers(@Context final UriInfo uriInfo, @QueryParam("employeeno") final String employeeno,
			@QueryParam("userName") final String userName, @QueryParam("status") final String status,
			@QueryParam("roleid") final String roleid) {
		final String currentUser = SessionUtil.getCurrentUser().getUserid();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employeeno", StringUtils.isEmpty(employeeno) ? null : employeeno);
		params.put("userName", StringUtils.isEmpty(userName) ? null : userName);
		params.put("status", StringUtils.isEmpty(status) ? null : status);
		params.put("roleid", StringUtils.isEmpty(roleid) ? null : roleid);
		boolean isAdmin = false;
		try {
			List<AuthRole> roles = authRoleMapper.getRoleByUser(currentUser);
			for (AuthRole authRole : roles) {
				if (StringUtils.isNotBlank(authRole.getRoleCode())&&authRole.getRoleCode().equalsIgnoreCase("R01")) {
					isAdmin = true;
					break;
				}
			}
			if (!isAdmin) {
//			params.put("parentUserId", currentUser); // 非管理员只能查到自己建的的用户
			} 
			PageList<Map> employees = (PageList<Map>) employeeMapper.findEmployees(params,PageBoundsUtils.createPageBounds(uriInfo));
			if (employees != null) {
				return EasyUIUtils.createResult(employees.getPaginator().getTotalCount(), true, null, employees);
			}
		} catch (Exception e) {
			logger.error("获取用户异常",e);
		}
		return EasyUIUtils.getEasyUIResult(0, false);
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Path("/status")
	@Transactional
	public EasyUIResult enableUser(final Map employee)
	{
		try {
			employeeMapper.updateStatus(employee);
		} catch (Exception e) {
			logger.error("禁用用户异常",e);
		}
		return EasyUIUtils.getEasyUIResult(1, true);
	}
	
	/**'
	 * 修改用户密码
	 * @param pwd
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "finally", "unchecked" })
	@POST
	@Path("/changePwd")
	public String changePwd(final Map user){

		String retStr = "修改密码成功";
		try {
			user.put("userpwd", MD5.getMD5(String.valueOf( user.get("userpwd"))));
			employeeMapper.changePwd(user);
		} catch (Exception e) {
			logger.error("修改密码异常", e);
			retStr  ="修改密码失败";
		}
		finally {
			return retStr;
		}
	}
	
	@SuppressWarnings({ "finally", "unchecked", "rawtypes" })
	@POST
	@Path("/addUser")
	public String addUser(final Map user)
	{
		String retStr = "添加用户成功";

		try
		{
			final Map empDB = employeeMapper.loadEmployee(user);
			if (null != empDB)
			{
				retStr = "输入的员工号已存在OMS";
				return retStr;
			}
			
			String roleid = (String) user.get("roleid");
			String userid = (String) user.get("userid");
			final String currentUser = SessionUtil.getCurrentUser().getUserid();
			user.put("creator", currentUser);
			user.put("authuserid",userid);
			user.put("createtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			user.put("userpwd", MD5.getMD5(UserConfig.DEFAULTPASSWD));
			user.put("employeeno", user.get("userid"));
			user.put("typecode", "UserData");
			user.put("tenant", "single");
			user.put("id", user.get("tenant")+"|"+user.get("typecode")+"|"+userid);
			
			employeeMapper.addEmployee(user);
			
			if(StringUtils.isNotBlank(roleid)){
				final String authuserrolerelid = userid + "_" + roleid;
				authRoleService.authUser(userid, roleid, currentUser, authuserrolerelid);
			}
		}
		catch (final Exception e)
		{
			logger.error("添加用户异常", e);
			retStr = "添加用户失败";
		}
		finally
		{
			return retStr;
		}
	}
	
	@POST
	@Path("/authUser")
	public boolean authUser(final UserDto user)
	{
		boolean ret = false;

		try
		{
			final String currentUser = SessionUtil.getCurrentUser().getUserid();

			final String authuserrolerelid = user.getUserid() + "_" + user.getRoleid();
			ret = this.authRoleService.authUser(user.getUserid(), user.getRoleid(), currentUser, authuserrolerelid);
		}
		catch (final Exception e)
		{
			logger.error("授权用户异常", e);
			ret = false;
		}
		return ret;
	}
}
