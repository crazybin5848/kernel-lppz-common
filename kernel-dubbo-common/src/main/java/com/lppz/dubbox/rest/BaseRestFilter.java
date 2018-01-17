package com.lppz.dubbox.rest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

import org.jboss.resteasy.plugins.server.servlet.HttpServletInputMessage;

import com.lppz.util.ReFelctionUtil;

public class BaseRestFilter {
	protected HttpServletRequest getHttpReq(ContainerRequestContext context) {
			HttpServletInputMessage hsinput=ReFelctionUtil.getDynamicObj(context.getRequest().getClass(), "request", context.getRequest());
	        HttpServletRequest req=ReFelctionUtil.getDynamicObj(hsinput.getClass(), "request", hsinput);
			return req;
		}
	protected Map<Method,Boolean> cacheAuth=new ConcurrentHashMap<Method,Boolean>();
}

