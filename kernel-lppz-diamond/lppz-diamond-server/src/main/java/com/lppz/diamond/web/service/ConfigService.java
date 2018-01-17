package com.lppz.diamond.web.service;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;

@Service
public class ConfigService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ProjectService projectService;
	
	public List<Map<String, Object>> queryConfigs(Long projectId, Long moduleId, int offset, int limit) {
		String sql = "SELECT * FROM conf_project_config a, conf_project_module b "
				+ "WHERE a.MODULE_ID = b.MODULE_ID AND a.DELETE_FLAG =0 AND a.PROJECT_ID=? ";
		
		if(moduleId != null) {
			sql = sql + " AND a.MODULE_ID = ? order by a.MODULE_ID limit ?,?";
			return jdbcTemplate.queryForList(sql, projectId, moduleId, offset, limit);
		} else {
			sql = sql + " order by a.MODULE_ID limit ?,?";
			return jdbcTemplate.queryForList(sql, projectId, offset, limit);
		}
		
	}
	
	public long queryConfigCount(Long projectId, Long moduleId) {
		String sql = "SELECT count(*) FROM conf_project_config a, conf_project_module b "
				+ "WHERE a.MODULE_ID = b.MODULE_ID AND a.DELETE_FLAG =0 AND a.PROJECT_ID=? ";
		
		if(moduleId != null) {
			sql = sql + " AND a.MODULE_ID = ? order by a.MODULE_ID";
			return jdbcTemplate.queryForObject(sql, Long.class, projectId, moduleId);
		} else {
			sql = sql + " order by a.MODULE_ID";
			return jdbcTemplate.queryForObject(sql, Long.class, projectId);
		}
	}
	
	public String queryConfigs(String projectCode, String type, String format) {
		String sql = "SELECT * FROM conf_project_config a, conf_project_module b, conf_project c " +
				"WHERE a.MODULE_ID = b.MODULE_ID AND a.PROJECT_ID=c.id AND a.DELETE_FLAG =0 AND c.PROJ_CODE=? ORDER BY b.MODULE_ID,a.CONFIG_KEY";
		List<Map<String, Object>> configs = jdbcTemplate.queryForList(sql, projectCode);
		if("php".equals(format)) {
			return viewConfigPhp(configs, type);
		} else if("json".equals(format)) {
			return viewConfigJson(configs, type);
		} else if("preview".equals(format)){
			return previewConfig(configs, type);
		} else
			return viewConfig(configs, type);
	}
	
	public String queryConfigsForView(String projectCode, String type, String format, String moduleId) {
		StringBuilder sql = new StringBuilder("SELECT * FROM conf_project_config a, conf_project_module b, conf_project c " +
				"WHERE a.MODULE_ID = b.MODULE_ID AND a.PROJECT_ID=c.id AND a.DELETE_FLAG =0 AND c.PROJ_CODE=? ");
		if (StringUtils.isNotBlank(moduleId)) {
			sql.append("AND A.MODULE_ID=? ");
		}
		sql.append(" ORDER BY b.MODULE_ID ,a.CONFIG_KEY");
		List<Map<String, Object>> configs = null;
		
		if (StringUtils.isNotBlank(moduleId)) {
			configs = jdbcTemplate.queryForList(sql.toString(), projectCode, moduleId);
		}else{
			configs = jdbcTemplate.queryForList(sql.toString(), projectCode);
		}
		if("php".equals(format)) {
			return viewConfigPhp(configs, type);
		} else if("json".equals(format)) {
			return viewConfigJson(configs, type);
		} else if("preview".equals(format)){
			return previewConfig(configs, type);
		}else
			return viewConfig(configs, type);
	}
	
	public String queryModules(String projectCode, String type, String format) {
		String sql = "SELECT * FROM conf_project_config a, conf_project_module b, conf_project c " +
				"WHERE a.MODULE_ID = b.MODULE_ID AND a.PROJECT_ID=c.id AND a.DELETE_FLAG =0 AND c.PROJ_CODE=?";
		List<Map<String, Object>> configs = jdbcTemplate.queryForList(sql, projectCode);
		if("php".equals(format)) {
			return viewModulesPhp(configs, type);
		} else if("json".equals(format)) {
			return viewModulesJson(configs, type);
		} else
			return viewModules(configs, type);
	}
	
	private String viewModulesJson(List<Map<String, Object>> configs,
			String type) {
		// TODO Auto-generated method stub
		return null;
	}

	private String viewModulesPhp(List<Map<String, Object>> configs, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	private String viewModules(List<Map<String, Object>> configs, String type) {
		String message = "";
		String value = null;
		Set<String> moduleSet = new HashSet<>();
		
		for(Map<String, Object> map : configs) {
			value = (String)map.get("MODULE_NAME");
			if("development".equals(type)) {
				moduleSet.add(value);
			} else if("production".equals(type)) {
				moduleSet.add(value);
			} else if("test".equals(type)) {
				moduleSet.add(value);
			} else if("build".equals(type)) {
				moduleSet.add(value);
			}
		}
		for (String module : moduleSet) {
			message += module + ",";
		}
		
		return message.substring(0, message.length()-1);
	}

	public String queryConfigs(String projectCode, String[] modules, String type, String format) {
		String sql = "SELECT * FROM conf_project_config a, conf_project_module b, conf_project c " +
				"WHERE a.MODULE_ID = b.MODULE_ID AND a.PROJECT_ID=c.id AND a.DELETE_FLAG =0 AND c.PROJ_CODE=? "
				+ "AND b.MODULE_NAME in ('" + StringUtils.join(modules, "','") + "') ORDER BY b.MODULE_ID";
		
		List<Map<String, Object>> configs = jdbcTemplate.queryForList(sql, projectCode);
		if("php".equals(format)) {
			return viewConfigPhp(configs, type);
		} else if("json".equals(format)) {
			return viewConfigJson(configs, type);
		} else
			return viewConfig(configs, type);
	}
	
	public String queryValue(String projectCode, String module, String key, String type) {
		String sql = "SELECT * FROM conf_project_config a, conf_project_module b, conf_project c " +
				"WHERE a.MODULE_ID = b.MODULE_ID AND a.PROJECT_ID=c.id AND a.DELETE_FLAG =0 AND c.PROJ_CODE=? "
				+ "AND b.MODULE_NAME=? AND a.CONFIG_KEY=?";
		Map<String, Object> config = jdbcTemplate.queryForMap(sql, projectCode, module, key);
		if("development".equals(type)) {
			return (String)config.get("CONFIG_VALUE");
		} else if("production".equals(type)) {
			return (String)config.get("PRODUCTION_VALUE");
		} else if("test".equals(type)) {
			return (String)config.get("TEST_VALUE");
		} else if("build".equals(type)) {
			return (String)config.get("BUILD_VALUE");
		} else
			return "";
	}
	
	@Transactional
	public void insertConfig(String configKey, String configValue, String configDesc, Long projectId, Long moduleId, String user) {
        String sql = "SELECT MAX(CONFIG_ID)+1 FROM conf_project_config";
        long id = 1;
		try {
			id = jdbcTemplate.queryForObject(sql, Long.class);
		} catch(NullPointerException e) {
			;
		}

        sql = "INSERT INTO conf_project_config(CONFIG_ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_DESC,PROJECT_ID,MODULE_ID,DELETE_FLAG,OPT_USER,OPT_TIME," +
				"PRODUCTION_VALUE,PRODUCTION_USER,PRODUCTION_TIME,TEST_VALUE,TEST_USER,TEST_TIME,BUILD_VALUE,BUILD_USER,BUILD_TIME) "
				+ "VALUES (?,?,?,?,?,?,0,?,?,?,?,?,?,?,?,?,?,?)";
		Date time = new Date();
		jdbcTemplate.update(sql, id, configKey, configValue, configDesc, projectId, moduleId, user, time,
				configValue, user, time, configValue, user, time, configValue, user, time);
		
		projectService.updateVersion(projectId);
	}
	
	@Transactional
	public void updateConfig(String type, Long configId, String configKey, String configValue, String configDesc, Long projectId, Long moduleId, String user) {
		if("development".equals(type)) {
			String sql = "update conf_project_config set CONFIG_KEY=?,CONFIG_VALUE=?,CONFIG_DESC=?,PROJECT_ID=?,MODULE_ID=?,OPT_USER=?,OPT_TIME=? where CONFIG_ID=?";
			jdbcTemplate.update(sql, configKey, configValue, configDesc, projectId, moduleId, user, new Date(), configId);
			projectService.updateVersion(projectId, type);
		} else if("production".equals(type)) {
			String sql = "update conf_project_config set CONFIG_KEY=?,PRODUCTION_VALUE=?,CONFIG_DESC=?,PROJECT_ID=?,MODULE_ID=?,PRODUCTION_USER=?,PRODUCTION_TIME=? where CONFIG_ID=?";
			jdbcTemplate.update(sql, configKey, configValue, configDesc, projectId, moduleId, user, new Date(), configId);
			projectService.updateVersion(projectId, type);
		} else if("test".equals(type)) {
			String sql = "update conf_project_config set CONFIG_KEY=?,TEST_VALUE=?,CONFIG_DESC=?,PROJECT_ID=?,MODULE_ID=?,TEST_USER=?,TEST_TIME=? where CONFIG_ID=?";
			jdbcTemplate.update(sql, configKey, configValue, configDesc, projectId, moduleId, user, new Date(), configId);
			projectService.updateVersion(projectId, type);
		} else if("build".equals(type)) {
			String sql = "update conf_project_config set CONFIG_KEY=?,BUILD_VALUE=?,CONFIG_DESC=?,PROJECT_ID=?,MODULE_ID=?,BUILD_USER=?,BUILD_TIME=? where CONFIG_ID=?";
			jdbcTemplate.update(sql, configKey, configValue, configDesc, projectId, moduleId, user, new Date(), configId);
			projectService.updateVersion(projectId, type);
		}
	}
	
	public void deleteConfig(Long id, Long projectId) {
//		String sql = "update conf_project_config set DELETE_FLAG=1 where CONFIG_ID=?";
		String sql = "delete from conf_project_config where CONFIG_ID=?";
		jdbcTemplate.update(sql, id);
		projectService.updateVersion(projectId);
	}
	
	private String viewConfig(List<Map<String, Object>> configs, String type) {
		String message = "";
		String lastModuleName = null;
		String currentNoduleName = null;
		boolean versionFlag = true;
		for(Map<String, Object> map : configs) {
			if(versionFlag) {
				if("development".equals(type)) {
					message += "#version = " + map.get("DEVELOPMENT_VERSION") + "\r\n";
				} else if("production".equals(type)) {
					message += "#version = " + map.get("PRODUCTION_VERSION") + "\r\n";
				} else if("test".equals(type)) {
					message += "#version = " + map.get("TEST_VERSION") + "\r\n";
				} else if("build".equals(type)) {
					message += "#version = " + map.get("BUILD_VERSION") + "\r\n";
				}
				
				versionFlag = false;
			}
			currentNoduleName = (String) map.get("MODULE_NAME");
			if (!currentNoduleName.equals(lastModuleName)) {
				lastModuleName = currentNoduleName;
				message += "MODULE_NAME" + " = " + currentNoduleName + "\r\n";
			}
			
			String desc = (String)map.get("CONFIG_DESC");
			desc = desc.replaceAll("\r\n", " ");
			if(StringUtils.isNotBlank(desc))
				message += "#" + desc + "\r\n";
			
			if("development".equals(type)) {
				message += map.get("CONFIG_KEY") + " = " + map.get("CONFIG_VALUE") + "\r\n";
			} else if("production".equals(type)) {
				message += map.get("CONFIG_KEY") + " = " + map.get("PRODUCTION_VALUE") + "\r\n";
			} else if("test".equals(type)) {
				message += map.get("CONFIG_KEY") + " = " + map.get("TEST_VALUE") + "\r\n";
			} else if("build".equals(type)) {
				message += map.get("CONFIG_KEY") + " = " + map.get("BUILD_VALUE") + "\r\n";
			}
		}
		
		return message;
	}
	
	private String previewConfig(List<Map<String, Object>> configs, String type) {
		String message = "";
		String lastModuleName = null;
		String currentNoduleName = null;
		boolean versionFlag = true;
		for(Map<String, Object> map : configs) {
			if(versionFlag) {
				if("development".equals(type)) {
					message += "#version = " + map.get("DEVELOPMENT_VERSION") + "\r\n";
				} else if("production".equals(type)) {
					message += "#version = " + map.get("PRODUCTION_VERSION") + "\r\n";
				} else if("test".equals(type)) {
					message += "#version = " + map.get("TEST_VERSION") + "\r\n";
				} else if("build".equals(type)) {
					message += "#version = " + map.get("BUILD_VERSION") + "\r\n";
				}
				
				versionFlag = false;
			}
			currentNoduleName = (String) map.get("MODULE_NAME");
			if (!currentNoduleName.equals(lastModuleName)) {
				lastModuleName = currentNoduleName;
				message += "MODULE_NAME" + " = " + currentNoduleName + "\r\n";
			}
			
			String desc = (String)map.get("CONFIG_DESC");
			if(StringUtils.isNotBlank(desc)){
				desc = desc.replaceAll("\r\n", " ");
				message += "\t#" + desc + "\r\n";
			}
			
			if("development".equals(type)) {
				message += "\t" + map.get("CONFIG_KEY") + " = " + map.get("CONFIG_VALUE") + "\r\n";
			} else if("production".equals(type)) {
				message += "\t" + map.get("CONFIG_KEY") + " = " + map.get("PRODUCTION_VALUE") + "\r\n";
			} else if("test".equals(type)) {
				message += "\t" + map.get("CONFIG_KEY") + " = " + map.get("TEST_VALUE") + "\r\n";
			} else if("build".equals(type)) {
				message += "\t" + map.get("CONFIG_KEY") + " = " + map.get("BUILD_VALUE") + "\r\n";
			}
		}
		
		return message;
	}
	
	private String viewConfigPhp(List<Map<String, Object>> configs, String type) {
		String message = "<?php\r\n"
						+ "return array(\r\n"
						+ "\t//profile = " + type + "\r\n";
		
		boolean versionFlag = true;
		for(Map<String, Object> map : configs) {
			if(versionFlag) {
				if("development".equals(type)) {
					message += "\t//version = " + map.get("DEVELOPMENT_VERSION") + "\r\n";
				} else if("production".equals(type)) {
					message += "\t//version = " + map.get("PRODUCTION_VERSION") + "\r\n";
				} else if("test".equals(type)) {
					message += "\t//version = " + map.get("TEST_VERSION") + "\r\n";
				} else if("build".equals(type)) {
					message += "\t//version = " + map.get("BUILD_VALUE") + "\r\n";
				}
				
				versionFlag = false;
			}
			
			String desc = (String)map.get("CONFIG_DESC");
			if(StringUtils.isNotBlank(desc))
				message += "\t//" + desc + "\r\n";
			
			if("development".equals(type)) {
				message += "\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("CONFIG_VALUE"));
			} else if("production".equals(type)) {
				message += "\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("PRODUCTION_VALUE"));
			} else if("test".equals(type)) {
				message += "\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("TEST_VALUE"));
			} else if("build".equals(type)) {
				message += "\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("BUILD_VALUE"));
			}
		}

		message += ");\r\n";
		
		return message;
	}
	
	private String viewConfigJson(List<Map<String, Object>> configs, String type) {
		Map<String, Object> confMap = new LinkedHashMap<String, Object>();
		boolean versionFlag = true;
		for(Map<String, Object> map : configs) {
			if(versionFlag) {
				if("development".equals(type)) {
					confMap.put("version", map.get("DEVELOPMENT_VERSION"));
				} else if("production".equals(type)) {
					confMap.put("version", map.get("PRODUCTION_VERSION"));
				} else if("test".equals(type)) {
					confMap.put("version", map.get("TEST_VERSION"));
				} else if("build".equals(type)) {
					confMap.put("version", map.get("BUILD_VALUE"));
				}
				
				versionFlag = false;
			}
			
			if("development".equals(type)) {
				confMap.put(map.get("CONFIG_KEY").toString(), map.get("CONFIG_VALUE"));
			} else if("production".equals(type)) {
				confMap.put(map.get("CONFIG_KEY").toString(), map.get("PRODUCTION_VALUE"));
			} else if("test".equals(type)) {
				confMap.put(map.get("CONFIG_KEY").toString(), map.get("TEST_VALUE"));
			} else if("build".equals(type)) {
				confMap.put(map.get("CONFIG_KEY").toString(), map.get("BUILD_VALUE"));
			}
		}
		
		return JSONUtils.toJSONString(confMap);
	}
	
	private String convertType(Object value) {
		String conf = String.valueOf(value).trim();
		if("true".equals(conf) || "false".equals(conf)) {
			return  conf + ",\r\n";
		} else if(isNumeric(conf)) {
			return  conf + ",\r\n";
		}else  {
			return  "'" + conf + "',\r\n";
		}
	}
	
	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[0-9]*$");
		else
			return false;
	}
}
