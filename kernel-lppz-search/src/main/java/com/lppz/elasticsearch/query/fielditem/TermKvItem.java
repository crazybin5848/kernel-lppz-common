package com.lppz.elasticsearch.query.fielditem;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class TermKvItem extends FieldItem{
/**
	 * 
	 */
	private static final long serialVersionUID = 4964513160010356920L;
private Object termValue;
public TermKvItem(){}
public TermKvItem(String termField,Object termValue){
	this.termField=termField;
	this.termValue=termValue;
}
public Object getTermValue() {
	return termValue;
}
public void setTermValue(Object termValue) {
	this.termValue = termValue;
}
@Override
public QueryBuilder build() {
	return QueryBuilders.termQuery(termField, termValue);
}
}
