package com.lppz.webauth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.util.StringUtils;
import com.lppz.webauth.shiro.utils.WebUtils;

public class PathFilter implements Filter {

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
		String uri = ((HttpServletRequest) servletrequest).getRequestURI();
		if (StringUtils.hasText(uri) && uri.indexOf("/init-app-web/console") != -1) {
//			WebUtils.write(servletresponse, "哎哟，地址错了哦!");
			WebUtils.toHttp(servletresponse).sendError(WebUtils.SC_UNAUTHORIZED);
		} else {
			filterchain.doFilter(servletrequest, servletresponse);
		}
	}

	@Override
	public void destroy() {

	}

}
