package com.lppz.dubbo.monitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lppz.dubbo.monitor.domain.DubboInvoke;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.agg.AggType;
import com.lppz.elasticsearch.agg.SearchAggModel;
import com.lppz.elasticsearch.disruptor.ContantIndexName;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.RangeItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.elasticsearch.search.SearchCondition;

public class EsStasticUtil {
	public static Map<String, List<ResultBucket>> countDubboInvoke(DubboInvoke dubboInvoke){
		SearchCondition sc = getSearchCondition(dubboInvoke);
		List<SearchAggModel> saggmodelList = new ArrayList<SearchAggModel>();
		SearchAggModel sagm = new SearchAggModel("invokeTime", "invokeTime",
				AggType.TERMS);
		List<SearchAggModel> subSearchAggModelList=new ArrayList<SearchAggModel>();
		subSearchAggModelList.add(new SearchAggModel("sumSucc", "success",
				AggType.SUM));
		subSearchAggModelList.add(new SearchAggModel("sumElapsed", "elapsed",
				AggType.SUM));
		sagm.setSubSearchAggModelList(subSearchAggModelList);
		saggmodelList.add(sagm);
		sc.setSaggmodelList(saggmodelList);
		return LppzEsComponent.getInstance()
				.agg(sc);
	}
	
	public static Map<String, List<ResultBucket>> getMethodsByService(DubboInvoke dubboInvoke){
		SearchCondition sc = getSearchCondition(dubboInvoke);
		List<SearchAggModel> saggmodelList = new ArrayList<SearchAggModel>();
		SearchAggModel sagm = new SearchAggModel("method", "method",
				AggType.TERMS);
		saggmodelList.add(sagm);
		sc.setSaggmodelList(saggmodelList);
		return LppzEsComponent.getInstance()
				.agg(sc);
	}
	
	public static Map<String, List<ResultBucket>> countDubboInvokeInfo(DubboInvoke dubboInvoke){
		SearchCondition sc = getSearchCondition(dubboInvoke);
		List<SearchAggModel> saggmodelList = new ArrayList<SearchAggModel>();
		SearchAggModel sagm = new SearchAggModel("succ", "_type",
				AggType.TERMS);
		List<SearchAggModel> list=new ArrayList<SearchAggModel>();
		list.add(new SearchAggModel("sumSucc", "success",
				AggType.SUM));
		list.add(new SearchAggModel("sumFail", "failure",
				AggType.SUM));
		list.add(new SearchAggModel("sumElapsed", "elapsed",
				AggType.SUM));
		list.add(new SearchAggModel("maxMaxElapsed", "maxElapsed",
				AggType.MAX));
		list.add(new SearchAggModel("maxMaxConcurrent", "maxConcurrent",
				AggType.MAX));
		sagm.setSubSearchAggModelList(list);
		saggmodelList.add(sagm);
		sc.setSaggmodelList(saggmodelList);
		Map<String, List<ResultBucket>> listMap = LppzEsComponent.getInstance()
				.agg(sc);
		return listMap;
	}
	
	
	public static Map<String, List<ResultBucket>> countDubboInvokeSFTopTen(
			DubboInvoke dubboInvoke) {
		SearchCondition sc = getSearchCondition(dubboInvoke);
		List<SearchAggModel> saggmodelList = new ArrayList<SearchAggModel>();
		SearchAggModel sagm = new SearchAggModel("succ", "service",
				AggType.TERMS);
		SearchAggModel subsaModel = new SearchAggModel("succ", "method",
				AggType.TERMS);
		subsaModel.setSubSearchAggModel(new SearchAggModel("sum", "success",
				AggType.SUM));
		sagm.setSubSearchAggModel(subsaModel);
		saggmodelList.add(sagm);
		SearchAggModel sagm1 = new SearchAggModel("fail", "service",
				AggType.TERMS);
		SearchAggModel subsaModel1 = new SearchAggModel("fail", "method",
				AggType.TERMS);
		subsaModel1.setSubSearchAggModel(new SearchAggModel("sum1", "failure",
				AggType.SUM));
		sagm1.setSubSearchAggModel(subsaModel1);
		saggmodelList.add(sagm1);
		sc.setSaggmodelList(saggmodelList);
		Map<String, List<ResultBucket>> list = LppzEsComponent.getInstance()
				.agg(sc);
		return list;
	}

	private static SearchCondition getSearchCondition(DubboInvoke dubboInvoke) {
		SearchQuery sq = new SearchQuery();
		List<FieldItem> fieldItemList = new ArrayList<FieldItem>();
		sq.setFieldItemList(fieldItemList);
		if (org.apache.commons.lang.StringUtils.isNotBlank(dubboInvoke
				.getConsumer()))
			fieldItemList.add(new TermKvItem("consumer", dubboInvoke
					.getConsumer()));
		if (org.apache.commons.lang.StringUtils.isNotBlank(dubboInvoke
				.getProvider()))
			fieldItemList.add(new TermKvItem("provider", dubboInvoke
					.getProvider()));
		if (org.apache.commons.lang.StringUtils.isNotBlank(dubboInvoke
				.getService()))
			fieldItemList.add(new TermKvItem("service", dubboInvoke
					.getService()));
		if (org.apache.commons.lang.StringUtils.isNotBlank(dubboInvoke
				.getMethod()))
			fieldItemList
					.add(new TermKvItem("method", dubboInvoke.getMethod()));
		if (org.apache.commons.lang.StringUtils.isNotBlank(dubboInvoke
				.getType()))
			fieldItemList.add(new TermKvItem("type", dubboInvoke.getType()));
		if (null != dubboInvoke.getInvokeDate()) {
			RangeItem f = new RangeItem();
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(dubboInvoke.getInvokeDate());
			f.setGeStr(time);
			f.setLeStr(time);
			f.setTermField("invokeDateTime");
			f.setFormat("yyyy-MM-dd HH:mm:ss");
			fieldItemList.add(f);
		}
		if (null != dubboInvoke.getInvokeDateFrom()) {
			RangeItem f = new RangeItem();
			String startCreatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(dubboInvoke.getInvokeDateFrom());
			f.setGeStr(startCreatetime);
			f.setTermField("invokeDateTime");
			f.setFormat("yyyy-MM-dd HH:mm:ss");
			fieldItemList.add(f);
		}
		if (null != dubboInvoke.getInvokeDateTo()) {
			RangeItem f = new RangeItem();
			String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(dubboInvoke.getInvokeDateTo());
			f.setLeStr(endTime);
			f.setTermField("invokeDateTime");
			f.setFormat("yyyy-MM-dd HH:mm:ss");
			fieldItemList.add(f);
		}
		SearchCondition sc = new SearchCondition(sq, ContantIndexName.INDEX + "*",
				new String[] { DubboInvoke.class.getName() });
		return sc;
	}
}