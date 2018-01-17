package com.lppz.bean;

import java.io.Serializable;
import java.util.Properties;

public class DiamondDataSourceBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dataSourceClassName;
	private Properties dataSourceProperties;
	private String dataSourceJNDI;
	private Boolean isCobar;
	private String schemaName;
	private String catgroup;
	private String protocol;
	private String zkServerAddr;
	private String zkMycatClusterName;
	private String nodePrefix;
	private String configLocation;
	private String mapperLocation;
	private String baseScanPackge;
	private Boolean autoCommit;
	private Long connectionTimeout;
	private Long idleTimeout;
	private Long maxLifetime;
	private Integer maximumPoolSize;
	private Integer minimumIdle;
	public String getDataSourceClassName() {
		return dataSourceClassName;
	}
	public void setDataSourceClassName(String dataSourceClassName) {
		this.dataSourceClassName = dataSourceClassName;
	}
	public Properties getDataSourceProperties() {
		return dataSourceProperties;
	}
	public void setDataSourceProperties(Properties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}
	public String getDataSourceJNDI() {
		return dataSourceJNDI;
	}
	public void setDataSourceJNDI(String dataSourceJNDI) {
		this.dataSourceJNDI = dataSourceJNDI;
	}
	public Boolean getIsCobar() {
		return isCobar;
	}
	public void setIsCobar(Boolean isCobar) {
		this.isCobar = isCobar;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getCatgroup() {
		return catgroup;
	}
	public void setCatgroup(String catgroup) {
		this.catgroup = catgroup;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
	public String getBaseScanPackge() {
		return baseScanPackge;
	}
	public void setBaseScanPackge(String baseScanPackge) {
		this.baseScanPackge = baseScanPackge;
	}
	public boolean getAutoCommit() {
		return autoCommit;
	}
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	
	public Long getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public Long getIdleTimeout() {
		return idleTimeout;
	}
	public void setIdleTimeout(Long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}
	public Long getMaxLifetime() {
		return maxLifetime;
	}
	public void setMaxLifetime(Long maxLifetime) {
		this.maxLifetime = maxLifetime;
	}
	public Integer getMaximumPoolSize() {
		return maximumPoolSize;
	}
	public void setMaximumPoolSize(Integer maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
	public Integer getMinimumIdle() {
		return minimumIdle;
	}
	public void setMinimumIdle(Integer minimumIdle) {
		this.minimumIdle = minimumIdle;
	}
	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiamondDataSourceBean [dataSourceClassName=");
		builder.append(dataSourceClassName);
		builder.append(", dataSourceProperties=");
		builder.append(dataSourceProperties);
		builder.append(", dataSourceJNDI=");
		builder.append(dataSourceJNDI);
		builder.append(", isCobar=");
		builder.append(isCobar);
		builder.append(", schemaName=");
		builder.append(schemaName);
		builder.append(", catgroup=");
		builder.append(catgroup);
		builder.append(", protocol=");
		builder.append(protocol);
		builder.append(", zkServerAddr=");
		builder.append(zkServerAddr);
		builder.append(", zkMycatClusterName=");
		builder.append(zkMycatClusterName);
		builder.append(", nodePrefix=");
		builder.append(nodePrefix);
		builder.append(", configLocation=");
		builder.append(configLocation);
		builder.append(", mapperLocation=");
		builder.append(mapperLocation);
		builder.append(", baseScanPackge=");
		builder.append(baseScanPackge);
		builder.append(", autoCommit=");
		builder.append(autoCommit);
		builder.append(", connectionTimeout=");
		builder.append(connectionTimeout);
		builder.append(", idleTimeout=");
		builder.append(idleTimeout);
		builder.append(", maxLifetime=");
		builder.append(maxLifetime);
		builder.append(", maximumPoolSize=");
		builder.append(maximumPoolSize);
		builder.append(", minimumIdle=");
		builder.append(minimumIdle);
		builder.append("]");
		return builder.toString();
	}
	
}
