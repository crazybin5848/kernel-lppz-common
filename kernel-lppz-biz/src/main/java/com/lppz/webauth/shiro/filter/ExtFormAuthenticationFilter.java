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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.webauth.shiro.utils.WebUtils;


/**
 *
 */
public class ExtFormAuthenticationFilter extends FormAuthenticationFilter
{
	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception
	{

		if (isLoginRequest(request, response))
		{
			if (isLoginSubmission(request, response))
			{
				if (log.isTraceEnabled())
				{
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			}
			else
			{
				if (log.isTraceEnabled())
				{
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		}
		else
		{
			if (log.isTraceEnabled())
			{
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the " + "Authentication url ["
						+ getLoginUrl() + "]");
			}

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
					String uri = ((HttpServletRequest) request).getRequestURI();
					if (StringUtils.hasText(uri) && uri.indexOf("/init-app-web/console") != -1)
					{
						if (WebUtils.isPost(request))
						{
							WebUtils.write(response, "请求超时,请重新登录系统!");
							return false;
						}
					}
					
					// 表示没有登录，重定向到登录页面
					saveRequestAndRedirectToLogin(request, response);
				}
			}
			return false;
		}
	}

}
