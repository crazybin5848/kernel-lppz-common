package com.lppz.webauth.shiro.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.lppz.oms.api.dto.UserDto;

public class SessionUtil {
	public static void setCurrentUser(UserDto user){
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.getSession().setAttribute("user", user);
	}
	
	public static UserDto getCurrentUser(){
		Subject currentUser = SecurityUtils.getSubject();
		return (UserDto) currentUser.getSession().getAttribute("user");
	}
}
