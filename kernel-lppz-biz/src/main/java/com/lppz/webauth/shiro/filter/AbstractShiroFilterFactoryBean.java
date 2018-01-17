package com.lppz.webauth.shiro.filter;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;

public abstract class AbstractShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	public abstract Map<String, String> getFilterChainDefinitionMap();
	
	public abstract Map<String, Filter> getFilters();
	
	@Override
	protected FilterChainManager createFilterChainManager() {
		
		super.getFilters().putAll(getFilters());
		super.getFilterChainDefinitionMap().putAll(getFilterChainDefinitionMap());
		
		FilterChainManager manager = super.createFilterChainManager();
        return manager;
	}
}
