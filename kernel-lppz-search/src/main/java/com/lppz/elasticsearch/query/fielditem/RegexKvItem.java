package com.lppz.elasticsearch.query.fielditem;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class RegexKvItem extends FieldItem{
/**
	 * 
	 */
	private static final long serialVersionUID = 2268596461111894529L;
private String regValue;
public RegexKvItem(){}
public RegexKvItem(String termField,String regValue){
	this.termField=termField;
	this.regValue=regValue;
}
@Override
public QueryBuilder build() {
	return QueryBuilders.regexpQuery(termField, regValue);
}
}
