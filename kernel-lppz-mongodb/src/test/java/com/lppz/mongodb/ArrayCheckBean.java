package com.lppz.mongodb;

import java.util.List;

public class ArrayCheckBean {
	
	private String table;
	private String pk;
	private String pkValue;
	private String arrayField;
	private int lines;
	private List<Integer> overKegLength;
	private int arrayLength;
	
	public ArrayCheckBean() {
	}
	
	public ArrayCheckBean(String table, String pk, String pkValue, String arrayField,int lines, List<Integer> overKegLength, int arrayLength) {
		this.table = table;
		this.pk = pk;
		this.pkValue = pkValue;
		this.arrayField = arrayField;
		this.lines = lines;
		this.overKegLength = overKegLength;
		this.arrayLength = arrayLength;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPkValue() {
		return pkValue;
	}

	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	public String getArrayField() {
		return arrayField;
	}

	public void setArrayField(String arrayField) {
		this.arrayField = arrayField;
	}

	public int getLines() {
		return lines;
	}
	public void setLines(int lines) {
		this.lines = lines;
	}
	public List<Integer> getOverKegLength() {
		return overKegLength;
	}
	public void setOverKegLength(List<Integer> overKegLength) {
		this.overKegLength = overKegLength;
	}

	public int getArrayLength() {
		return arrayLength;
	}

	public void setArrayLength(int arrayLength) {
		this.arrayLength = arrayLength;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArrayCheckBean [table=");
		builder.append(table);
		builder.append(", pk=");
		builder.append(pk);
		builder.append(", pkValue=");
		builder.append(pkValue);
		builder.append(", arrayField=");
		builder.append(arrayField);
		builder.append(", lines=");
		builder.append(lines);
		builder.append(", overKegLength=");
		builder.append(overKegLength);
		builder.append(", arrayLength=");
		builder.append(arrayLength);
		builder.append("]");
		return builder.toString();
	}
}
