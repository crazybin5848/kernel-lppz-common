package com.lppz.bean;

import org.yaml.snakeyaml.Yaml;

import com.lppz.diamond.client.event.ConfigurationListener;


public class DiamondBean {
	
	private String serverAddress;
	private int serverPort;
	private String profile;
	private String projCode;
	private boolean useLocal;
	private ConfigurationListener listener;
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	
	public ConfigurationListener getListener() {
		return listener;
	}
	public void setListener(ConfigurationListener listener) {
		this.listener = listener;
	}
	
	public boolean isUseLocal() {
		return useLocal;
	}
	public void setUseLocal(boolean useLocal) {
		this.useLocal = useLocal;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiamondBean [serverAddress=");
		builder.append(serverAddress);
		builder.append(", serverPort=");
		builder.append(serverPort);
		builder.append(", profile=");
		builder.append(profile);
		builder.append(", projCode=");
		builder.append(projCode);
		builder.append(", useLocal=");
		builder.append(useLocal);
		builder.append(", listener=");
		builder.append(listener);
		builder.append("]");
		return builder.toString();
	}
	public static void main(String[] args) {
		DiamondBean data = new DiamondBean();
		data.setServerAddress("192.168.37.247");
		data.setServerPort(8283);
		data.setProjCode("kernel-redis");
		data.setProfile("development");
		data.setUseLocal(false);
		System.out.println(new Yaml().dump(data));
	}
}
