package com.lppz.canal.model;

public class ChangeColumn {

	private String name;
	private String value;
	private String mysqlType;
	private String updated;
	
	public ChangeColumn() {
		// TODO Auto-generated constructor stub
	}

	public ChangeColumn(String name, String value, String mysqlType,
			String updated) {
		super();
		this.name = name;
		this.value = value;
		this.mysqlType = mysqlType;
		this.updated = updated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMysqlType() {
		return mysqlType;
	}

	public void setMysqlType(String mysqlType) {
		this.mysqlType = mysqlType;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangeColumn [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", mysqlType=");
		builder.append(mysqlType);
		builder.append(", updated=");
		builder.append(updated);
		builder.append("]");
		return builder.toString();
	}
}
