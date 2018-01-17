package com.lppz.elasticsearch.query.fielditem;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class TermArrayKvItem extends FieldItem{
/**
	 * 
	 */
	private static final long serialVersionUID = 4964513160010356920L;
private Object[] termValueArray;
public TermArrayKvItem(){}
public TermArrayKvItem(String termField,Object[] termValueArray){
	this.termField=termField;
	this.termValueArray=termValueArray;
}
@Override
public QueryBuilder build() {
	return QueryBuilders.termsQuery(termField, termValueArray);
}
public Object[] getTermValueArray() {
	return termValueArray;
}
public void setTermValueArray(Object[] termValueArray) {
	this.termValueArray = termValueArray;
}
}