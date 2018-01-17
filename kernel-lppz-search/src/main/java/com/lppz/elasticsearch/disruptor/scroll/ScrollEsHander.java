package com.lppz.elasticsearch.disruptor.scroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.query.BoolSearchQuery;
import com.lppz.elasticsearch.query.Operator;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.TermArrayKvItem;
import com.lppz.util.disruptor.BaseHandler;

public class ScrollEsHander implements BaseHandler<EsScrollModel>{
	private Logger logger = Logger.getLogger(ScrollEsHander.class);
	private PrepareBulk bulk;
	public ScrollEsHander(PrepareBulk bulk) {
		this.bulk=bulk;
	}
	public ScrollEsHander() {}
	@Override
	public void handle(List<EsScrollModel> list) {
		Set<String> index=new HashSet<String>();
		Set<String> type=new HashSet<String>();
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		for(EsScrollModel model:list){
			index.add(model.getIndex());
			type.add(model.getType());
			List<String> ll=map.get(model.getTermFiled());
			if(ll==null)
				ll=new ArrayList<String>();
			ll.add(model.getTermValue());
			map.put(model.getTermFiled(), ll);
		}
		if(CollectionUtils.isEmpty(index)||CollectionUtils.isEmpty(type)||map.isEmpty())
			return;
		SearchQuery sq=new SearchQuery();
		if(map!=null&&map.size()==1){
			sq.setFieldItemList(new ArrayList<FieldItem>());
			String field=map.keySet().iterator().next();
			List<String> valueList=map.get(field);
			FieldItem fi=new TermArrayKvItem(field,valueList.toArray(new String[0]));
			sq.getFieldItemList().add(fi);
		}
		else{
			sq.setSearchQueryList(new ArrayList<SearchQuery>());
			BoolSearchQuery bsq=new BoolSearchQuery(Operator.OR);
			sq.getSearchQueryList().add(bsq);
			for(String field:map.keySet()){
				List<String> valueList=map.get(field);
				FieldItem fi=new TermArrayKvItem(field,valueList.toArray(new String[0]));
				bsq.addFileItem(fi);
			}
		}
		LppzEsComponent.getInstance().scrollSearch(index.toArray(new String[0]), type.toArray(new String[0]), sq, 10000, 60000, bulk);
	}
}