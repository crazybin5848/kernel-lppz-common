package com.lppz.hive.bean;


public class HiveSqoopSingleBean {
	private HiveSqoopConfigBean configBean;
	
	private Rdbms2HDfsBean sourceBean;

	public Rdbms2HDfsBean getSourceBean() {
		return sourceBean;
	}

	public void setSourceBean(Rdbms2HDfsBean sourceBean) {
		this.sourceBean = sourceBean;
	}

	public HiveSqoopConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(HiveSqoopConfigBean configBean) {
		this.configBean = configBean;
	}
}