package com.lppz.oms.service.auth;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.PermissionDto;

public interface AuthPermService {

	List<Map> findPaged(Map<String, Object> params, PageBounds page);
	
	public List<PermissionDto> getPermissionsByPermType(String permtype);
	
	public PermissionDto getByPermCode(String permCode);
	
	public boolean createPerm(PermissionDto perm);

	public PermissionDto getMaxPermindex(PermissionDto perm);

	public boolean deletePerm(PermissionDto perm);

	public boolean updatePerm(PermissionDto perm);
}
