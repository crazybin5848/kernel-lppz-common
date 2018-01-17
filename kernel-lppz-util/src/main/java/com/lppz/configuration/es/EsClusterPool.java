package com.lppz.configuration.es;

import java.io.Serializable;

public class EsClusterPool implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8059228672106618044L;
	public String getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(String maxTotal) {
		this.maxTotal = maxTotal;
	}
	public String getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(String maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	private String maxTotal;
	private String maxIdle;
	private String maxWaitMillis;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EsClusterPool [maxTotal=");
		builder.append(maxTotal);
		builder.append(", maxIdle=");
		builder.append(maxIdle);
		builder.append(", maxWaitMillis=");
		builder.append(maxWaitMillis);
		builder.append("]");
		return builder.toString();
	}
}
