package com.lppz.hbase.client;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.HbaseOp;
import org.apache.hadoop.hbase.client.coprocessor.model.MainHbaseCfPk;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.OrderBy;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseDDLAdminImpl;

public class HBaseDDLAdminTest extends BaseTest{
	
	HBaseDDLAdminInterface hda;
	@Before
	public void init() throws Exception{
		super.initConf();
		hda=new HBaseDDLAdminImpl();
	}
	
	@Test
	public void testModifyTableFamily() {
		try {
			TreeSet<String> olSet=new TreeSet<String>();
			olSet.add("olid");
			FamilyCond fc=new FamilyCond("orderline",new RowKeyComposition(new MainHbaseCfPk("olid", 12),olSet,null));
			hda.modifyTableFamily("omsorder",HbaseOp.ADD, fc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddTable() {
		try {
			TreeSet<String> orderdetailSet=new TreeSet<String>();
			TreeSet<String> olSet=new TreeSet<String>();
			TreeSet<String> oldetailSet=new TreeSet<String>();
			TreeSet<String> oshipSet=new TreeSet<String>();
			orderdetailSet.add("boid");
			orderdetailSet.add("auid");
//			orderdetailSet.add("createTime");
			HbaseDataType orderBy=new HbaseDataType();
			orderBy.setDataType(Date.class);
			orderBy.setOrderBy(OrderBy.DESC);
			orderBy.setQulifier("createTime");
			
			olSet.add("olid");
			oldetailSet.add("oldid");
			oshipSet.add("osid");
			RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("boid", 12),orderdetailSet,null);
			LinkedHashSet<String> familyColsNeedIdx=new LinkedHashSet<String>(3);
			familyColsNeedIdx.add("auid");
//			familyColsNeedIdx.add("createTime");
			familyColsNeedIdx.add("boid");
			rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
			hda.creatTable("omsorder", new FamilyCond[]{new FamilyCond("orderdetail",rkc),
					new FamilyCond("orderline",new RowKeyComposition(new MainHbaseCfPk("olid", 12),olSet,null)),
					new FamilyCond("orderlinedetail",new RowKeyComposition(new MainHbaseCfPk("oldid", 12),oldetailSet,null)),
					new FamilyCond("ordershipment",new RowKeyComposition(new MainHbaseCfPk("osid", 12),oshipSet,null))}, 36,"orderdetail", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTable(){
		TreeSet<String> omsorder=new TreeSet<String>();
//		omsorder.add("id");
		omsorder.add("createdate");
		omsorder.add("outorderid");
		omsorder.add("orderid");
		
//		HbaseDataType orderBy=new HbaseDataType();
//		orderBy.setDataType(Date.class);
//		orderBy.setOrderBy(OrderBy.DESC);
//		orderBy.setQulifier("createdate");
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("orderid", 21),omsorder,null);
//		rkc.setOrderBy(orderBy);
		LinkedHashSet<String> familyColsNeedIdx=new LinkedHashSet<String>(3);
//		familyColsNeedIdx.add("id");
		familyColsNeedIdx.add("createdate");
		familyColsNeedIdx.add("outorderid");
		familyColsNeedIdx.add("orderid");
		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		//#####################################################
		TreeSet<String> orderlineSet=new TreeSet<String>();
		orderlineSet.add("id");
		orderlineSet.add("creationtime");
		orderlineSet.add("skuid");
		orderlineSet.add("orderlineid");
		orderlineSet.add("quantityunitcode");
		
		//#####################################################
		TreeSet<String> paymentInfoSet=new TreeSet<String>();
		paymentInfoSet.add("id");
		paymentInfoSet.add("creationtime");
		
		//#####################################################
		TreeSet<String> shipmentSet=new TreeSet<String>();
		shipmentSet.add("id");
		shipmentSet.add("shippingmethod");
		shipmentSet.add("olqsstatus");
		shipmentSet.add("stockroomlocationid");
		
//		//#####################################################
//		TreeSet<String> omsorderlineSet=Sets.newTreeSet();
//		omsorderlineSet.add("aolid");
//		LinkedHashSet<String> familyColsNeedIdx2=new LinkedHashSet<String>(3);
//		familyColsNeedIdx2.add("aoid");
//		familyColsNeedIdx2.add("createdate");
//		familyColsNeedIdx2.add("outorderid");
//		familyColsNeedIdx2.add("orderid");
//		RowKeyComposition rkc2=new RowKeyComposition(new MainHbaseCfPk("aolid", 12),omsorderlineSet,null);
//		rkc2.setFamilyColsNeedIdx(familyColsNeedIdx);
//		//#####################################################
//		TreeSet<String> orderlinepromotionSet=Sets.newTreeSet();
//		orderlinepromotionSet.add("aolpid");
//		
//		//#####################################################
//		TreeSet<String> orderlineattrSet=Sets.newTreeSet();
//		orderlineattrSet.add("aolattrid");
//		
//		//#####################################################
//		TreeSet<String> orderlinelocationroleSet=Sets.newTreeSet();
//		orderlinelocationroleSet.add("asrcid");
//		//#####################################################
//		TreeSet<String> orderlinequantitySet=Sets.newTreeSet();
//		orderlinequantitySet.add("aolqid");
		try {
//			hda.creatNameSpace("omshisods");
//			hda.creatTable("hbaseorder", new FamilyCond[]{new FamilyCond("order",rkc),
//					new FamilyCond("orderline",new RowKeyComposition(new MainHbaseCfPk("id", 12),orderlineSet,null))}, 36,"order",  isCompress);
//			hda.creatTable("payshipinfo", new FamilyCond[]{new FamilyCond("paymentinfo",new RowKeyComposition(new MainHbaseCfPk("id", 12),paymentInfoSet,null)),
//					new FamilyCond("shipment",new RowKeyComposition(new MainHbaseCfPk("id", 12),shipmentSet,null))}, 36,"paymentinfo", isCompress);
			
//					new FamilyCond("paymentinfo",new RowKeyComposition(new MainHbaseCfPk("id", 12),paymentInfoSet,null)),
//					new FamilyCond("shipment",new RowKeyComposition(new MainHbaseCfPk("id", 12),shipmentSet,null))},);
			
//			hda.creatTable("omsorderline", new FamilyCond[]{
//					new FamilyCond("orderline",rkc2),
//					new FamilyCond("busi_orderline_promotion_info",new RowKeyComposition(new MainHbaseCfPk("aolpid", 12),orderlinepromotionSet,null)),
//					new FamilyCond("orderlineattributes",new RowKeyComposition(new MainHbaseCfPk("aolattrid", 12),orderlineattrSet,null)),
//					new FamilyCond("orderlinequantities",new RowKeyComposition(new MainHbaseCfPk("aolqid", 12),orderlinequantitySet,null)),
//					new FamilyCond("orderlinedata_locationroles",new RowKeyComposition(new MainHbaseCfPk("asrcid", 12),orderlinelocationroleSet,null))}, 36,"orderline");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropTable() {
		try {
			hda.deleteTable("omsorder");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetTableDesc() {
		try {
			HBaseDDLResult result=hda.getTableDesc("hbaseorder", "order");
			RowKeyComposition rkc=result.getRkc();
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListTableDesc() {
		try {
			while(true){
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testRegion() {
//		try {
//			List<HRegionInfo> list=AbstractHbaseClient.getAdmin().getTableRegions(TableName.valueOf("omsorder"));
//			for(HRegionInfo info:list){
//				info.
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public static void main(String[] args) {
//		String value="阳德路东溪桥驾校  往东150米 ，（玲珑轮胎店）  旁边《### 左边路口向内80米》";
//		String v=value.replaceAll("\\#", "\\$");
//		System.out.println(v);
//	}
}
