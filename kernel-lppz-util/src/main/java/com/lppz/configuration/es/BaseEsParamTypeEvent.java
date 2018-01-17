package com.lppz.configuration.es;

import java.io.Serializable;

public class BaseEsParamTypeEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -471541445714021806L;
	private String id;
	private String esOperType;
	private String typeName;
	private String idxName;
	private String termField;
	private String idxCurrday;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEsOperType() {
		return esOperType;
	}

	public void setEsOperType(String esOperType) {
		this.esOperType = esOperType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getIdxName() {
		return idxName;
	}

	public void setIdxName(String idxName) {
		this.idxName = idxName;
	}

	public String getIdxCurrday() {
		return idxCurrday;
	}

	public void setIdxCurrday(String idxCurrday) {
		this.idxCurrday = idxCurrday;
	}

	public String getTermField() {
		return termField;
	}

	public void setTermField(String termField) {
		this.termField = termField;
	}
}