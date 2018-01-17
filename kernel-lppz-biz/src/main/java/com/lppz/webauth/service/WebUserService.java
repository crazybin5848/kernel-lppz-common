package com.lppz.webauth.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.UserDto;


public interface WebUserService
{
	public void changePassword(String userId, String newPassword); // 修改密码

	public UserDto findUser(String userId); // 根据用户ID查找用户

	public Set<String> findStringRoles(String userId);// 根据用户ID查找其角色

	public Set<String> findStringPermissions(String userId);// 根据用户ID查找其权限
	
	public List<PermissionDto> findStringPermissionsByPermtype(String userId);// 根据用户ID和权限类型查找其权限

	public Map<String, List<String>> findPermissions();// 查找所有权限列表

	public Map<String, List<String>> findUrl2Roles();// 查找权限URL对应的权限列表
	
	public boolean userLogin(String username,String password);//用户登录
}
