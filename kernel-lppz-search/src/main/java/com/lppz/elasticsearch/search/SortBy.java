package com.lppz.elasticsearch.search;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

public class SortBy {
	private String fieldSort;
	private SortOrder sortOrder;

	public SortBy(String fieldSort, SortOrder sortOrder) {
		this.fieldSort = fieldSort;
		this.sortOrder = sortOrder;
	}

	public void setFieldSort(String fieldSort) {
		this.fieldSort = fieldSort;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public FieldSortBuilder build() {
		return SortBuilders.fieldSort(fieldSort).order(
				sortOrder == null ? SortOrder.ASC : sortOrder);
	}
}
