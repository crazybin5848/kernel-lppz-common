package com.lppz.elasticsearch.result;

public class BucketKv {
	private String bucketKey;
	private Long bucketDocCount;
	public BucketKv(String bucketKey,Long bucketDocCount){
		this.bucketKey=bucketKey;
		this.bucketDocCount=bucketDocCount;
	}
	public String getBucketKey() {
		return bucketKey;
	}
	public void setBucketKey(String bucketKey) {
		this.bucketKey = bucketKey;
	}
	public Long getBucketDocCount() {
		return bucketDocCount;
	}
	public void setBucketDocCount(Long bucketDocCount) {
		this.bucketDocCount = bucketDocCount;
	}
}
