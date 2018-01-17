package com.lppz.hbase.client;


import java.util.Arrays;

import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseIdxAdminImpl;

public class HBaseIdxAdminTest extends BaseTest{
	HBaseIdxAdminImpl hia;
	boolean isCompress = false;
	@Before
	public void init(){
		hia=new HBaseIdxAdminImpl();
		try {
			super.initConf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testAddIdx() {
		try {
//			ArrayList<HbaseDataType> orderByList=new ArrayList<HbaseDataType>(1);
//			HbaseDataType orderBy=new HbaseDataType();
//			orderBy.setDataType(Date.class);
//			orderBy.setOrderBy(OrderBy.ASC);
//			orderBy.setQulifier("createTime");
//			HbaseDataType orderBy1=new HbaseDataType();
//			orderBy1.setDataType(Integer.class);
//			orderBy1.setOrderBy(OrderBy.DESC);
//			orderBy1.setQulifier("cash");
//			orderByList.add(orderBy);
//			orderByList.add(orderBy1);
//			hia.addIdx("omsorder", "idxOlSku1", "orderline", Arrays.asList(new String[]{"sku"}),null);
			hia.addIdx("hbaseorder", "idxOrderCrTime", "order", Arrays.asList(new String[]{"createdate","issuedate","paymentdate"}),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDropIdx() {
		try {
			hia.dropIdx("omsorder", "idxOrderCrTime", "orderdetail");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDropIdxTable() {
		try {
			hia.dropIdxTable("omsorder", "orderdetail", "createTime");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddIdxTable() {
		try {
			hia.addIdxTable("hbaseorder", "order", "createdate");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListIdx() {
		try {
			HBaseDDLResult result=hia.getIdx("hbaseorder", "order");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
