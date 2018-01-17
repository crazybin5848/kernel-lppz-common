/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.datasource;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;


/**
 * @author zoubin
 */
public class LppzBoncpDataSource implements DataSource,Serializable,Closeable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3047716120546703694L;
	private static final Logger logger = Logger.getLogger(LppzBoncpDataSource.class);
	private BoneCPConfig config;
	private Properties props=new Properties();
	private transient BoneCP boneCP;
	private String jdbcUrl;
	private String jdbcDriverName;
	private String userName;
	private String passwd;
	private boolean lazyInit=true;
	private int minConnectionsPerPartition;
	private int maxConnectionsPerPartition=5;
	private int partitionCount=3;
	private boolean closeConnectionWatch=false;
	private boolean logStatementsEnabled=false;
	private int queryExecuteTimeLimit;
	private int connectionTimeout;
	private int maxConnectionAge;
	private int idleMaxAge;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public int getMinConnectionsPerPartition() {
		return minConnectionsPerPartition;
	}

	public void setMinConnectionsPerPartition(int minConnectionsPerPartition) {
		this.minConnectionsPerPartition = minConnectionsPerPartition;
	}

	public int getMaxConnectionsPerPartition() {
		return maxConnectionsPerPartition;
	}

	public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
		this.maxConnectionsPerPartition = maxConnectionsPerPartition;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public int getPartitionCount() {
		return partitionCount;
	}

	public void setPartitionCount(int partitionCount) {
		this.partitionCount = partitionCount;
	}

	public boolean isCloseConnectionWatch() {
		return closeConnectionWatch;
	}

	public void setCloseConnectionWatch(boolean closeConnectionWatch) {
		this.closeConnectionWatch = closeConnectionWatch;
	}

	public boolean isLogStatementsEnabled() {
		return logStatementsEnabled;
	}

	public String getJdbcDriverName() {
		return jdbcDriverName;
	}

	public void setJdbcDriverName(String jdbcDriverName) {
		this.jdbcDriverName = jdbcDriverName;
	}

	public void setLogStatementsEnabled(boolean logStatementsEnabled) {
		this.logStatementsEnabled = logStatementsEnabled;
	}

	public int getQueryExecuteTimeLimit() {
		return queryExecuteTimeLimit;
	}

	public void setQueryExecuteTimeLimit(int queryExecuteTimeLimit) {
		this.queryExecuteTimeLimit = queryExecuteTimeLimit;
	}
	
	public LppzBoncpDataSource build(){
		try {
			Class.forName(jdbcDriverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		this.config=new BoneCPConfig();
		config.setJdbcUrl(this.jdbcUrl);
		this.config.setUsername(userName);
		this.config.setPassword(passwd);
		this.config.setPartitionCount(partitionCount);
        config.setLazyInit(this.lazyInit);
        config.setMinConnectionsPerPartition(minConnectionsPerPartition);
        config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
        config.setLogStatementsEnabled(this.logStatementsEnabled);
        config.setQueryExecuteTimeLimitInMs(queryExecuteTimeLimit);
		this.config.setCloseConnectionWatch(closeConnectionWatch);
		this.config.setConnectionTimeoutInMs(connectionTimeout);
		this.config.setIdleMaxAgeInSeconds(idleMaxAge);
		this.config.setMaxConnectionAgeInSeconds(maxConnectionAge);
		props.put("user",userName);
        props.put("password",passwd);
		try {
			this.boneCP=new BoneCP(config);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}
	    return this;
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getMaxConnectionAge() {
		return maxConnectionAge;
	}

	public void setMaxConnectionAge(int maxConnectionAge) {
		this.maxConnectionAge = maxConnectionAge;
	}

	public int getIdleMaxAge() {
		return idleMaxAge;
	}

	public void setIdleMaxAge(int idleMaxAge) {
		this.idleMaxAge = idleMaxAge;
	}

	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		if(boneCP==null)
			return null;
		return this.boneCP.getConnection();
	}

	public Connection getConnection() throws SQLException {
		if(boneCP==null)
			return null;
		return this.boneCP.getConnection();
	}
	 public void close()
	 {
		 this.boneCP.close();
	 }
}