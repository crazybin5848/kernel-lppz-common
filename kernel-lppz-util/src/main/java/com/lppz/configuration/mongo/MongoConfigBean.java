package com.lppz.configuration.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public class MongoConfigBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5146300500615278768L;

	private int poolSize;
	
	private int maxWaitTime;
	
	private int socketTimeout;

	private int maxConnectionLifeTime;
	
	private int connectTimeout;
	
	private List<Properties> serverAddress;
	
	private String db;
	
	private List<String> tables;
	
	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getMaxConnectionLifeTime() {
		return maxConnectionLifeTime;
	}

	public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
		this.maxConnectionLifeTime = maxConnectionLifeTime;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public List<Properties> getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(List<Properties> serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MongoConfigBean [poolSize=");
		builder.append(poolSize);
		builder.append(", maxWaitTime=");
		builder.append(maxWaitTime);
		builder.append(", socketTimeout=");
		builder.append(socketTimeout);
		builder.append(", maxConnectionLifeTime=");
		builder.append(maxConnectionLifeTime);
		builder.append(", connectTimeout=");
		builder.append(connectTimeout);
		builder.append(", serverAddress=");
		builder.append(serverAddress);
		builder.append(", db=");
		builder.append(db);
		builder.append(", tables=");
		builder.append(tables);
		builder.append("]");
		return builder.toString();
	}
}
