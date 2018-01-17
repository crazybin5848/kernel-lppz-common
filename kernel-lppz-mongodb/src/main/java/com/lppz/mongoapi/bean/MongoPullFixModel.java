package com.lppz.mongoapi.bean;

public class MongoPullFixModel {
	private String table;
	private String pk;
	private String pkValue;
	private String arrayField;
	private String fieldName;
	private String arrayFieldName;
	
	public MongoPullFixModel(){
		
	}
	
	public MongoPullFixModel(String table, String pk, String pkValue,
			String arrayField){
		this.table = table;
		this.pk = pk;
		this.pkValue = pkValue;
		setArrayField(arrayField);
	}
	
	public MongoPullFixModel(byte[] redisKey){
		String[] params = new String(redisKey).split(":");
		this.table = params[0];
		this.pk = params[1];
		this.pkValue = params[2];
		setArrayField(params[3]);
	}
	
	public static String buildFixKegKey(String table, String pk, String pkValue,
			String arrayField){
		return new StringBuilder().append(table).append(":").append(pk).append(":").append(pkValue).append(":").append(arrayField).toString();
	}
	
	public static String buildFixKegKey(String table, String pk, String pkValue,
			String arrayField,int kegNo){
		return new StringBuilder().append(table).append(":").append(pk).append(":").append(pkValue).append(":").append(arrayField).append(":").append(arrayField).toString();
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
		String[] fields = arrayField.split("\\.");
		if (fields.length > 0) {
			this.fieldName = fields[0];
			if (fields.length > 1) {
				this.arrayFieldName = fields[1];
			}
		}
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getArrayFieldName() {
		return arrayFieldName;
	}

	public void setArrayFieldName(String arrayFieldName) {
		this.arrayFieldName = arrayFieldName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MongoPullFixModel [table=");
		builder.append(table);
		builder.append(", pk=");
		builder.append(pk);
		builder.append(", pkValue=");
		builder.append(pkValue);
		builder.append(", arrayField=");
		builder.append(arrayField);
		builder.append(", fieldName=");
		builder.append(fieldName);
		builder.append(", arrayFieldName=");
		builder.append(arrayFieldName);
		builder.append("]");
		return builder.toString();
	}
}
