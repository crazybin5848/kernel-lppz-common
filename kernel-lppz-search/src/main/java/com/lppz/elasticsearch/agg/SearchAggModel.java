package com.lppz.elasticsearch.agg;

import java.util.List;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public class SearchAggModel {
	private AggType type;
	private String name;
	private String field;
	private List<SearchAggModel> subSearchAggModelList;
	private SearchAggModel subSearchAggModel;
	public SearchAggModel getSubSearchAggModel() {
		return subSearchAggModel;
	}
	public void setSubSearchAggModel(SearchAggModel subSearchAggModel) {
		this.subSearchAggModel = subSearchAggModel;
	}
	public SearchAggModel(){}
	public SearchAggModel(String name,String field,AggType type){
		this.name=name;
		this.field=field;
		this.type=type;
	}
	@SuppressWarnings({ "rawtypes"})
	public AbstractAggregationBuilder build() {
		if (type == null)
			return null;
		type.setField(field);
		type.setName(name);
		AbstractAggregationBuilder aab = type.build();
		if (aab instanceof AggregationBuilder && subSearchAggModelList != null) {
			for(SearchAggModel searchAggModel:subSearchAggModelList){
				((AggregationBuilder) aab).subAggregation(searchAggModel.build());
			}
		}
		else if(aab instanceof AggregationBuilder && subSearchAggModel != null){
			((AggregationBuilder) aab).subAggregation(subSearchAggModel.build());
		}
		return aab;
	}

	public AggType getType() {
		return type;
	}

	public void setType(AggType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	public List<SearchAggModel> getSubSearchAggModelList() {
		return subSearchAggModelList;
	}
	public void setSubSearchAggModelList(List<SearchAggModel> subSearchAggModelList) {
		this.subSearchAggModelList = subSearchAggModelList;
	}

}
