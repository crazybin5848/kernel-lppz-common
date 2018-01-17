package com.lppz.webauth.shiro;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.lppz.webauth.shiro.utils.SpringUtil;

public class ExtIniWebEnvironment extends IniWebEnvironment {
	
	@Override
	protected FilterChainResolver createFilterChainResolver() {
		
		// 1、创建FilterChainResolver
		PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
		
		// 2、创建FilterChainManager
		DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
		
		// 3、注册Filter
		for (DefaultFilter filter : DefaultFilter.values()) {
			filterChainManager.addFilter(filter.name(),
					(Filter) ClassUtils.newInstance(filter.getFilterClass()));
		}
		
		filterChainManager.addFilter("authc", (Filter) getBean("formAuthenticationFilter"));
		filterChainManager.addFilter("anyRoles", (Filter) getBean("anyRolesFilter"));
		
		// 4、注册URL-Filter的映射关系

		//////////////////////////
		
//		/permission.jsp = authc
//		/main.jsp = authc
//		/unauthorized.jsp = anon
//		/login.jsp = anon
		filterChainManager.addToChain("/permission.jsp", "authc");
		filterChainManager.addToChain("/main.jsp", "authc");
		filterChainManager.addToChain("/unauthorized.jsp", "anon");
		filterChainManager.addToChain("/login.jsp", "anon");

//		<!-- menu to role mapping  -->
//		/spring/order/orderImport = authc, roles[sa]
//		/spring/order/validationOrder = authc, anyRoles[admin,sa]
		filterChainManager.addToChain("/spring/order/orderImport", "authc", "roles[sa]");
		filterChainManager.addToChain("/spring/order/validationOrder", "authc", "anyRoles[admin,sa]");
		
//	<!-- function to permission mapping -->
//		/spring/order/view = authc, perms[order:view]
//		/spring/order/create = authc, perms[order:create]
//		/spring/order/update = authc, perms[order:update]
//		/spring/order/delete = authc, perms[order:delete]
		filterChainManager.addToChain("/spring/order/view", "authc", "perms[order:view]");
		filterChainManager.addToChain("/spring/order/create", "authc", "perms[order:create]");
		filterChainManager.addToChain("/spring/order/update", "authc", "perms[order:update]");
		filterChainManager.addToChain("/spring/order/delete", "authc", "perms[order:delete]");
		
		//////////////////////////
		
		// 5、设置Filter的属性
		FormAuthenticationFilter authcFilter = (FormAuthenticationFilter) filterChainManager.getFilter("authc");
		authcFilter.setLoginUrl("/login.jsp");
		RolesAuthorizationFilter rolesFilter = (RolesAuthorizationFilter) filterChainManager.getFilter("roles");
		rolesFilter.setUnauthorizedUrl("/unauthorized.jsp");
		
		filterChainResolver.setFilterChainManager(filterChainManager);
		return filterChainResolver;
	}

	public Object getBean(String id) {
		WebApplicationContext webAppContext = (WebApplicationContext) 
				this.getServletContext().getAttribute(
						WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		return webAppContext.getBean(id);
	}
}
