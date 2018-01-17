package com.lppz.elasticsearch.test;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.lppz.elasticsearch.LppzEsComponent;

public class TestSearchComponent {
	
	public static void main(String[] args) throws UnknownHostException {
		Settings settings = Settings.settingsBuilder().put("cluster.name", "es-lppz-cluster")
		        .put("client.transport.sniff", true).build();
		TransportClient client1 = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.6.30.84"), 9300))
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168"), 9301));
//		QueryStringQueryBuilder queryStringBuilder= new QueryStringQueryBuilder("3433");
//		queryStringBuilder.useDisMax(true);  
////		             queryStringBuilder.analyzer("ik").field("remoteClientAddr");  
//		             queryStringBuilder.field("remoteClientAddr");  
//		             queryStringBuilder.field("responsebody");  
////		             queryStringBuilder.field("desc",1);  
//		//		             2016-03-22T02:19:46.714Z
//		client1.prepareDelete("httplog-2016.07.01", "logs", "AVWkhOVTfAPWIHWIPHKL").
		LppzRestHttpLogDto dto=new LppzRestHttpLogDto();
		dto.setHostUri("1231:qwe");
		dto.setHttpMethodType("post");
		dto.setRemoteClientAddr("wqqwe:242342");
		dto.setRequestbody("wrwrwer");
		dto.setResponsebody("32424234");
		dto.setRequesthttpHeader("123");
		dto.setResponsehttpHeader("3434");
		dto.setClassType(LppzRestHttpLogDto.class.getName());
		UpdateResponse up=null;
		try {
//			up=client1.prepareUpdate("httplog-2016.07.01", "logs", "AVWkgoPTfAPWIHWIPHKK").setDoc(generateJson(dto)).execute().actionGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SearchResponse response = client1.prepareSearch("httplog-2016.07.01")
//				.setTypes("logs")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//		        .setQuery(QueryBuilders.andQuery(QueryBuilders.termQuery("remoteClientAddr","50426"))
//		        		.add(QueryBuilders.andQuery(QueryBuilders.termQuery("hostUri","23")).add(QueryBuilders.rangeQuery("@timestamp").format("yyyy-MM-dd hh:mm:ss").lte("2016-03-22 02:19:47"))))           // Query
				.setQuery(
						QueryBuilders
								.boolQuery()
//								.must(QueryBuilders
//										.boolQuery()
//										.should(QueryBuilders.termQuery(
//												"remoteClientAddr", "50426")).should(QueryBuilders.termQuery(
//														"remoteClientAddr", "50415")))
//								.must(queryStringBuilder)
//								.must(QueryBuilders.regexpQuery("remoteClientAddr", "6546.*"))
								.must(QueryBuilders.wildcardQuery("hostUri", "10.6.30.133:8889*"))
								.must(QueryBuilders.wildcardQuery("responsebody", "fuck"))
								.must(QueryBuilders.rangeQuery("@timestamp")
										.format("yyyy-MM-dd hh:mm:ss")
										.lte("2016-07-22 02:19:47")))
										.addHighlightedField("remoteClientAddr").
										 setHighlighterPreTags("<em>").setHighlighterPostTags("</em>")
										.addSort(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC))
								.addAggregation(
							            AggregationBuilders.terms("agg1").field("hostUri")
//							            .subAggregation(AggregationBuilders.dateHistogram("agg2").field("@timestamp").subAggregation(AggregationBuilders.))
									    )
									    //.addAggregation(AggregationBuilders.dateHistogram("agg2").field("@timestamp"))
				//		        		.add(QueryBuilders.andQuery().add()           // Query
//		         .setPostFilter()     // Filter
		        // .setPostFilter(QueryBuilders.rangeQuery("@timestamp").format("yyyy-MM-dd hh:mm:ss").lte("2016-03-22 02:19:47"))     // Filter
		        .setFrom(0).setSize(60).setExplain(true)
//		        .setQuery(queryStringBuilder)
		        .execute()
		        .actionGet();
		//System.out.println(response);
		for(Iterator<SearchHit> it=response.getHits().iterator();it.hasNext();){
			SearchHit hit=it.next();
			Map<String,Object> map=hit.getSource();
//			Map<String, HighlightField> result = hit.highlightFields();
//			// 从设定的高亮域中取得指定域
//			HighlightField titleField = result.get("remoteClientAddr");
//			// 取得定义的高亮标签
//			Text[] titleTexts = titleField.fragments();
//			// 为title串值增加自定义的高亮标签
//			String title = "";
//			for (Text text : titleTexts) {
//				title += text.string();
//			}
//			// 将追加了高亮标签的串值重新填充到对应的对象
//			// product.setTitle(title);
//			// 打印高亮标签追加完成后的实体对象
//			System.out.println(title);
			for(String s:map.keySet()){
				System.out.println(s+":"+map.get(s));
			}
		}
		Terms agg1 =response.getAggregations().get("agg1");
		//Histogram agg2=response.getAggregations().get("agg2");
		for(Bucket b:agg1.getBuckets()){
			Loggers.getLogger(LppzEsComponent.class).info(b.getKey()+":"+b.getDocCount());
		}
//		for(org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket b:agg2.getBuckets()){
//			Loggers.getLogger(SearchComponent.class).info(b.getKey()+":"+b.getDocCount());
//		}
		client1.close();
	}
}