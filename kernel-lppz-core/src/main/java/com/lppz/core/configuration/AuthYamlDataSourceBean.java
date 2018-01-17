package com.lppz.core.configuration;

import java.util.Properties;

public class AuthYamlDataSourceBean {
private String dataSourceClassName;
private Properties dataSourceProperties;
private String dataSourceJNDI;
private String isCobar;
private String configLocation;
private String mapperLocation;
private String baseScanPackge;
private String autoCommit;
private String connectionTimeout;
private String idleTimeout;
private String maxLifetime;
private String maximumPoolSize;
private String minimumIdle;
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
public String getIsCobar() {
	return isCobar;
}
public void setIsCobar(String isCobar) {
	this.isCobar = isCobar;
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
public String getAutoCommit() {
	return autoCommit;
}
public void setAutoCommit(String autoCommit) {
	this.autoCommit = autoCommit;
}
public String getConnectionTimeout() {
	return connectionTimeout;
}
public void setConnectionTimeout(String connectionTimeout) {
	this.connectionTimeout = connectionTimeout;
}
public String getIdleTimeout() {
	return idleTimeout;
}
public void setIdleTimeout(String idleTimeout) {
	this.idleTimeout = idleTimeout;
}
public String getMaxLifetime() {
	return maxLifetime;
}
public void setMaxLifetime(String maxLifetime) {
	this.maxLifetime = maxLifetime;
}
public String getMaximumPoolSize() {
	return maximumPoolSize;
}
public void setMaximumPoolSize(String maximumPoolSize) {
	this.maximumPoolSize = maximumPoolSize;
}
public String getMinimumIdle() {
	return minimumIdle;
}
public void setMinimumIdle(String minimumIdle) {
	this.minimumIdle = minimumIdle;
}
}
