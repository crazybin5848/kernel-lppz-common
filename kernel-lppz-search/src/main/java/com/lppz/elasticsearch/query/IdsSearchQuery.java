package com.lppz.elasticsearch.query;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class IdsSearchQuery extends SearchQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5533642270862320210L;
	private List<String> ids=new ArrayList<String>();
	
	public IdsSearchQuery(List<String> ids) {
		this.ids = ids;
	}
	
	public QueryBuilder build() {
		if (ids==null||ids.isEmpty())
			return null;
		return QueryBuilders.idsQuery().ids(ids);
	}
}
