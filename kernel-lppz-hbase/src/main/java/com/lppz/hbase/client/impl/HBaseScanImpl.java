package com.lppz.hbase.client.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.MultiThreadScan;
import org.apache.hadoop.hbase.client.coprocessor.agg.AggHandlerProxy.AggEnum;
import org.apache.hadoop.hbase.client.coprocessor.agggroupby.AggGroupByHandlerProxy.AggGroupByEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.AggGroupResult;
import org.apache.hadoop.hbase.client.coprocessor.model.AggResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ClassEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanCond;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CasCadeScanMap;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.PagerList;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.PartRowKeyString;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanOrderedKV;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseStringUtil;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.hbase.client.HBaseScanInterface;
import com.lppz.hbase.client.configuration.BaseHbaseClientConfig;

public class HBaseScanImpl extends BaseHbaseClientConfig implements
		HBaseScanInterface {
	private static final Logger logger = LoggerFactory
			.getLogger(HBaseScanImpl.class);

	public Map<String, List<CascadeCell>> scanResultByParentRowKey(String tableName,
			String[] childFamilyNameArray,String parentRowKey,String parentHbaseTable,String parentCF,int caching) {
		try {
			String prefix=parentRowKey.split(Constants.SPLITTER)[0];
			String startRow=prefix+Constants.REGTABLEHBASESTART;
			String endRow=prefix+Constants.REGTABLEHBASESTOP;
			String ppRowKey=buildppk(parentRowKey,parentHbaseTable,parentCF);
			ScanCond sc = buildScanSc(null, childFamilyNameArray, caching, null,
					startRow,endRow,false,true,null,ppRowKey);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			List<ScanResult> scrList = multiScan.multiScan(tableName, null,
					 hTablePool);
			return convert2MapResult(scrList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public Map<String, List<CascadeCell>> scanBatchResultByParentRowKey(String tableName,
			String[] childFamilyNameArray,String[] parentRowKeyArray,String parentHbaseTable,String parentCF,int caching) {
		try {
			String prefix=parentRowKeyArray[0].split(Constants.SPLITTER)[0];
			String startRow=prefix+Constants.REGTABLEHBASESTART;
			String endRow=prefix+Constants.REGTABLEHBASESTOP;
			List<String> parentRowKeyList=new ArrayList<String>();
			for(String parentRowKey:parentRowKeyArray){
				String ppRowKey=buildppk(parentRowKey,parentHbaseTable,parentCF);
				parentRowKeyList.add(ppRowKey);
			}
			ScanCond sc = buildScanSc(null, childFamilyNameArray, caching, null,
					startRow,endRow,false,true,null,parentRowKeyList);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			List<ScanResult> scrList = multiScan.multiScan(tableName, null,
					hTablePool);
			return convert2MapResult(scrList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private ScanCond buildScanSc(PartRowKeyString pr, String[] childFamilyNameArray,
			int caching, CasCadeScanMap casCadeScanMap, String startRow,
			String endRow,boolean isReverse,boolean isFirstPage,List<String> scanqulifier,
			List<String> parentRowKeyList) {
		ScanCond sc = buildScanSc(pr, null, caching, casCadeScanMap, startRow, endRow, isReverse, isFirstPage);
		sc.setScanqulifier(scanqulifier);
		sc.setParentRowKeyList(parentRowKeyList);
		sc.setChildFamilyNameArray(childFamilyNameArray);
		return sc;
	}

	private Map<String, List<CascadeCell>> convert2MapResult(List<ScanResult> scrList) {
		if(CollectionUtils.isEmpty(scrList))
			return null;
		Map<String, List<CascadeCell>> map=new HashMap<String, List<CascadeCell>>();
		for(ScanResult sr:scrList){
			CascadeCell ccc=sr.build().getSource();
			List<CascadeCell> llc=map.get(Bytes.toString(ccc.getFamily()));
			if(CollectionUtils.isEmpty(llc)){
				llc=new ArrayList<CascadeCell>();
			}
			llc.add(ccc);
			map.put(Bytes.toString(ccc.getFamily()), llc);
		}
		return map;
	}

	public List<ScanResult> scanResultByParentRowKey(String tableName,
			String familiName, List<String> scanqulifier,
			String parentRowKey,String parentHbaseTable,String parentCF,int caching,CasCadeScanMap casCadeScanMap) {
		try {
			String prefix=parentRowKey.split(Constants.SPLITTER)[0];
			String startRow=prefix+Constants.REGTABLEHBASESTART;
			String endRow=prefix+Constants.REGTABLEHBASESTOP;
			String ppRowKey=buildppk(parentRowKey,parentHbaseTable,parentCF);
			ScanCond sc = buildScanSc(null, familiName, caching, casCadeScanMap,
					startRow,endRow,false,true,scanqulifier,ppRowKey);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			return multiScan.multiScan(tableName, null,
					 hTablePool);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public List<ScanResult> scanBatchResultByParentRowKey(String tableName,
			String familiName, List<String> scanqulifier,
			String[] parentRowKeyArray,String parentHbaseTable,String parentCF,int caching,CasCadeScanMap casCadeScanMap) {
		try {
			String prefix=parentRowKeyArray[0].split(Constants.SPLITTER)[0];
			String startRow=prefix+Constants.REGTABLEHBASESTART;
			String endRow=prefix+Constants.REGTABLEHBASESTOP;
			List<String> parentRowKeyList=new ArrayList<String>();
			for(String parentRowKey:parentRowKeyArray){
				String ppRowKey=buildppk(parentRowKey,parentHbaseTable,parentCF);
				parentRowKeyList.add(ppRowKey);
			}
			ScanCond sc = buildScanSc(null, familiName, caching, casCadeScanMap,
					startRow,endRow,false,true,scanqulifier,parentRowKeyList);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			return multiScan.multiScan(tableName, null,
					hTablePool);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private ScanCond buildScanSc(PartRowKeyString pr, String familiName,
			int caching, CasCadeScanMap casCadeScanMap, String startRow,
			String endRow,boolean isReverse,boolean isFirstPage,List<String> scanqulifier,
			List<String> parentRowKeyList) {
		ScanCond sc = buildScanSc(pr, familiName, caching, casCadeScanMap, startRow, endRow, isReverse, isFirstPage);
		sc.setScanqulifier(scanqulifier);
		sc.setParentRowKeyList(parentRowKeyList);
		return sc;
	}

	private String buildppk(String parentRowKey, String parentHbaseTable,
			String parentCF) {
		RowKeyComposition rkc=getRKCFromMapCache(parentHbaseTable,parentCF);
		String mainId=rkc.getMainHbaseCfPk().getOidQulifierName();
		String[] ss=null;
		  if(parentRowKey.contains(Constants.QLIFIERSPLITTER)){
			  String r=parentRowKey.split(Constants.QLIFIERSPLITTER,2)[1];
			  ss=r.split(Constants.SPLITTER);
		  }
		  else
		   ss=parentRowKey.split(Constants.SPLITTER);
		  for(String s:ss){
			  if(s.contains(Constants.QSPLITTER)){
				  String[] k=s.split(Constants.QSPLITTER);
				  if(k[0].equals(mainId)){
					  return s;
				  }
			  }
		  }
		return null;
	}

	public List<ScanResult> scanResultDetail(String tableName,
			String familiName, List<String> scanqulifier,
			List<TreeMap<String, String>> complexScanCond, int caching,
			CasCadeScanMap casCadeScanMap,boolean... isNature) {
		try {
			RowKeyComposition rkc = getRKCFromMapCache(tableName, familiName);
			List<TreeMap<String, String>> tmpComplexCond=checkAndResetComplexScanCond(complexScanCond, rkc);
			PartRowKeyString pr = new PartRowKeyString(tmpComplexCond,
					null, scanqulifier);
			ScanCond sc = buildScanSc(pr, familiName, caching, casCadeScanMap,
					null, null,false,true);
			if(isNature!=null&&isNature.length>0){
				sc.setOrgPr(new PartRowKeyString(complexScanCond,
					null, scanqulifier));
			}
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			return multiScan.multiScan(tableName, rkc,
					 hTablePool,isNature);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public AggGroupResult scanGroupByResultByRowKey(String tableName,
			String familiName, String scanqulifier, ClassEnum classType,
			List<ScanOrderedKV> simpleScanCond, List<String> groupBy,
			int caching, AggGroupByEnum type) {
		try {
			RowKeyComposition rkc = getRKCFromMapCache(tableName, familiName);
			checkAndResetSimpleScanCond(simpleScanCond, rkc);
			PartRowKeyString pr = new PartRowKeyString(null, simpleScanCond,
					AggGroupByEnum.count.equals(type) ? null
							: Arrays.asList(new String[] { scanqulifier }));
			ScanCond sc = buildScanSc(pr, familiName, caching, null, null, null,false,true);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			return multiScan.multiGroupScan(tableName, rkc
					.getFamilyColsNeedIdx(), aggregationGroupByClient,
					hTablePool, type, groupBy, classType == null ? null
							: classType.build());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public AggResult scanAggResultByRowKey(String tableName, String familiName,
			String scanqulifier, ClassEnum classType,
			List<ScanOrderedKV> simpleScanCond, int caching, AggEnum type) {
		try {
			RowKeyComposition rkc = getRKCFromMapCache(tableName, familiName);
			checkAndResetSimpleScanCond(simpleScanCond, rkc);
			PartRowKeyString pr = new PartRowKeyString(null, simpleScanCond,
					AggEnum.count.equals(type) ? null
							: Arrays.asList(new String[] { scanqulifier }));
			ScanCond sc = buildScanSc(pr, familiName, caching, null, null, null,false,true);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			return multiScan.multiAggScan(tableName,
					rkc.getFamilyColsNeedIdx(), aggClient, hTablePool, type,
					classType == null ? null : classType.build());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public PagerList scanResultByRowKeyPage(String tableName,
			String familiName, List<String> scanqulifier,
			List<ScanOrderedKV> simpleScanCond,
			CasCadeScanMap casCadeScanMap, Boolean isOrderBy,
			int startRow,int limit) {
		try {
			RowKeyComposition rkc = getRKCFromMapCache(tableName, familiName);
			checkAndResetSimpleScanCond(simpleScanCond, rkc);
			PartRowKeyString pr = new PartRowKeyString(null, simpleScanCond,
					scanqulifier);
			if (limit > 0)
				pr.setLimit(limit);
//			String stopRow=null;
//			boolean isfirstPage=true;
//			if(!StringUtils.isBlank(startRow)){
//				stopRow=startRow.split(Constants.SPLITTER)[0]+(isReverse?Constants.REGTABLEHBASESTART:
//					Constants.REGTABLEHBASESTOP);
//				isfirstPage=false;
//			}
			ScanCond sc = buildScanSc(pr, familiName, limit, casCadeScanMap,
					String.valueOf(startRow), null,false,true);
			MultiThreadScan multiScan = new MultiThreadScan(sc);
			PagerList pg = multiScan.multiScanByPage(tableName,
    					rkc.getFamilyColsNeedIdx(), hTablePool,isOrderBy);
			if (pg != null) {
				pg.setStartRow(startRow);
				pg.setLimit(limit);
				return pg;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	// public void mapJoin(TbJoinPoint tbjp) {
	// ListCacadeCell lcc=null;
	// try {
	// lcc=joinHbaseClient.join(tbjp);
	// } catch (Throwable e) {
	// logger.error(e.getMessage(),e);
	// e.printStackTrace();
	// }
	// jedisProducer.sendMsg(lcc.getJedisPrefixNo());
	// for(Map<JoinRelationShip, String> mm:lcc.getCascadeCellList()){
	// StringBuilder sb=new StringBuilder("");
	// for(JoinRelationShip js:mm.keySet()){
	// sb.append(js.getTableName()+"."+js.getJoinPoint()+":"+mm.get(js)+",");
	// }
	// System.out.println(sb.toString());
	// }
	// System.out.println();
	// }

//	private PagerList buildPagerList(List<ScanResult> listScan, int limit) {
//		PagerList pg = new PagerList();
//		if (listScan != null && !listScan.isEmpty()) {
//				pg.setStartRow(Bytes.toString(listScan.get(0).build().getSource()
//						.getRow()));
//				if (listScan.size() <= limit) {
//					pg.setListScan(listScan);
//					pg.setEndRow(Bytes.toString(listScan.get(listScan.size()-1).build().getSource()
//							.getRow()));
//					pg.setEndPage(true);
//				} else {
//					ScanResult scr = listScan.remove(listScan.size() - 1);
//					pg.setEndRow(Bytes.toString(scr.build().getSource().getRow()));
//					pg.setListScan(listScan);
//					pg.setEndPage(false);
//				}
////				if(isReverse){
////					if(!CollectionUtils.isEmpty(pg.getListScan()))
////					Collections.reverse(pg.getListScan());
////				}
//		}
//		return pg;
//	}

	private ScanCond buildScanSc(PartRowKeyString pr, String familiName,
			int caching, CasCadeScanMap casCadeScanMap, String startRow,
			String endRow,boolean isReverse,boolean isFirstPage,List<String> scanqulifier,String scanParentPK) {
		ScanCond sc = buildScanSc(pr, familiName, caching, casCadeScanMap, startRow, endRow, isReverse, isFirstPage);
		sc.setScanqulifier(scanqulifier);
		sc.setScanParentPK(scanParentPK);
		return sc;
	}
	
	private ScanCond buildScanSc(PartRowKeyString pr, String[] childFamilyNameArray,
			int caching, CasCadeScanMap casCadeScanMap, String startRow,
			String endRow,boolean isReverse,boolean isFirstPage,List<String> scanqulifier,String scanParentPK) {
		ScanCond sc = buildScanSc(pr, null, caching, casCadeScanMap, startRow, endRow, isReverse, isFirstPage);
		sc.setScanqulifier(scanqulifier);
		sc.setScanParentPK(scanParentPK);
		sc.setChildFamilyNameArray(childFamilyNameArray);
		return sc;
	}
	
	private ScanCond buildScanSc(PartRowKeyString pr, String familiName,
			int caching, CasCadeScanMap casCadeScanMap, String startRow,
			String endRow,boolean isReverse,boolean isFirstPage) {
		ScanCond sc = new ScanCond();
		if (pr != null)
			sc.setPr(pr);
		if (!StringUtils.isBlank(familiName))
			sc.setFamiliName(familiName);
		if (!StringUtils.isBlank(startRow))
			sc.setStartRow(startRow);
		if (!StringUtils.isBlank(endRow))
			sc.setEndRow(endRow);
		if (casCadeScanMap != null)
			sc.setCasCadeScanMap(casCadeScanMap);
		if (caching > 0)
			sc.setCaching(caching);
		sc.setReversed(isReverse);
		sc.setFirstPage(isFirstPage);
		return sc;
	}
	
	private void checkAndResetSimpleScanCond(
			List<ScanOrderedKV> simpleScanCond, RowKeyComposition rkc) {
		if (!CollectionUtils.isEmpty(simpleScanCond)) {
			for (ScanOrderedKV sok : simpleScanCond) {
				sok.checkAndSetMainValue(rkc.getMainHbaseCfPk());
				sok.setValue(HbaseStringUtil.formatStringVal(sok.getValue()));
			}
		}
	}
	
	private List<TreeMap<String, String>> checkAndResetComplexScanCond(
			List<TreeMap<String, String>> complexScanCond, RowKeyComposition rkc) {
		if (!CollectionUtils.isEmpty(complexScanCond)) {
			List<TreeMap<String, String>> lisMap=new ArrayList<TreeMap<String, String>>();
			for (TreeMap<String, String> map : complexScanCond) {
				if(map.containsKey(rkc.getMainHbaseCfPk().getOidQulifierName())){
					String pkValueStr=map.get(rkc.getMainHbaseCfPk().getOidQulifierName());
					pkValueStr=rkc.getMainHbaseCfPk().buidFixedLengthNumber(pkValueStr);
					TreeMap<String, String> tmpMap=new TreeMap<>();
					tmpMap.put(rkc.getMainHbaseCfPk().getOidQulifierName(), pkValueStr);
					lisMap.add(tmpMap);
				}
			}
			return lisMap.isEmpty()?complexScanCond:lisMap;
		}
		return complexScanCond;		
	}
}