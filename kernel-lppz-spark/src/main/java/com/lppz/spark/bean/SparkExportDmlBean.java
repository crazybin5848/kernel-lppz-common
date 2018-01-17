package com.lppz.spark.bean;



public class SparkExportDmlBean {
	private String esIndex;
	private String esType;
	private String schema;
	private Long total4Once;
	private String sql;
	private Integer partionNum;
	private String dateColumnNames;
	
	public String getEsIndex() {
		return esIndex;
	}

	public void setEsIndex(String esIndex) {
		this.esIndex = esIndex;
	}

	public Long getTotal4Once() {
		return total4Once;
	}

	public void setTotal4Once(Long total4Once) {
		this.total4Once = total4Once;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Integer getPartionNum() {
		return partionNum;
	}

	public void setPartionNum(Integer partionNum) {
		this.partionNum = partionNum;
	}

	public String getEsType() {
		return esType;
	}

	public void setEsType(String esType) {
		this.esType = esType;
	}

	public String getDateColumnNames() {
		return dateColumnNames;
	}

	public void setDateColumnNames(String dateColumnNames) {
		this.dateColumnNames = dateColumnNames;
	}
}