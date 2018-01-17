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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.esotericsoftware.minlog.Log;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.lppz.oms.api.dto.AuthRole;
import com.lppz.oms.api.dto.EasyUIResult;
import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.RolePermissionDto;
import com.lppz.oms.api.dto.RoleSelect;
import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.persist.AuthRoleMapper;
import com.lppz.oms.service.auth.AuthRoleService;
import com.lppz.webauth.model.TreeNode;
import com.lppz.webauth.rest.common.DateTools;
import com.lppz.webauth.rest.common.EasyUIUtils;
import com.lppz.webauth.rest.common.PageBoundsUtils;


/**
 * wlj
 */

@Controller
@Consumes({"application/json"})
@Produces({"application/json"})
@Path("/ui/authRole")
public class AuthRoleResource
{
	private static final Logger logger = LoggerFactory.getLogger(AuthRoleResource.class);
	
	@Autowired
	private AuthRoleMapper authRoleMapper;
	@Autowired
	private AuthRoleService authRoleService;
	
	@GET
	public EasyUIResult findAllRoles(@Context final UriInfo uriInfo, 
			@QueryParam("rolecode") final String rolecode,
			@QueryParam("rolename") final String rolename, 
			@QueryParam("enableflg") final String enableflg)
	{
		logger.info("find All Roles start");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rolecode", StringUtils.isEmpty(rolecode) ? null : rolecode);
		params.put("rolename", StringUtils.isEmpty(rolename) ? null : rolename);
		params.put("enableflg", StringUtils.isEmpty(enableflg) ? null : enableflg);

		try {
			PageList<Map> authRole = (PageList<Map>) authRoleService.findPaged(params,PageBoundsUtils.createPageBounds(uriInfo));
			if (authRole != null) {
				return EasyUIUtils.createResult(authRole.getPaginator().getTotalCount(), true, null, authRole);
			}
		} catch (Exception e) {
			logger.error("获取所有角色异常", e);
		}

		return EasyUIUtils.getEasyUIResult(0, false);
	}

	@POST
	@Path("/add")
	public String addRole(final AuthRole role)
	{
		String retStr = "";
		try
		{
			if (authRoleService.getByRoleCode(role.getRoleCode()) != null)
			{
				retStr = "角色代码已存在";
			}
			else
			{
				role.setAuthRoleId(role.getRoleCode());// AuthRoleId与rolecode一致
				String currentUserId;
				final UserDto user = authRoleService.getCurrentUser();
				if (null == user)
				{
					retStr = "获取当前用户失败";
					return retStr;
				}
				
				currentUserId = user.getUserid();
				role.setCreator(currentUserId);// 创建人为当前用户
				role.setCreateTime(DateTools.formatDate(new Date()));
				role.setUpdator(currentUserId);// 更新人为当前用户
				role.setUpdateTime(DateTools.formatDate(new Date()));
				
				final List<RolePermissionDto> rolePermList = loadRolePerms(role);
				
				if ("".equals(role.getSuperRoleCode().trim()))
				{
					role.setSuperRoleCode(null);
				}
				if (authRoleService.create(role, rolePermList))
				{
					retStr = "新增成功";
				}
				else
				{
					retStr = null;
				}
			}
		}
		catch (final Exception e)
		{
			logger.error("添加角色异常", e);
			retStr = null;
		}
		return retStr;
	}

	@GET
	@Path("/getByRoleCode/{roleCode}")
	public AuthRole getByRoleCode(@PathParam("roleCode") final String roleCode)
	{
		try {
			return authRoleService.getByRoleCode(roleCode);
		} catch (Exception e) {
			logger.error("根据角色码获取角色异常", e);
		}
		return null;
	}

	@SuppressWarnings("finally")
	@POST
	@Path("/edit")
	public String editRole(final AuthRole role)
	{
		String retStr = "";
		try
		{
			final UserDto user = authRoleService.getCurrentUser();
			if (null == user)
			{
				retStr = "获取当前用户失败";
				return retStr;
			}

			role.setUpdator(user.getUserid());// 更新人为当前用户
			role.setUpdateTime(DateTools.formatDate(new Date()));

			final List<RolePermissionDto> rolePermList = loadRolePerms(role);

			if ("".equals(role.getSuperRoleCode().trim()))
			{
				role.setSuperRoleCode(null);
			}
			if (authRoleService.update(role, rolePermList))
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
		finally
		{
			return retStr;
		}

	}

	@GET
	@Path("/getRoles/{roleCode}")
	public JSONArray getRoles(@PathParam("roleCode") final String roleCode)
	{
		JSONArray result = null;
		try {
			final TreeNode treeNode = getPermTreeMap("NA".equalsIgnoreCase(roleCode) ? "" : roleCode).get("root");
			final JSONObject root = new JSONObject();
			TreeNode.getJsonTree(treeNode, root);
			
			result = (JSONArray) root.get("children");
			logger.debug("children node is : {}", result);
		} catch (Exception e) {
			logger.error("根据角色码获取角色异常", e);
		}

		return result;
	}

	private List<RolePermissionDto> loadRolePerms(final AuthRole role) throws Exception
	{
		List<RolePermissionDto> rolePermList = null;
		if (StringUtils.isNotBlank(role.getPermCodes()))
		{
			final String[] codes = role.getPermCodes().split(",");

			if (codes != null && codes.length > 0)
			{
				final UserDto user = authRoleService.getCurrentUser();
				final String userId = null != user ? user.getUserid() : "未知用户";

				rolePermList = new ArrayList<RolePermissionDto>();
				for (final String code : codes)
				{
					final RolePermissionDto rolePermDto = new RolePermissionDto();
					rolePermDto.setCreator(userId);
					rolePermDto.setCreatetime(DateTools.formatDate(new Date()));
					rolePermDto.setUpdator(userId);// 更新人为当前用户
					rolePermDto.setUpdatetime(DateTools.formatDate(new Date()));
					rolePermDto.setAuthroleid(role.getAuthRoleId());
					rolePermDto.setAuthrolepermrelid(role.getAuthRoleId() + "_" + code);
					rolePermDto.setPermCode(code);

					rolePermList.add(rolePermDto);
				}
			}
		}

		return rolePermList;
	}

	public Map<String, String> getUser2PermsMap(final List<PermissionDto> permList)
	{
		final Map<String, String> user2PermsMap = new HashMap<String, String>();

		for (final PermissionDto perm : permList)
		{
			user2PermsMap.put(perm.getPermcode(), perm.getPermcode());
		}

		return user2PermsMap;
	}

	public Map<String, TreeNode> getPermTreeMap(final String rolecode)
	{
		final Map<String, TreeNode> permMap = new TreeMap<String, TreeNode>();
		final List<PermissionDto> permList = authRoleMapper.getPermissions();

		Map<String, String> user2PermsMap = null;
		if (StringUtils.isNotBlank(rolecode))
		{
			final List<PermissionDto> userPermList = authRoleMapper.getPermissionsByRolecode(rolecode);

			user2PermsMap = getUser2PermsMap(userPermList);
		}
		String checked = null;

		permMap.put("root", new TreeNode("root", "", null, ""));
		for (final PermissionDto perm : permList)
		{
			if (StringUtils.isNotBlank(rolecode))
			{
				if (null != user2PermsMap.get(perm.getPermcode()))
				{
					checked = "true";
				} 
			}

			permMap.put(perm.getPermcode(), new TreeNode(perm.getPermcode(), perm.getMenuname(), checked, perm.getParentcode()));
			checked = null;
		}

		final Set<String> keys = permMap.keySet();
		for (final Object k : keys)
		{
			final TreeNode node = permMap.get(k);

			if (null != node)
			{
				TreeNode parentNode = null;
				if (!StringUtils.isNotBlank(node.getParentCode()) || "M-root".equalsIgnoreCase(node.getParentCode()))
				{
					parentNode = permMap.get("root");
				}
				else
				{
					parentNode = permMap.get(node.getParentCode());
				}

				if (null != parentNode)
				{
					Map<String, TreeNode> children = parentNode.getChildren();

					if (null == children)
					{
						children = new TreeMap<String, TreeNode>();
						parentNode.setChildren(children);
					}

					if (!"root".equalsIgnoreCase(node.getNodeCode()))
					{
						children.put(node.getNodeCode(), node);
					}
				}

			}
		}

		return permMap;
	}

	@GET
	@Path("/getSuperRole")
	public JSONArray getRole()
	{
		final JSONArray result = new JSONArray();
		try {
			final List<Map> roleList = authRoleMapper.getSuperRole();
			if (roleList == null || roleList.size() == 0)
			{
				return null;
			}
			for (final Map item : roleList)
			{
				final JSONObject obj = new JSONObject();
				obj.put("id", item.get("rolecode"));
				obj.put("text", item.get("rolename"));
				result.add(obj);
			}
		} catch (Exception e) {
			logger.error("获取上级角色异常", e);
		}
		return result;
	}

	@GET
	@Path("/getAllRole")
	public JSONArray getAllRole()
	{
		final JSONArray result = new JSONArray();
		try {
			final List<RoleSelect> roleList = authRoleService.getAllRole();
			if (roleList == null || roleList.size() == 0)
			{
				return null;
			}
			for (final RoleSelect item : roleList)
			{
				final JSONObject obj = new JSONObject();
				obj.put("id", item.getRoleCode());
				obj.put("text", item.getRoleName());
				result.add(obj);
			}
		} catch (Exception e) {
			logger.error("获取所有角色异常", e);
		}
		return result;
	}
	
	@GET
	@Path("/getAllEnableRole")
	public JSONArray getAllEnableRole()
	{
		final JSONArray result = new JSONArray();
		try {
			final List<RoleSelect> roleList = authRoleService.getAllEnableRole();
			if (roleList == null || roleList.size() == 0)
			{
				return null;
			}
			for (final RoleSelect item : roleList)
			{
				final JSONObject obj = new JSONObject();
				obj.put("id", item.getRoleCode());
				obj.put("text", item.getRoleName());
				result.add(obj);
			}
		} catch (Exception e) {
			logger.error("获取所有角色异常", e);
		}
		return result;
	}

	@POST
	@Path("/auth")
	public boolean authRole(final AuthRole role)
	{
		return false;
	}


//	@GET
//	@Path("/batchInsert/{testCount}")
//	public JSONArray batchInsert(@PathParam("testCount") final int testCount) throws Exception
//	{
//		System.out.println(testCount);
//
//		final List<String> list = new ArrayList<String>(testCount);
//		for (int i = 1; i <= testCount; i++)
//		{
//			list.add(String.valueOf(i));
//		}
//
//		final long start = System.currentTimeMillis();
//		batchInsert(list);
//		final long end = System.currentTimeMillis();
//
//		final long ret = end - start;
//
//		System.out.println("batchInsert ============================== " + ret);
//
//		return null;
//	}
//
//	@GET
//	@Path("/batchInsertOrUpdate/{testCount}")
//	public JSONArray batchInsertOrUpdate(@PathParam("testCount") final int testCount) throws Exception
//	{
//		System.out.println(testCount);
//
//		final List<String> list = new ArrayList<String>(testCount);
//		for (int i = 1; i <= testCount; i++)
//		{
//			list.add(String.valueOf(i));
//		}
//
//		final long start = System.currentTimeMillis();
//		authRoleFacade.batchInsertOrUpdate(list);
//		final long end = System.currentTimeMillis();
//
//		final long ret = end - start;
//
//		System.out.println("batchInsertOrUpdate ============================== " + ret);
//
//		return null;
//	}
//
//
//	public void batchInsert(final List<String> list) throws Exception
//	{
//		for (final String code : list)
//		{
//			final Map paraMap = new HashMap();
//			paraMap.put("customerId", "binzou" + UUID.randomUUID().toString());
//			paraMap.put("outOrderId", code);
//			authRoleFacade.insertTemp(paraMap);
//		}
//	}
}
