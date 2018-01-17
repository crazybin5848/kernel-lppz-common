package com.lppz.elasticsearch.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResultAggBuck {
	private ResultSearchAgg subSearchAgg;
	private BucketKv bucketKv;
	public ResultAggBuck(ResultSearchAgg subSearchAgg,BucketKv bucketKv){
		this.subSearchAgg=subSearchAgg;
		this.bucketKv=bucketKv;
	}
	public ResultSearchAgg getSubSearchAgg() {
		return subSearchAgg;
	}
	public void setSubSearchAgg(ResultSearchAgg subSearchAgg) {
		this.subSearchAgg = subSearchAgg;
	}
	public BucketKv getBucketKv() {
		return bucketKv;
	}
	public void setBucketKv(BucketKv bucketKv) {
		this.bucketKv = bucketKv;
	}
	
	public ResultBucket buildResult(){
		ResultBucket rb=null;
		if(null==subSearchAgg){
			ResultStatsSearchAgg rssagg=new ResultStatsSearchAgg();
			rssagg.setCount(bucketKv.getBucketDocCount());
			rb=new ResultBucket(Arrays.asList(new String[]{bucketKv.getBucketKey()}),new HashMap<String,ResultStatsSearchAgg>(1));
			rb.getMapResultStats().put("bucketCount", rssagg);
			return rb;
		}
		List<String> buckString=new ArrayList<String>();
		buckString.add(bucketKv.getBucketKey());
		rb=new ResultBucket(buckString,null);
		this.build(rb);
		return rb;
	}
	
	private void build(ResultBucket rb){
		if(subSearchAgg==null)
			return;
		if(subSearchAgg.getRssaMap()!=null){
			rb.setMapResultStats(subSearchAgg.getRssaMap());
			return;
		}
		if(subSearchAgg.getSubResultAggBuckList()!=null){
			rb.getBucketKeyList().add(subSearchAgg.getSubResultAggBuckList().get(0).getBucketKv().getBucketKey());
			subSearchAgg.getSubResultAggBuckList().get(0).build(rb);
		}
	}
	
}