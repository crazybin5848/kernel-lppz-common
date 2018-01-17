package com.lppz.mongodb;

import java.util.List;

public class ArrayTableBean{
	private String tableName;
	private String pk;
	private List<String> arrayFields;
	public ArrayTableBean() {
	}
	public ArrayTableBean(String talbeName, String pk, List<String> arrayFields) {
		this.tableName = talbeName;
		this.pk = pk;
		this.arrayFields = arrayFields;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public List<String> getArrayFields() {
		return arrayFields;
	}
	public void setArrayFields(List<String> arrayFields) {
		this.arrayFields = arrayFields;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArrayTableBean [tableName=");
		builder.append(tableName);
		builder.append(", pk=");
		builder.append(pk);
		builder.append(", arrayFields=");
		builder.append(arrayFields);
		builder.append("]");
		return builder.toString();
	}
}