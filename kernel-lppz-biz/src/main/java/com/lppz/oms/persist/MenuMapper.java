package com.lppz.oms.persist;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.MenuData;

public interface MenuMapper {
	
	List<MenuData> findRootMenu();
	List<MenuData> findFuncMenu();
	
	List<MenuData> findRootMenu(PageBounds page);
	
	List<MenuData> findValidateRootMenu(String userid);
	List<MenuData> findValidateFuncMenu(String userid);

}
