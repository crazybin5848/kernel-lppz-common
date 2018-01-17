package com.lppz.mongoapi;

import java.util.List;

import org.junit.Test;

import scala.inline;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.MatchItem;
import com.lppz.elasticsearch.query.fielditem.MatchPhraseItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.result.SearchAllResult;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mobile.model.es.ESSnsActivity;
import com.lppz.mobile.model.es.ESSnsStatus;
import com.lppz.mobile.model.es.ESSnsTopic;
import com.lppz.mobile.model.es.ESSnsUser;
import com.lppz.mongoapi.util.EsIndexUtils;

public class EsSearchTest extends SpringBaseTest{

	String indexName = "sns-status-20170807";
	String[] types = new String[]{"com.lppz.mobile.model.es.ESSnsStatus"};
//	String content = "哦色";
//	String content = "写";
//	String content = "我日";
	String content = "玩";
	
	@Test
	public void matchPhraseTest(){
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
		
		sq.setSearchQueryList(searchQueryList);
			
		SearchQuery csq=new SearchQuery();
		List<FieldItem> fieldItemList=Lists.newArrayList();
		csq.setFieldItemList(fieldItemList);
		
		fieldItemList.add(new MatchPhraseItem("content", content));
		
		searchQueryList.add(csq);
		
		SearchCondition sc = new SearchCondition(sq,indexName,types);
		SearchAllResult result = LppzEsComponent.getInstance().search(sc);
		if(result.getResultSearchList() != null){
			System.out.println("matchPhraseTest : "+result.getResultSearchList().size());
			for(SearchResult r : result.getResultSearchList()){
//				System.out.println("matchPhraseTest : "+r.getIndex() + " " + ((ESSnsStatus)r.getSource()).getContent());
			}
		}
	}
	@Test
	public void matchTest(){
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
		
		sq.setSearchQueryList(searchQueryList);
		
		SearchQuery csq=new SearchQuery();
		List<FieldItem> fieldItemList=Lists.newArrayList();
		csq.setFieldItemList(fieldItemList);
		
		fieldItemList.add(new MatchItem("content", content));
		
		searchQueryList.add(csq);
		
		SearchCondition sc = new SearchCondition(sq,indexName,types);
		SearchAllResult result = LppzEsComponent.getInstance().search(sc);
		if(result.getResultSearchList() != null){
			System.out.println("matchTest : "+result.getResultSearchList().size());
			for(SearchResult r : result.getResultSearchList()){
//				System.out.println("matchTest : "+r.getIndex() + " " + ((ESSnsStatus)r.getSource()).getContent());
			}
		}
	}
	@Test
	public void termTest(){
		indexName = "sns-user-*";
		indexName = "sns-status-*";
		types = new String[]{/*ESSnsUser.class.getName()*/ESSnsStatus.class.getName()};
		SearchQuery sq=new SearchQuery();
		List<SearchQuery> searchQueryList=Lists.newArrayList();
		
		sq.setSearchQueryList(searchQueryList);
		
		SearchQuery csq=new SearchQuery();
		List<FieldItem> fieldItemList=Lists.newArrayList();
		csq.setFieldItemList(fieldItemList);
		
//		fieldItemList.add(new TermKvItem("content", content));
//		fieldItemList.add(new TermKvItem("userId", "149847070"));
//		fieldItemList.add(new TermKvItem("followingCount", 2));
		fieldItemList.add(new TermKvItem("statusId", "a0ea0609-e9bf-445b-9a64-fa0ccabcfe5c"));
		
		searchQueryList.add(csq);
		
		SearchCondition sc = new SearchCondition(sq,indexName,types);
		SearchAllResult result = LppzEsComponent.getInstance().search(sc);
		if(result.getResultSearchList() != null){
			System.out.println("termTest : "+result.getResultSearchList().size());
			for(SearchResult r : result.getResultSearchList()){
				System.out.println("termTest : "+r.getIndex() + " " + JSON.toJSONString(r.getSource()));
			}
		}
	}
	
	@Test
	public void getTest(){
		String[] indexs = new String[]{"sns-status-5"
				,"sns-status-1"
				,"sns-status-1"
				,"sns-status-2"
				,"sns-status-3"
				,"sns-status-3"
				,"sns-status-4"
				,"sns-status-5"};
		String[] typs = new String[]{ESSnsStatus.class.getName()};
		String[] ids = new String []{"1ldkxhn5e02gw"
				, "31e65478-b2a7-4324-955c-7cb14cce27a2"
				, "afc6d18e-c812-4c16-a977-c2c8f2390787"
				, "fd807b06-b2ee-4188-84b0-0fe8777ecce1"
				, "8d877ac0-b3e5-4b15-83cd-bc328c6303a3"
				, "df552e5a-f9d6-4507-848c-29d4e17f51b9"
				, "4c5ebec0-d426-43ce-a7b8-7326e10981e4"
				, "84b66040-1601-4c6d-b35f-f69ae9cc1523"};
		for (int i = 0; i < ids.length; i++) {
//			LppzEsComponent.getInstance().delete(indexs[i], typs[i], ids[i]);
			SearchResult result = LppzEsComponent.getInstance().searchById(indexs[i], typs[0], EsIndexUtils.buildId(ids[i]));
			if (result != null) {
				System.out.println("termTest : "+JSON.toJSONString(result.getSource()));
			}
		}
	}
}
