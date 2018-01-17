package com.lppz.spark.bean;

public class SparkSqlConfigBean {
	private String rdbmsjdbcUrl;
	private String rdbmsjdbcUser;
	private String rdbmsjdbcPasswd;
	private String rdbmsdbDriver;
	private String schema;
	
	public String getRdbmsjdbcUrl() {
		return rdbmsjdbcUrl;
	}
	public void setRdbmsjdbcUrl(String rdbmsjdbcUrl) {
		this.rdbmsjdbcUrl = rdbmsjdbcUrl;
	}
	public String getRdbmsjdbcUser() {
		return rdbmsjdbcUser;
	}
	public void setRdbmsjdbcUser(String rdbmsjdbcUser) {
		this.rdbmsjdbcUser = rdbmsjdbcUser;
	}
	public String getRdbmsjdbcPasswd() {
		return rdbmsjdbcPasswd;
	}
	public void setRdbmsjdbcPasswd(String rdbmsjdbcPasswd) {
		this.rdbmsjdbcPasswd = rdbmsjdbcPasswd;
	}
	public String getRdbmsdbDriver() {
		return rdbmsdbDriver;
	}
	public void setRdbmsdbDriver(String rdbmsdbDriver) {
		this.rdbmsdbDriver = rdbmsdbDriver;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
}