package com.lppz.elasticsearch.query.fielditem;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class WildCardKvItem extends FieldItem{
/**
	 * 
	 */
	private static final long serialVersionUID = 8058006642345232152L;
private String wildCardValue;
public WildCardKvItem(){}
public WildCardKvItem(String termField,String wildCardValue){
	this.termField=termField;
	this.wildCardValue=wildCardValue;
}
@Override
public QueryBuilder build() {
	return QueryBuilders.wildcardQuery(termField, wildCardValue);
}
}
