package com.lppz.diamond.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.lppz.diamond.client.config.ConfigurationInterpolator;
import com.lppz.diamond.client.config.PropertiesReader;
import com.lppz.diamond.client.config.PropertyConverter;
import com.lppz.diamond.client.event.EventSource;
import com.lppz.diamond.client.event.EventType;
import com.lppz.diamond.client.netty.ClientChannelInitializer;
import com.lppz.diamond.client.netty.Netty4Client;
import com.lppz.diamond.client.util.FileUtils;
import com.lppz.diamond.client.util.NamedThreadFactory;

public class PropertiesConfiguration extends EventSource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfiguration.class);

	private StrSubstitutor substitutor;
	
	private Map<String, String> store = null;
	
	private Map<String, Map<String,String>> storeMap = null;
	
	private Netty4Client client;
	
	private volatile boolean reloadable = true;
	
	private static final ExecutorService reloadExecutorService = Executors.newSingleThreadExecutor(new NamedThreadFactory("ReloadConfigExecutorService", true));
	
	private static String _host;
	private static int _port = 0;
	private static String _projCode;
	private static String _profile;
	private static String _modules;
	private Set<String> modulesSet = new HashSet<>();
	
	private static final int CONNECT_FAIL_RETRYNUM = 3;
	
	private static final long FIRST_CONNECT_TIMEOUT = 60;
	
	/**
	 * 从jvm参数中获取 projCode、profile、host和port值
	 * 
	 * @param projCode
	 * @param profile
	 */
	public PropertiesConfiguration() {
		_host = getHost();
		_port = getPort();
		_projCode = getProjCode();
		_profile = getProfile();
		_modules = getModules();
		
		connectServer(_host, _port, _projCode, _profile, _modules);
		substitutor = new StrSubstitutor(createInterpolator());
	}

	/**
	 * 从jvm参数中获取 host和port值
	 * 
	 * @param projCode
	 * @param profile
	 */
	public PropertiesConfiguration(final String projCode, final String profile) {
		_host = getHost();
		_port = getPort();
		_projCode = projCode;
		_profile = profile;
		_modules = "";
		
		connectServer(_host, _port, _projCode, _profile, _modules);
		substitutor = new StrSubstitutor(createInterpolator());
	}
	
	/**
	 * 从jvm参数中获取 host和port值
	 * 
	 * @param projCode
	 * @param profile
	 */
	public PropertiesConfiguration(final String projCode, final String profile, String modules) {
		_host = getHost();
		_port = getPort();
		_projCode = projCode;
		_profile = profile;
		_modules = modules;
		addModuleSet(modules);
		
		connectServer(_host, _port, _projCode, _profile, _modules);
		substitutor = new StrSubstitutor(createInterpolator());
	}

	public PropertiesConfiguration(String host, int port, final String projCode, final String profile) {
		_host = host;
		_port = port;
		_projCode = projCode;
		_profile = profile;
		_modules = "";
		
		connectServer(_host, _port, _projCode, _profile, _modules);
		substitutor = new StrSubstitutor(createInterpolator());
	}
	
	@Deprecated
	public PropertiesConfiguration(boolean reloadable, String host, int port, final String projCode, final String profile) {
		_host = host;
		_port = port;
		_projCode = projCode;
		_profile = profile;
		_modules = "";
		this.reloadable = reloadable;
		
		getModulesFromServer(host, port, projCode, profile);
		substitutor = new StrSubstitutor(createInterpolator());
	}
	
	private void addModuleSet(String modules){
		if (StringUtils.isNotBlank(modules)) {
			String[] array = modules.split(",");
			for (String module : array) {
				modulesSet.add(module);
			}
		}
	}
	
	public PropertiesConfiguration(String host, int port, final String projCode, final String profile, String modules,boolean... reloadable) {
		_host = host;
		_port = port;
		_projCode = projCode;
		_profile = profile;
		_modules = modules;
		addModuleSet(modules);
		if(reloadable!=null&&reloadable.length>0)
		this.reloadable=reloadable[0];
		connectServer(_host, _port, _projCode, _profile, _modules);
		substitutor = new StrSubstitutor(createInterpolator());
	}
	
	public void getModulesFromServer(String host, int port, final String projCode, final String profile){
		Assert.notNull(projCode, "连接getModules， projCode不能为空");
		
		final String clientMsg = "getModules={\"projCode\": \"" + projCode + "\", \"profile\": \"" + profile + "\", "
				 + " \"version\": \"3.6.4\"}";
		int failNum = 0;
		boolean isRetry = false;
		try {
			do{
				if(client!=null){
					client.close();
					client=null;
				}
				client = new Netty4Client(host, port, new ClientChannelInitializer(clientMsg));
				
				if(client.isConnected()) {
					String message = client.receiveMessage(FIRST_CONNECT_TIMEOUT);
					
					if(StringUtils.isNotBlank(message)) {
						LOGGER.info("加载module信息，项目编码：{}，Profile：{}, modules：{}", projCode, profile, message);
						
						addModuleSet(message);
					} else {
						throw new ConfigurationRuntimeException("从服务器端获取module信息为空，Client 请求信息为：" + clientMsg);
					}
				} else {
					if (failNum++ <= CONNECT_FAIL_RETRYNUM) {
						isRetry = true;
						continue;
					}else{
						throw new ConfigurationRuntimeException("连接服务端异常，PropertiesConfiguration 初始化失败。");
					}
				}
				reloadableThread(projCode, profile);
			}while(isRetry);
		} catch (Exception e) {
			if(client != null) {
				client.close();
			}
			throw new ConfigurationRuntimeException(e.getMessage(), e);
		}
	}
	
	public void connectServer(String host, int port, final String projCode, final String profile, final String modules) {
		Assert.notNull(projCode, "连接superdiamond， projCode不能为空");
		
		final String clientMsg = "superdiamond={\"projCode\": \"" + projCode + "\", \"profile\": \"" + profile + "\", "
				+ "\"modules\": \"" + modules + "\", \"version\": \"3.6.4\"}";
		int failNum = 0;
		boolean isRetry = false;
		try {
			do{
				if(client!=null){
					client.close();
					client=null;
				}
				client = new Netty4Client(host, port, new ClientChannelInitializer(clientMsg));
				
				if(client.isConnected()) {
					String message = client.receiveMessage(FIRST_CONNECT_TIMEOUT);
					
					if(StringUtils.isNotBlank(message)) {
						String versionStr = null;
						int index = message.indexOf("\r\n");
						if (index > 0) {
							versionStr = message.substring(0, index);
						}
						LOGGER.info("加载配置信息，项目编码：{}，Profile：{}，modules：{}，Version：{}", projCode, profile, modules, versionStr==null?null:versionStr.split(" = ")[1]);
						
						FileUtils.saveData(projCode, profile, message);
						LOGGER.info("message: {}", message);
						loadStoreMap(new StringReader(message), false);
					} else {
						throw new ConfigurationRuntimeException("从服务器端获取配置信息为空，Client 请求信息为：" + clientMsg);
					}
				} else {
					if (failNum++ <= CONNECT_FAIL_RETRYNUM) {
						LOGGER.warn("连接失败，开始第{}次重试", failNum);
						isRetry = true;
						continue;
					}else{
						String message = FileUtils.readConfigFromLocal(projCode, profile);
						if(message != null && !"".equals(message)) {
							String versionStr = message.substring(0, message.indexOf("\r\n"));
							LOGGER.info("加载本地备份配置信息，项目编码：{}，Profile：{}, Version：{}", projCode, profile, versionStr.split(" = ")[1]);
							
							loadStoreMap(new StringReader(message), false);
						} else{
							throw new ConfigurationRuntimeException("本地没有备份配置数据，PropertiesConfiguration 初始化失败。");						
						}
					}
				}
				reloadableThread(projCode, profile);
			}while(isRetry);
		} catch (Exception e) {
			if(client != null) {
				client.close();
			}
			throw new ConfigurationRuntimeException(e.getMessage(), e);
		}
	}

	public void reloadableThread(final String projCode, final String profile) {
		if(reloadable)
		reloadExecutorService.submit(new Runnable() {
			
			@Override
			public void run() {
				while(reloadable) {
					try {
						if(client.isConnected()) {
							String message = client.receiveMessage();
							
							if(message != null) {
								String versionStr = message.substring(0, message.indexOf("\r\n"));
								LOGGER.info("重新加载配置信息，项目编码：{}，Profile：{}, Version：{}", projCode, profile, versionStr.split(" = ")[1]);
								FileUtils.saveData(projCode, profile, message);
								loadStoreMap(new StringReader(message), true);
							}
						} else {
							TimeUnit.SECONDS.sleep(1);
						}
					} catch(Exception e) {
						
					}
				}
			}
		});
	}
	
	public void close() {
		reloadable = false;
		
		if(client != null && client.isConnected())
			client.close();
	}
	
	public boolean isReloadable() {
		return reloadable;
	}

	public void setReloadable(boolean reloadable) {
		this.reloadable = reloadable;
	}

	public void load(String config) throws ConfigurationRuntimeException {
		loadStoreMap(new StringReader(config), false);
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @param in
	 * @param reload 初次初始化加载为false，服务端推送加载为true。
	 * @throws Exception
	 * @{@link Deprecated} 由于项目使用配置文件（module）为单位加载配置属性，原生读取方式不能满足实际需求，扩展loadStoreMap方法构造一个map<String,Properties>对象用于根据module区分配置 2017-08-31
	 */
	@Deprecated
	public void load(Reader in, boolean reload) throws ConfigurationRuntimeException {
		Map<String, String> tmpStore = new LinkedHashMap<String, String>();
		
		PropertiesReader reader = new PropertiesReader(in);
		try {
			while (reader.nextProperty()) {
				String key = reader.getPropertyName();
				String value = reader.getPropertyValue();
				tmpStore.put(key, value);
				if(reload) {
					String oldValue = store.remove(key);
					if(oldValue == null)
						fireEvent(EventType.ADD, key, value);
					else if(!oldValue.equals(value)) 
						fireEvent(EventType.UPDATE, key, value);
				}
			}
			
			if(reload) {
				for(String key : store.keySet()) {
					fireEvent(EventType.CLEAR, key, store.get(key));
				}
			}
		} catch (IOException ioex) {
			throw new ConfigurationRuntimeException(ioex);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				;
			}
		}
		
		if(store != null)
			store.clear();
		
		store = tmpStore;
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @param in
	 * @param reload 初次初始化加载为false，服务端推送加载为true。
	 * @throws Exception
	 */
	public void loadStoreMap(Reader in, boolean reload) throws ConfigurationRuntimeException {
		Map<String, Map<String,String>> tmpStoreMap = new LinkedHashMap<String, Map<String,String>>();
		Map<String, String> tmpStore = null;
		Map<String, String> tmpStore2 = new LinkedHashMap<String, String>();
		
		PropertiesReader reader = new PropertiesReader(in);
		try {
			while (reader.nextProperty()) {
				String key = reader.getPropertyName();
				String value = reader.getPropertyValue();
				if ("MODULE_NAME".equals(key)) {
					tmpStore = tmpStoreMap.get(value);
					if (tmpStore == null) {
						tmpStore = new LinkedHashMap<String, String>();
						tmpStoreMap.put(value, tmpStore);
					}
					continue;
				}
				tmpStore.put(key, value);
				tmpStore2.put(key, value);
				if(reload) {
					String oldValue = tmpStore.remove(key);
					tmpStore2.remove(key);
					if(oldValue == null)//TODO 多module配置时间处理待完善
						fireEvent(EventType.ADD, key, value);
					else if(!oldValue.equals(value)) 
						fireEvent(EventType.UPDATE, key, value);
				}
			}
			
			if(reload) {//TODO 多module配置时间处理待完善
				for(String key : tmpStore.keySet()) {
					fireEvent(EventType.CLEAR, key, tmpStore.get(key));
				}
			}
		} catch (IOException ioex) {
			throw new ConfigurationRuntimeException(ioex);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				;
			}
		}
		
		if(storeMap != null)
			storeMap.clear();
		
		if (store != null) {
			store.clear();
		}
		
		store = tmpStore2;
		storeMap = tmpStoreMap;
	}
	
	public static String getProjCode() {
		if(StringUtils.isNotBlank(_projCode))
			return _projCode;
		
		_projCode = System.getenv("SUPERDIAMOND_PROJCODE");
		if(StringUtils.isBlank(_projCode)) {
			return System.getProperty("superdiamond.projcode");
		} else {
			return _projCode;
		}
	}
	
	public static String getProfile() {
		if(StringUtils.isNotBlank(_profile))
			return _profile;
		
		_profile = System.getenv("SUPERDIAMOND_PROFILE");
		if(StringUtils.isBlank(_profile)) {
			return System.getProperty("superdiamond.profile", "development");
		} else {
			return _profile;
		}
	}
	
	public static String getModules() {
		if(StringUtils.isNotBlank(_modules))
			return _modules;
		
		_modules = System.getenv("SUPERDIAMOND_MODULES");
		if(StringUtils.isBlank(_modules)) {
			return System.getProperty("superdiamond.modules");
		} else {
			return _modules;
		}
	}
	
	public static String getHost() {
		if(StringUtils.isNotBlank(_host))
			return _host;
		
		_host = System.getenv("SUPERDIAMOND_HOST");
		if(StringUtils.isBlank(_host)) {
			return System.getProperty("superdiamond.host", "localhost");
		} else {
			return _host;
		}
	}
	
	public static int getPort() {
		if(_port > 1)
			return _port;
			
		if(StringUtils.isBlank(System.getenv("SUPERDIAMOND_PORT"))) {
			return Integer.valueOf(System.getProperty("superdiamond.port", "8283"));
		} else {
			return Integer.valueOf(System.getenv("SUPERDIAMOND_PORT"));
		}
	}
	
	// --------------------------------------------------------------------
	
	private String getProperty(String key) {
		return store.get(key);
	}
	
	public Properties getProperties() {
		Properties properties = new Properties();
		
		for(String key : store.keySet()) {
			properties.setProperty(key, getString(key));
		}
		return properties;
	}
	
	public Map<String,Properties> getPropertiesMap() {
		Map<String,Properties> map = new HashMap<>();
		for(Entry<String, Map<String,String>> entry : storeMap.entrySet()){
			Properties properties = new Properties();
			map.put(entry.getKey(), properties);
			for(Entry<String, String> ent : entry.getValue().entrySet()) {
				properties.setProperty(ent.getKey(), ent.getValue());
			}
		}
		return map;
	}

	public boolean getBoolean(String key) {
		Boolean b = getBoolean(key, null);
		if (b != null) {
			return b.booleanValue();
		} else {
			throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(key, BooleanUtils.toBooleanObject(defaultValue))
				.booleanValue();
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toBoolean(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Boolean object", e);
			}
		}
	}

	public byte getByte(String key) {
		Byte b = getByte(key, null);
		if (b != null) {
			return b.byteValue();
		} else {
			throw new NoSuchElementException('\'' + key + " doesn't map to an existing object");
		}
	}

	public byte getByte(String key, byte defaultValue) {
		return getByte(key, new Byte(defaultValue)).byteValue();
	}

	public Byte getByte(String key, Byte defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toByte(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Byte object", e);
			}
		}
	}

	public double getDouble(String key) {
		Double d = getDouble(key, null);
		if (d != null) {
			return d.doubleValue();
		} else {
			throw new NoSuchElementException('\'' + key
					+ "' doesn't map to an existing object");
		}
	}

	public double getDouble(String key, double defaultValue) {
		return getDouble(key, new Double(defaultValue)).doubleValue();
	}

	public Double getDouble(String key, Double defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toDouble(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Double object", e);
			}
		}
	}

	public float getFloat(String key) {
		Float f = getFloat(key, null);
		if (f != null) {
			return f.floatValue();
		} else {
			throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
		}
	}

	public float getFloat(String key, float defaultValue) {
		return getFloat(key, new Float(defaultValue)).floatValue();
	}

	public Float getFloat(String key, Float defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toFloat(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Float object", e);
			}
		}
	}

	public int getInt(String key) {
		Integer i = getInteger(key, null);
		if (i != null) {
			return i.intValue();
		} else {
			throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
		}
	}

	public int getInt(String key, int defaultValue) {
		Integer i = getInteger(key, null);

		if (i == null) {
			return defaultValue;
		}

		return i.intValue();
	}

	public Integer getInteger(String key, Integer defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toInteger(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to an Integer object", e);
			}
		}
	}

	public long getLong(String key) {
		Long l = getLong(key, null);
		if (l != null) {
			return l.longValue();
		} else {
			throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
		}
	}

	public long getLong(String key, long defaultValue) {
		return getLong(key, new Long(defaultValue)).longValue();
	}

	public Long getLong(String key, Long defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toLong(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Long object", e);
			}
		}
	}

	public short getShort(String key) {
		Short s = getShort(key, null);
		if (s != null) {
			return s.shortValue();
		} else {
			throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
		}
	}

	public short getShort(String key, short defaultValue) {
		return getShort(key, new Short(defaultValue)).shortValue();
	}

	public Short getShort(String key, Short defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return PropertyConverter.toShort(interpolate(value));
			} catch (ConversionException e) {
				throw new ConversionException('\'' + key + "' doesn't map to a Short object", e);
			}
		}
	}

	public String getString(String key) {
		String s = getString(key, null);
		if (s != null) {
			return s;
		} else {
			return null;
		}
	}

	public String getString(String key, String defaultValue) {
		String value = getProperty(key);

		if (value instanceof String) {
			return interpolate((String) value);
		} else {
			return interpolate(defaultValue);
		}
	}

	protected String interpolate(String value) {
		Object result = substitutor.replace(value);
		return (result == null) ? null : result.toString();
	}

	protected ConfigurationInterpolator createInterpolator() {
        ConfigurationInterpolator interpol = new ConfigurationInterpolator();
        interpol.setDefaultLookup(new StrLookup()
        {
            @Override
            public String lookup(String var)
            {
            	String prop = getProperty(var);
                return (prop != null) ? prop : null;
            }
        });
        return interpol;
    }

	public Set<String> getModulesSet() {
		return modulesSet;
	}

	public void setModulesSet(Set<String> modulesSet) {
		this.modulesSet = modulesSet;
	}
}
