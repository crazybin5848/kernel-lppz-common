package com.lppz.spark.bean;

import java.util.List;

public class Rdbms2HDfsBean {
	private String hiveschema;
	private String hivetableName;
	private String ddl;
	private boolean mode;
	private String hdfsUrl;
	private List<HivePartionCol> hpcList;

	public List<HivePartionCol> getHpcList() {
		return hpcList;
	}

	public void setHpcList(List<HivePartionCol> hpcList) {
		this.hpcList = hpcList;
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

	public String getHdfsUrl() {
		return hdfsUrl;
	}

	public void setHdfsUrl(String hdfsUrl) {
		this.hdfsUrl = hdfsUrl;
	}
}