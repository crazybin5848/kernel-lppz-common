package com.lppz.hbase.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.OrderBy;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseIdxAdminImpl;

public class HBaseIdxAdminTest2 extends BaseTest{
	HBaseIdxAdminImpl hia;
	boolean isCompress = false;
	@Before
	public void init(){
		hia=new HBaseIdxAdminImpl();
	}
	@Test
	public void testAddIdx() {
		try {
			ArrayList<HbaseDataType> orderByList=new ArrayList<HbaseDataType>(1);
			HbaseDataType orderBy=new HbaseDataType();
			orderBy.setDataType(Date.class);
			orderBy.setOrderBy(OrderBy.ASC);
			
			orderBy.setQulifier("createTime");
			HbaseDataType orderBy1=new HbaseDataType();
			orderBy1.setDataType(Integer.class);
			orderBy1.setOrderBy(OrderBy.DESC);
			orderBy1.setQulifier("cash");
			orderByList.add(orderBy);
//			orderByList.add(orderBy1);
//			hia.addIdx("omsorder", "idxOlSku1", "orderline", Arrays.asList(new String[]{"sku"}),null);
//			hia.addIdx("hbaseorder", "idxOrderShadname", "order", Arrays.asList(new String[]{"shad-name"}),null);
			hia.addIdx("hbaseorder", "idxOrderState", "order", Arrays.asList(new String[]{"state"}),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDropIdx() {
		try {
			hia.dropIdx("hbaseorder", "order", "idxOrderUsername");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDropIdxTable() {
		try {
			hia.dropIdxTable("omsorder", "orderdetail", "boid");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddIdxTable() {
		try {
			hia.addIdxTable("hbaseorder", "order", "state");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
