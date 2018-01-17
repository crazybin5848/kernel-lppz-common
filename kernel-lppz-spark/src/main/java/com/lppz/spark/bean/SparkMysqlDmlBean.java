package com.lppz.spark.bean;

import java.util.List;

import com.lppz.spark.util.SparkHiveUtil;

public class SparkMysqlDmlBean {
	private String tableName;
	private String partitionColumn;
	private String relateKey;
	private boolean isColTypeInt = true;
	private int numPartitions;
	private Long total4Once;
	private Long offset;
	private String sql;
	private String colList;
	private String[] colArray;
	public String[] getColArray() {
		return colArray;
	}

	public void setColArray(String[] colArray) {
		this.colArray = colArray;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPartitionColumn() {
		return partitionColumn;
	}

	public void setPartitionColumn(String partitionColumn) {
		this.partitionColumn = partitionColumn;
	}

	public int getNumPartitions() {
		return numPartitions;
	}

	public void setNumPartitions(int numPartitions) {
		this.numPartitions = numPartitions;
	}

	public Long getTotal4Once() {
		return total4Once;
	}

	public void setTotal4Once(Long total4Once) {
		this.total4Once = total4Once;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isColTypeInt() {
		return isColTypeInt;
	}

	public void setColTypeInt(boolean isColTypeInt) {
		this.isColTypeInt = isColTypeInt;
	}

	public String getRelateKey() {
		return relateKey;
	}

	public void setRelateKey(String relateKey) {
		this.relateKey = relateKey;
	}

	public String getColList() {
		return colList;
	}

	public void setColList(String colList) {
		this.colList = colList;
	}

	public void buildSplitArray(List<String> oidList) {
		this.colArray=SparkHiveUtil.buildStringArray(oidList, numPartitions, relateKey);
	}
}