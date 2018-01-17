package com.lppz.elasticsearch.query.fielditem;

import java.io.Serializable;

import org.elasticsearch.index.query.QueryBuilder;

public abstract class FieldItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5876755029033399032L;
	protected String termField;

	public String getTermField() {
		return termField;
	}

	public void setTermField(String termField) {
		this.termField = termField;
	}
	
	public abstract QueryBuilder build();
}
