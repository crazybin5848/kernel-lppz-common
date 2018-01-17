package com.lppz.webauth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lppz.core.configuration.MultiMyBatisConfig;
import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.persist.UserMapper;
import com.lppz.webauth.service.WebUserService;

@Configuration
@Import(MultiMyBatisConfig.class)
public class WebUserServiceImpl implements WebUserService
{
	private static final Logger logger = LoggerFactory.getLogger(WebUserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Override
	public void changePassword(final String userId, final String newPassword)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public UserDto findUser(final String userId)
	{
		UserDto user ;
		try
		{
			user = userMapper.findUserById(userId);
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return user;
	}

	/*
	 * 
	 * select * from authrole as r
	 * join authuserrolerel as u2r on r.rolecode = u2r.authroleid
	 * join authuser as u on u.userid = u2r.authuserid
	 * where u.userid = 'admin'
	 */
	@Override
	public Set<String> findStringRoles(final String userId)
	{
		Set<String> rolesSet = new HashSet<String>();
		try
		{
			List<String> rols = userMapper.findRoles(userId);
			rolesSet.addAll(rols);
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return rolesSet;
	}

	/*
	 * select * from authpermission as p
	 * join authrolepermissionrel as p2r on p.permcode = p2r.permcode
	 * join authrole as r on r.rolecode = p2r.authroleid
	 * join authuserrolerel as u2r on r.rolecode = u2r.authroleid
	 * join authuser as u on u.userid = u2r.authuserid
	 * where u.userid = 'admin'
	 */
	@Override
	public Set<String> findStringPermissions(final String userId)
	{
		final Set<String> permissions = new HashSet<String>();
		try
		{
			List<String> list = userMapper.findPermissions(userId);
			permissions.addAll(list);
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return permissions;
	}

	/*
	 * select permcode, menuurl from authpermission
	 */
	@Override
	public Map<String, List<String>> findPermissions()
	{
		final Map<String, List<String>> url2CodeMap = new HashMap<String, List<String>>();

		List<Map<String, String>> list;
		try
		{
			list = userMapper.findPermissionAndMenu();
			List<String> codes = null;
			String menuurl = null;

			for (final Map<String, String> map : list)
			{
				menuurl = (String) map.get("menuurl");
				codes = url2CodeMap.get(menuurl);
				if (null == codes)
				{
					codes = new ArrayList<String>();
					url2CodeMap.put(menuurl, codes);
				}
				codes.add((String) map.get("permcode"));
			}
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return url2CodeMap;
	}

	/*
	 * select distinct r.rolecode as rolecode, p.menuurl as menuurl from authpermission as p
	 * join authrolepermissionrel as p2r on p.permcode = p2r.permcode
	 * join authrole as r on r.rolecode = p2r.authroleid
	 * join authuserrolerel as u2r on r.rolecode = u2r.authroleid
	 * join authuser as u on u.userid = u2r.authuserid
	 * where p.menuurl != '/xxx/xx/xx.jsp'
	 * order by menuurl
	 */
	@Override
	public Map<String, List<String>> findUrl2Roles()
	{
		final Map<String, List<String>> url2RolesMap = new HashMap<String, List<String>>();
		List<Map<String, String>> list;
		try
		{
			list = userMapper.findRoleAndMenu();
			List<String> roles = null;
			String menuurl = null;

			for (final Map<String, String> map : list)
			{
				menuurl = (String) map.get("menuurl");
				roles = url2RolesMap.get(menuurl);

				if (null == roles)
				{
					roles = new ArrayList<String>();
					url2RolesMap.put(menuurl, roles);
				}

				roles.add((String) map.get("rolecode"));
			}
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		

		return url2RolesMap;
	}

	@Override
	public List<PermissionDto> findStringPermissionsByPermtype(String userId)
	{
		List<PermissionDto> dtos;
		try
		{
			dtos = userMapper.findPermissionsWithPermtype(userId);
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return dtos;
	}
	/**
	 * 用户登录
	 */
	@Override
	public boolean userLogin(String username, String password) {
		UserDto user = userMapper.findUserById(username);
		if (user!=null&&password.equals(user.getUserpwd())) {
			return true;
		}
		return false;
	}

}
