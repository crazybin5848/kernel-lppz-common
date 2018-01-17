package com.lppz.hbase.client;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.HbaseClientInterface;
import org.apache.hadoop.hbase.client.coprocessor.agg.AggHandlerProxy.AggEnum;
import org.apache.hadoop.hbase.client.coprocessor.agggroupby.AggGroupByHandlerProxy.AggGroupByEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.AggGroupResult;
import org.apache.hadoop.hbase.client.coprocessor.model.AggResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ClassEnum;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CasCadeScanMap;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.PagerList;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanOrderedKV;

public interface HBaseScanInterface extends HbaseClientInterface{
	public List<ScanResult> scanResultDetail(String tableName,
			String familiName, List<String> scanqulifier,
			List<TreeMap<String, String>> complexScanCond, int caching,
			CasCadeScanMap casCadeScanMap,boolean... isNature);
	public Map<String, List<CascadeCell>> scanResultByParentRowKey(String tableName,
			String[] childFamilyNameArray,String parentRowKey,String parentHbaseTable,String parentCF,int caching);
	public List<ScanResult> scanResultByParentRowKey(String tableName,
			String familiName, List<String> scanqulifier,
			String parentRowKey,String parentHbaseTable,String parentCF,int caching,CasCadeScanMap casCadeScanMap);
	public PagerList scanResultByRowKeyPage(String tableName,
			String familiName, List<String> scanqulifier,
			List<ScanOrderedKV> simpleScanCond,
			CasCadeScanMap casCadeScanMap, Boolean isOrderBy,
			int startRow, int limit);

	public AggGroupResult scanGroupByResultByRowKey(String tableName,
			String familiName, String scanqulifier, ClassEnum classType,
			List<ScanOrderedKV> simpleScanCond, List<String> groupBy,
			int caching, AggGroupByEnum type);

	public AggResult scanAggResultByRowKey(String tableName, String familiName,
			String scanqulifier, ClassEnum classType,
			List<ScanOrderedKV> simpleScanCond, int caching, AggEnum type);
}
