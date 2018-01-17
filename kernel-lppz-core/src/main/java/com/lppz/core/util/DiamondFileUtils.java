package com.lppz.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import redis.clients.zkprocess.entity.LpJedisClusterYamlBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lppz.bean.DiamondBean;
import com.lppz.bean.DiamondClusterBean;
import com.lppz.bean.DiamondClusterNodeBean;
import com.lppz.bean.DiamondDataSourceBean;
import com.lppz.bean.PortBean;
import com.lppz.configuration.es.EsBaseYamlBean;
import com.lppz.configuration.es.EsClusterPool;
import com.lppz.configuration.hbase.HBaseConfigYamlBean;
import com.lppz.configuration.jedis.JedisClusterPool;
import com.lppz.configuration.jedis.JedisClusterYamlBean;
import com.lppz.configuration.jedis.JedisSentinelYamlBean;
import com.lppz.configuration.mongo.MongoConfigBean;
import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.diamond.client.PropertiesConfiguration;
import com.lppz.util.exception.DiamondNodeFullException;
import com.lppz.util.exception.DiamondNodeNotExistException;
import com.lppz.util.net.NetUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 配置中心属性输出文件工具类
 * yaml文件没有使用yaml.dump(obj,writer)，因为考虑到页面预览配置需要输出文件内容，这里统一把获取到的属性列表转为字符串
 * properties文件没有用properties.store(writer,comments),因为":"输出文件时会添加\
 * @author licheng
 *
 */
public class DiamondFileUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DiamondFileUtils.class);
	private static DiamondBean diamondBean = null;
	private static PropertiesConfiguration config;
	private static String project_Name;
	
	/**
	 * 从配置中心获取服务启动的配置，并生成配置文件到相应的目录
	 */
	public static void loadProjProperties(String ... projectName){
		PortBean portBean = null;
		project_Name = projectName.length == 0?null:projectName[0];
		if (diamondBean != null && !diamondBean.isUseLocal()) {
			try {
				String host = diamondBean.getServerAddress();
				int port = diamondBean.getServerPort();
				String projCode = diamondBean.getProjCode();
				String profile = diamondBean.getProfile();
				config = new PropertiesConfiguration(host, port, projCode, profile, "", false);
				Map<String,Properties> properties = config.getPropertiesMap();
				
				if (properties != null && !properties.isEmpty()) {
					portBean = getCurrentPort(properties);
					for (Entry<String, Properties> entry : properties.entrySet()) {
						DiamondFileUtils.createFile(entry.getKey(), entry.getValue(), portBean);
					}
					if(diamondBean.getListener()!=null){
						config.setReloadable(true);
						config.addConfigurationListener(diamondBean.getListener());
						config.reloadableThread(projCode, profile);
					}
				}
			} catch (DiamondNodeFullException e){
				logger.error("当前机器配置服务已满",e);
				System.exit(-1);
			} catch (DiamondNodeNotExistException e){
				logger.error("当前启动机器不在配置列表中",e);
				System.exit(-1);
			} catch (Exception e) {
				logger.error("加载配置中心信息异常",e);
				System.exit(-1);
			}
		}
	}
	
	public static void closeClient(){
		if (config != null) {
			config.close();
		}
	}
	
	private static PortBean getCurrentPort(Map<String, Properties> propertires) 
			throws UnknownHostException, DiamondNodeFullException, DiamondNodeNotExistException {
		int minPort = 0;
		Properties properties = propertires.get(CLUSTER_YAML_NAME);
		if (properties != null) {
			try {
				Map<String, DiamondClusterBean> nodeMap = DiamondFileUtils.buildDiamondClusterMap(properties);
				InetAddress addr = InetAddress.getLocalHost();
				String ip=addr.getHostAddress().toString(); //获取本机ip
				String hostName=addr.getHostName().toString(); //获取本机计算机名称  
				DiamondClusterBean clusterBean = nodeMap.get(hostName);
				if (clusterBean != null && CollectionUtils.isNotEmpty(clusterBean.getClusterNodes())) {
					List<DiamondClusterNodeBean> portsList = clusterBean.getClusterNodes();
					for (DiamondClusterNodeBean nodeBean : portsList) {
						if (nodeBean.getPorts() != null && nodeBean.getPorts().size() > 0) {
							Set<Entry<String, Integer>> entrySet = nodeBean.getPorts().entrySet();
							boolean isUsing = false;
							for (Entry<String, Integer> entry : entrySet) {
								isUsing = NetUtil.isPortUsing(ip, entry.getValue());
								if (isUsing) {
									break;
								}else{
									if (minPort == 0) {
										minPort = entry.getValue();
									}else{
										minPort=minPort<entry.getValue()?minPort:entry.getValue();
									}
								}
							}
							if (!isUsing) {
								return new PortBean(minPort, nodeBean.getPorts(), nodeBean.getParams());
							}
						}
					}
				}else if (clusterBean == null) {
					throw new DiamondNodeNotExistException(buildErrorMsg(hostName, nodeMap, properties));
				}
				throw new DiamondNodeFullException(buildErrorMsg(hostName, null, properties));
			} catch (UnknownHostException e) {
				throw e;
			}
		}
		return null;
	}
	
	private static String buildErrorMsg(String hostName,Map<String, DiamondClusterBean> nodeMap, Properties properties){
		StringBuilder sb = new StringBuilder();
		sb.append("serveraddress:").append(diamondBean.getServerAddress())
		.append(",serverport:").append(diamondBean.getServerPort())
		.append(",projcode:").append(diamondBean.getProjCode())
		.append(",projfile:").append(diamondBean.getProfile())
		.append(",hostname:").append(hostName);
		if (nodeMap != null) {
			sb.append(" not in ").append(nodeMap);
		}
		sb.append(" properties : ").append(properties)
		.append(" properties size : ").append(properties.size());
		return sb.toString();
	}
	
	public static void createFile(String module, Properties properties, PortBean portBean) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
			, MalformedURLException, NoSuchMethodException, SecurityException {
		logger.debug("module {} properties {}", module, properties);
		String basePath = getBasePath(portBean==null?null:portBean.getMinPort());
		
		if (module.equals(DUBBO_PRO_FILE_NAME)) {
			writeFile(buildMetaPath(basePath, module), buildPropertiesFile(properties, portBean));
		}else if (module.endsWith(PROPERTIES_SUFFIX)) {
			writeFile(buildNomalPath(basePath, module), buildPropertiesFile(properties, portBean));
		}else if(module.matches(DATASOURCE_FILE_REGEX)){
			writeDatasourceFile(buildMetaPath(basePath, module), properties);
		}else if(module.matches(ES_FILE_REGEX)){		
			writeEsYamlFile(buildMetaPath(basePath, module), properties);
		}else if(module.matches(JEDIS_CLUSTER_FILE_REGEX)){
			writeJedisClusterYamlFile(buildMetaPath(basePath, module), properties);
		}else if(module.matches(JEDIS_SENTINEL_FILE_REGEX)){
			writeJedisSentinelYamlFile(buildMetaPath(basePath, module), properties);
		}else if(HBASE_YAML_NAME.equals(module)){
			writeHbaseYamlFile(buildMetaPath(basePath, module), properties);
		}else if(LP_JEDIS_YAML_NAME.equals(module)){
			writeLpJedisYamlFile(buildMetaPath(basePath, module), properties);
		}else if(MONGODB_YAML_NAME.equals(module)){
			writeMongodbFile(buildMetaPath(basePath, module), properties);
		}
	}
	
	/**
	 * !!redis.clients.zkprocess.entity.LpJedisClusterYamlBean
		jedisClusterPool: {blockWhenExhausted: false, maxIdle: '5000', maxTotal: '50000',
  		maxWaitMillis: '10000', numTestsPerEvictionRun: '100', testOnBorrow: false, testOnReturn: false}
		redisNodePrefix: node
		sharding: 'fuck'
		timeout: 5000
		zkNodes: 192.168.37.246:2181
	 * @param buildMetaPath
	 * @param properties
	 */
	private static void writeLpJedisYamlFile(String filePath,
			Properties properties) {
		LpJedisClusterYamlBean jedisYamlBean = new LpJedisClusterYamlBean();
		redis.clients.zkprocess.entity.JedisClusterPool clusterPool = new redis.clients.zkprocess.entity.JedisClusterPool();
		clusterPool.setBlockWhenExhausted(Boolean.valueOf(properties.getProperty(JEDIS_BLOCKWHENEXHAUSTED_KEY,FALSE)));
		clusterPool.setMaxIdle(properties.getProperty(MAXIDLE_KEY));
		clusterPool.setMaxTotal(properties.getProperty(MAXTOTAL_KEY));
		clusterPool.setMaxWaitMillis(properties.getProperty(MAXWAITMILLIS_KEY));
		clusterPool.setNumTestsPerEvictionRun(properties.getProperty(JEDIS_NUMTESTSPEREVICTIONRUN_KEY));
		clusterPool.setTestOnBorrow(Boolean.valueOf(properties.getProperty(JEDIS_TESTONBORROW_KEY,FALSE)));
		clusterPool.setTestOnReturn(Boolean.valueOf(properties.getProperty(JEDIS_TESTONRETURN_KEY,FALSE)));
		jedisYamlBean.setJedisClusterPool(clusterPool);
		jedisYamlBean.setTimeout(Integer.parseInt(properties.getProperty(TIMEOUT_KEY,DEFAULT_VALUE_50000)));
		jedisYamlBean.setRedisNodePrefix(properties.getProperty("redisNodePrefix"));
		jedisYamlBean.setSharding(properties.getProperty("sharding"));
		jedisYamlBean.setZkNodes(properties.getProperty("zkNodes"));
		writeFile(filePath, buildYamlContents(jedisYamlBean));
	}
	
	/**
	 * !!com.lppz.configuration.hbase.HBaseConfigYamlBean {
		zkClientPort: '2181',
		zkQuorum: 'hanode6,hanode7,hanode3,hanode4,hanode5',
		hbaseRpcTimeOut: 30000,
		hbaseScanTimeoutPeriod: 60000,
		hbaseClientScannerCaching: 20
		}
	 * @param buildMetaPath
	 * @param properties
	 */
	private static void writeHbaseYamlFile(String filePath,
			Properties properties) {
		HBaseConfigYamlBean yamlBean = new HBaseConfigYamlBean();
		yamlBean.setZkClientPort(properties.getProperty("zkClientPort"));
		yamlBean.setZkQuorum(properties.getProperty("zkQuorum"));
		yamlBean.setHbaseRpcTimeOut(Long.valueOf(properties.getProperty("hbaseRpcTimeOut","30000")));
		yamlBean.setHbaseScanTimeoutPeriod(Integer.valueOf(properties.getProperty("hbaseScanTimeoutPeriod","60000")));
		yamlBean.setHbaseClientScannerCaching(Long.valueOf(properties.getProperty("hbaseClientScannerCaching","20")));
		
		writeFile(filePath, buildYamlContents(yamlBean));
	}

	private static String getBasePath(Integer basePort) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
			, MalformedURLException, NoSuchMethodException, SecurityException {
		String basePath = null;
		if (StringUtils.isNotBlank(project_Name)) {
			File baseDir = new File(project_Name);
			if (baseDir.exists()) {
				basePath = baseDir.getAbsolutePath();
			}
		}else{
			File baseDir = new File(DEV_CLASS_PATH);
			if (!baseDir.exists()) {
				baseDir = new File(TOMCAT_CLASS_PATH);
				if (!baseDir.exists()) {
					basePath = buildMiroAppBasePath(basePort);
				}else{
					basePath = baseDir.getAbsolutePath();
				}
			}else{
				basePath = baseDir.getAbsolutePath();
			}
		}
		return basePath;
	}
	
	/**
	 * 构建微服务配置文件基础路径
	 * @param basePort
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 */
	private static String buildMiroAppBasePath(Integer basePort) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException{
		File confDirectory = null;
		String basePath = null;
		if (basePort != null) {
			confDirectory = new File(String.format(MICRO_BAST_PATH,basePort));				
		}else{
			confDirectory = new File(META_INF_PATH);
		}
		if (!confDirectory.exists()) {
			confDirectory.mkdirs();
		}
		
		basePath = confDirectory.getParentFile().getAbsolutePath();
		if(basePort != null){
			File programRootDir = new File(basePath);
			URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
			add.setAccessible(true);
			add.invoke(classLoader, programRootDir.toURI().toURL());
		}
		return basePath;
	}

	private static String buildMetaPath(String basePath,String modual) {
		return new StringBuilder(basePath).append(File.separator).append(META_INF).append(File.separator).append(modual).toString();
	}
	
	private static String buildNomalPath(String basePath,String modual){
		return new StringBuilder(basePath).append(File.separator).append(modual).toString();
	}
	
	public static String buildPropertiesFile(Properties properties, PortBean basePort){
		StringBuilder sb = new StringBuilder();
		Map<String, Integer> portsMap = null;
		Map<String, String> paramsMap = null;
		if (basePort != null) {
			portsMap = basePort.getPortsMap();
			paramsMap = basePort.getParamsMap();
		}
				
		for (Entry<Object,Object> entry :properties.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue()).append(NEXT_LINE);
		}
		String contents = sb.toString();
		//替换端口配置
		if (portsMap != null) {
			Set<Entry<String, Integer>> set = portsMap.entrySet();
			for (Entry<String, Integer> entry : set) {
				contents = contents.replaceAll(String.format(PARAMS_REGEX, entry.getKey()), String.valueOf(entry.getValue()));
			}
		}
		
		//替换其他自定义参数配置
		if (paramsMap != null) {
			Set<Entry<String, String>> set = paramsMap.entrySet();
			for (Entry<String, String> entry : set) {
				contents = contents.replaceAll(String.format(PARAMS_REGEX, entry.getKey()), entry.getValue());
			}
			
		}
		
		 
		contents = parseChaineseToUnioncode(contents);
		return contents;
	}
	
	public static String parseChaineseToUnioncode(String src){
		 StringBuffer unicode = new StringBuffer();
		 
		    for (int i = 0; i < src.length(); i++) {
		 
		        // 取出每一个字符
		        char c = src.charAt(i);
		 
		        if (isChinese(c)) {
		        	// 转换为unicode
		        	unicode.append("\\u" + String.valueOf(Integer.toHexString(c)).toUpperCase());
				}else{
					unicode.append(c);
				}
		    }
		 
		    return unicode.toString();
	}
	
    /**
     * 是否是中文字符<br>
     * 包含中文标点符号<br>
     * 
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D) {
            return true;
        } else if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

	/**
	 * jedisClusterPool: !!com.lppz.configuration.jedis.JedisClusterPool {
 maxTotal: 50000,
 maxIdle: 5000,
 maxWaitMillis: 10000,
 testOnBorrow: false,
 testOnReturn: false,
 blockWhenExhausted: false,
 numTestsPerEvictionRun: 100
},
jedisClusterNode: [{host: "10.7.0.8",port: 6380},{host: "10.7.0.8",port: 6381},{host: "10.7.0.8",port: 6382}],
timeout: 50000,
maxRedirections: 5
	 */
	public static void writeJedisClusterYamlFile(String filePath, Properties properties){
		JedisClusterYamlBean clusterYamlBean = new JedisClusterYamlBean();
		clusterYamlBean.setJedisClusterNode(buildNodeProperties(properties.getProperty(JEDIS_NODE_KEY)));
		JedisClusterPool clusterPool = new JedisClusterPool();
		clusterPool.setBlockWhenExhausted(Boolean.valueOf(properties.getProperty(JEDIS_BLOCKWHENEXHAUSTED_KEY,FALSE)));
		clusterPool.setMaxIdle(properties.getProperty(MAXIDLE_KEY));
		clusterPool.setMaxTotal(properties.getProperty(MAXTOTAL_KEY));
		clusterPool.setMaxWaitMillis(properties.getProperty(MAXWAITMILLIS_KEY));
		clusterPool.setNumTestsPerEvictionRun(properties.getProperty(JEDIS_NUMTESTSPEREVICTIONRUN_KEY));
		clusterPool.setTestOnBorrow(Boolean.valueOf(properties.getProperty(JEDIS_TESTONBORROW_KEY,FALSE)));
		clusterPool.setTestOnReturn(Boolean.valueOf(properties.getProperty(JEDIS_TESTONRETURN_KEY,FALSE)));
		clusterYamlBean.setJedisClusterPool(clusterPool);
		clusterYamlBean.setMaxRedirections(Integer.parseInt(properties.getProperty(JEDIS_MAXREDIRECTIONS_KEY, DEFAULT_VALUE_10)));
		clusterYamlBean.setTimeout(Integer.parseInt(properties.getProperty(TIMEOUT_KEY, DEFAULT_VALUE_50000)));
		
		writeFile(filePath, buildYamlContents(clusterYamlBean));
	}
	
	/**
	 * jedisClusterPool: !!com.lppz.configuration.jedis.JedisClusterPool {
 maxTotal: 50000,
 maxIdle: 5000,
 maxWaitMillis: 10000,
 testOnBorrow: false,
 testOnReturn: false,
 blockWhenExhausted: false,
 numTestsPerEvictionRun: 100
},
jedisClusterNode: [{host: "192.168.37.243",port: 26301},{host: "192.168.37.243",port: 26302}],
timeout: 50000,
masterName: "master1"
	 */
	public static void writeJedisSentinelYamlFile(String filePath, Properties properties){
		JedisSentinelYamlBean jedisSentinelYamlBean = new JedisSentinelYamlBean();
		jedisSentinelYamlBean.setJedisClusterNode(buildNodeProperties(properties.getProperty(JEDIS_NODE_KEY)));
		JedisClusterPool clusterPool = new JedisClusterPool();
		clusterPool.setBlockWhenExhausted(Boolean.valueOf(properties.getProperty(JEDIS_BLOCKWHENEXHAUSTED_KEY,FALSE)));
		clusterPool.setMaxIdle(properties.getProperty(MAXIDLE_KEY));
		clusterPool.setMaxTotal(properties.getProperty(MAXTOTAL_KEY));
		clusterPool.setMaxWaitMillis(properties.getProperty(MAXWAITMILLIS_KEY));
		clusterPool.setNumTestsPerEvictionRun(properties.getProperty(JEDIS_NUMTESTSPEREVICTIONRUN_KEY));
		clusterPool.setTestOnBorrow(Boolean.valueOf(properties.getProperty(JEDIS_TESTONBORROW_KEY,FALSE)));
		clusterPool.setTestOnReturn(Boolean.valueOf(properties.getProperty(JEDIS_TESTONRETURN_KEY,FALSE)));
		jedisSentinelYamlBean.setJedisClusterPool(clusterPool);
		jedisSentinelYamlBean.setTimeout(Integer.parseInt(properties.getProperty(TIMEOUT_KEY,DEFAULT_VALUE_50000)));
		jedisSentinelYamlBean.setMasterName(properties.getProperty(JEDIS_MASTERNAME_KEY));
		writeFile(filePath, buildYamlContents(jedisSentinelYamlBean));
	}
	
	public static void writeEsYamlFile(String filePath, Properties properties){
		EsBaseYamlBean esBaseYamlBean = new EsBaseYamlBean();
		esBaseYamlBean.setClusterName(properties.getProperty(CLUSTER_NAME_KEY));
		
		EsClusterPool pool = new EsClusterPool();
		pool.setMaxIdle(properties.getProperty(MAXIDLE_KEY));
		pool.setMaxTotal(properties.getProperty(MAXTOTAL_KEY));
		pool.setMaxWaitMillis(properties.getProperty(MAXWAITMILLIS_KEY));
		esBaseYamlBean.setEsClusterPool(pool);
		String nodesStr = properties.getProperty(ES_CLUSTER_NODE_KEY);
		
		esBaseYamlBean.setEsclusterNode(buildNodeProperties(nodesStr));
		writeFile(filePath, buildYamlContents(esBaseYamlBean));
	}
	

	@SuppressWarnings("unchecked")
	private static List<Properties> buildNodeProperties(String nodesStr) {
		return JSON.parseObject(nodesStr, List.class);
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> buildNodeString(String nodesStr) {
		return JSON.parseObject(nodesStr, List.class);
	}

	public static void writeDatasourceFile(String filePath, Properties properties){
		Map<String,DiamondDataSourceBean> map = buildDataSourceMap(properties);
		String realDataSourceBeanName = LppzBasicDataSource.class.getName();
		String sourceName = DiamondDataSourceBean.class.getName();
		String dataSourceContext = new Yaml().dump(map);
		dataSourceContext = dataSourceContext.replaceAll(sourceName, realDataSourceBeanName);
		writeFile(filePath, dataSourceContext);
	}

	public static void writeMongodbFile(String filePath, Properties properties){
		Map<String,MongoConfigBean> map = buildMongodbMap(properties);
		String context = new Yaml().dump(map);
		writeFile(filePath, context);
	}
	
	/**
	 * 获取app节点情况
	 * @param properties
	 * @return
	 */
	public static Map<String,DiamondClusterBean> buildDiamondClusterMap(Properties properties){
		Map<String,DiamondClusterBean> map = new HashMap<>();
		DiamondClusterBean clusterBean = null;
		List<DiamondClusterNodeBean> portsList = null;
		if (properties != null && properties.entrySet() != null) {
			for (Entry<Object, Object> entry : properties.entrySet()) {
				clusterBean = new DiamondClusterBean();
				String nodeJSON = (String) entry.getValue();
				portsList =  parseClusterNodeBeanByJSON(nodeJSON);
				clusterBean.setClusterNodes(portsList);
				map.put((String)entry.getKey(), clusterBean);
			}
		}
		
		return map;
	}
	
	private static List<DiamondClusterNodeBean> parseClusterNodeBeanByJSON(String json){
		List<DiamondClusterNodeBean> portsList = new ArrayList<>();
		Object o = JSON.parse(json);
		if (o instanceof JSONArray) {
			JSONArray array = (JSONArray) o;
			JSONObject tmpJSONObject = null;
			for (int i = 0; i < array.size(); i++) {
				tmpJSONObject = array.getJSONObject(i);
				portsList.add(JSON.toJavaObject(tmpJSONObject, DiamondClusterNodeBean.class));
			}
		}else if(o instanceof JSONObject){
			portsList.add(JSON.toJavaObject((JSONObject)o, DiamondClusterNodeBean.class));
		}
		
		return portsList;
	}
	
	private static void writeFile(String filePath,
			String contents) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			os.write(contents.getBytes());
		} catch (IOException e) {
			logger.error("", e);
		}finally{
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					
					logger.error("", e);
				}
			}
		}
	}
	
	public static String buildYamlContents(Object bean){
		return new Yaml().dump(bean);
	}

	private static Map<String,DiamondDataSourceBean> buildDataSourceMap(Properties properties) {
		Map<String,DiamondDataSourceBean> map = new HashMap<>();
		String[] tmpKey = null;
		DiamondDataSourceBean tmpDataSource = null;
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			tmpKey = entry.getKey().toString().split(POINT_REGEX);
			if (tmpKey.length != 2) {
				logger.error("数据源属性没有配置对应数据源名称 {}",entry.getKey());
				return null;
			}
			tmpDataSource = map.get(tmpKey[0]);
			if (tmpDataSource==null) {
				tmpDataSource = new DiamondDataSourceBean();
				map.put(tmpKey[0], tmpDataSource);
				buildDataSourceProperties(tmpKey[0],tmpDataSource,properties);
			}
		}
		return map;
	}

	private static Map<String,MongoConfigBean> buildMongodbMap(Properties properties) {
		Map<String,MongoConfigBean> map = new HashMap<>();
		String[] tmpKey = null;
		MongoConfigBean bean = null;
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			tmpKey = entry.getKey().toString().split(POINT_REGEX);
			if (tmpKey.length != 2) {
				logger.error("数据源属性没有配置对应数据源名称 {}",entry.getKey());
				return null;
			}
			bean = map.get(tmpKey[0]);
			if (bean==null) {
				bean = new MongoConfigBean();
				map.put(tmpKey[0], bean);
				buildMongodbProperties(tmpKey[0],bean,properties);
			}
		}
		return map;
	}

	/**
	 * {
master-member-service: !!com.lppz.core.datasource.LppzBasicDataSource {
dataSourceClassName: 'com.mysql.jdbc.jdbc2.optional.MysqlDataSource',
dataSourceProperties: {user: 'root',password: 'root'},
dataSourceJNDI: ,
isCobar: 'true',
schemaName: 'MEMBERDB',
catgroup: 'app',
protocol: 'mysql',
zkServerAddr: 10.6.24.11:2181,
zkMycatClusterName: 'mycat-cluster-member-test',
nodePrefix: 'mycat_fz_',
configLocation: 'classpath:mybatis-config.xml',
mapperLocation: 'classpath:membersqlmap/*.xml',
baseScanPackge: 'com.lppz.member.bir.dao',
autoCommit: 'false',
connectionTimeout: '20000',
idleTimeout: '1800000',
maxLifetime: '3600000',
maximumPoolSize: '100',
minimumIdle: '10'
}
}
	 * @param datasourceName
	 * @param dataSource
	 * @param properties
	 */
	@SuppressWarnings("unchecked")
	private static void buildDataSourceProperties(String datasourceName,DiamondDataSourceBean dataSource, Properties properties) {
		dataSource.setAutoCommit(Boolean.valueOf(properties.getProperty(getDatasourceProName(datasourceName,DS_AUTOCOMMIT_KEY), FALSE)));
		dataSource.setDataSourceClassName(MysqlDataSource.class.getName());
		Properties p = new Properties();
		String propValue = properties.getProperty(getDatasourceProName(datasourceName,DATASOURCE_PROPERTIES_KEY));
		if (StringUtils.isNotBlank(propValue)) {			
			p.putAll((LinkedHashMap<Object,Object>)new Yaml().load(propValue));
		}
		
		dataSource.setDataSourceProperties(p);
		dataSource.setDataSourceJNDI(getPropertiesValue(datasourceName,DS_JNDI_KEY,properties));
		dataSource.setIsCobar(Boolean.valueOf(getPropertiesValue(datasourceName, DS_ISCOBAR_KEY,properties, FALSE)));
		dataSource.setSchemaName(getPropertiesValue(datasourceName, DS_SCHEMANAME_KEY,properties));
		dataSource.setCatgroup(getPropertiesValue(datasourceName, DS_CATGROUP_KEY,properties));
		dataSource.setProtocol(getPropertiesValue(datasourceName,DS_PROTOCOL_KEY,properties));
		dataSource.setZkServerAddr(getPropertiesValue(datasourceName,DS_ZKSERVERADDR_KEY,properties));
		dataSource.setZkMycatClusterName(getPropertiesValue(datasourceName,DS_ZKMYCATCLUSTERNAME_KEY,properties));
		dataSource.setNodePrefix(getPropertiesValue(datasourceName,DS_NODEPREFIX_KEY,properties));
		dataSource.setConfigLocation(getPropertiesValue(datasourceName,DS_CONFIGLOCATION_KEY,properties));
		dataSource.setMapperLocation(getPropertiesValue(datasourceName,DS_MAPPERLOCATION_KEY,properties));
		dataSource.setBaseScanPackge(getPropertiesValue(datasourceName,DS_BASESCANPACKGE_KEY,properties));
		dataSource.setConnectionTimeout(Long.parseLong(getPropertiesValue(datasourceName,DS_CONNECTIONTIMEOUT_KEY,properties, DEFAULT_VALUE_900000)));
		dataSource.setIdleTimeout(Long.parseLong(getPropertiesValue(datasourceName,DS_IDLETIMEOUT_KEY,properties, DEFAULT_VALUE_900000)));
		dataSource.setMaxLifetime(Long.parseLong(getPropertiesValue(datasourceName,DS_MAXLIFETIME_KEY,properties, DEFAULT_VALUE_900000)));
		dataSource.setMaximumPoolSize(Integer.parseInt(getPropertiesValue(datasourceName,DS_MAXIMUMPOOLSIZE_KEY,properties, DEFAULT_VALUE_100)));
		dataSource.setMinimumIdle(Integer.parseInt(getPropertiesValue(datasourceName,DS_MINIMUMIDLE_KEY,properties, DEFAULT_VALUE_10)));
	}
	
	private static void buildMongodbProperties(String name,MongoConfigBean bean, Properties properties) {
		bean.setConnectTimeout(Integer.parseInt(getPropertiesValue(name,MG_CONNECTTIMEOUT_KEY,properties, DEFAULT_VALUE_25000)));
		bean.setDb(getPropertiesValue(name,MG_DB_KEY,properties, ""));
		bean.setMaxConnectionLifeTime(Integer.parseInt(getPropertiesValue(name,MG_CONNECTIONLIFETIME_KEY,properties, DEFAULT_VALUE_150000)));
		bean.setMaxWaitTime(Integer.parseInt(getPropertiesValue(name,MG_MAXWAITTIME_KEY,properties, DEFAULT_VALUE_300000)));
		bean.setPoolSize(Integer.parseInt(getPropertiesValue(name,MG_POOLSIZE_KEY,properties, DEFAULT_VALUE_200)));
		bean.setServerAddress(buildNodeProperties(getPropertiesValue(name,MG_SERVERADDRESS_KEY,properties, "[]")));
		bean.setSocketTimeout(Integer.parseInt(getPropertiesValue(name,MG_SOCKETTIMEOUT_KEY,properties, DEFAULT_VALUE_100000)));
		bean.setTables(buildNodeString(getPropertiesValue(name,MG_TABLES_KEY,properties, "[]")));
	}
	
	private static String getPropertiesValue(String datasourceName,String key, Properties properties,String ... defaultValue){
		String value = null;
		if (defaultValue != null && defaultValue.length>0) {
			value = properties.getProperty(getDatasourceProName(datasourceName,key), defaultValue[0]);
		}else{
			value = properties.getProperty(getDatasourceProName(datasourceName,key));
		}
		return value;
	}
	
	private static String getDatasourceProName(String dsname, String proName){
		return dsname + "." + proName;
	}
	
	public static DiamondBean getDiamondProperties() throws IOException{
		Resource resource = new ClassPathResource(DIAMOND_YAML_PATH);
		try {
			if (resource.exists()) {
				diamondBean = new Yaml().loadAs(resource.getInputStream(), DiamondBean.class);
			}
			return diamondBean;
		} catch (IOException e) {
			throw e;
		}
	}
	public static DiamondBean setDiamondProperties(DiamondBean diamondBean1) throws IOException{
		diamondBean = diamondBean1;
		return diamondBean;
	}
	
	public static final String DATASOURCE_NAME_KEY="datasource-name";
	public static final String DATASOURCE_PROPERTIES_KEY="dataSourceProperties";
	public static final String DIAMOND_YAML_PATH = "META-INF/diamond.yaml";
	public static final String DUBBO_PRO_FILE_PATH = "META-INF/dubbo.properties";
	public static final String DUBBO_PRO_FILE_NAME = "dubbo.properties";
	public static final String CLUSTER_YAML_NAME = "cluster.yaml";
	public static final String HBASE_YAML_NAME = "hbase-lppz-client.yaml";
	public static final String MONGODB_YAML_NAME = "mongodb-cluster.yaml";
	public static final String LP_JEDIS_YAML_NAME = "lpjedis-cluster.yaml";
	public static final String PROPERTIES_SUFFIX = ".properties";
	public static final String DATASOURCE_FILE_REGEX = "datasource\\S*.yaml";
	public static final String ES_FILE_REGEX = "es-cluster\\S*.yaml";
	public static final String JEDIS_CLUSTER_FILE_REGEX = "jedis-cluster\\S*.yaml";
	public static final String JEDIS_SENTINEL_FILE_REGEX = "jedis-sentinel\\S*.yaml";
	public static final String META_INF_PATH = "conf/META-INF";
	public static final String META_INF = "META-INF";
	public static final String PARAMS_REGEX = "\\$\\{%s\\}";
	public static final String POINT_REGEX = "\\.";
	public static final String NEXT_LINE = "\r\n";
	public static final String JEDIS_NODE_KEY = "jedisClusterNode";
	public static final String JEDIS_BLOCKWHENEXHAUSTED_KEY = "blockWhenExhausted";
	public static final String JEDIS_NUMTESTSPEREVICTIONRUN_KEY = "numTestsPerEvictionRun";
	public static final String JEDIS_MASTERNAME_KEY = "masterName";
	public static final String JEDIS_TESTONBORROW_KEY = "testOnBorrow";
	public static final String JEDIS_TESTONRETURN_KEY = "testOnReturn";
	public static final String JEDIS_MAXREDIRECTIONS_KEY = "maxRedirections";
	public static final String DS_AUTOCOMMIT_KEY = "autoCommit";
	public static final String DS_JNDI_KEY = "dataSourceJNDI";
	public static final String DS_ISCOBAR_KEY = "isCobar";
	public static final String DS_SCHEMANAME_KEY = "schemaName";
	public static final String DS_CATGROUP_KEY = "catgroup";
	public static final String DS_PROTOCOL_KEY = "protocol";
	public static final String DS_ZKSERVERADDR_KEY = "zkServerAddr";
	public static final String DS_ZKMYCATCLUSTERNAME_KEY = "zkMycatClusterName";
	public static final String DS_NODEPREFIX_KEY = "nodePrefix";
	public static final String DS_CONFIGLOCATION_KEY = "configLocation";
	public static final String DS_MAPPERLOCATION_KEY = "mapperLocation";
	public static final String DS_BASESCANPACKGE_KEY = "baseScanPackge";
	public static final String DS_CONNECTIONTIMEOUT_KEY = "connectionTimeout";
	public static final String DS_IDLETIMEOUT_KEY = "idleTimeout";
	public static final String DS_MAXLIFETIME_KEY = "maxLifetime";
	public static final String DS_MAXIMUMPOOLSIZE_KEY = "maximumPoolSize";
	public static final String DS_MINIMUMIDLE_KEY = "minimumIdle";
	
	public static final String MG_CONNECTTIMEOUT_KEY = "connectTimeout";
	public static final String MG_DB_KEY = "db";
	public static final String MG_CONNECTIONLIFETIME_KEY = "connectionLifeTime";
	public static final String MG_MAXWAITTIME_KEY = "maxWaitTime";
	public static final String MG_POOLSIZE_KEY = "poolSize";
	public static final String MG_SERVERADDRESS_KEY = "serverAddress";
	public static final String MG_SOCKETTIMEOUT_KEY = "socketTimeout";
	public static final String MG_TABLES_KEY = "tables";
	
	public static final String MAXIDLE_KEY = "maxIdle";
	public static final String MAXTOTAL_KEY = "maxTotal";
	public static final String TIMEOUT_KEY = "timeout";
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	public static final String CLUSTER_NAME_KEY = "clusterName";
	public static final String ES_CLUSTER_NODE_KEY = "esclusterNode";
	public static final String DEFAULT_VALUE_50000 = "50000";
	public static final String DEFAULT_VALUE_300000 = "300000";
	public static final String DEFAULT_VALUE_25000 = "25000";
	public static final String DEFAULT_VALUE_150000 = "150000";
	public static final String DEFAULT_VALUE_200 = "200";
	public static final String DEFAULT_VALUE_100000 = "100000";
	public static final String DEFAULT_VALUE_900000 = "900000";
	public static final String DEFAULT_VALUE_100 = "100";
	public static final String DEFAULT_VALUE_10 = "10";
	
	public static final String MAXWAITMILLIS_KEY = "maxWaitMillis";
	public static final String DEV_CLASS_PATH = "target"+File.separator+"classes";
	public static final String TOMCAT_CLASS_PATH="WEB-INF"+File.separator+"classes";
	public static final String DEV_TOMCAT_CLASS_PATH="target"+File.separator + "%s" +File.separator +"WEB-INF"+File.separator+"classes";
	public static final String MICRO_BAST_PATH = "conf_%s" + File.separator +"META-INF";
}
