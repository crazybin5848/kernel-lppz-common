package com.lppz.spark.bean;

public class HivePartionCol {
	private String col;
	private String type;
	private String value;
	private String orginalCol;
	public String getOrginalCol() {
		return orginalCol;
	}
	public void setOrginalCol(String orginalCol) {
		this.orginalCol = orginalCol;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
