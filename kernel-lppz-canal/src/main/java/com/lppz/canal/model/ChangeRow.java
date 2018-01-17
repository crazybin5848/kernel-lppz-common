package com.lppz.canal.model;

import java.util.List;

public class ChangeRow {
	private String primaryKeyValue;
	private String foreignKeyValue;
	private List<ChangeColumn> beforeColumns;
	private List<ChangeColumn> afterColumns;
	public List<ChangeColumn> getBeforeColumns() {
		return beforeColumns;
	}
	public void setBeforeColumns(List<ChangeColumn> beforeColumns) {
		this.beforeColumns = beforeColumns;
	}
	public List<ChangeColumn> getAfterColumns() {
		return afterColumns;
	}
	public void setAfterColumns(List<ChangeColumn> afterColumns) {
		this.afterColumns = afterColumns;
	}
	
	public String getPrimaryKeyValue() {
		return primaryKeyValue;
	}
	public void setPrimaryKeyValue(String primaryKey) {
		this.primaryKeyValue = primaryKey;
	}
	
	public String getForeignKeyValue() {
		return foreignKeyValue;
	}
	public void setForeignKeyValue(String foreignKeyValue) {
		this.foreignKeyValue = foreignKeyValue;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangeRow [primaryKeyValue=");
		builder.append(primaryKeyValue);
		builder.append(", foreignKeyValue=");
		builder.append(foreignKeyValue);
		builder.append(", beforeColumns=");
		builder.append(beforeColumns);
		builder.append(", afterColumns=");
		builder.append(afterColumns);
		builder.append("]");
		return builder.toString();
	}
}
