package com.lppz.oms.persist;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.AuthRole;
import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.RolePermissionDto;
import com.lppz.oms.api.dto.RoleSelect;
import com.lppz.oms.api.dto.UserRoleDto;

public interface AuthRoleMapper {

	List<Map> findPaged(Map<String, Object> params, PageBounds page);

	List<PermissionDto> getPermissions();

	List<PermissionDto> getPermissionsByRolecode(@QueryParam("rolecode") String rolecode);

	List<Map> getSuperRole();

	AuthRole getByRoleCode(@QueryParam("rolecode") String rolecode);

	void updateRole(AuthRole role) throws Exception;

	void createRole(AuthRole role) throws Exception;

	void deleteRolePermissionRel(@QueryParam("authRoleId") String authRoleId) throws Exception;

	void createRolePermissionRel(RolePermissionDto dto);

	public List<RoleSelect> getAllRole() throws Exception;

	public List<AuthRole> getRoleByUser(@QueryParam("userId") final String userId) throws Exception;

	public void createUserRoleRel(UserRoleDto userRole) throws Exception;

	public int authUser(Map<String, Object> params) throws Exception;

	List<RoleSelect> getAllEnableRole();
}
