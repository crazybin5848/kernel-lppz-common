package com.lppz.canal.model;

import java.util.List;

public class TableModel {
	private String schema;
	private String tableName;
	private String id;
	private String parentId;
	private String optime;
	private String optype;
	private String sql;
	
	private String key;
	private int mainId;
	private String tag;
	private String prefix;
	private List<ChangeRow> changeRows;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<ChangeRow> getChangeRows() {
		return changeRows;
	}
	public void setChangeRows(List<ChangeRow> changeRows) {
		this.changeRows = changeRows;
	}
	public String getOptime() {
		return optime;
	}
	public void setOptime(String optime) {
		this.optime = optime;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getMainId() {
		return mainId;
	}
	public void setMainId(int mainId) {
		this.mainId = mainId;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableModel [schema=");
		builder.append(schema);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", id=");
		builder.append(id);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", optime=");
		builder.append(optime);
		builder.append(", optype=");
		builder.append(optype);
		builder.append(", sql=");
		builder.append(sql);
		builder.append(", key=");
		builder.append(key);
		builder.append(", mainId=");
		builder.append(mainId);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", prefix=");
		builder.append(prefix);
		builder.append(", changeRows=");
		builder.append(changeRows);
		builder.append("]");
		return builder.toString();
	}
}
