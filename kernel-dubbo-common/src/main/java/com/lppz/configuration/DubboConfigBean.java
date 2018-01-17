package com.lppz.configuration;

import java.util.List;
import java.util.Properties;

public class DubboConfigBean {
	private List<Properties> registryAddress;
	private String applicationName;
	private String applicationOwner;
	private String applicationOrg;
	private String annotation_package;
	public List<Properties> getRegistryAddress() {
		return registryAddress;
	}
	public void setRegistryAddress(List<Properties> registryAddress) {
		this.registryAddress = registryAddress;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationOwner() {
		return applicationOwner;
	}
	public void setApplicationOwner(String applicationOwner) {
		this.applicationOwner = applicationOwner;
	}
	public String getApplicationOrg() {
		return applicationOrg;
	}
	public void setApplicationOrg(String applicationOrg) {
		this.applicationOrg = applicationOrg;
	}
	public String getAnnotation_package() {
		return annotation_package;
	}
	public void setAnnotation_package(String annotation_package) {
		this.annotation_package = annotation_package;
	}


}
