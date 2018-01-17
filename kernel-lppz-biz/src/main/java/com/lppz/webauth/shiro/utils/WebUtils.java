package com.lppz.webauth.shiro.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSON;


/**
 *
 */
public class WebUtils
{
	public static final int SC_UNAUTHENTICATED = 211;

	public static final int SC_UNAUTHORIZED = 212;

	public static boolean isAjax(final HttpServletRequest request)
	{
		return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")
				.toString()));
	}

	public static void sendJsonResp(final HttpServletResponse httpResponse, final int statusCode, final Object returnedObj)
			throws UnsupportedEncodingException, IOException
	{
		httpResponse.getOutputStream().write(JSON.toJSONString(returnedObj).toString().getBytes("UTF-8"));
		httpResponse.setContentType("text/json; charset=UTF-8");
		httpResponse.setStatus(statusCode);
	}

	public static void sendJsonResp(final HttpServletResponse httpResponse, final int statusCode)
			throws UnsupportedEncodingException, IOException
	{
		httpResponse.setContentType("text/json; charset=UTF-8");
		httpResponse.setStatus(statusCode);
	}

	public static void issueRedirect(final ServletRequest request, final ServletResponse response, final String url)
			throws IOException
	{
		org.apache.shiro.web.util.WebUtils.issueRedirect(request, response, url, null, true, true);
	}

	public static HttpServletResponse toHttp(final ServletResponse response)
	{
		return org.apache.shiro.web.util.WebUtils.toHttp(response);
	}

	public static void write(final ServletResponse response, final String text) throws IOException
	{
		((HttpServletResponse) response).getOutputStream().write(text.getBytes());
	}
	
	public static boolean isPost(final ServletRequest request)
	{
		return "POST".equalsIgnoreCase(((HttpServletRequest) request).getMethod());
	}
	
	public static String getSessionInfo()
	{
		final Subject subject = SecurityUtils.getSubject();
		if (null != subject)
		{
			Session sess = subject.getSession();
			if (null != sess)
			{
				return "Host is : " + sess.getHost() + ", SID is : " + sess.getId();
			}
		}
		
		return "Session info cannot be get!";
	}
}
