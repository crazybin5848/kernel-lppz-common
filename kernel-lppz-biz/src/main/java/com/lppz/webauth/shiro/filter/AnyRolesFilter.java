package com.lppz.webauth.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import com.lppz.webauth.shiro.utils.WebUtils;


public class AnyRolesFilter extends AccessControlFilter
{
	private String unauthorizedUrl = "/auth/account/unauthorized";
	private String loginUrl = "/auth/account/login";

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
	protected boolean isAccessAllowed(final ServletRequest request, final ServletResponse response, final Object mappedValue)
			throws Exception
	{
		final String[] roles = (String[]) mappedValue;
		if (roles == null)
		{
			return true;// 如果没有设置角色参数，默认成功
		}
		for (final String role : roles)
		{
			if (getSubject(request, response).hasRole(role))
			{
				return true;
			}
		}
		return false;// 跳到onAccessDenied处理
	}

	@Override
	protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception
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

}
