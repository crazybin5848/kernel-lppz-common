package com.lppz.configuration.es;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public class EsBaseYamlBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2916393368949395097L;
	private EsClusterPool esClusterPool;
	private List<Properties> esclusterNode;
	private String clusterName;
	private int threadPoolSize=50;

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public EsClusterPool getEsClusterPool() {
		return esClusterPool;
	}

	public void setEsClusterPool(EsClusterPool esClusterPool) {
		this.esClusterPool = esClusterPool;
	}

	public List<Properties> getEsclusterNode() {
		return esclusterNode;
	}

	public void setEsclusterNode(List<Properties> esclusterNode) {
		this.esclusterNode = esclusterNode;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EsBaseYamlBean [esClusterPool=");
		builder.append(esClusterPool);
		builder.append(", esclusterNode=");
		builder.append(esclusterNode);
		builder.append(", clusterName=");
		builder.append(clusterName);
		builder.append("]");
		return builder.toString();
	}
}
