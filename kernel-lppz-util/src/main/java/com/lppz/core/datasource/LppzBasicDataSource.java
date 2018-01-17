/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.datasource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;


/**
 * 自定义的动态路由数据源 继承自 spring jdbc的AbstractRoutingDataSource
 * 
 * @author zoubin
 */
public class LppzBasicDataSource extends HikariDataSource implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7047716120546703694L;

	private static final Logger logger = LoggerFactory.getLogger(LppzBasicDataSource.class);
	private String protocol; 
	private String schemaName; 
	private String zkServerAddr;
	private String zkMycatClusterName;
	private String nodePrefix;
	private String catgroup;
	private boolean isCobar;
	private String configLocation;
	private String mapperLocation;
	private String baseScanPackge;
	private Integer currentNode;
	private boolean needLog;
	private String ip;
	private String port;
	private Integer errorCount=0;
	
	public LppzBasicDataSource buildInstance(String ip,String port){
		LppzBasicDataSource lb=new LppzBasicDataSource();
		lb.setDataSourceClassName(this.getDataSourceClassName());
		lb.setConfigLocation(this.getConfigLocation());
		lb.setMapperLocation(this.getMapperLocation());
		lb.setBaseScanPackge(this.getBaseScanPackge());
		lb.setAutoCommit(this.isAutoCommit());
		lb.setConnectionTimeout(this.getConnectionTimeout());
		lb.setIdleTimeout(this.getIdleTimeout());
		lb.setMaxLifetime(this.getMaxLifetime());
		lb.setMaximumPoolSize(this.getMaximumPoolSize());
		lb.setMinimumIdle(this.getMinimumIdle());
		lb.setDataSourceProperties(buildProps(ip,port));
		lb.setNodePrefix(this.getNodePrefix());
		lb.setIp(ip);
		lb.setPort(port);
		lb.setIsCobar(false);
		return lb;
	}
	
	public static LppzBasicDataSource buildDefaultInstance(String ip,String port,int maxPoolSize,int minPoolSize,String schema,String user,String password){
		LppzBasicDataSource lb=new LppzBasicDataSource();
		lb.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		lb.setAutoCommit(false);
		lb.setConnectionTimeout(120000);
		lb.setIdleTimeout(600000);
		lb.setMaxLifetime(1800000);
		lb.setMaximumPoolSize(maxPoolSize);
		lb.setMinimumIdle(minPoolSize);
		lb.setProtocol("mysql");
		lb.setSchemaName(schema);
		lb.setUsername(user);
		lb.setPassword(password);
		lb.setDataSourceProperties(lb.buildProps(ip, port));
		lb.setIp(ip);
		lb.setPort(port);
		lb.setIsCobar(false);
		return lb;
	}
	
	private Properties buildProps(String ip, String port) {
		Properties props=super.getDataSourceProperties();
        String jdbcUrl="jdbc:"+this.protocol+"://"+ip+":"+port+"/"+this.schemaName;
//        		if("mysql".equals(this.protocol.toLowerCase())){
//        			jdbcUrl+="?useConfigs=maxPerformance&allowMultiQueries=true&rewriteBatchedStatements=true&autoReconnect=true&zeroDateTimeBehavior=convertToNull";	
//        		}
        props.put("url", jdbcUrl);
        return props;
	}

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
	public boolean isCobar() {
		return isCobar;
	}
	public void setIsCobar(boolean isCobar) {
		this.isCobar = isCobar;
	}
	@Override
	public Connection getConnection() throws SQLException
	{
		super.setDataSource(null);
		return super.getConnection();
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getZkServerAddr() {
		return zkServerAddr;
	}
	public void setZkServerAddr(String zkServerAddr) {
		this.zkServerAddr = zkServerAddr;
	}
	public String getZkMycatClusterName() {
		return zkMycatClusterName;
	}
	public void setZkMycatClusterName(String zkMycatClusterName) {
		this.zkMycatClusterName = zkMycatClusterName;
	}
	public String getNodePrefix() {
		return nodePrefix;
	}
	public void setNodePrefix(String nodePrefix) {
		this.nodePrefix = nodePrefix;
	}
	public Integer getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Integer currentNode) {
		this.currentNode = currentNode;
	}

	public void setCobar(boolean isCobar) {
		this.isCobar = isCobar;
	}

	public String getCatgroup() {
		return catgroup;
	}

	public void setCatgroup(String catgroup) {
		this.catgroup = catgroup;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public boolean isNeedLog() {
		return needLog;
	}

	public void setNeedLog(boolean needLog) {
		this.needLog = needLog;
	}
}