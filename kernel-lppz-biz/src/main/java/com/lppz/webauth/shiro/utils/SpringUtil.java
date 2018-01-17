package com.lppz.webauth.shiro.utils;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}

	public static Object getBean(String id) {
		return applicationContext.getBean(id);
	}
	
	public static String getWebContextInitParameter(String paramName) {
		
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext(); 
		if (null != webApplicationContext && null != webApplicationContext.getServletContext())
		{
	      ServletContext servletContext = webApplicationContext.getServletContext(); 
			return servletContext.getInitParameter(paramName);
		}

		return "NO_PARAM_FOUND";
	}
}
