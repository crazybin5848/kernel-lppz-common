package com.lppz.spark.bean;

import java.util.Map;


public class SparkHiveSqlBean {
	private SparkSqlConfigBean configBean;
	private SparkMysqlDmlBean mysqlBean;
	public SparkMysqlDmlBean getMysqlBean() {
		return mysqlBean;
	}

	public void setMysqlBean(SparkMysqlDmlBean mysqlBean) {
		this.mysqlBean = mysqlBean;
	}

	private boolean mysqsqlUseMain;
	private Rdbms2HDfsBean sourcebean;
	private Map<String,SparkHiveSqlBean> sparkMapbean;
	public Rdbms2HDfsBean getSourcebean() {
		return sourcebean;
	}

	public void setSourcebean(Rdbms2HDfsBean sourcebean) {
		this.sourcebean = sourcebean;
	}

	public SparkSqlConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(SparkSqlConfigBean configBean) {
		this.configBean = configBean;
	}

	public boolean isMysqsqlUseMain() {
		return mysqsqlUseMain;
	}

	public void setMysqsqlUseMain(boolean mysqsqlUseMain) {
		this.mysqsqlUseMain = mysqsqlUseMain;
	}

	public Map<String, SparkHiveSqlBean> getSparkMapbean() {
		return sparkMapbean;
	}

	public void setSparkMapbean(Map<String, SparkHiveSqlBean> sparkMapbean) {
		this.sparkMapbean = sparkMapbean;
	}

}