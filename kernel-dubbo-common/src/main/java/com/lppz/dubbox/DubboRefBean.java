package com.lppz.dubbox;

public class DubboRefBean {
	public static final String LPPZCODE="lppzCode";
	public DubboRefBean(){}
	public DubboRefBean(String code,String url){
		this.code=code;
		this.url=url;
	}
private String code;
private String group="";
private String version="1.0.0";
public String getGroup() {
	return group;
}
public void setGroup(String group) {
	this.group = group;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
private String url;
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
}
