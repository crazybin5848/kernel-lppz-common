package com.lppz.webauth.shiro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.api.dto.UserDto;
import com.lppz.oms.persist.UserMapper;
import com.lppz.webauth.model.User;
import com.lppz.webauth.service.AuthorizingService;
import com.lppz.webauth.service.WebUserService;
import com.lppz.webauth.shiro.utils.SessionUtil;
import com.lppz.webauth.shiro.utils.SpringUtil;
import com.lppz.webauth.shiro.utils.WebUtils;

@Service
@Scope("singleton")
public class RealmProvider extends AuthorizingRealm implements AuthorizingService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private WebUserService webUserService;

	@Autowired
	private UserMapper userMapper;

	public void setWebUserService(final WebUserService webUserService) {
		this.webUserService = webUserService;
	}

	@Override
	public String getName() {
		return "lppzRealmProvider";
	}

	public AuthorizationInfo loadAuthorizationInfoFromDB(final String userId) {
		final OmsAuthorizationInfo authorizationInfo = new OmsAuthorizationInfo();
		authorizationInfo.setRoles(webUserService.findStringRoles(userId));

		Set<String> permStr = new HashSet<String>();
		List<PermissionDto> userPerms = webUserService.findStringPermissionsByPermtype(userId);
		for (PermissionDto perm : userPerms) {
			permStr.add(perm.getPermcode());
		}

		authorizationInfo.setStringPermissions(permStr);
		authorizationInfo.setPermissions(userPerms);

		return authorizationInfo;
	}

	public static MapCache<Object, AuthorizationInfo> authorizationCache;

	public static void setAuthorizationCache(MapCache<Object, AuthorizationInfo> authorizationCache) {
		RealmProvider.authorizationCache = authorizationCache;
	}

	public AuthorizationInfo initAuthorizationCache(final String userId) throws CacheException, Exception {
		
		OmsAuthorizationInfo authorizationInfo = null;
		MapCache<Object, AuthorizationInfo> mapCache = (MapCache<Object, AuthorizationInfo>) authorizationCache;
		if (null != mapCache) {
			authorizationInfo = (OmsAuthorizationInfo) mapCache.get(userId);

			if (null != authorizationInfo) {
				log.info("Get auth data from cache.");
			} else {
				log.warn("authorizationInfo is null, will reload auth data from db!");
				authorizationInfo = (OmsAuthorizationInfo) loadAuthorizationInfoFromDB(userId);
				mapCache.put(userId, authorizationInfo);
				setAuthorizationCache(mapCache);
			}
		} else {
			log.warn("The mapCache is null, auth data will be loaded from db!");
			authorizationInfo = (OmsAuthorizationInfo) loadAuthorizationInfoFromDB(userId);
			
			final Map<Object, AuthorizationInfo> map = new HashMap<Object, AuthorizationInfo>();
			map.put(userId, authorizationInfo);
			mapCache = new MapCache<Object, AuthorizationInfo>(userId, map);
			setAuthorizationCache(mapCache);
		}

		return authorizationInfo;
	}

	@Override
	public boolean hasPermission(final String permCode) {
		String userId;
		try {
			userId = getCurrentUserId();
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		Set<String> perms = null;
		OmsAuthorizationInfo authorizationInfo = null;
		final MapCache<Object, AuthorizationInfo> mapCache = (MapCache<Object, AuthorizationInfo>) authorizationCache;
		if (null != mapCache) {
			authorizationInfo = (OmsAuthorizationInfo) mapCache.get(userId);

			if (null != authorizationInfo) {
				perms = authorizationInfo.getStringPermissions();
				log.info("Get auth data from cache.");
			} else {
				log.warn("authorizationInfo is null, will reload auth data from db!");
				authorizationInfo = (OmsAuthorizationInfo) loadAuthorizationInfoFromDB(userId);
				mapCache.put(userId, authorizationInfo);
			}
		} else {
			log.warn("The mapCache is null, auth data will be loaded from db!");
		}

		if (null != perms && !"".equals(perms)) {
			perms = authorizationInfo.getStringPermissions();
		}
		if (perms != null)
			for (final String perm : perms) {
				if (permCode.equals(perm)) {
					return true;
				}
			}

		return false;
	}

	@Override
	public List<PermissionDto> getPermTree() {
		log.info(WebUtils.getSessionInfo());

		String userId;
		try {
			userId = getCurrentUserId();
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		List<PermissionDto> perms = null;
		OmsAuthorizationInfo authorizationInfo = null;
		MapCache<Object, AuthorizationInfo> mapCache = (MapCache<Object, AuthorizationInfo>) authorizationCache;
		if (null != mapCache) {
			authorizationInfo = (OmsAuthorizationInfo) mapCache.get(userId);

			if (null != authorizationInfo) {
				perms = authorizationInfo.getPermissions();
				log.info("Get auth data from cache.");
			} else {
				log.warn("authorizationInfo is null, will reload auth data from db!");
				authorizationInfo = (OmsAuthorizationInfo) loadAuthorizationInfoFromDB(userId);
				mapCache.put(userId, authorizationInfo);
			}
		} else {
			log.warn("The mapCache is null, auth data will be loaded from db!");
		}

		if (null == perms || "".equals(perms)) {
			perms = authorizationInfo.getPermissions();
		}

		return perms;
	}

	private String getCurrentUserId() throws Exception {
		if (SessionUtil.getCurrentUser() != null)
			return SessionUtil.getCurrentUser().getUserid();
		return null;
	}

	public void clearCachedByUser(final PrincipalCollection principals) {
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 当缓存中没有权限数据，此方法会被调用，目前登录时调用了initAuthorizationCache方法加载缓存，即此方法不会被调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
		log.debug("doGetAuthorizationInfo......");
		final String userId = (String) principals.getPrimaryPrincipal();

		try {
			return initAuthorizationCache(userId);
		} catch (CacheException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
			throws AuthenticationException {/*
											 * //System.out.println(
											 * "doGetAuthenticationInfo......");
											 * 
											 * final UsernamePasswordToken
											 * userToken =
											 * (UsernamePasswordToken) token;
											 * final String password =
											 * String.valueOf(userToken.
											 * getPassword());
											 * 
											 * final String ENABLE_AUTH =
											 * SpringUtil.
											 * getWebContextInitParameter(
											 * "ENABLE_AUTH");
											 * 
											 * if ("true".equalsIgnoreCase(
											 * ENABLE_AUTH)){ if
											 * (!webUserService.userLogin(
											 * userToken.getUsername(),
											 * password)) {// 用户名/密码错误 throw new
											 * IncorrectCredentialsException();
											 * } }
											 * 
											 * return new
											 * SimpleAuthenticationInfo(
											 * userToken.getUsername(),
											 * password, getName());
											 */

		// System.out.println("doGetAuthenticationInfo......");

		final UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		final String password = String.valueOf(userToken.getPassword());

		final String ENABLE_AUTH = SpringUtil.getWebContextInitParameter("ENABLE_AUTH");

		if ("true".equalsIgnoreCase(ENABLE_AUTH)) {
			// final String retCode =
			// loginFacade.VerifyLogin2Acc(userToken.getUsername(), password);

			UserDto user = null;
			// if ("-1".equalsIgnoreCase(retCode))
			// {
			// throw new UnknownAccountException();// 未知的用户账号
			// }
			// else if ("-2".equalsIgnoreCase(retCode))// 用户名/密码错误
			// {
			// throw new IncorrectCredentialsException();
			// }
			// else if ("-3".equalsIgnoreCase(retCode))// 用户被ACC禁用
			// {
			// throw new AuthenticationException("用户被禁用,请联系管理员!");
			// }
			// else if ("0".equalsIgnoreCase(retCode)) // 登陆成功
			{
				try {
					if (!webUserService.userLogin(userToken.getUsername(), password)) {
						throw new IncorrectCredentialsException();
					}else{
						user = webUserService.findUser(userToken.getUsername());
					}
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
					throw new IncorrectCredentialsException(e);
				}

				if (user != null) {
					if (!"1".equalsIgnoreCase(user.getStatus())) {
						throw new AuthenticationException("用户被禁用,请联系管理员!");
					}

					try {
						long ib = System.currentTimeMillis();

						initAuthorizationCache(user.getUserid());

						SessionUtil.setCurrentUser(user);

						long ie = System.currentTimeMillis();
						long ir = ie - ib;
						log.debug(ir + "===========initAuthorizationCache");

					} catch (CacheException e) {
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				} else {
					throw new AuthenticationException("用户不存在OMS系统,请联系管理员,添加用户!");
					// user =
					// loginFacade.getEmployInfo2Acc(userToken.getUsername());
					// if (user != null)
					// {
					// user.setAuthuserid(userToken.getUsername());
					// user.setUserpwd("");
					// user.setStatus("1");
					// user.setCreator(userToken.getUsername());
					// user.setCreatetime(DateTools.formatDate(new Date()));
					// user.setEmployeeno(userToken.getUsername());
					// userFacade.create(user);
					// }
				}

				// if (Boolean.TRUE.equals(user.getLocked())) {
				// throw new LockedAccountException(); // 帐号锁定
				// }
			}
			// else
			// {
			// throw new AuthenticationException(retCode);
			// }
		}

		return new SimpleAuthenticationInfo(userToken.getUsername(), password, getName());

	}

	// @Override
	// public void setAuthorizationCache(final Cache<Object, AuthorizationInfo>
	// authorizationCache)
	// {
	// // TODO Auto-generated method stub
	// // System.out.println("setAuthorizationCache......");
	// super.setAuthorizationCache(authorizationCache);
	// }

	// @Override
	// public Cache<Object, AuthorizationInfo> getAuthorizationCache()
	// {
	// return super.getAuthorizationCache();
	// }

	@Override
	protected void afterCacheManagerSet() {
		// TODO Auto-generated method stub
		// System.out.println("afterCacheManagerSet......");
		super.afterCacheManagerSet();
	}

	@Override
	protected void clearCachedAuthorizationInfo(final PrincipalCollection principals) {
		// TODO Auto-generated method stub
		// System.out.println("clearCachedAuthorizationInfo......");
		// final String userId = (String) principals.getPrimaryPrincipal();
		// System.out.println("remove AuthorizationInfo from cache : " +
		// userService.findStringPermissions(userId));
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	protected void doClearCache(final PrincipalCollection principals) {
		super.doClearCache(principals);
	}

	@Override
	protected Object getAuthorizationCacheKey(final PrincipalCollection principals) {
		return principals.getPrimaryPrincipal();
	}

}
