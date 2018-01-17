package com.lppz.oms.service.auth.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.AuthRole;
import com.lppz.oms.api.dto.RolePermissionDto;
import com.lppz.oms.api.dto.RoleSelect;
import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.api.dto.UserRoleDto;
import com.lppz.oms.persist.AuthRoleMapper;
import com.lppz.oms.service.auth.AuthRoleService;
import com.lppz.webauth.shiro.utils.SessionUtil;

@Service
public class AuthRoleServiceImpl implements AuthRoleService {

	private static final Logger logger = LoggerFactory.getLogger(AuthRoleServiceImpl.class);
	public static final String YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

	public static final SimpleDateFormat defaultDateformat = new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS);
	@Autowired
	private AuthRoleMapper authRoleMapper;
	
	@Override
	public UserDto getCurrentUser() throws Exception {
		return SessionUtil.getCurrentUser();
	}

	
	
	@Override
	public List<Map> findPaged(Map<String, Object> params, PageBounds page) {
		return authRoleMapper.findPaged(params, page);
	}



	@Override
	@Transactional
	public boolean update(AuthRole role, List<RolePermissionDto> rolePermList) {
		try
		{
			this.updateRole(role);
			this.deleteRolePermissionRel(role);

			if (null != rolePermList && rolePermList.size()>0)
			{
				for (final RolePermissionDto rolePerm : rolePermList)
				{
					this.authRoleMapper.createRolePermissionRel(rolePerm);
				}
			}
		}
		catch (final Exception e)
		{
			logger.error("update role error", e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean updateRole(AuthRole role) {
		try {
			authRoleMapper.updateRole(role);
		} catch (Exception e) {
			logger.error("update role error", e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean deleteRolePermissionRel(AuthRole role) {
		try {
			authRoleMapper.deleteRolePermissionRel(role.getAuthRoleId());
		} catch (Exception e) {
			logger.error("delete RolePermissionRel error", e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean create(AuthRole role, List<RolePermissionDto> rolePermList){
		try
		{
			this.createRole(role);
			if (null != rolePermList && rolePermList.size()>0)
			{
				for (final RolePermissionDto rolePerm : rolePermList)
				{
					this.authRoleMapper.createRolePermissionRel(rolePerm);
				}
			}
		}
		catch (final Exception e)
		{
			logger.error("create role error", e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean createRole(AuthRole role) {
		try {
			authRoleMapper.createRole(role);
		} catch (Exception e) {
			logger.error("create role error", e);
			return false;
		}
		return true;
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<RoleSelect> getAllRole() {
		logger.info("**** get all Role start ****");
		try
		{
			return authRoleMapper.getAllRole();
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public List<RoleSelect> getAllEnableRole() {
		try
		{
			return authRoleMapper.getAllEnableRole();
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public boolean authUser(String userId, String roleId, String creator, String authuserrolerelid) {
		try
		{
			final String updateTime = defaultDateformat.format(new Date());
			final List<AuthRole> roles = authRoleMapper.getRoleByUser(userId);

			if (null == roles || roles.size()==0)
			{
				// create user and role relation
				final UserRoleDto userRole = new UserRoleDto();
				userRole.setAuthuserrolerelid(authuserrolerelid);
				userRole.setAuthuserid(userId);
				userRole.setAuthroleid(roleId);
				userRole.setCreator(creator);
				authRoleMapper.createUserRoleRel(userRole);
			}

			Map<String, Object> param = new HashMap<String, Object>(); 
			param.put("authuserid", userId);
			param.put("authroleid", roleId);
			param.put("updatetime", updateTime);
			param.put("creator", creator);
			param.put("authuserrolerelid", authuserrolerelid);
			logger.info(param.toString());
			int num = authRoleMapper.authUser(param);
			if(num > 0){
				return true;
			} else {
				logger.info("authUser falia");
				return false;
			}
		}
		catch (final Exception e)
		{
			logger.error("auth user error", e);
			return false;
		}
	}



	@Override
	public AuthRole getByRoleCode(String rolecode) {
		return authRoleMapper.getByRoleCode(rolecode);
	}
	
	
}
