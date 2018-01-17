package com.lppz.bean;

import java.util.Map;

public class PortBean{
	private Integer minPort;
	private Map<String,Integer> portsMap;
	private Map<String,String> paramsMap;
	
	public PortBean() {
	}
	
	public PortBean(Integer minPort, Map<String, Integer> portsMap, Map<String,String> paramsMap) {
		this.minPort = minPort;
		this.portsMap = portsMap;
		this.paramsMap = paramsMap;
	}

	public int getMinPort() {
		return minPort;
	}

	public void setMinPort(Integer minPort) {
		this.minPort = minPort;
	}

	public Map<String, Integer> getPortsMap() {
		return portsMap;
	}

	public void setPortsMap(Map<String, Integer> portsMap) {
		this.portsMap = portsMap;
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PortBean [minPort=");
		builder.append(minPort);
		builder.append(", portsMap=");
		builder.append(portsMap);
		builder.append(", paramsMap=");
		builder.append(paramsMap);
		builder.append("]");
		return builder.toString();
	}
}