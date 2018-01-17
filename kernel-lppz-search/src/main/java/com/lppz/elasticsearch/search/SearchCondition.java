package com.lppz.elasticsearch.search;

import java.io.Serializable;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.support.AbstractClient;

import com.lppz.elasticsearch.agg.SearchAggModel;
import com.lppz.elasticsearch.query.SearchQuery;

public class SearchCondition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8840237833590890662L;
	private List<SortBy> sortList;
	private SearchQuery searchQuery;
	public SearchQuery getSearchQuery() {
		return searchQuery;
	}

	public SearchQuery getPostFilter() {
		return postFilter;
	}

	private SearchQuery postFilter;
	private List<SearchAggModel> saggmodelList;
	private int offset;
	private int size=1000;
	private String idxName;
	private String[] types;
	private SearchType searchType;
	public SearchCondition(SearchQuery searchQuery, String idxName,String[] types) {
		this.searchQuery = searchQuery;
		this.idxName = idxName;
		this.types = types;
	}

	public SearchRequestBuilder build(AbstractClient client,SearchType searchType,boolean isAgg) {
		SearchType st = buildSearchType(searchType);
		SearchRequestBuilder reb = client.prepareSearch(idxName)
				.setSearchType(st)
				.setQuery(searchQuery.build());
		if(types!=null&&types.length>0)
			reb.setTypes(types);
		
		if (sortList != null && !sortList.isEmpty()) {
			for (SortBy sort : sortList) {
				reb.addSort(sort.build());
			}
		}
		if(isAgg){
		if (saggmodelList != null && !saggmodelList.isEmpty()) {
			for (SearchAggModel sam : saggmodelList) {
				reb.addAggregation(sam.build());
			}
		}
		}
		if (postFilter != null) {
			reb.setPostFilter(postFilter.build());
		}
		reb.setFrom(offset);
		reb.setSize(size>10000?10000:size);
		return reb.setExplain(true);
	}

	private SearchType buildSearchType(SearchType searchType) {
		SearchType st=null;
		if(searchType!=null){
			st=searchType;
		}
		else{
			st=this.searchType;
		}	
		if(st==null)
			st=SearchType.DFS_QUERY_THEN_FETCH;
		return st;
	}

	public void setSortList(List<SortBy> sortList) {
		this.sortList = sortList;
	}

	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
	}

	public void setPostFilter(SearchQuery postFilter) {
		this.postFilter = postFilter;
	}

	public void setSaggmodelList(List<SearchAggModel> saggmodelList) {
		this.saggmodelList = saggmodelList;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getIdxName() {
		return idxName;
	}

	public void setIdxName(String idxName) {
		this.idxName = idxName;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public List<SortBy> getSortList() {
		return sortList;
	}

	public List<SearchAggModel> getSaggmodelList() {
		return saggmodelList;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

}
