package io.mycat.sencondaryindex.model;

import java.io.Serializable;

public class CatTransDetailModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5766194039591048443L;
	private String dmlType;
	private String tmpValue;
	private String idxid;
	private String indexName;
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getDmlType() {
		return dmlType;
	}
	public void setDmlType(String dmlType) {
		this.dmlType = dmlType;
	}
	public String getTmpValue() {
		return tmpValue;
	}
	public void setTmpValue(String tmpValue) {
		this.tmpValue = tmpValue;
	}
	public String getIdxid() {
		return idxid;
	}
	public void setIdxid(String idxid) {
		this.idxid = idxid;
	}
}
