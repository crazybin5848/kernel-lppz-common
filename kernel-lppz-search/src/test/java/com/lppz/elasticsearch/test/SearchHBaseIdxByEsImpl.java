//package com.lppz.elasticsearch.test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.hadoop.hbase.client.coprocessor.model.EsIdxHbaseType;
//import org.apache.hadoop.hbase.client.coprocessor.model.idx.ScanOrderStgKV;
//import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanOrderedKV;
//import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
//import org.elasticsearch.client.support.AbstractClient;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import com.lppz.elasticsearch.EsClientUtil;
//import com.lppz.elasticsearch.LppzEsComponent;
//import com.lppz.elasticsearch.result.SearchAllResult;
//import com.lppz.elasticsearch.result.SearchResult;
//import com.lppz.elasticsearch.search.SearchCondition;
//
//public class SearchHBaseIdxByEsImpl extends PathMatchingResourcePatternResolver{
//public static void main(String[] args) {
//	SearchHBaseIdxByEsImpl ss=new SearchHBaseIdxByEsImpl();
//	ss.initEs();
////	ScanOrderStgKV sstgkv=new ScanOrderStgKV(new ScanOrderedKV("createTime","2017-02-06 03:03:11",CompareOp.GREATER_OR_EQUAL),new ScanOrderedKV("createTime","2017-02-07 03:03:11",CompareOp.LESS_OR_EQUAL));
//	List<String> list=ss.buildPrefixListByEs("omsorder", sstgkv, "orderdetail", false);
//	System.out.println();
//}
//
//private List<String> buildPrefixListByEs(String tbName,
//		 String familiName, boolean reversed) {
//	SearchCondition searchCond=sstgkv.buildSearchCond(tbName,familiName,reversed);
//	SearchAllResult sar=LppzEsComponent.getInstance().search(searchCond);
//	List<String> listPrefix=new ArrayList<String>(sar.getResultSearchList().size());
////	for(SearchResult sr:sar.getResultSearchList()){
////		EsIdxHbaseType type=(EsIdxHbaseType)sr.getSource();
////		listPrefix.add(type.getPrefix());
////	}
//	return listPrefix;
//}
//
//private void initEs(){
//	Resource[] resources = null;
//	AbstractClient client=null;
//	try {
//		resources = getResources("classpath*:/META-INF/es-cluster*.yaml");
//	} catch (Exception e) {
////		LOG.error(e.getMessage(),e);
//		e.printStackTrace();
//		return;
//	}
//	if(!ArrayUtils.isEmpty(resources)){
//		Resource clientRes = resources[0];
//		try {
//			client = EsClientUtil.buildPoolClientProxy(clientRes.getInputStream());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	LppzEsComponent.getInstance().setClient(client);
//}
//}
