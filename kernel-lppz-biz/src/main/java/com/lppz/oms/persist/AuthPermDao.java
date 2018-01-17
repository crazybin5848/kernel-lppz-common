package com.lppz.oms.persist;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.PermissionDto;

public interface AuthPermDao {

	public List<Map> findPaged(Map<String, Object> params, PageBounds page);
	
	public List<PermissionDto> getPermissionsByPermType(@QueryParam("permtype") String permtype);
	
	public PermissionDto getByPermCode(@QueryParam("permCode") String permCode) throws Exception;
	
	public PermissionDto getMaxPermindex(@QueryParam("permtype") String permtype) throws Exception;
	
	public void createPerm(PermissionDto perm);
	
	public void updatePerm(PermissionDto perm);
	
	public void deletePerm(@QueryParam("permcode") String permcode) throws Exception;
}
