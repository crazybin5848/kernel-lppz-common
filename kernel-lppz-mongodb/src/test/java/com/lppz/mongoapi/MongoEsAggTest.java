package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import kafka.utils.threadsafe;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.agg.AggType;
import com.lppz.elasticsearch.agg.SearchAggModel;
import com.lppz.elasticsearch.query.BoolSearchQuery;
import com.lppz.elasticsearch.query.Operator;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.RangeItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.query.fielditem.WildCardKvItem;
import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.elasticsearch.result.ResultStatsSearchAgg;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.elasticsearch.search.SortBy;
import com.lppz.mongoapi.dao.MongoEsAggDao;
import com.lppz.oms.kafka.dto.OrderLogDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/mongodb-lppz-spring.xml"})
public class MongoEsAggTest {
	
	String table = "orders";
	@Resource
	private MongoEsAggDao mongoDao;

	@Test
	public void aggTest(){
//		String idxName = "orderlog-*";
//		String [] types = new String[]{OrderLogDto.class.getName()};
		String idxName = "sns-hotcard-201704";
		String [] types = null;
//		String [] types = new String[]{"com.lppz.mobile.api.pojo.SnsSearchHotCard"};
		
		SearchQuery sq = buildSnsHotcardSq();
		
		List<SearchAggModel> saggmodelList = new ArrayList<SearchAggModel>();
		SearchAggModel aggModel = new SearchAggModel("type_terms", "type", AggType.TERMS);
		//saggmodelList.add(aggModel);
//		saggmodelList.add(new SearchAggModel("weight_sum", "weight", AggType.SUM));
//		saggmodelList.add(new SearchAggModel("weight_avg", "weight", AggType.AVG));
//		saggmodelList.add(new SearchAggModel("weight_stats", "weight", AggType.STATS));
		List<SearchAggModel> subSearchAggModelList=new ArrayList<SearchAggModel>();
		//subSearchAggModelList.add(new SearchAggModel("title_count", "title", AggType.COUNT));
		subSearchAggModelList.add(new SearchAggModel("weight_sum", "weight", AggType.SUM));
		aggModel.setSubSearchAggModelList(subSearchAggModelList);
		saggmodelList.add(aggModel);
		
		SearchCondition sc = new SearchCondition(sq,idxName,types);
//		sc.setOffset(0);
//		sc.setSize(10);
		sc.setSaggmodelList(saggmodelList);
		Map<String, List<ResultBucket>> listMap = LppzEsComponent.getInstance()
				.agg(sc);
		Map<String,Object> result = mongoDao.aggregationInEs(sc);
		if (result != null && (Boolean)result.get("success")) {
			Map<String, Number> resultMap = (Map<String, Number>) result.get("result");
			if (resultMap != null) {
				for (Entry<String, Number> r : resultMap.entrySet()) {
					System.out.println(r.getKey() + " " + r.getValue());
				}
			}
		}
		System.out.println(result);
	}
	
	private SearchQuery buildSnsHotcardSq() {
		String startTime = "1092760000000";
		String endTime ="9492760537912";
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
		
		sq.setSearchQueryList(searchQueryList);
		//范围查询
		if (StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)) {
			SearchQuery csq=new SearchQuery();
			List<FieldItem> fieldItemList=Lists.newArrayList();
			csq.setFieldItemList(fieldItemList);
			
			RangeItem rangeItem = new RangeItem();
			rangeItem.setTermField("crtTime");
			rangeItem.setGtStr(startTime);
			rangeItem.setLtStr(endTime);
			fieldItemList.add(rangeItem);
			
			searchQueryList.add(csq);
			
		}
		return sq;
	}
}
