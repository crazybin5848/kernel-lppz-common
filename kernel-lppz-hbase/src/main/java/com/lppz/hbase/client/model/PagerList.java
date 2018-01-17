package com.lppz.hbase.client.model;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.coprocessor.model.ScanResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;

public class PagerList {
	private int startRow;
	private int limit;
	private List<ScanResult> listScan;
	public List<ScanResult> getListScan() {
		return listScan;
	}
	public void setListScan(List<ScanResult> listScan) {
		this.listScan = listScan;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void sort(List<HbaseDataType> orderBy){
		if(CollectionUtils.isNotEmpty(listScan)){
			for(ScanResult sr:listScan){
				sr.build().getSource().getQulifyerValueMap();
			}
		}
	}
}
