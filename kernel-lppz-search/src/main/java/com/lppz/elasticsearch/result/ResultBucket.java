package com.lppz.elasticsearch.result;

import java.util.List;
import java.util.Map;

public class ResultBucket {
	private Map<String,ResultStatsSearchAgg> mapResultStats;
	private List<String> bucketKeyList;
	public ResultBucket(List<String> bucketKeyList,Map<String,ResultStatsSearchAgg> mapResultStats){
		this.bucketKeyList=bucketKeyList;
		this.mapResultStats=mapResultStats;
	}
	public Map<String, ResultStatsSearchAgg> getMapResultStats() {
		return mapResultStats;
	}
	public void setMapResultStats(Map<String, ResultStatsSearchAgg> mapResultStats) {
		this.mapResultStats = mapResultStats;
	}
	public List<String> getBucketKeyList() {
		return bucketKeyList;
	}
	public void setBucketKeyList(List<String> bucketKeyList) {
		this.bucketKeyList = bucketKeyList;
	}
}
