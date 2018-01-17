package com.lppz.elasticsearch.query;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.lppz.elasticsearch.query.fielditem.FieldItem;

public enum Operator {
	AND, OR, NO;
	private List<FieldItem> kvList;

	public void setKvList(List<FieldItem> kvList) {
		this.kvList = kvList;
	}

	public BoolQueryBuilder build() {
		BoolQueryBuilder bb = QueryBuilders.boolQuery();
		if (Operator.AND.equals(this)){
			for(FieldItem fi:kvList){
				bb.must(fi.build());
			}
			return bb;
		}
		else if (Operator.OR.equals(this)){
			for(FieldItem fi:kvList){
				bb.should(fi.build());
			}
			return bb;
		}
		else{
			for(FieldItem fi:kvList){
				bb.mustNot(fi.build());
			}
			return bb;
		}
	}
}
