package com.lppz.elasticsearch.result.handleagg;

import java.util.Map;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import com.lppz.elasticsearch.result.BucketKv;
import com.lppz.elasticsearch.result.ResultAggBuck;
import com.lppz.elasticsearch.result.ResultSearchAgg;

public class TermsAGGHandler {
	private static TermsAGGHandler instance = new TermsAGGHandler();

	private TermsAGGHandler() {
	}

	public static TermsAGGHandler getInstance() {
		return instance;
	}

	public void handle(Aggregation agg, ResultSearchAgg raggg) {
		Terms agg1 = (Terms) agg;
		for (Bucket b : agg1.getBuckets()) {
			BucketKv bkv=new BucketKv(b.getKeyAsString(), b.getDocCount());
			ResultAggBuck bab=new ResultAggBuck(null,bkv);
			raggg.getSubResultAggBuckList().add(bab);
			Map<String, Aggregation> map = b.getAggregations().asMap();
			if (map != null && !map.isEmpty()) {
				ResultSearchAgg rsagg = new ResultSearchAgg();
				for (String key : map.keySet()) {
					rsagg.build(map.get(key));
				}
				bab.setSubSearchAgg(rsagg);
			}
		}
	}
}
