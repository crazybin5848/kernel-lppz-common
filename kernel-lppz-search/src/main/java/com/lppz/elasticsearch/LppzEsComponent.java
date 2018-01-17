package com.lppz.elasticsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import com.alibaba.fastjson.JSON;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.result.ResultAggBuck;
import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.elasticsearch.result.ResultSearchAgg;
import com.lppz.elasticsearch.result.ResultStatsSearchAgg;
import com.lppz.elasticsearch.result.SearchAllResult;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.elasticsearch.search.SortBy;
import com.lppz.util.EsJsonSourceModel;

@SuppressWarnings("deprecation")
public class LppzEsComponent {
	private AbstractClient client;
	private static final Logger logger = Logger.getLogger(LppzEsComponent.class);

	public void setClient(AbstractClient client) {
		this.client = client;
	}

	private static LppzEsComponent instance = new LppzEsComponent();

	private LppzEsComponent() {
	}

	public static LppzEsComponent getInstance() {
		return instance;
	}

	public AbstractClient getClient() {
		return client;
	}

	public void scrollSearch(String[] index, String[] types,
			SearchQuery searchQuery,int scrollSize, int timeMillis,
			PrepareBulk prepareBulk) {
		if (client == null)
			return;
		SearchRequestBuilder reb = client.prepareSearch(index)
				.setSearchType(SearchType.SCAN).setSize(scrollSize)
				.setScroll(TimeValue.timeValueMinutes(1));
		try {
			handleScrollSearch(types, searchQuery,0,scrollSize, prepareBulk, reb,false);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	private void handleScrollSearch(String[] types, SearchQuery searchQuery,
			int from, int scrollSize, PrepareBulk prepareBulk, SearchRequestBuilder reb,boolean isPager)
			throws Exception {
		if (types != null && types.length > 0)
			reb.setTypes(types);
		if (searchQuery != null)
			reb.setQuery(searchQuery.build());
		SearchResponse scrollResp = reb.execute().actionGet();
		int att=0;
		int countStart=0;
		int countStop=0;
		while (true) {
			SearchHit[] hits = scrollResp.getHits().getHits();
			if(isPager){
				if (hits.length > 0){
					countStop+=hits.length;
					if(from>=countStart&&from<countStop){
						List<SearchHit> srList=Arrays.asList(hits);
						//hits=null;
						List<SearchHit> srl=srList.subList(from-countStart, from+scrollSize>=srList.size()?srList.size():scrollSize);
						List<SearchResult> ll=new ArrayList<SearchResult>(srl.size());
						for(SearchHit searchHit:srl){
						SearchResult sr = buildResult(searchHit);
						ll.add(sr);
						}
						prepareBulk.bulk(ll);
						if(ll.size()==scrollSize)
							return;
						att=from+scrollSize-srList.size();
						buildSearchRespAtt(prepareBulk,
									scrollResp, att);
					}
					scrollResp = continueScroll(scrollResp);
					countStart+=hits.length;
					hits=null;
				} 
			}
			else{
				if (hits.length > 0) {
					List<SearchHit> srList=Arrays.asList(hits);
					hits=null;
					for (int i=0;i<srList.size();) {
						List<SearchHit> srl=srList.subList(i, i+scrollSize>=srList.size()?srList.size():i+scrollSize);
						i+=scrollSize;
						List<SearchResult> ll=new ArrayList<SearchResult>(srl.size());
						for(SearchHit searchHit:srl){
						SearchResult sr = buildResult(searchHit);
						ll.add(sr);
						}
						prepareBulk.bulk(ll);
					}
				}
				scrollResp = continueScroll(scrollResp);
			}
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
	}

	private void buildSearchRespAtt(PrepareBulk prepareBulk,
			SearchResponse scrollResp, int att)
			throws Exception {
		if(att<=0)
			return;
		scrollResp = continueScroll(scrollResp);
		if (scrollResp.getHits().getHits().length == 0) {
			return;
		}
		SearchHit[] hits = scrollResp.getHits().getHits();
		if(hits.length<=0)
			return;
		List<SearchHit> srList=Arrays.asList(hits);
		hits=null;
		List<SearchHit> srl=srList.subList(0,att>srList.size()?srList.size():att);
		List<SearchResult> lll=new ArrayList<SearchResult>(srl.size());
		for(SearchHit searchHit:srl){
		SearchResult sr = buildResult(searchHit);
		lll.add(sr);
		}
		prepareBulk.bulk(lll);
		int at=att-srList.size();
		buildSearchRespAtt(prepareBulk, scrollResp, at);
	}

	private SearchResponse continueScroll(SearchResponse scrollResp)
			throws Exception {
		try {
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
					.setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
		} catch (Exception e) {
			throw e;
		}
		return scrollResp;
	}
	
	public void scrollSearchPager(String[] index, String[] types,
			SearchQuery searchQuery,int from,int scrollSize,List<SortBy> sortList, int timeMillis,
			PrepareBulk prepareBulk) {
		if (client == null)
			return;
		SearchRequestBuilder reb = client.prepareSearch(index)
				.setSearchType(SearchType.SCAN).setFrom(from)
				.setSize(scrollSize)
				.setScroll(TimeValue.timeValueMinutes(1));
		if(CollectionUtils.isNotEmpty(sortList)){
			for (SortBy sort : sortList) {
				reb.addSort(sort.build());
			}
			reb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		}
		try {
			handleScrollSearch(types, searchQuery,from,scrollSize, prepareBulk, reb,true);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public long count(String[] index, String[] types, SearchQuery searchQuery) {
		if (client == null)
			return 0;
		CountRequestBuilder crb = client.prepareCount(index);
		if (types != null && types.length > 0)
			crb.setTypes(types);
		if (searchQuery != null)
			crb.setQuery(searchQuery.build());
		return crb.execute().actionGet().getCount();
	}

	public Map<String, List<ResultBucket>> agg(SearchCondition sc) {
		if (client == null)
			return null;
		Map<String, List<ResultBucket>> mapList = new HashMap<String, List<ResultBucket>>();
		long tStart = System.currentTimeMillis();
		SearchResponse response = sc.build(client, SearchType.COUNT, true)
				.execute().actionGet();
		Aggregations aggs = response.getAggregations();
		if (aggs != null) {
			Map<String, Aggregation> map = aggs.asMap();
			for (String keyAgg : map.keySet()) {
				List<ResultBucket> l = new ArrayList<ResultBucket>();
				Aggregation agg = map.get(keyAgg);
				ResultSearchAgg ragg = new ResultSearchAgg();
				ragg.build(agg);
				ragg.setTermKey(keyAgg);
				if (ragg.getRssaMap() != null && !ragg.getRssaMap().isEmpty()) {
					mapList.put(keyAgg, Arrays.asList(new ResultBucket(null, ragg.getRssaMap())));
				}
				if (CollectionUtils.isNotEmpty(ragg.getSubResultAggBuckList())) {
					for (ResultAggBuck rab : ragg.getSubResultAggBuckList())
						l.add(rab.buildResult());
					mapList.put(ragg.getTermKey(), l);
				}
			}
		}
		long tEnd = System.currentTimeMillis();
		logger.debug(tEnd - tStart + "millions");
		return mapList;
	}

	public SearchAllResult search(SearchCondition sc) {
		if (client == null)
			return null;
		long tStart = System.currentTimeMillis();
		SearchResponse response = sc.build(client, null, false).execute()
				.actionGet();
		SearchAllResult sra = new SearchAllResult();
		for (Iterator<SearchHit> it = response.getHits().iterator(); it
				.hasNext();) {
			SearchHit hit = it.next();
			SearchResult sr = buildResult(hit);
			sra.addResultSearchList(sr);
		}
		long tEnd = System.currentTimeMillis();
		logger.debug(tEnd - tStart + "millions");
		return sra;
	}

	public SearchResult searchById(String index, String type, String id) {
		if (client == null)
			return null;
		GetResponse gr = client.prepareGet(index, type, id).execute()
				.actionGet();
		SearchResult sr = new SearchResult();
		Map<String, Object> map = gr.getSource();
		if (map == null) {
			return null;	
		}
		map.put("__id", gr.getId());
		map.put("__type", gr.getType());
		map.put("__index", gr.getIndex());
		sr.build(map);
		return sr;
	}
	
	public List<SearchResult> searchMultiById(String index, String type, List<String> idArray,boolean needContent) {
		if (client == null)
			return null;
		MultiGetResponse gr = client.prepareMultiGet().add(index, type, idArray).execute().actionGet();
		List<SearchResult> resultList=new ArrayList<SearchResult>();
		for(Iterator<MultiGetItemResponse> it=gr.iterator();it.hasNext();){
			MultiGetItemResponse resp=it.next();
			SearchResult sr = new SearchResult();
			if(needContent){
				if (resp.getResponse() != null) {
					Map<String, Object> map = resp.getResponse().getSource();
					if (map != null) {
						map.put("__id", resp.getId());
						map.put("__type", resp.getType());
						map.put("__index", resp.getIndex());
						sr.build(map);
						resultList.add(sr);
					}
				}else{
					logger.error(resp.getFailure().getFailure());
				}
			}
			else{
				sr.setId(resp.getId());
				resultList.add(sr);
			}
		}
		return resultList;
	}

	private SearchResult buildResult(SearchHit hit) {
		Map<String, Object> map = hit.getSource();
		SearchResult sr = new SearchResult();
		buildMap(map, hit);
		sr.build(map);
		return sr;
	}

	public BulkResponse batch(List<EsModel> esModelList) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (EsModel esModel : esModelList) {
			if (EsDMlEnum.Insert.equals(esModel.getEnumType())) {
				if (esModel.getId() == null) {
					esModel.setId(Long.toString(Math.abs(UUID.randomUUID()
							.getMostSignificantBits()), 36));
				}
				String jsonSource = esModel.getJsonSource() instanceof String ? (String) esModel
						.getJsonSource() : JSON.toJSONString(esModel
						.getJsonSource());
				bulkRequest.add(client.prepareIndex(esModel.getIndex(),
						esModel.getType(), esModel.getId()).setSource(
						jsonSource));
			}
			if (EsDMlEnum.Update.equals(esModel.getEnumType())) {
				String jsonSource = esModel.getJsonSource() instanceof String ? (String) esModel
						.getJsonSource() : JSON.toJSONString(esModel
						.getJsonSource());
				bulkRequest.add(client.prepareUpdate(esModel.getIndex(),
						esModel.getType(), esModel.getId()).setDoc(jsonSource));
			}
			if (EsDMlEnum.Delete.equals(esModel.getEnumType()))
				bulkRequest.add(client.prepareDelete(esModel.getIndex(),
						esModel.getType(), esModel.getId()));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		return bulkResponse;
	}
	
	public BulkResponse batchUpdateDelete(List<EsModel> esModelList) {
		BulkRequest bulkRequest=new BulkRequest();
		for (EsModel esModel : esModelList) {
			if (EsDMlEnum.Update.equals(esModel.getEnumType())) {
				String jsonSource = esModel.getJsonSource() instanceof String ? (String) esModel
						.getJsonSource() : JSON.toJSONString(esModel
								.getJsonSource());
						UpdateRequest request=new UpdateRequest(esModel.getIndex(),
								esModel.getType(), esModel.getId()).doc(jsonSource);
						bulkRequest.add(request);
			}
			if (EsDMlEnum.Delete.equals(esModel.getEnumType())){
				DeleteRequest request=new DeleteRequest(esModel.getIndex(),
						esModel.getType(), esModel.getId());
				bulkRequest.add(request);
			}
		}
		return client.bulk(bulkRequest).actionGet();
	}

	public BulkResponse batchInsertShard(List<EsModel> esModelList) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (EsModel esModel : esModelList) {
			if (esModel.getId() == null) {
				esModel.setId(Long.toString(
						Math.abs(UUID.randomUUID().getMostSignificantBits()),
						36));
			}
			Object jsonSource = esModel.getJsonSource();
			EsJsonSourceModel ejs = null;
			if (esModel.getT() == null) {
				if (esModel.getJsonSource() instanceof EsJsonSourceModel) {
					ejs = (EsJsonSourceModel) esModel.getJsonSource();
				} else if (esModel.getJsonSource() instanceof String) {
					try {
						ejs = (EsJsonSourceModel) JSON.parseObject(
								(String) esModel.getJsonSource(),
								Class.forName(esModel.getType()));
					} catch (Exception e) {
					}
				}
				if (ejs != null) {
					esModel.setT(ejs);
					EsJsonSourceModel t = esModel.getT();
					t.setShardId(Integer.parseInt(String.valueOf(Math.abs(UUID.randomUUID()
							.getMostSignificantBits()) % 30)));
					jsonSource = JSON.toJSONString(t);
				}
			}
			bulkRequest.add(client.prepareIndex(esModel.getIndex(),
					esModel.getType(), esModel.getId()).setSource(
					jsonSource instanceof String ? (String) jsonSource : JSON
							.toJSONString(jsonSource)));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		return bulkResponse;
	}

	public IndexResponse insert(String index, String type, String id,
			Object jsonSource) {
		if (id == null) {
			id = Long.toString(
					Math.abs(UUID.randomUUID().getMostSignificantBits()), 36);
		}
		return client
				.prepareIndex(index, type, id)
				.setSource(
						jsonSource instanceof String ? (String) jsonSource
								: JSON.toJSONString(jsonSource)).execute()
				.actionGet();
	}

	public UpdateResponse update(String index, String type, String id,
			Object jsonSource) {
		return client.prepareUpdate(index, type, id)
				.setDoc(JSON.toJSONString(jsonSource)).execute().actionGet();
	}

	public DeleteResponse delete(String index, String type, String id) {
		return client.prepareDelete(index, type, id).execute().actionGet();
	}

	private void buildMap(Map<String, Object> map, SearchHit hit) {
		map.put("__id", hit.getId());
		map.put("__type", hit.getType());
		map.put("__index", hit.getIndex());
	}
	
    public void deleteIndex(String indexName) {
        if (!isIndexExists(indexName)) {
		    logger.info(indexName + " not exists");
		} else {
		    DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
		            .get();
		    if (dResponse.isAcknowledged()) {
		        logger.info("delete index "+indexName+"  successfully!");
		    }else{
		        logger.info("Fail to delete index "+indexName);
		    }
		}
    }

    public void createIndex(String indexName) {
		if (isIndexExists(indexName)) {
		    logger.info("Index  " + indexName + " already exits!");
		} else {
		    CreateIndexResponse cIndexResponse = client.admin().indices().prepareCreate(indexName)
		            .get();
		    if (cIndexResponse.isAcknowledged()) {
		        logger.info("create index successfully！");
		    } else {
		        logger.info("Fail to create index!");
		    }
		}
    }
    
    public Set<String> getIndex(String indexNamePrefix) {
    		GetIndexRequest request=new GetIndexRequest();
    		request.indices(indexNamePrefix+"*");
    		GetIndexResponse resp=client.admin().indices().getIndex(request).actionGet();
    		String[] idxArray=resp.getIndices();
    		Set<String> setString=new LinkedHashSet<String>();
    		setString.addAll(Arrays.asList(idxArray));
    		return setString;
    	}

    public boolean isIndexExists(String indexName) {
		IndicesExistsResponse inExistsResponse;
		try {
			inExistsResponse = client.admin().indices().prepareExists(indexName).get();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
        return inExistsResponse.isExists();
    }
}