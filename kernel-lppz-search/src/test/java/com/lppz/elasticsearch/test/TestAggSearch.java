package com.lppz.elasticsearch.test;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.lppz.configuration.es.EsBaseYamlBean;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.spark.util.SparkYamlUtils;

public class TestAggSearch {

private static final Logger LOG = LoggerFactory.getLogger(TestAggSearch.class); 

	public static void main(String[] args) throws UnknownHostException,IOException {
		
		if (args.length == 0)
			throw new IOException("need yaml config");
		EsBaseYamlBean bean = SparkYamlUtils.loadYaml(args[0], false,EsBaseYamlBean.class);
		List<Properties> serverslist = bean.getEsclusterNode();
		TransportAddress[] transportAddress = new TransportAddress[serverslist.size()];
		for (int i = 0; i<serverslist.size(); i++){
			Properties prop = serverslist.get(i);
			transportAddress[i] = new InetSocketTransportAddress(InetAddress.getByName(prop.getProperty("host")), Integer.parseInt(String.valueOf(prop.get("port")))); 
		}
		Settings settings = Settings.settingsBuilder().put("cluster.name", bean.getClusterName())
		        .put("client.transport.sniff", true).build();
		TransportClient client1 = TransportClient.builder().settings(settings).build().addTransportAddresses(transportAddress) ;
		        //.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168"), 9301));
		
		SearchRequestBuilder responsebuilder = client1.prepareSearch("twitter")
				.setTypes("tweet"); //.setSearchType(SearchType.COUNT);
		//AggregationBuilder aggregation = AggregationBuilders.terms("userAgg").field("userid");
		SearchResponse response = responsebuilder.addAggregation(AggregationBuilders.terms("userAgg").field("user")).execute().actionGet();
        
		Terms agg = response.getAggregations().get("userAgg");  
        System.out.println(agg.getBuckets().size());  
        for (Terms.Bucket entry : agg.getBuckets()) {  
            String key = (String) entry.getKey(); // bucket key  
            long docCount = entry.getDocCount(); // Doc count  
            System.out.println("key " + key + " doc_count " + docCount); 
        }
        
        /*Map<String, Aggregation> aggMap = response.getAggregations().asMap();
        
        StringTerms userTerms = (StringTerms) aggMap.get("userAgg");
        
        Iterator<Bucket> userBucketIt = userTerms.getBuckets().iterator();
        
        while(userBucketIt.hasNext())
        {
            Bucket userBucket = userBucketIt.next();
            System.out.println(userBucket.getKey() + "用户有" + userBucket.getDocCount() +"个。");
        }
        */
		
		client1.close();
	}
}