package com.lppz.oms.api.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuData {
	private String menuid;
	private String parentid;
	private String menuname;
	private String url;
	private String permtype;
	private String permindex;
	
	private List<MenuData> menus=new ArrayList<>(0);

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermtype() {
		return permtype;
	}

	public void setPermtype(String permtype) {
		this.permtype = permtype;
	}

	public String getPermindex() {
		return permindex;
	}

	public void setPermindex(String permindex) {
		this.permindex = permindex;
	}

	public List<MenuData> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuData> menus) {
		this.menus = menus;
	}

}
