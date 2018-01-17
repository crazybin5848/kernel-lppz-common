package com.lppz.oms.persist;

import java.util.List;
import java.util.Map;

import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.UserDto;

public interface UserMapper {
	UserDto findUser(String userName);
	UserDto findUserById(String userId);
	List<String> findRoles(String userId);
	List<String> findPermissions(String userId);
	List<Map<String, String>> findPermissionAndMenu();
	List<Map<String, String>> findRoleAndMenu();
	List<PermissionDto> findPermissionsWithPermtype(String userId);
}
