package com.lppz.bean;

import java.io.Serializable;

public class RollbackMsgs implements Serializable{
	
	private static final long serialVersionUID = -6893884438399140553L;

	private String key;
	
	private String keySource;
	
	private String module;
	
	private java.util.Map<String,Object> Map;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeySource() {
		return keySource;
	}

	public void setKeySource(String keySource) {
		this.keySource = keySource;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public java.util.Map<String, Object> getMap() {
		return Map;
	}

	public void setMap(java.util.Map<String, Object> map) {
		Map = map;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RollbackBean [key=");
		builder.append(key);
		builder.append(", keySource=");
		builder.append(keySource);
		builder.append(", module=");
		builder.append(module);
		builder.append(", Map=");
		builder.append(Map);
		builder.append("]");
		return builder.toString();
	}
}
