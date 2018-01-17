package com.lppz.webauth.rest.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.lppz.oms.api.dto.MenuData;
import com.lppz.oms.persist.MenuMapper;
import com.lppz.webauth.service.AuthorizingService;


@Controller
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/ui/menu")
public class MenuResource
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private AuthorizingService realmProvider;
	@Resource
	private MenuMapper menuMapper;

	public AuthorizingService getRealmProvider() {
		return realmProvider;
	}

	public void setRealmProvider(AuthorizingService realmProvider) {
		this.realmProvider = realmProvider;
	}

	public MenuMapper getMenuMapper() {
		return menuMapper;
	}

	public void setMenuMapper(MenuMapper menuMapper) {
		this.menuMapper = menuMapper;
	}

	@GET
	@Path("/getMenu")
	public List<MenuData> getMenus()
	{
		log.debug("load menus after user logged in");
		final List<MenuData> menuList = fetchMenus();
		return menuList;
	}
	
	@GET
	@Path("/getMenuByUser/{userid}")
	public List<MenuData> getMenuByUser(@PathParam("userid")String userid)
	{
		log.debug("load menus after user logged in");
		final List<MenuData> menuList = fetchValidateMenus(userid);
		return menuList;
	}
	
	private List<MenuData> fetchMenus(){
		try {
			List<MenuData> menus = menuMapper.findRootMenu();
			Map<String,MenuData> menuCache = new HashMap<String,MenuData>();
			for (MenuData menu : menus) {
				menuCache.put(menu.getMenuid(), menu);
			}
			List<MenuData> funcs = menuMapper.findFuncMenu();
			//二级子菜单
			for (MenuData func : funcs) {
				if(realmProvider.hasPermission(func.getMenuid())&& menuCache.containsKey(func.getParentid())){
					MenuData parent = menuCache.get(func.getParentid());
					parent.getMenus().add(func);
				}
			}
			return menus;
		} catch (Exception e) {
			log.error("获取菜单异常",e);
		}
		return null;
	}
	
	private List<MenuData> fetchValidateMenus(String userid){
		try {
			List<MenuData> menus = menuMapper.findValidateRootMenu(userid);
			Map<String,MenuData> menuCache = new HashMap<String,MenuData>();
			for (MenuData menu : menus) {
				menuCache.put(menu.getMenuid(), menu);
			}
			List<MenuData> funcs = menuMapper.findValidateFuncMenu(userid);
			//二级子菜单
			for (MenuData func : funcs) {
				if(realmProvider.hasPermission(func.getMenuid())&& menuCache.containsKey(func.getParentid())){
					MenuData parent = menuCache.get(func.getParentid());
					parent.getMenus().add(func);
				}
			}
			return menus;
		} catch (Exception e) {
			log.error("获取菜单异常",e);
		}
		return null;
	}
}
