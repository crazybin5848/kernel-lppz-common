package com.lppz.canal.bean;

import java.util.Map;

import com.lppz.canal.enums.OperEnums;

public class ChangeDataBean {
	private String schema;
	private String table;
	private OperEnums operEnums;
	private Map<String,String> values;
	private String key;
	private String hbaseTable;
	private String hbaseCf;
	private String prefix;
	private String rowkey;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public OperEnums getOperEnums() {
		return operEnums;
	}
	public void setOperEnums(OperEnums operEnums) {
		this.operEnums = operEnums;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getHbaseTable() {
		return hbaseTable;
	}
	public void setHbaseTable(String hbaseTable) {
		this.hbaseTable = hbaseTable;
	}
	public String getHbaseCf() {
		return hbaseCf;
	}
	public void setHbaseCf(String hbaseCf) {
		this.hbaseCf = hbaseCf;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangeDataBean [schema=");
		builder.append(schema);
		builder.append(", table=");
		builder.append(table);
		builder.append(", operEnums=");
		builder.append(operEnums);
		builder.append(", values=");
		builder.append(values);
		builder.append(", key=");
		builder.append(key);
		builder.append(", hbaseTable=");
		builder.append(hbaseTable);
		builder.append(", hbaseCf=");
		builder.append(hbaseCf);
		builder.append(", prefix=");
		builder.append(prefix);
		builder.append(", rowkey=");
		builder.append(rowkey);
		builder.append("]");
		return builder.toString();
	}
}
