package com.lppz.elasticsearch.agg;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

public enum AggType {
	TERMS,AVG,SUM,COUNT,MAX,MIN,DATE,STATS,CARDINATE;
	private String name;
	private String field;
	public void setName(String name) {
		this.name = name;
	}
	public void setField(String field) {
		this.field = field;
	}
	public AbstractAggregationBuilder build(){
		AbstractAggregationBuilder ab=null;
		if(AggType.TERMS.equals(this)){
			ab=AggregationBuilders.terms(name).field(field);
		}
		if(AggType.CARDINATE.equals(this)){
			ab=AggregationBuilders.cardinality(name).field(field);
		}
		if(AggType.DATE.equals(this)){
			ab=AggregationBuilders.dateHistogram(name).field(field);
		}
		if(AggType.STATS.equals(this)){
			ab=AggregationBuilders.stats(name).field(field);
		}
		if(AggType.AVG.equals(this)){
			ab=AggregationBuilders.avg(name).field(field);
		}
		if(AggType.SUM.equals(this)){
			ab=AggregationBuilders.sum(name).field(field);
		}
		if(AggType.COUNT.equals(this)){
			ab=AggregationBuilders.count(name).field(field);
		}
		if(AggType.MAX.equals(this)){
			ab=AggregationBuilders.max(name).field(field);
		}
		if(AggType.MIN.equals(this)){
			ab=AggregationBuilders.min(name).field(field);
		}
		return ab;
	}
}
