package com.lppz.bean;

import java.util.Map;

public class DiamondClusterNodeBean {
	private Map<String,Integer> ports;
	
	private Map<String,String> params;

	public Map<String, Integer> getPorts() {
		return ports;
	}

	public void setPorts(Map<String, Integer> ports) {
		this.ports = ports;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiamondClusterNodeBean [ports=");
		builder.append(ports);
		builder.append(", params=");
		builder.append(params);
		builder.append("]");
		return builder.toString();
	}
}
