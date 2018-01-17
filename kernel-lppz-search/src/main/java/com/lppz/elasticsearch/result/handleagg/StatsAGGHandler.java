package com.lppz.elasticsearch.result.handleagg;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;

import com.lppz.elasticsearch.result.ResultSearchAgg;
import com.lppz.elasticsearch.result.ResultStatsSearchAgg;

public class StatsAGGHandler {
	private static StatsAGGHandler instance = new StatsAGGHandler();

	private StatsAGGHandler() {
	}

	public static StatsAGGHandler getInstance() {
		return instance;
	}

	public void handle(Aggregation agg, ResultSearchAgg raggg) {
		if(agg instanceof Min){
			Min min=(Min)agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setMin(min.getValue());
			raggg.getRssaMap().put(min.getName(),rsa);
		}
		if(agg instanceof Max){
			Max max=(Max)agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setMax(max.getValue());
			raggg.getRssaMap().put(max.getName(),rsa);
		}
		if(agg instanceof Sum){
			Sum sum=(Sum)agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setSum(sum.getValue());
			raggg.getRssaMap().put(sum.getName(),rsa);
		}
		if(agg instanceof Avg){
			Avg avg=(Avg)agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setAvg(avg.getValue());
			raggg.getRssaMap().put(avg.getName(),rsa);
		}
		if(agg instanceof Stats){
			Stats stats=(Stats)agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setAvg(stats.getAvg());
			rsa.setCount(stats.getCount());
			rsa.setMax(stats.getMax());
			rsa.setMin(stats.getMin());
			rsa.setSum(stats.getSum());
			raggg.getRssaMap().put(stats.getName(),rsa);
		}
		if (agg instanceof ValueCount) {
			ValueCount count = (ValueCount) agg;
			ResultStatsSearchAgg rsa=new ResultStatsSearchAgg();
			rsa.setCount(count.getValue());
			raggg.getRssaMap().put(count.getName(),rsa);
		}
	}
}
