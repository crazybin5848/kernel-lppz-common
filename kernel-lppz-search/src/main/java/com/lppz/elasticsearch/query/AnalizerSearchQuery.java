package com.lppz.elasticsearch.query;

import java.util.List;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class AnalizerSearchQuery extends SearchQuery{
private List<String> searchFields;
private String queryString;
private String analizer;
public String getAnalizer() {
	return analizer;
}
public void setAnalizer(String analizer) {
	this.analizer = analizer;
}
public List<String> getSearchFields() {
	return searchFields;
}
public void setSearchFields(List<String> searchFields) {
	this.searchFields = searchFields;
}
public String getQueryString() {
	return queryString;
}
public void setQueryString(String queryString) {
	this.queryString = queryString;
}
public QueryBuilder build(){
	if(queryString==null)
		return null;
	QueryStringQueryBuilder queryStringBuilder= new QueryStringQueryBuilder(queryString);
	queryStringBuilder.useDisMax(true); 
	if(analizer!=null)
		queryStringBuilder.analyzer(analizer);
	for(String field:searchFields)
	queryStringBuilder.field(field);
	return queryStringBuilder;
}
}
