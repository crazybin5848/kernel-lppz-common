package com.lppz.hbase.client;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.coprocessor.agg.AggHandlerProxy.AggEnum;
import org.apache.hadoop.hbase.client.coprocessor.agggroupby.AggGroupByHandlerProxy.AggGroupByEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.AggGroupResult;
import org.apache.hadoop.hbase.client.coprocessor.model.AggResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ClassEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.OrderBy;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CasCadeScanMap;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.PagerList;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanCascadeResult;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanOrderedKV;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapred.IFile;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseScanImpl;

public class HBaseScanOrderTest extends BaseTest{
	HBaseScanImpl hbs;
	@Before
	public void init(){
		hbs=new HBaseScanImpl();
		super.initConf();
	}
//	AP170608036628550
//	DD170608036628753
//	FN170608036628519
//	GW170608036628282
//	GW170608036628301
//	GW170608036628302
//	GW170608036628375
//	GW170608036628429
//	GW170608036628430
//	GW170608036628489
	@Test
	public void testScanResultByRowKeyLoop() {
			testScanResultByRowKey(new String[]{"WX160223B06901968"});
//			testScanResultByRowKey(new String[]{"AP170608036628550"});
//			testScanResultByRowKey(new String[]{"DD170608036628753"});
//			testScanResultByRowKey(new String[]{"FN170608036628519"});
//			testScanResultByRowKey(new String[]{"GW170608036628282"});
//			testScanResultByRowKey(new String[]{"GW170608036628301"});
//			testScanResultByRowKey(new String[]{"GW170608036628302"});
//			testScanResultByRowKey(new String[]{"GW170608036628375"});
//			testScanResultByRowKey(new String[]{"GW170608036628429"});
//			testScanResultByRowKey(new String[]{"GW170608036628430"});
//			testScanResultByRowKey(new String[]{"GW170608036628489"});
//			testScanResultByRowKey(new String[]{"QJ160417008900082"});
//			testScanResultByRowKey("WX160417008900742");
//			testScanResultByRowKey("WX160417008901130");
//			testScanResultByRowKey("QJ160417008900082");
//			testScanResultByRowKey("WX160417008900742");
//			testScanResultByRowKey("WX160417008901130");
//			testScanResultByRowKey("QJ160417008900082");
//			testScanResultByRowKey("WX160417008900742");
//			testScanResultByRowKey("WX160417008901130");
	}
	
//	@Test
	public void testScanResultByRowKey(String[] orderidArray) {
		List<ScanOrderedKV> simpleCondList=new ArrayList<ScanOrderedKV>();
//		simpleCondList.add(new ScanOrderedKV("orderid",orderid,CompareOp.EQUAL));
		List<TreeMap<String, String>> complexScanCond=new ArrayList<TreeMap<String, String>>();
		for(String orderid:orderidArray){
			TreeMap<String, String> map=new TreeMap<String, String>();
			map.put("username", "丁玲");
			complexScanCond.add(map);
		}
//		simpleCondList.add(new ScanOrderedKV("orderid","QJ160417008900082",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderid","QJ160417008901037",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderid","WX160417008900742",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderid" ,"WX160417008901130",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("createdate","2016-04-17 23:00:34",CompareOp.GREATER_OR_EQUAL));
//		simpleCondList.add(new ScanOrderedKV("createdate","2016-04-18 00:00:59",CompareOp.LESS_OR_EQUAL));
		CasCadeScanMap casCadeScanMap=new CasCadeScanMap();
//		List<ScanOrderedKV> olListScan=new ArrayList<ScanOrderedKV>();
//		olListScan.add(new ScanOrderedKV("sku","123",CompareOp.EQUAL));
		casCadeScanMap.put("orderline",null);
		long tStart = System.currentTimeMillis(); 
		System.out.println("tStart:"+tStart);
		List<ScanResult> list=hbs.scanResultDetail("hbaseorder", "order", null, complexScanCond, 10000, casCadeScanMap,true);
		for(ScanResult sr:list){
			ScanCascadeResult scr=sr.build();
			Map<String,String> orderMap=scr.getSource().getQulifyerValueMap();
			System.out.println(Bytes.toString(scr.getSource().getRow()));
			for(String s:orderMap.keySet()){
				String v=orderMap.get(s);
//				if(s.equals("orderid"))
				System.out.println(s+"::"+v);
			}
			List<ScanCascadeResult> olList=scr.getCascadeMap().get("orderline");
			if(olList!=null){
				List<String> scanPPkList=new ArrayList<String>();
				for(ScanCascadeResult scr1:olList){
//					System.out.println(Bytes.toString(scr1.getSource().getRow()));
//					long tStart1 = System.currentTimeMillis(); 
//					List<ScanResult> orderlinequantities=hbs.scanResultByParentRowKey("hbaseshipment", "orderlinequantities",null, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 100, null);
//					long tEnd1 = System.currentTimeMillis(); 
//					System.out.println("orderlinequantities:"+(tEnd1 - tStart1) + "millions");
//					long tStart2 = System.currentTimeMillis(); 
//					Map<String,List<CascadeCell>> hbaseorderlinepromotionMap=hbs.scanResultByParentRowKey("hbaseorderlinepromotion", new String[]{"linepromotion","lineattribute","orderlinelocaltion"}, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 20);
//					if(hbaseorderlinepromotionMap!=null)
//					System.out.println(hbaseorderlinepromotionMap.size());
//					long tEnd2 = System.currentTimeMillis(); 
//					System.out.println("hbaseorderlinepromotionMap:"+(tEnd2 - tStart2) + "millions");
					scanPPkList.add(Bytes.toString(scr1.getSource().getRow()));
				}
				List<ScanResult> listOrderLinequanties=hbs.scanBatchResultByParentRowKey("hbaseshipment", "orderlinequantities",null, scanPPkList.toArray(new String[0]), "hbaseorder", "orderline", 100, null);
				Map<String,List<CascadeCell>> hbaseorderlinepromotionMap=hbs.scanBatchResultByParentRowKey("hbaseorderlinepromotion", new String[]{"linepromotion","lineattribute","orderlinelocaltion"}, scanPPkList.toArray(new String[0]), "hbaseorder", "orderline", 100);
				System.out.println("-----------------");
			}
//			List<ScanResult> hbasedeliveryList=hbs.scanResultByParentRowKey("hbasedelivery", "delivery",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			
			//List<ScanResult> hbasereturnList=hbs.scanResultByParentRowKey("hbasereturn", "return",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			long tStart3 = System.currentTimeMillis(); 
			List<ScanResult> shipmentList=hbs.scanResultByParentRowKey("hbaseshipment", "shipment",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			List<ScanResult> ordersrlocaltionList=hbs.scanResultByParentRowKey("hbaseordersrlocaltion", "ordersrlocaltion",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			List<ScanResult> hbasemergeorderpoolList=hbs.scanResultByParentRowKey("hbasemergeorderpool", "mergeorderpool",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			List<ScanResult> hbaselackorderList=hbs.scanResultByParentRowKey("hbaselackorder", "lackorder",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			Map<String,List<CascadeCell>> hbasepaymentinfoMap=hbs.scanResultByParentRowKey("hbasepaymentinfo", new String[]{"paymentinfo","promotioninfo"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
			Map<String,List<CascadeCell>> hbaseomsinterfaceMap=hbs.scanResultByParentRowKey("hbaseomsinterface", new String[]{"omsinterface","omsinterfacewms"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
			long tEnd3 = System.currentTimeMillis(); 
			System.out.println("bbb:"+(tEnd3 - tStart3) + "millions");
			System.out.println();
		}
		long tEnd = System.currentTimeMillis(); 
		System.out.println("totalEnd:"+tEnd);
		System.out.println(tEnd - tStart + "millions");
//		System.out.println(list.size());
	}
	
	@Test
	public void testScanResultPage() {
		List<ScanOrderedKV> simpleCondList=new ArrayList<ScanOrderedKV>();
//		simpleCondList.add(new ScanOrderedKV("issuedate","2017-06-08 00:00:00",CompareOp.GREATER_OR_EQUAL));
//		simpleCondList.add(new ScanOrderedKV("issuedate","2017-06-09 23:59:59",CompareOp.LESS_OR_EQUAL));
//		simpleCondList.add(new ScanOrderedKV("username","丁玲",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shippingfirstname","丁玲",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shad-mobilephone","15570552809",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("basestore","1028",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderpayment","53.30",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("paidamount","53.30",CompareOp.EQUAL));
		simpleCondList.add(new ScanOrderedKV("orderid","WX150803000628385",CompareOp.EQUAL));

		
//		simpleCondList.add(new ScanOrderedKV("logisticstatus","OUTSTORAGE",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("liangpintag","0",CompareOp.EQUAL));
		CasCadeScanMap casCadeScanMap=new CasCadeScanMap();
//		List<ScanOrderedKV> olListScan=new ArrayList<ScanOrderedKV>();
//		olListScan.add(new ScanOrderedKV("sku","123",CompareOp.EQUAL));
		casCadeScanMap.put("orderline",null);
		long tStart = System.currentTimeMillis(); 
		System.out.println("tStart:"+tStart);
		PagerList list=hbs.scanResultByRowKeyPage("hbaseorder", "order", null, simpleCondList, casCadeScanMap, true, 0, 15);
		if(list.getListResult()!=null)
		for(ScanResult sr:list.getListResult()){
			if (sr != null) {
				ScanCascadeResult scr=sr.build();
				Map<String,String> orderMap=scr.getSource().getQulifyerValueMap();
				System.out.println(Bytes.toString(scr.getSource().getRow()));
				for(String s:orderMap.keySet()){
					String v=orderMap.get(s);
//				if(s.equals("orderid"))
//				System.out.println(s+"::"+v);
				}
			}
//			List<ScanCascadeResult> olList=scr.getCascadeMap().get("orderline");
//			if(olList!=null){
//				for(ScanCascadeResult scr1:olList){
//					System.out.println(Bytes.toString(scr1.getSource().getRow()));
//					@SuppressWarnings("unused")
//					List<ScanResult> orderlinequantities=hbs.scanResultByParentRowKey("hbaseshipment", "orderlinequantities",null, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 100, null);
//					Map<String,List<CascadeCell>> hbaseorderlinepromotionMap=hbs.scanResultByParentRowKey("hbaseorderlinepromotion", new String[]{"linepromotion","lineattribute","orderlinelocaltion"}, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 20);
//					if(hbaseorderlinepromotionMap!=null)
//					System.out.println(hbaseorderlinepromotionMap.size());
//				}
//			}
//			List<ScanResult> hbasedeliveryList=hbs.scanResultByParentRowKey("hbasedelivery", "delivery",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			
			//List<ScanResult> hbasereturnList=hbs.scanResultByParentRowKey("hbasereturn", "return",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			
			
//			List<ScanResult> shipmentList=hbs.scanResultByParentRowKey("hbaseshipment", "shipment",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> ordersrlocaltionList=hbs.scanResultByParentRowKey("hbaseordersrlocaltion", "ordersrlocaltion",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> hbasemergeorderpoolList=hbs.scanResultByParentRowKey("hbasemergeorderpool", "mergeorderpool",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> hbaselackorderList=hbs.scanResultByParentRowKey("hbaselackorder", "lackorder",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			Map<String,List<CascadeCell>> hbasepaymentinfoMap=hbs.scanResultByParentRowKey("hbasepaymentinfo", new String[]{"paymentinfo","promotioninfo"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
//			Map<String,List<CascadeCell>> hbaseomsinterfaceMap=hbs.scanResultByParentRowKey("hbaseomsinterface", new String[]{"omsinterface","omsinterfacewms"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
			System.out.println();
		}
		long tEnd = System.currentTimeMillis(); 
		System.out.println("tEnd:"+tEnd);
		System.out.println(tEnd - tStart + "millions");
		System.out.println(list.getListResult().size());
	}
	
	@Test
	public void testScanReturnResultByRowKey() {
		List<TreeMap<String, String>> complexScanCond=new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> map=new TreeMap<String, String>();
//		map.put("shippingfirstname", "王文珍");
		map.put("oid","FX150731000000007");
		complexScanCond.add(map);
		CasCadeScanMap casCadeScanMap=new CasCadeScanMap();
//		List<ScanOrderedKV> olListScan=new ArrayList<ScanOrderedKV>();
//		olListScan.add(new ScanOrderedKV("sku","123",CompareOp.EQUAL));
		casCadeScanMap.put("returnline",null);
		long tStart = System.currentTimeMillis(); 
		System.out.println("tStart:"+tStart);
		List<ScanResult> list=hbs.scanResultDetail("hbasereturn", "return", null, complexScanCond, 10000, casCadeScanMap,true);
		System.out.println("=================="+list.size());
		for(ScanResult sr:list){
			ScanCascadeResult scr=sr.build();
			Map<String,String> orderMap=scr.getSource().getQulifyerValueMap();
			System.out.println("head:"+Bytes.toString(scr.getSource().getRow()));
			for(String s:orderMap.keySet()){
				String v=orderMap.get(s);
//				if(s.equals("orderid"))
//				System.out.println(s+"::"+v);
			}
			List<ScanCascadeResult> olList=scr.getCascadeMap().get("returnline");
			if(olList!=null){
				List<String> scanPPkList=new ArrayList<String>();
				for(ScanCascadeResult scr1:olList){
					System.out.println("line:"+Bytes.toString(scr1.getSource().getRow()));
					scanPPkList.add(Bytes.toString(scr1.getSource().getRow()));
				}
				System.out.println("-----------------");
			}
//			List<ScanResult> hbasedeliveryList=hbs.scanResultByParentRowKey("hbasedelivery", "delivery",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
			
			//List<ScanResult> hbasereturnList=hbs.scanResultByParentRowKey("hbasereturn", "return",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
		}
		long tEnd = System.currentTimeMillis(); 
		System.out.println("totalEnd:"+tEnd);
		System.out.println(tEnd - tStart + "millions");
//		System.out.println(list.size());
	}
	
	@Test
	public void testReturnScanResultPage() {
		List<ScanOrderedKV> simpleCondList=new ArrayList<ScanOrderedKV>();
		simpleCondList.add(new ScanOrderedKV("creationtime","2016-02-01 00:00:00",CompareOp.GREATER_OR_EQUAL));
		simpleCondList.add(new ScanOrderedKV("creationtime","2016-02-05 23:59:59",CompareOp.LESS_OR_EQUAL));
//		simpleCondList.add(new ScanOrderedKV("username","丁玲",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shippingfirstname","丁玲",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shad-mobilephone","15570552809",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("basestore","1028",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderpayment","53.30",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("paidamount","53.30",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderid","WX150803000628385",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("oid","FX150731000000007",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("outorderid","1666926439052649",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("type","returnOrder",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("returncode","FX150801Q00015038",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("returncode","JD160204Q06561803",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("orderid","JD160201006529125",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shippingfirstname","王文珍",CompareOp.EQUAL));
//		simpleCondList.add(new ScanOrderedKV("shad-mobilephone","15570552809",CompareOp.EQUAL));
		
		
		CasCadeScanMap casCadeScanMap=new CasCadeScanMap();
		casCadeScanMap.put("returnline",null);
		long tStart = System.currentTimeMillis(); 
		System.out.println("tStart:"+tStart);
		PagerList list=hbs.scanResultByRowKeyPage("hbasereturn", "return", null, simpleCondList, casCadeScanMap, true, 0, 15);
		if(list.getListResult()!=null)
			for(ScanResult sr:list.getListResult()){
				if (sr != null) {
					ScanCascadeResult scr=sr.build();
					Map<String,String> orderMap=scr.getSource().getQulifyerValueMap();
					System.out.println("head : "+Bytes.toString(scr.getSource().getRow()));
					for(String s:orderMap.keySet()){
						String v=orderMap.get(s);
//				if(s.equals("orderid"))
//				System.out.println(s+"::"+v);
					}
					List<ScanCascadeResult> olList=scr.getCascadeMap().get("returnline");
					if(olList!=null){
						for(ScanCascadeResult scr1:olList){
							System.out.println("line : "+Bytes.toString(scr1.getSource().getRow()));
//							@SuppressWarnings("unused")
//							List<ScanResult> orderlinequantities=hbs.scanResultByParentRowKey("hbaseshipment", "orderlinequantities",null, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 100, null);
//							Map<String,List<CascadeCell>> hbaseorderlinepromotionMap=hbs.scanResultByParentRowKey("hbaseorderlinepromotion", new String[]{"linepromotion","lineattribute","orderlinelocaltion"}, Bytes.toString(scr1.getSource().getRow()), "hbaseorder", "orderline", 20);
//							if(hbaseorderlinepromotionMap!=null)
//								System.out.println(hbaseorderlinepromotionMap.size());
						}
					}
				}
//			List<ScanResult> hbasedeliveryList=hbs.scanResultByParentRowKey("hbasedelivery", "delivery",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
				
				//List<ScanResult> hbasereturnList=hbs.scanResultByParentRowKey("hbasereturn", "return",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
				
				
//			List<ScanResult> shipmentList=hbs.scanResultByParentRowKey("hbaseshipment", "shipment",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> ordersrlocaltionList=hbs.scanResultByParentRowKey("hbaseordersrlocaltion", "ordersrlocaltion",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> hbasemergeorderpoolList=hbs.scanResultByParentRowKey("hbasemergeorderpool", "mergeorderpool",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			List<ScanResult> hbaselackorderList=hbs.scanResultByParentRowKey("hbaselackorder", "lackorder",null, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 100, null);
//			Map<String,List<CascadeCell>> hbasepaymentinfoMap=hbs.scanResultByParentRowKey("hbasepaymentinfo", new String[]{"paymentinfo","promotioninfo"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
//			Map<String,List<CascadeCell>> hbaseomsinterfaceMap=hbs.scanResultByParentRowKey("hbaseomsinterface", new String[]{"omsinterface","omsinterfacewms"}, Bytes.toString(scr.getSource().getRow()), "hbaseorder", "order", 20);
				System.out.println();
			}
		long tEnd = System.currentTimeMillis(); 
		System.out.println("tEnd:"+tEnd);
		System.out.println(tEnd - tStart + "millions");
		System.out.println(list.getListResult().size());
	}
	
	@Test
	public void testIdxScanResultByRowKey() {
		List<TreeMap<String, String>> complexScanCondList=new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> tree1=new TreeMap<String, String>();
		tree1.put("auid", "u000000539");
		TreeMap<String, String> tree2=new TreeMap<String, String>();
		tree2.put("auid", "u000000912");
		complexScanCondList.add(tree1);
		complexScanCondList.add(tree2);
		long tStart = System.currentTimeMillis(); 
		List<ScanResult> list=null;//hbs.scanResultByRowKey("idx_omsorder", "orderdetail", null, null, complexScanCondList, 5, null);
		long tEnd = System.currentTimeMillis(); 
		System.out.println(tEnd - tStart + "millions");
		System.out.println(list.size());
		for(ScanResult sr:list){
			String auid=Bytes.toString(sr.build().getSource().getRow());
			System.out.println("auid:"+auid);
//			List<ScanCascadeResult> ll=sr.build().getCascadeMap().get("orderline");
//			System.out.println(ll.size());
//			for(ScanCascadeResult scr:ll){
//				System.out.println(Bytes.toString(scr.getSource().getRow()));
//			}
		}
	}
	
//	@Test
//	public void testScanResultByRowKeyPage() {
//		List<ScanOrderedKV> simpleCondList=new ArrayList<ScanOrderedKV>();
////		simpleCondList.add(new ScanOrderedKV("createTime","2016-05-23 02:07:16",CompareOp.GREATER_OR_EQUAL));
////		simpleCondList.add(new ScanOrderedKV("outorderid","out0000001",CompareOp.GREATER_OR_EQUAL));
////		simpleCondList.add(new ScanOrderedKV("storeid","s0001",CompareOp.GREATER_OR_EQUAL));
//		simpleCondList.add(new ScanOrderedKV("auid","u000000333",CompareOp.EQUAL));
//		ArrayList<HbaseDataType> orderByList=new ArrayList<HbaseDataType>(1);
//			HbaseDataType orderBy=new HbaseDataType();
//			orderBy.setDataType(Date.class);
//			orderBy.setOrderBy(OrderBy.ASC);
//			orderBy.setQulifier("createTime");
//			orderByList.add(orderBy);
//			PagerList list=null;
//			String startRow=null;
//			CasCadeScanMap casCadeScanMap=new CasCadeScanMap();
//			List<ScanOrderedKV> olListScan=new ArrayList<ScanOrderedKV>();
//			olListScan.add(new ScanOrderedKV("sku","123",CompareOp.EQUAL));
//			casCadeScanMap.put("orderline",olListScan);//.put("orderlinedetail",null);
//			casCadeScanMap.put("ordershipment",null);
//			for(list=hbs.scanResultByRowKeyPage("omsorder", "orderdetail", null, simpleCondList,
//					casCadeScanMap, orderByList, startRow,6,false);;){
//				for(ScanResult sr:list.getListScan()){
//					String row=Bytes.toString(sr.build().getSource().getRow());
//					String createTime=sr.build().getSource().getQulifyerValueMap().get("createTime");
//					System.out.println("row:"+row+"createTime:"+createTime);
//				}
//				System.out.println();
//				startRow=list.getEndRow();
//				if(list.isEndPage())
//				{
//					startRow=list.getEndRow();
//					for(list=hbs.scanResultByRowKeyPage("omsorder", "orderdetail", null, simpleCondList,
//							casCadeScanMap, orderByList, startRow,6,true);;){
//						for(ScanResult sr:list.getListScan()){
//							String row=Bytes.toString(sr.build().getSource().getRow());
//							String createTime=sr.build().getSource().getQulifyerValueMap().get("createTime");
//							System.out.println("row:"+row+"createTime:"+createTime);
//						}
//						System.out.println();
//						startRow=list.getEndRow();
//						if(list.isEndPage())
//							return;
//						list=hbs.scanResultByRowKeyPage("omsorder", "orderdetail", null, simpleCondList,
//								casCadeScanMap, orderByList, startRow, 6,true);
//					}
//				}
//				list=hbs.scanResultByRowKeyPage("omsorder", "orderdetail", null, simpleCondList, 
//						casCadeScanMap, orderByList, startRow, 6,false);
//			}
//	}

	@Test
	public void testGroupByResultScan() {
		String scanqulifier="cash";
		ClassEnum classType=ClassEnum.Double;
		int caching=100;
		AggGroupByEnum type=AggGroupByEnum.avg;
		String tableName="omsorder";
		List<ScanOrderedKV> simpleScanCond=new ArrayList<ScanOrderedKV>(1);
		String familiName="orderdetail";
		List<String> groupBy=new ArrayList<String>(1);
		simpleScanCond.add(new ScanOrderedKV("createTime","2016-06-16 09:27:40",CompareOp.GREATER_OR_EQUAL));
		simpleScanCond.add(new ScanOrderedKV("createTime","2016-06-16 09:27:53",CompareOp.LESS_OR_EQUAL));
		groupBy.add("auid");
		AggGroupResult agp=hbs.scanGroupByResultByRowKey(tableName, familiName, scanqulifier, classType, simpleScanCond, groupBy, caching, type);
		Object o=agp.buildValue(type, classType);
		System.out.println();
	}
	
	@Test
	public void testAggResultScan() {
		String scanqulifier="createTime";
		ClassEnum classType=ClassEnum.Date;
		int caching=100;
		AggEnum type=AggEnum.max;
		String tableName="omsorder";
		List<ScanOrderedKV> simpleScanCond=new ArrayList<ScanOrderedKV>(1);
//		simpleScanCond.add(new ScanOrderedKV("createTime","2016-06-16 09:27:40",CompareOp.GREATER_OR_EQUAL));
//		simpleScanCond.add(new ScanOrderedKV("createTime","2016-06-16 09:27:57",CompareOp.LESS_OR_EQUAL));
		String familiName="orderdetail";
		AggResult agp=hbs.scanAggResultByRowKey(tableName, familiName, scanqulifier, classType, simpleScanCond, caching, type);
		Object o=agp.buildValue(type, classType);
		System.out.println();
	}

	@Test
	public void testMapJoin() {
//		TbJoinPoint joinListCond=new TbJoinPoint();
//		joinListCond.setTableName("B");
//		joinListCond.setFamilyName("cfb");
//		joinListCond.getJoinPointMap().put("b11", new TbJoinPoint("A","cfa","a11"));
//		hbs.mapJoin(joinListCond);
	}

}
