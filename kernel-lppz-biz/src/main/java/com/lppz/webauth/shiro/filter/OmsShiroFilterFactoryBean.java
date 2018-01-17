package com.lppz.webauth.shiro.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lppz.core.inittb.InitTableInterface;
import com.lppz.webauth.service.WebUserService;
import com.lppz.webauth.shiro.utils.SpringUtil;


public class OmsShiroFilterFactoryBean extends AbstractShiroFilterFactoryBean
{
	Logger log = LoggerFactory.getLogger(getClass());

	@Resource(name = "initAuthDataBean")
	private InitTableInterface initAuthDataBean;

	private WebUserService webUserService;

	@Autowired
	public void setWebUserService(final WebUserService webUserService)
	{
		this.webUserService = webUserService;
	}

	@Override
	public Map<String, String> getFilterChainDefinitionMap()
	{
		final Map<String, String> chains = new HashMap<String, String>();

		final String ENABLE_AUTH = SpringUtil.getWebContextInitParameter("ENABLE_AUTH");

		if ("true".equalsIgnoreCase(ENABLE_AUTH))
		{
			// build up the chains:
			chains.put("/pages/login/login.jsp", "anon");
			chains.put("/pages/*", "authc");
			chains.put("/webresources/ui/**", "authc");

			// init auth data
			initAuthData();

			// <!-- menu to role mapping -->
			loadAuthRole(chains);

			// <!-- function to permission mapping -->
			loadAuthPerm(chains);
		}

		return chains;
	}

	private void initAuthData()
	{
		 this.initAuthDataBean.initData();
	}

	private void loadAuthPerm(final Map<String, String> chains)
	{
		final Map<String, List<String>> url2CodesMap = webUserService.findPermissions();

		int i = 0;
		String codesStr = "";
		final Set<String> keys = url2CodesMap.keySet();
		for (final String url : keys)
		{
			final List<String> codes = url2CodesMap.get(url);

			if (codes!=null&&!"".equals(codes))
			{
				i = 0;
				codesStr = "";
				for (; i < codes.size(); i++)
				{
					codesStr += codes.get(i);

					if (i < codes.size() - 1)
					{
						codesStr += ",";
					}
				}
				
				chains.put(url, "authc, perms[" + codesStr + "]");
//				chains.put("/xxx/xxx/xxx.jsp", "authc, perms[" + codesStr + "]");
				log.debug(url + ", authc, perms[" + codesStr + "]");
			}
		}
	}
	
	private void loadAuthPerm1(final Map<String, String> chains)
	{
//		 chains.put("/init-app-web/console/main", "authc, perms[111]");
//		 chains.put("/init-app-web/console/isUpToDate", "authc, perms[222]");
//		chains.put("/pages/test.jsp", "authc, perms[111,222]");
		
//		chains.put("/pages/test.jsp", "authc, perms[111]");
//		chains.put("/pages/test.jsp", "authc, perms[222]");
		
//		final List<Permission> permList = webUserService.findPermissions();
//		for (final Permission perm : permList)
//		{
//			chains.put(perm.getUrl(), "authc, perms[" + perm.getCode() + "]");
//			chains.put("/xxx/xxx/xxx.jsp", "authc, perms[" + perm.getCode() + "]");
//			log.info("add chain url [" + perm.getUrl() + "], perms[" + perm.getCode() + "]");
//			System.out.println("add chain url [" + perm.getUrl() + "], perms[" + perm.getCode() + "]");
//		}
	}

	private void loadAuthRole(final Map<String, String> chains)
	{
//		 chains.put("/init-app-web/console/main", "authc, anyRoles[R01]");
//		 chains.put("/init-app-web/console/isUpToDate", "authc, anyRoles[R01]");

		final Map<String, List<String>> url2RolesMap = webUserService.findUrl2Roles();

		int i = 0;
		String rolesStr = "";
		final Set<String> keys = url2RolesMap.keySet();
		for (final String url : keys)
		{
			final List<String> roles = url2RolesMap.get(url);

			if (null!=roles&&!"".equals(roles))
			{
				i = 0;
				rolesStr = "";
				for (; i < roles.size(); i++)
				{
					rolesStr += roles.get(i);

					if (i < roles.size() - 1)
					{
						rolesStr += ",";
					}
				}
				
				chains.put(url, "authc, anyRoles[" + rolesStr + "]");
//				chains.put("/xxx/xxx/xxx.jsp", "authc, anyRoles[" + rolesStr + "]");
				log.debug(url + ", authc, anyRoles[" + rolesStr + "]");
			}
		}
	}

	@Override
	public Map<String, Filter> getFilters()
	{
		final Map<String, Filter> filters = new HashMap<String, Filter>();

		final Filter formAuthenticationFilter = (Filter) SpringUtil.getBean("extFormAuthenticationFilter");
		final Filter anyRolesFilter = (Filter) SpringUtil.getBean("anyRolesFilter");
		final Filter extPermsFilter = (Filter) SpringUtil.getBean("extPermissionsAuthorizationFilter");
		final Filter sessValidFilter = (Filter) SpringUtil.getBean("sessionValidationFilter");

		filters.put("authc", formAuthenticationFilter);
		filters.put("anyRoles", anyRolesFilter);
		filters.put("perms", extPermsFilter);
		filters.put("sesValid", sessValidFilter);

		return filters;
	}

}
