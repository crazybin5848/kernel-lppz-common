package com.lppz.oms.service.auth;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.AuthRole;
import com.lppz.oms.api.dto.RolePermissionDto;
import com.lppz.oms.api.dto.RoleSelect;
import com.lppz.oms.api.dto.UserDto;

public interface AuthRoleService {

	public List<Map> findPaged(Map<String, Object> params, PageBounds page);

	public UserDto getCurrentUser() throws Exception;

	public boolean update(AuthRole role, List<RolePermissionDto> rolePermList);

	public boolean updateRole(AuthRole role);

	public boolean deleteRolePermissionRel(AuthRole role);

	public boolean create(AuthRole role, List<RolePermissionDto> rolePermList);

	public boolean createRole(AuthRole role);

	public List<RoleSelect> getAllRole();

	public boolean authUser(final String userId, final String roleId, final String creator,
			final String authuserrolerelid);

	public AuthRole getByRoleCode(String rolecode);
	
	List<RoleSelect> getAllEnableRole();
}
