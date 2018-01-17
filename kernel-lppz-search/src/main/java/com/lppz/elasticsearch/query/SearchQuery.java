package com.lppz.elasticsearch.query;

import java.io.Serializable;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.lppz.elasticsearch.query.fielditem.FieldItem;

public class SearchQuery implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3492050572027400599L;

	public List<SearchQuery> getSearchQueryList() {
		return searchQueryList;
	}

	public void setSearchQueryList(List<SearchQuery> searchQueryList) {
		this.searchQueryList = searchQueryList;
	}

	public List<FieldItem> getFieldItemList() {
		return fieldItemList;
	}

	public void setFieldItemList(List<FieldItem> fieldItemList) {
		this.fieldItemList = fieldItemList;
	}

	private List<SearchQuery> searchQueryList;
	private List<FieldItem> fieldItemList;

	public QueryBuilder build() {
		if (searchQueryList == null && fieldItemList == null)
			return null;
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		if (searchQueryList != null) {
			for (SearchQuery sq : searchQueryList) {
				qb.must(sq.build());
			}
		}
		if (fieldItemList != null) {
			for (FieldItem fi : fieldItemList) {
				qb.must(fi.build());
			}
		}
		return qb;
	}
}