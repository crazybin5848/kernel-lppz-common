package com.lppz.mongoapi.dao;

import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mongoapi.bean.DictModel;

public interface MongoEsAggDao {

	/**
	 * 聚合查询方法
	 * @param sc
	 * @return 聚合结果
	 * result Map<String, List<ResultBucket>>结果集合
	 * sucess 是否成功
	 * message 提示消息
	 */
	public Map<String,Object> aggregationInEs(SearchCondition sc);
	
}
