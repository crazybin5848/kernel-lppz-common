package com.lppz.oms.api.dto;

import java.io.Serializable;

public class RoleSelect implements Serializable {
	private static final long serialVersionUID = 4933902845280527160L;

	private String roleCode;

	private String roleName;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
