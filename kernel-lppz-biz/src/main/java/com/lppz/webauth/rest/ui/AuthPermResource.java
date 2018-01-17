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
package com.lppz.webauth.rest.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.lppz.oms.api.dto.EasyUIResult;
import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.service.auth.AuthPermService;
import com.lppz.webauth.rest.common.EasyUIUtils;
import com.lppz.webauth.rest.common.PageBoundsUtils;

@Controller
@Consumes({"application/json"})
@Produces({"application/json"})
@Path("/ui/authPerm")
public class AuthPermResource
{
	private static final Logger logger = LoggerFactory.getLogger(AuthPermResource.class);

	@Autowired
	private AuthPermService authPermService;

	@GET
	public EasyUIResult findAllPerms(@Context final UriInfo uriInfo, 
			@QueryParam("permcode") final String permcode,
			@QueryParam("menuname") final String menuname)
	{
		logger.debug("find All Roles start");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permcode", StringUtils.isEmpty(permcode) ? null : permcode);
		params.put("menuname", StringUtils.isEmpty(menuname) ? null : menuname);
		

		try {
			PageList<Map> authRole = (PageList<Map>) authPermService.findPaged(params,PageBoundsUtils.createPageBounds(uriInfo));
			if (authRole != null) {
				return EasyUIUtils.createResult(authRole.getPaginator().getTotalCount(), true, null, authRole);
			}
		} catch (Exception e) {
			logger.error("获取所有权限异常",e);
		}
		return EasyUIUtils.getEasyUIResult(0, false);
	}

	@GET
	@Path("/getPermissionsByPermType")
	public JSONArray getRole()
	{
		final JSONArray result = new JSONArray();
		try {
			final List<PermissionDto> permList = authPermService.getPermissionsByPermType("menu");
			if (permList == null || permList.size() == 0)
			{
				return null;
			}
			for (final PermissionDto item : permList)
			{
				final JSONObject obj = new JSONObject();
				obj.put("id", item.getPermcode());
				obj.put("text", item.getPermcode() + "-" + item.getMenuname());
				result.add(obj);
			}
		} catch (Exception e) {
			logger.error("根据权限类型获取权限异常", e);
		}
		return result;
	}
	
	@POST
	@Path("/add")
	public String addRole(final PermissionDto perm)
	{
		String retStr = "";
		if (authPermService.getByPermCode(perm.getPermcode()) != null)
		{
			retStr = "权限代码已存在";
		}
		else
		{
			try
			{
				perm.setAuthpermid(perm.getPermcode());
				if ("".equals(perm.getParentcode().trim()))
				{
					perm.setParentcode(null);
				}
				
				if (authPermService.createPerm(perm))
				{
					retStr = "新增成功";
				}
				else
				{
					retStr = null;
				}
			}
			catch (final Exception e)
			{
				logger.error("添加角色异常", e);
				retStr = null;
			}
		}
		return retStr;
	}
	
	@POST
	@Path("/edit")
	public String editRole(final PermissionDto perm)
	{
		String retStr = "";
		try
		{
			perm.setAuthpermid(perm.getPermcode());
			if (authPermService.updatePerm(perm))
			{
				retStr = "修改成功";
			}
			else
			{
				retStr = "修改失败";
			}
		}
		catch (final Exception e)
		{
			logger.error("修改角色异常", e);
			retStr = "修改失败";
		}
		return retStr;
	}
	
	@POST
	@Path("/delete")
	public EasyUIResult deletePerm(final PermissionDto perm)
	{
		try {
			authPermService.deletePerm(perm);
		} catch (Exception e) {
			logger.error("删除权限异常",e);
		}
		return EasyUIUtils.getEasyUIResult(1, true);
	}
	
	@GET
	@Path("/getByPermCode/{permcode}")
	public PermissionDto getByPermCode(@PathParam("permcode") final String permcode)
	{
		try {
			return authPermService.getByPermCode(permcode);
		} catch (Exception e) {
			logger.error("根据权限码获取权限异常", e);
		}
		return null;
	}
	
	@POST
	@Path("/getNewPermCode")
	public PermissionDto getNewPermCode(final PermissionDto perm)
	{
		try {
			return authPermService.getMaxPermindex(perm);
		} catch (Exception e) {
			logger.error("获取权限码异常", e);
		}
		
		return null;
	}
	
}
