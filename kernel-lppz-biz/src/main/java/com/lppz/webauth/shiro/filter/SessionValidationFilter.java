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
 *
 *
 */
package com.lppz.webauth.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SessionValidationFilter extends OncePerRequestFilter
{
	Logger log = LoggerFactory.getLogger(getClass());
	
	private String loginUrl = "/auth/account/login";
	
	
	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
			IOException
	{
		String uri = ((HttpServletRequest) request).getRequestURI();
		log.info("session validation uri[" + uri + "]");
		
		final Subject subject = SecurityUtils.getSubject();
		final String userId = (String) subject.getPrincipal();

		if (StringUtils.hasText(userId))
		{
			chain.doFilter(request, response);
		}
		else
		{
			log.info("session timeout, redirect to login page.");
			((HttpServletRequest) request).getSession().setAttribute("isUiReq", "true");
			
			if (StringUtils.hasText(uri) && uri.indexOf("/init-app-web/console") != -1)
			{
				((HttpServletResponse) response).getOutputStream().write("请求超时,请重新登录系统!".getBytes());
				return;
			}
		 	
			WebUtils.issueRedirect(request, response, loginUrl);
		}
	}

}
