package com.lppz.hive.bean;

import java.util.List;

public class Rdbms2HDfsBean {
private String hiveschema;
private String hivetableName;
private String sqltableName;
private String pk;
private String sql;
private String ddl;
private boolean mode;
private List<HivePartionCol> hpcList;
public List<HivePartionCol> getHpcList() {
	return hpcList;
}
public void setHpcList(List<HivePartionCol> hpcList) {
	this.hpcList = hpcList;
}
public String getPk() {
	return pk;
}
public String getHiveschema() {
	return hiveschema;
}
public void setHiveschema(String hiveschema) {
	this.hiveschema = hiveschema;
}
public String getHivetableName() {
	return hivetableName;
}
public void setHivetableName(String hivetableName) {
	this.hivetableName = hivetableName;
}
public void setPk(String pk) {
	this.pk = pk;
}
public String getSql() {
	return sql;
}
public void setSql(String sql) {
	this.sql = sql;
}
public String getDdl() {
	return ddl;
}
public void setDdl(String ddl) {
	this.ddl = ddl;
}
public boolean isMode() {
	return mode;
}
public void setMode(boolean mode) {
	this.mode = mode;
}
public String getSqltableName() {
	return sqltableName;
}
public void setSqltableName(String sqltableName) {
	this.sqltableName = sqltableName;
}
}
