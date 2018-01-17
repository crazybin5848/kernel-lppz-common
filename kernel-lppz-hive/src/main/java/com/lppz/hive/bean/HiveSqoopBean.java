package com.lppz.hive.bean;

import java.util.List;

public class HiveSqoopBean {
	private HiveSqoopConfigBean configBean;
	
	private List<Rdbms2HDfsBean> sourceList;

	public List<Rdbms2HDfsBean> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<Rdbms2HDfsBean> sourceList) {
		this.sourceList = sourceList;
	}

	public HiveSqoopConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(HiveSqoopConfigBean configBean) {
		this.configBean = configBean;
	}
}