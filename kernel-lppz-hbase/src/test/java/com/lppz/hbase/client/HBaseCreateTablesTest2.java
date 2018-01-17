package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.MainHbaseCfPk;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseLinkedHashSet;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseTreeSet;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseDDLAdminImpl;

public class HBaseCreateTablesTest2 extends BaseTest{
	HBaseDDLAdminImpl hda;
	boolean isCompress = true;
	@Before
	public void init(){
		hda=new HBaseDDLAdminImpl();
		super.initConf();
	}
	
	/**
	 * 创建订单和订单行表
	 * orders
	 * orderlines
	 */
	@Test
	public void createOrderDetail(){
		HbaseTreeSet<String> omsorder=new HbaseTreeSet<String>();
		
//		omsorder.add("oid");
//		omsorder.add("createdate");
		omsorder.add("outorderid");
		omsorder.add("paymentDate");
		omsorder.add("orderid");
//		omsorder.add("basestore");
//		omsorder.add("parentorder");
//		omsorder.add("issuedate");
//		omsorder.add("scheduledshippingdate");
//		omsorder.add("username");
//		omsorder.add("state");
//		omsorder.add("mergenumber");
//		omsorder.add("shad_mobilephone");
//		omsorder.add("shad-name");
//		omsorder.add("shad-phonenumber");
		
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("orderid", 21),omsorder,null);
		HbaseLinkedHashSet<String> familyColsNeedIdx=new HbaseLinkedHashSet<String>(14);
//		familyColsNeedIdx.add("orderid");
//		familyColsNeedIdx.add("outorderid");
//		familyColsNeedIdx.add("paymentdate");
		familyColsNeedIdx.add("mergenumber");
		familyColsNeedIdx.add("parentorder");
		familyColsNeedIdx.add("shad-mobilephone");
		familyColsNeedIdx.add("shad-phonenumber");
		familyColsNeedIdx.add("username");
		familyColsNeedIdx.add("shad-name");
		familyColsNeedIdx.add("issuedate");
		familyColsNeedIdx.add("createdate");
		familyColsNeedIdx.add("scheduledshippingdate");
		familyColsNeedIdx.add("basestore");
		familyColsNeedIdx.add("state");
//		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		//#####################################################
		
		try {
			hda.creatTable("hbaseorder", new FamilyCond[]{new FamilyCond("order",rkc)}, 36,"order", isCompress);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropOrderTable() {
		try {
			hda.deleteTable("hbaseorder");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}