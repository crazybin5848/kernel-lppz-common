package com.lppz.oms.service.auth.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lppz.oms.api.dto.PermissionDto;
import com.lppz.oms.persist.AuthPermDao;
import com.lppz.oms.service.auth.AuthPermService;

@Service("authPermService")
public class AuthPermServiceImpl implements AuthPermService {
	private static final Logger logger = LoggerFactory.getLogger(AuthPermServiceImpl.class);

	@Autowired
	private AuthPermDao authPermDao; 
	@javax.annotation.Resource
	DefaultSqlSessionFactory sqlSessionFactory; 
	
	@Override
	public List<Map> findPaged(Map<String, Object> params, PageBounds page) {
		return authPermDao.findPaged(params, page);
	}

	@Override
	public List<PermissionDto> getPermissionsByPermType(String permtype) {
		List<PermissionDto> permList = authPermDao.getPermissionsByPermType(permtype);
		//根据配置判断是否开启菜单跟节点
		String rootisenable = ConfigPropertiesUtils.getKey("perm.root.isenable");
		String rootcode = ConfigPropertiesUtils.getKey("perm.root.permcode");
		
		if(StringUtils.equals("1", rootisenable)){
			PermissionDto rootdto = new PermissionDto();
			rootdto.setPermcode(rootcode);
			rootdto.setMenuname("菜单根节点");
			rootdto.setParentcode("");
			permList.add(rootdto);
		}
		return permList;
	}

	@Override
	public PermissionDto getByPermCode(String permCode) {
		PermissionDto perm = null;
		try
		{
			perm = authPermDao.getByPermCode(permCode);
		}
		catch (final Exception e)
		{
			logger.error("get perm by permCode error", e);
		}
		logger.info("**** get end ****");
		return perm;
	}

	@Override
	@Transactional
	public boolean createPerm(PermissionDto perm) {
		try
		{
			PermissionDto newPerm = generatePermindex(perm);
			perm.setPermindex(newPerm.getPermindex());
			
			authPermDao.createPerm(perm);
		}
		catch (final Exception e)
		{
			logger.error("create perm error", e);
			return false;
		}
		return true;
	}

	private synchronized PermissionDto generatePermindex(PermissionDto perm) throws Exception
	{
		PermissionDto newPerm = authPermDao.getMaxPermindex(perm.getPermtype());
		int index = Integer.valueOf(newPerm.getPermindex()) + 1;
		newPerm.setPermindex(String.valueOf(index));
		
		return newPerm;
	}
	
	@Override
	public PermissionDto getMaxPermindex(PermissionDto perm)
	{
		PermissionDto newPerm = null;
		
		try
		{
			String type = "func".equalsIgnoreCase(perm.getPermtype()) ? "F" : "M";
			String code = "";
			if (!StringUtils.isBlank(perm.getParentcode()))
			{
				String[] strArray = perm.getParentcode().split("-");
				if (null != strArray && strArray.length >= 2)
				{
					code = strArray[1];
				}
			}
			
			newPerm = generatePermindex(perm);
			
			if (null != newPerm)
			{
				if ("func".equalsIgnoreCase(perm.getPermtype()))
				{
					newPerm.setPermcode(type + "_" + code + "_" + newPerm.getPermindex());
				}
				else if ("menu".equalsIgnoreCase(perm.getPermtype()))
				{
					newPerm.setPermcode(type + "-" + code + "_" + newPerm.getPermindex());
				}
			}
		}
		catch (final Exception e)
		{
			logger.error("get getMaxPermindex by permCode error", e);
		}
		return newPerm;
	}
	
	@Override
	@Transactional
	public boolean deletePerm(PermissionDto perm)
	{
		try
		{
			this.authPermDao.deletePerm(perm.getPermcode());
		}
		catch (final Exception e)
		{
			logger.error("delete perm error", e);
			return false;
		}
		return true;
	}
	
	@Override
	@Transactional
	public boolean updatePerm(PermissionDto perm)
	{
		try
		{
			this.authPermDao.updatePerm(perm);
		}
		catch (final Exception e)
		{
			logger.error("update perm error", e);
			return false;
		}
		return true;
	}
}
class ConfigPropertiesUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(ConfigPropertiesUtils.class);

	final static Resource resource = new ClassPathResource("/META-INF/dubbo.properties");

	public static Properties getObject()
	{
		Properties props = null;
		try
		{
			props = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return props;
	}

	public static String getKey(final String keystr)
	{
		String restr = null;
		try
		{
			final Properties props = PropertiesLoaderUtils.loadProperties(resource);
			restr = props.getProperty(keystr);
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return restr;
	}
}
