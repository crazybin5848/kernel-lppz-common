package com.lppz.hive.bean;

public class Hdfs2RdbmsBean {
	private HiveSqoopConfigBean configBean;
	private String rdbmstableName;
	private String rdbmsschemaName;
	private String dfsInputDir;

	public HiveSqoopConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(HiveSqoopConfigBean configBean) {
		this.configBean = configBean;
	}

	public String getRdbmstableName() {
		return rdbmstableName;
	}

	public void setRdbmstableName(String rdbmstableName) {
		this.rdbmstableName = rdbmstableName;
	}

	public String getRdbmsschemaName() {
		return rdbmsschemaName;
	}

	public void setRdbmsschemaName(String rdbmsschemaName) {
		this.rdbmsschemaName = rdbmsschemaName;
	}

	public String getDfsInputDir() {
		return dfsInputDir;
	}

	public void setDfsInputDir(String dfsInputDir) {
		this.dfsInputDir = dfsInputDir;
	}
}