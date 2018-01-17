package com.lppz.webauth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.persist.EmployeeMapper;
import com.lppz.util.MD5;
import com.lppz.webauth.model.User;
import com.lppz.webauth.model.UserConfig;
import com.lppz.webauth.shiro.RealmProvider;


@Controller
@RequestMapping("/account/")
public class AccountController
{
	public static final String WEB_NAME = "/oms-web";

	private Logger log = LoggerFactory.getLogger(getClass());

	private RealmProvider realmProvider;
	
	@Resource
	private EmployeeMapper employeeMapper;

	@Autowired
	public void setRealmProvider(final RealmProvider realmProvider)
	{
		this.realmProvider = realmProvider;
	}

//	private UserFacade userFacade;
//
//	@Autowired
//	/**
//	 * @param userFacade the userFacade to set
//	 */
//	public void setUserFacade(final UserFacade userFacade)
//	{
//		this.userFacade = userFacade;
//	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(final Model model)
	{
		log.info("redirect to login page!");
		model.addAttribute("user", new UserDto());
		return "redirect:/pages/login/login.jsp";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@ModelAttribute final User user, final HttpServletRequest request)
			throws Exception
	{
		log.info("post login");
		return doLogin(user, request);
	}

	public String doLogin(final User user, final HttpServletRequest request) throws Exception
	{
		String error = null;
		String username = user.getUsername();
		String password = user.getPassword();
		String md5Password =  MD5.getMD5(password);
		final boolean rememberMe = user.isRememberMe();

		final Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated())
		{
			final UserDto userDto = getCurrentUser();
			request.getSession().setAttribute("USER_KEY", userDto);
			checkDefaultPasswd(request,password);
			return "redirect:/pages/index.jsp";
		}

		final UsernamePasswordToken token = new UsernamePasswordToken(username, md5Password);
		try
		{	
			if (RealmProvider.authorizationCache != null) {
				RealmProvider.authorizationCache.remove(user.getUsername());
			}
			token.setRememberMe(rememberMe);
			subject.login(token);
		}
		catch (final UnknownAccountException e)
		{
			log.warn("unknown account incorrect!", e);
			error = "未知的用户账号";
		}
		catch (final IncorrectCredentialsException e)
		{
			log.warn("username/password incorrect!", e);
			error = "用户名/密码错误";
		}
		catch (final AuthenticationException e)
		{
			log.warn("other authentication error!", e);
			// 其他错误，比如锁定，如果想单独处理请单独catch 处理
			error = e.getMessage();
		}
		if (error != null)
		{
			// 出错了，返回登录页面
			request.getSession().setAttribute("ERROR_MSG", error);
			log.info("login error, redirect to login page. error = " + error);
			return "redirect:/pages/login/login.jsp";
		}
		else
		{// 登录成功

			final UserDto userDto = getCurrentUser();
			request.getSession().setAttribute("USER_KEY", userDto);
			checkDefaultPasswd(request,password);
			log.info("login success, redirect to index page.");
			return "redirect:/pages/index.jsp";
		}
	}
	
	
	private void checkDefaultPasswd(HttpServletRequest request,String password) {
		if(UserConfig.DEFAULTPASSWD.equals(password))
			request.getSession().setAttribute("isdefaultPs", true);
	}

	public UserDto getCurrentUser() throws Exception
	{
		final Subject subject = SecurityUtils.getSubject();
		final String userId = (String) subject.getPrincipal();

		if (!org.apache.shiro.util.StringUtils.hasText(userId))
		{
			log.warn("getCurrentUser error, userId is null!");
			return null;
		}

		return getUserById(userId);
	}
	
	
	@SuppressWarnings("unchecked")
	public UserDto getUserById(final String userId) throws Exception
	{
		Map employee = new HashMap<>();
		employee.put("userid", userId);
		Map map = employeeMapper.loadEmployee(employee);
		UserDto dto = new UserDto();
		dto.setUserid((String) map.get("userid"));
		dto.setUsername((String) map.get("username"));
		dto.setUserpwd((String) map.get("userpwd"));
		return dto;
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(final Model model)
	{
		model.addAttribute("user", new UserDto());

		final Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated())
		{
			subject.logout();
		}

		log.info("user " + subject.getPrincipal() + " logout success");
		log.info("logout success, redirect to login page.");
		return "redirect:/pages/login/login.jsp";
	}

	@RequestMapping(value = "clearCache", method = RequestMethod.POST)
	public String clearCache(final Model model)
	{

		final Subject subject = SecurityUtils.getSubject();
		if (!subject.isAuthenticated())
		{
			return "login";
		}

		realmProvider.clearCachedByUser(subject.getPrincipals());

		log.info("用户" + subject.getPrincipal() + "权限更新，缓存已清理。");
		final UserDto user = new UserDto();
		user.setUsername((String) subject.getPrincipal());
		model.addAttribute("user", new UserDto());
		return "main";
	}

	@RequestMapping(value = "unauthorized", method = RequestMethod.GET)
	public String authenticated()
	{
		log.info("current user have no permission for access the resource(url/menu/function), redirect to unauthorized page.");
		return "redirect:/pages/unauthorized.jsp";
	}

	public String getLastAccessUrl(final HttpServletRequest request)
	{
		final SavedRequest savedRequest = WebUtils.getSavedRequest(request);

		String url = savedRequest != null ? savedRequest.getRequestUrl() : null;
		if (StringUtils.hasText(url))
		{
			final String webName = StringUtils.hasText(request.getContextPath()) ? request.getContextPath() : WEB_NAME;
			url = url.substring(webName.length(), url.length());
		}

		return url;
	}
}
