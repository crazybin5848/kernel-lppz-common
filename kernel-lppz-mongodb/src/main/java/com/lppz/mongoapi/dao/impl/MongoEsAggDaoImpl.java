package com.lppz.mongoapi.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.google.common.collect.Maps;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.disruptor.BaseEsLogEvent2Sender;
import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.elasticsearch.result.ResultStatsSearchAgg;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.mongoapi.dao.MongoEsAggDao;
import com.lppz.util.disruptor.sender.BaseClusterdDisruptorSender;

@SuppressWarnings("unchecked")
@Component
public class MongoEsAggDaoImpl implements MongoEsAggDao{
	private static final Logger logger = LoggerFactory.getLogger(MongoEsAggDaoImpl.class);
	public static final String keyFormat = "app:::%s";
	public static final String filedFormatKeg = "app:::%s:::keg";
	public static final String kegNoKey = "kegNo";
	public static final String redisSyncKey = "app:::redisSyncKey";
	
	@Value("${mongo.insert.batch.size:1000}")
	private int size;
	@Value("${mongo.insert.batch.timeStep:500}")
	private int timeStep;
	@Value("${mongo.insert.batch.threadNum:10}")
	private int threadNum;
	@Value("${mongo.push.array.max.length:1000}")
	private int mongoArrayMaxLength;
	@Value("${es.agg.row.max.limit:1000000}")
	private int esAggRowMaxLimit;
	
	@Resource
	private Jedis jedis;
	
	private BaseClusterdDisruptorSender<Map<String,Document>> sender;
	
//	private BaseEsLogEvent2Sender esIncSender;
	
	private Map<String,BaseEsLogEvent2Sender> esSenderMap = new HashMap<>();
	private Map<String,DictModel> dicMap = new HashMap<>();
	
	
	@Override
	public Map<String, Object> aggregationInEs(SearchCondition sc) {
		long total=LppzEsComponent.getInstance().count(new String[]{sc.getIdxName()},
				sc.getTypes(),
				sc.getSearchQuery());
		Map<String,Object> rtnMap=Maps.newHashMap();
		Map<String, Map<String, Number>> retMap = new HashMap<String, Map<String, Number>>();
		if (total > esAggRowMaxLimit) {
			rtnMap.put("success", false);
			rtnMap.put("message", "扫描结果超过"+esAggRowMaxLimit+"条，不处理聚合请求");
		}else{
			Map<String, List<ResultBucket>> resultMap = LppzEsComponent.getInstance().agg(sc);

			Set<String> set = resultMap.keySet();
			for (String key : set) {
				List<ResultBucket> rbList=resultMap==null||resultMap.isEmpty()
			      		   ?null:resultMap.get(key);	
				if(rbList==null)
			      	   return null;
			         for(ResultBucket rb:rbList){
			        	 Map<String,Number> resmap = new HashMap<>();
			             String keyy = rb.getBucketKeyList().get(0);
			             Map<String, ResultStatsSearchAgg> map = rb.getMapResultStats();
							for (Entry<String, ResultStatsSearchAgg> result : map.entrySet()) {
								resmap.putAll(getAggValue(result.getValue()));
							}
							retMap.put(keyy, resmap);
			         }
			        
			}

			rtnMap.put("result", retMap);
			rtnMap.put("success", true);
			logger.info("map:{}",resultMap);
		}
		return rtnMap;
	}
	
	
	private Map<String,Number> toNumber(List<ResultBucket> value) {
		Map<String,Number> resmap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(value)) {
			for (ResultBucket bucket : value) {
				Map<String, ResultStatsSearchAgg> map = bucket.getMapResultStats();
				for (Entry<String, ResultStatsSearchAgg> result : map.entrySet()) {
					resmap.putAll(getAggValue(result.getValue()));
				}
			}
			return resmap;
		}
		return resmap;
	}

	private Map<String,Number> getAggValue(ResultStatsSearchAgg value) {
		Map<String,Number> resmap = new HashMap<>();
		if (value.getAvg() != -1) {
			resmap.put("avg", value.getAvg()) ;
		}
		
		if (value.getMax() !=-1) {
			resmap.put("max",value.getMax());
		}
		
		if (value.getMin() != -1) {
			resmap.put("min",value.getMin());
		}
		
		if (value.getCount() != -1) {
			resmap.put("count",value.getCount());
		}
		
		if (value.getSum() != -1) {
			resmap.put("sum",value.getSum());
		}
		return resmap;
	}

}
