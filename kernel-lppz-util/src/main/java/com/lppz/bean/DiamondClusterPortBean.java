package com.lppz.bean;


public class DiamondClusterPortBean {
	private String name;
	private int port;
	
	public DiamondClusterPortBean() {
	}
	
	public DiamondClusterPortBean(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiamondClusterPortBean [name=");
		builder.append(name);
		builder.append(", port=");
		builder.append(port);
		builder.append("]");
		return builder.toString();
	}
}
