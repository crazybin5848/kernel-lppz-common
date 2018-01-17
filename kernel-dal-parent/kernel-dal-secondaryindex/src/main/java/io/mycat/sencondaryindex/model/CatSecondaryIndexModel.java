package io.mycat.sencondaryindex.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;


public class CatSecondaryIndexModel implements Serializable{
	public String getPrimaryValue() {
		return primaryValue;
	}
	public void setPrimaryValue(String primaryValue) {
		this.primaryValue = primaryValue;
	}
	/**
	 * 
	 */
	public CatSecondaryIndexModel(String schemaName){
		this.schemaName=schemaName;
	}
	public CatSecondaryIndexModel(){}
	private static final long serialVersionUID = -8752117111201396691L;
	private String tbName;
	private String schemaName;
	private String columnName;
	private String idxValue;
	private String shardingValue;
	private String primaryValue;
	private String transNo;
	private String dmlType;
	private String catId;
	
	public String getDmlType() {
		return dmlType;
	}
	public void setDmlType(String dmlType) {
		this.dmlType = dmlType;
	}
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public String buildEsId() {
		return new StringBuilder(shardingValue).append("-").append(columnName.toLowerCase()).append("-").append(primaryValue).
				toString();
	}
	
	public enum Operation {
		Insert,Update,Delete,Select,ADDIDX,DROPIDX
	}
	private Operation op; 
	
	public Operation getOp() {
		return op;
	}
	public void setOp(Operation op) {
		this.op = op;
		if(op!=null)
		this.dmlType=op.name();
	}
	public String getTbName() {
		return tbName;
	}
	public void setTbName(String tbName) {
		if(StringUtils.isNotBlank(tbName)){
			this.tbName = tbName.replaceAll("`", "");
		}else{
			this.tbName=tbName;
		}
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getIdxValue() {
		return idxValue;
	}
	public void setIdxValue(String idxValue) {
		this.idxValue = idxValue;
	}
	public String getShardingValue() {
		return shardingValue;
	}
	public void setShardingValue(String shardingValue) {
		this.shardingValue = shardingValue;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
}