/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.webauth.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.util.StringUtils;

import com.lppz.webauth.shiro.utils.WebUtils;


/**
 *
 */
public class ExtPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter
{
	private String unauthorizedUrl = "/auth/account/unauthorized";
	private String loginUrl = "/auth/account/login";

	@Override
	public void setUnauthorizedUrl(final String unauthorizedUrl)
	{
		this.unauthorizedUrl = unauthorizedUrl;
	}

	@Override
	public void setLoginUrl(final String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	@Override
	protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws IOException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		final Subject subject = getSubject(request, response);

		if (subject.getPrincipal() == null)
		{
			if (WebUtils.isAjax(httpRequest))
			{
				WebUtils.sendJsonResp(httpResponse, WebUtils.SC_UNAUTHENTICATED);
			}
			else
			{
				// 表示没有登录，重定向到登录页面
				saveRequestAndRedirectToLogin(request, response);
			}
		}
		else
		{
			if (WebUtils.isAjax(httpRequest))
			{
				WebUtils.sendJsonResp(httpResponse, WebUtils.SC_UNAUTHORIZED);
			}
			else
			{
				if (StringUtils.hasText(unauthorizedUrl))
				{
					// 如果有未授权页面跳转过去
					WebUtils.issueRedirect(request, response, unauthorizedUrl);
				}
				else
				{
					// 否则返回401 未授权状态码
					WebUtils.toHttp(response).sendError(WebUtils.SC_UNAUTHORIZED);
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException
	{
      Subject subject = getSubject(request, response);
      String[] perms = (String[]) mappedValue;

      boolean isPermitted = true;
      if (perms != null && perms.length > 0) {
          if (perms.length == 1) {
              if (!subject.isPermitted(perms[0])) {
                  isPermitted = false;
              }
          } else {
         	 for (String p : perms) 
         	 {
                if (subject.isPermitted(p)) {
                   isPermitted = true;
                   break;
               }
         	 }
          }
      }

      return isPermitted;
	}

	
}
