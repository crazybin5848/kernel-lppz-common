package com.lppz.elasticsearch.result.handleagg;

import java.util.Map;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;

import com.lppz.elasticsearch.result.BucketKv;
import com.lppz.elasticsearch.result.ResultAggBuck;
import com.lppz.elasticsearch.result.ResultSearchAgg;

public class DateHistoricalAGGHandler {
	private static DateHistoricalAGGHandler instance = new DateHistoricalAGGHandler();

	private DateHistoricalAGGHandler() {
	}

	public static DateHistoricalAGGHandler getInstance() {
		return instance;
	}

	public void handle(Aggregation agg, ResultSearchAgg raggg) {
		Histogram agg1 = (Histogram) agg;
		for (Bucket b : agg1.getBuckets()) {
			BucketKv bkv=new BucketKv(b.getKeyAsString(), b.getDocCount());
			Map<String, Aggregation> map = b.getAggregations().asMap();
			ResultAggBuck bab=new ResultAggBuck(null,bkv);
			raggg.getSubResultAggBuckList().add(bab);
			if (map != null && !map.isEmpty()) {
				for (String key : map.keySet()) {
					ResultSearchAgg rsagg = new ResultSearchAgg();
					rsagg.build(map.get(key));
					bab.setSubSearchAgg(rsagg);
					raggg.setTermKey(key);
					break;
				}
			}
		}
	}
}
