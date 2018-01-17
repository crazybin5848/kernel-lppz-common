package com.lppz.canal.bean;

import java.io.Serializable;
import java.util.List;

public class TableBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7934113490698654567L;
	
	private String schemaName;
	
	private String tableName;
	
	private String primaryKey;
	
	private String foreignKey;
	
	private String parentSchemaName;
	
	private String parentTalbeName;
	
	private String parentTalbePrimaryKey;
	
	private List<String> relationKeys;
	
	private boolean isRootTable;
	
	private boolean isLeafTable;
	
	private String hbaseTableName;
	
	private String hbaseCFName;
	
	private String parentFamilyName;
	
	private String mainIdColumn;
	
	private String colList;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getParentTalbeName() {
		return parentTalbeName;
	}

	public void setParentTalbeName(String parentTalbeName) {
		this.parentTalbeName = parentTalbeName;
	}

	public String getParentTalbePrimaryKey() {
		return parentTalbePrimaryKey;
	}

	public void setParentTalbePrimaryKey(String parentTalbePrimaryKey) {
		this.parentTalbePrimaryKey = parentTalbePrimaryKey;
	}

	public List<String> getRelationKeys() {
		return relationKeys;
	}

	public void setRelationKeys(List<String> relationKeys) {
		this.relationKeys = relationKeys;
	}

	public String getParentSchemaName() {
		return parentSchemaName;
	}

	public void setParentSchemaName(String parentSchemaName) {
		this.parentSchemaName = parentSchemaName;
	}

	public boolean isRootTable() {
		return isRootTable;
	}

	public void setRootTable(boolean isRootTable) {
		this.isRootTable = isRootTable;
	}

	public boolean isLeafTable() {
		return isLeafTable;
	}

	public void setLeafTable(boolean isLeafTable) {
		this.isLeafTable = isLeafTable;
	}

	public String getHbaseTableName() {
		return hbaseTableName;
	}

	public void setHbaseTableName(String hbaseTableName) {
		this.hbaseTableName = hbaseTableName;
	}

	public String getHbaseCFName() {
		return hbaseCFName;
	}

	public void setHbaseCFName(String hbaseCFName) {
		this.hbaseCFName = hbaseCFName;
	}

	public String getParentFamilyName() {
		return parentFamilyName;
	}

	public void setParentFamilyName(String parentFamilyName) {
		this.parentFamilyName = parentFamilyName;
	}

	public String getMainIdColumn() {
		return mainIdColumn;
	}

	public void setMainIdColumn(String mainIdColumn) {
		this.mainIdColumn = mainIdColumn;
	}

	public String getColList() {
		return colList;
	}

	public void setColList(String colList) {
		this.colList = colList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableBean [schemaName=");
		builder.append(schemaName);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", primaryKey=");
		builder.append(primaryKey);
		builder.append(", foreignKey=");
		builder.append(foreignKey);
		builder.append(", parentSchemaName=");
		builder.append(parentSchemaName);
		builder.append(", parentTalbeName=");
		builder.append(parentTalbeName);
		builder.append(", parentTalbePrimaryKey=");
		builder.append(parentTalbePrimaryKey);
		builder.append(", relationKeys=");
		builder.append(relationKeys);
		builder.append(", isRootTable=");
		builder.append(isRootTable);
		builder.append(", isLeafTable=");
		builder.append(isLeafTable);
		builder.append(", hbaseTableName=");
		builder.append(hbaseTableName);
		builder.append(", hbaseCFName=");
		builder.append(hbaseCFName);
		builder.append(", parentFamilyName=");
		builder.append(parentFamilyName);
		builder.append(", mainIdColumn=");
		builder.append(mainIdColumn);
		builder.append(", colList=");
		builder.append(colList);
		builder.append("]");
		return builder.toString();
	}
}
