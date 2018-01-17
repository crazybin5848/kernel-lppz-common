package com.lppz.mongoapi.bean;

public class SyncRedisToEsModel {
	
	private String table;
	private String pk;
	private String pkValue;
	private String column;
	
	public SyncRedisToEsModel() {
	}
	
	public SyncRedisToEsModel(String table, String pk, String pkValue,
			String column) {
		super();
		this.table = table;
		this.pk = pk;
		this.pkValue = pkValue;
		this.column = column;
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
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SyncRedisToEsModel [table=");
		builder.append(table);
		builder.append(", pk=");
		builder.append(pk);
		builder.append(", pkValue=");
		builder.append(pkValue);
		builder.append(", column=");
		builder.append(column);
		builder.append("]");
		return builder.toString();
	}
}
