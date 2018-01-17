/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.datasource;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

//import com.lppz.core.conn.MultiProxyConnection;
//import com.lppz.core.exceptions.DalDataSourceOperationException;
//import com.zaxxer.hikari.proxy.ConnectionProxy;


/**
 * 自定义的动态路由数据源 继承自 spring jdbc的AbstractRoutingDataSource
 * 
 * @author zoubin
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements Serializable
{
    private static final long serialVersionUID = -7720810158625748042L;
    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSource.class);
	private String configLocation;
	private String mapperLocation;
	private String baseScanPackge;
	public String getBaseScanPackge() {
		return baseScanPackge;
	}

	public void setBaseScanPackge(String baseScanPackge) {
		this.baseScanPackge = baseScanPackge;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String getMapperLocation() {
		return mapperLocation;
	}

	public void setMapperLocation(String mapperLocation) {
		this.mapperLocation = mapperLocation;
	}


	private Map<Object, Object> _targetDataSources;

	private Object _defaultTargetDataSource;

	/**
	 * @return the _defaultTargetDataSource
	 */
	public Object getDefaultTargetDataSource()
	{
		return _defaultTargetDataSource;
	}

//	private MultiProxyConnection conn;

	public DynamicDataSource(){
	}
	
	public DynamicDataSource(final Object defaultTargetDataSource,final Map<Object, Object> targetDataSources){
		setDefaultTargetDataSource(defaultTargetDataSource);
		setTargetDataSources(targetDataSources);
	}
	@Override
	public void setDefaultTargetDataSource(final Object defaultTargetDataSource)
	{
		this._defaultTargetDataSource = defaultTargetDataSource;
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	/**
	 * @return the conn
	 */
//	public MultiProxyConnection getConn()
//	{
//		return conn;
//	}

	@Override
	public void setTargetDataSources(final Map<Object, Object> targetDataSources)
	{
		this._targetDataSources = targetDataSources;
		super.setTargetDataSources(targetDataSources);
	}

	/**
	 * @return the _targetDataSources
	 */
	public Map<Object, Object> getTargetDataSources()
	{
		return _targetDataSources;
	}

	@Override
	protected Object determineCurrentLookupKey()
	{
		final String dsName = generateRouteKey();
		LOG.debug("current datasource name is [{}]", dsName);
		return dsName;
	}
	
	public DataSource determineTargetDataSource(){
		return super.determineTargetDataSource();
	}
	
	/**
	 * @return
	 */
	protected String generateRouteKey()
	{
		return DynamicDataSourceHolder.get();
	}

	
	/**
	 * 
	 */
//	private void initMultiConn()
//	{
//		try{
//			ConnectionProxy proxy=(ConnectionProxy)getConnection();
//			conn=new MultiProxyConnection(proxy);
//		}
//		catch (final SQLException | NoSuchFieldException | IllegalAccessException e)
//		{
//			LOG.error(e.getMessage(), e);
//			throw new DalDataSourceOperationException("Could not get a connection from the datasource", e);
//		}
//	}

//	public void initInnerMultiConn()  {
//		try {
//			if(conn==null||conn.isClosed()){
//				initMultiConn();
//			}
//		} catch (SQLException e1) {
//			LOG.error(e1.getMessage(), e1);
//			throw new DalDataSourceOperationException(
//					"Could not get a connection from the datasource", e1);
//		}
//		Map<Object, Connection> multiconn = conn.getMulticonn();
//		final Map<Object, Object> mapds = getTargetDataSources();
//		int i = 0;
//		for (final Object key : mapds.keySet()) {
//			final Object d = mapds.get(key);
//			if (d instanceof DataSource) {
//				try {
//					synchronized (mapds) {
//						if (multiconn.get(key) == null
//								|| multiconn.get(key).isClosed()) {
//							if (i++ == 0) {
//								multiconn.put(key, conn.getProxconn());
//							} else {
//								multiconn.put(key,
//										((DataSource) d).getConnection());
//							}
//						}
//					}
//				} catch (SQLException e) {
//					LOG.error(e.getMessage(), e);
//					throw new DalDataSourceOperationException(
//							"Could not get a connection from the datasource", e);
//				}
//			}
//		}
//	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
//		initInnerMultiConn();
	}
	
	
	@SuppressWarnings("unchecked")
	@PreDestroy
	public void destory() throws IllegalArgumentException, IllegalAccessException{
		LOG.info("destory datasources.......");
		Field field = ReflectionUtils.findField(this.getClass(), "targetDataSources");
		field.setAccessible(true);
		if(field.get(this)!=null)
		for (Entry<Object, Object> entry : ((Map<Object, Object>) field.get(this)).entrySet()) {
			if(Closeable.class.isInstance(entry.getValue())){
				try {
					((Closeable)entry.getValue()).close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
}
