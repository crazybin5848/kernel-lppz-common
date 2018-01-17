package com.lppz.mycat.monitor.model;

import java.util.Date;

public class NodeModel {
	
	private String nodeName;
	private String ip;
	private int port;
	private String linuxuser;
	private String linuxpwd;
	private Date restartTime;
	
	public NodeModel() {
	}
	public NodeModel(String nodeName, String ip, int port, String linuxuser, String linuxpwd) {
		this.ip = ip;
		this.port = port;
		this.nodeName = nodeName;
		this.linuxuser = linuxuser;
		this.linuxpwd = linuxpwd;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public String getLinuxuser() {
		return linuxuser;
	}
	public void setLinuxuser(String linuxuser) {
		this.linuxuser = linuxuser;
	}
	public String getLinuxpwd() {
		return linuxpwd;
	}
	public void setLinuxpwd(String linuxpwd) {
		this.linuxpwd = linuxpwd;
	}
	
	public Date getRestartTime() {
		return restartTime;
	}
	public void setRestartTime(Date restartTime) {
		this.restartTime = restartTime;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NodeModel [nodeName=");
		builder.append(nodeName);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", port=");
		builder.append(port);
		builder.append(", linuxuser=");
		builder.append(linuxuser);
		builder.append(", linuxpwd=");
		builder.append(linuxpwd);
		builder.append(", restartTime=");
		builder.append(restartTime);
		builder.append("]");
		return builder.toString();
	}
}
