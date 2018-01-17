package com.lppz.elasticsearch.result;

import java.util.ArrayList;
import java.util.List;
public class SearchAllResult {
private List<SearchResult> resultSearchList=new ArrayList<SearchResult>();
public void addResultSearchList(SearchResult resultSearch) {
	resultSearchList.add(resultSearch);
}
public List<SearchResult> getResultSearchList() {
	return resultSearchList;
}
public void setResultSearchList(List<SearchResult> resultSearchList) {
	this.resultSearchList = resultSearchList;
}
}
