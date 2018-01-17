package com.lppz.elasticsearch.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.lppz.elasticsearch.result.handleagg.DateHistoricalAGGHandler;
import com.lppz.elasticsearch.result.handleagg.StatsAGGHandler;
import com.lppz.elasticsearch.result.handleagg.TermsAGGHandler;

public class ResultSearchAgg {
	public Map<String, ResultStatsSearchAgg> getRssaMap() {
		return rssaMap;
	}

	public void setRssaMap(Map<String, ResultStatsSearchAgg> rssaMap) {
		this.rssaMap = rssaMap;
	}

	private List<ResultAggBuck> subResultAggBuckList=new ArrayList<ResultAggBuck>();
	private String termKey;
	public String getTermKey() {
		return termKey;
	}

	public void setTermKey(String termKey) {
		this.termKey = termKey;
	}

	public List<ResultAggBuck> getSubResultAggBuckList() {
		return subResultAggBuckList;
	}

	public void setSubResultAggBuckList(List<ResultAggBuck> subResultAggBuckList) {
		this.subResultAggBuckList = subResultAggBuckList;
	}

	private Map<String,ResultStatsSearchAgg> rssaMap;

	public void build(Aggregation agg) {
		if (agg instanceof Terms) {
			TermsAGGHandler.getInstance().handle(agg, this);
		} else if (agg instanceof Histogram) {
			DateHistoricalAGGHandler.getInstance().handle(agg, this);
		} else {
			if(rssaMap==null)
			rssaMap = new HashMap<String,ResultStatsSearchAgg>();
			StatsAGGHandler.getInstance().handle(agg, this);
		}
	}
}