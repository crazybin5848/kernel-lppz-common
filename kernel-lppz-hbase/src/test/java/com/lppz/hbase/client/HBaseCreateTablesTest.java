package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.MainHbaseCfPk;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseLinkedHashSet;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseTreeSet;
import org.junit.Before;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseDDLAdminImpl;

public class HBaseCreateTablesTest extends BaseTest{
	HBaseDDLAdminImpl hda;
	boolean isCompress = true;
	@Before
	public void init(){
		hda=new HBaseDDLAdminImpl();
		super.initConf();
	}
	
//	@Ignore
	@Test
	public void testCreateTable(){
		//1.orders
		//2.orderlines
		createOrderDetail();
		
		//2.shipments
		//3.orderlinequantities
		createShipments();
		
		//2.paymentinfo
		//2.busi_promotion_info
		createPaymentInfo();
		
		//3.busi_orderline_promotion_info
		//3.orderlineattributes
		//3.orderlinedata_locationroles
		createOrderlinePromotion();
		
		//2.busi_lp_deliverye_data
		//3.busi_lp_deliverye_line_data
		createDeliveries();
		
		//2.busi_omsintefacemutual_data
		//2.busi_omsintefacemutual_wms_data
		createInferface();
		
		//orderdata_Srlocaltions
		createOrderdataSrlocaltions();
		//busi_merge_order_pool_data
		createMergeOrderPool();
		//busi_lack_order
		createLackOrder();
		
		//returns
		//returnlines
		createReturn();
		//busi_return_package_data
		//busi_refund_only_data
		createReturnPackage();
		//busi_return_pick_order
		//busi_return_pick_orderline
		createReturnPickOrder();
	}
	
	@Test
	public void testDropTable(){
		//1.orders
		//2.orderlines
		testDropOrderTable();
		
		//2.shipments
		//3.orderlinequantities
		testDropShipmentTable();
		
		//2.paymentinfo
		//2.busi_promotion_info
		testDropPaymentInfoTable();
		
		//3.busi_orderline_promotion_info
		//3.orderlineattributes
		//3.orderlinedata_locationroles
		testDropOrderlinePromotionTable();
		
		//2.busi_lp_deliverye_data
		//3.busi_lp_deliverye_line_data
		testDropDeliveriesTable();
		
		//2.busi_omsintefacemutual_data
		//2.busi_omsintefacemutual_wms_data
		testDropInferfaceTable();
		
		testDropOrderdataSrlocaltionsTable();
		testDropLackOrderTable();
		testDropMergeOrderPoolTable();
		testDropReturnPackageTable();
		testDropReturnPickOrderTable();
		testDropReturnTable();
	}
	
	/**
	 * 创建订单和订单行表
	 * orders
	 * orderlines
	 */
	@Test
	public void createOrderDetail(){
		HbaseTreeSet<String> omsorder=new HbaseTreeSet<String>();
		
		omsorder.add("outorderid");
		omsorder.add("orderid");
		omsorder.add("basestore");
		omsorder.add("issuedate");
		omsorder.add("paymentdate");
		omsorder.add("state");
		omsorder.add("logisticstatus");
		omsorder.add("paymentstatus");
		omsorder.add("ordertype");
		omsorder.add("ordercategory");
		omsorder.add("splittag");
		omsorder.add("liangpintag");
		omsorder.add("paidamount");
		omsorder.add("orderpayment");
		omsorder.add("username");
		omsorder.add("shippingfirstname");
		omsorder.add("shad-mobilephone");
		omsorder.add("shad-phoneNumber");
		omsorder.add("shad-countrySubentity");
		omsorder.add("shad-cityName");
		omsorder.add("shad-name");
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("orderid", 21),omsorder,null);
		
		HbaseLinkedHashSet<String> familyColsNeedIdx=new HbaseLinkedHashSet<String>(9);
		familyColsNeedIdx.add("orderid");
		familyColsNeedIdx.add("outorderid");
		familyColsNeedIdx.add("shad-mobilephone");
		familyColsNeedIdx.add("shad-phoneNumber");
		familyColsNeedIdx.add("username");
		familyColsNeedIdx.add("shippingfirstname");
		familyColsNeedIdx.add("basestore");
		familyColsNeedIdx.add("issuedate");
		familyColsNeedIdx.add("paymentdate");

		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		//#####################################################
		HbaseTreeSet<String> orderlineSet=new HbaseTreeSet<String>();
		orderlineSet.add("olid");
		
		RowKeyComposition rkcLine=new RowKeyComposition(new MainHbaseCfPk("olid", 21),orderlineSet,null);
		HbaseLinkedHashSet<String> familyColsNeedIdxLine=new HbaseLinkedHashSet<String>(0);
		rkcLine.setFamilyColsNeedIdx(familyColsNeedIdxLine);
		
		try {
			hda.creatTable("hbaseorder", new FamilyCond[]{new FamilyCond("order",rkc),
					new FamilyCond("orderline",rkcLine)}, 36,"order", isCompress,3);
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
	
	/**
	 * 创建shipment
	 * orderlinequantities表
	 */
	@Test
	public void createShipments() {
		HbaseTreeSet<String> shipment=new HbaseTreeSet<String>();
		shipment.add("shipid");//id改为shipid
//		shipment.add("orderfk");
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("shipid", 21),shipment,null);
		
		//###############################################
		HbaseTreeSet<String> orderLineQuantity=new HbaseTreeSet<String>();
		orderLineQuantity.add("olqid");
//		orderLineQuantity.add("orderline");
		orderLineQuantity.add("shipment");
		
		RowKeyComposition rkcOrderLineQuantity=new RowKeyComposition(new MainHbaseCfPk("olqid", 21),orderLineQuantity,null);
		
		try {
			hda.creatTable("hbaseshipment", new FamilyCond[]{new FamilyCond("shipment",rkc),
					new FamilyCond("orderlinequantities",rkcOrderLineQuantity)}, 36,"shipment", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropShipmentTable() {
		try {
			hda.deleteTable("hbaseshipment");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * paymentinfo
	 * promotioninfo
	 */
	@Test
	public void createPaymentInfo() {
		HbaseTreeSet<String> paymentinfo=new HbaseTreeSet<String>();
		paymentinfo.add("payid");//id
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("payid", 21),paymentinfo,null);
		
		HbaseTreeSet<String> promotioninfo=new HbaseTreeSet<String>();
		promotioninfo.add("pid");//id
		
		RowKeyComposition rkcPromotion=new RowKeyComposition(new MainHbaseCfPk("pid", 21),promotioninfo,null);
		
		try {
			hda.creatTable("hbasepaymentinfo", new FamilyCond[]{new FamilyCond("paymentinfo",rkc),
					new FamilyCond("promotioninfo",rkcPromotion)}, 36,"paymentinfo", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testDropPaymentInfoTable() {
		try {
			hda.deleteTable("hbasepaymentinfo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建orderlinepromotion表
	 */
	@Test
	public void createOrderlinePromotion() {
		HbaseTreeSet<String> orderlinePromotion=new HbaseTreeSet<String>();
		orderlinePromotion.add("olpid");//id改为olpid
//		orderlinePromotion.add("orderline");
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("olpid", 21),orderlinePromotion,null);
		
		HbaseTreeSet<String> orderSrlocaltion=new HbaseTreeSet<String>();
		orderSrlocaltion.add("srcid");
		orderSrlocaltion.add("destid");
		RowKeyComposition rkcosl=new RowKeyComposition(new MainHbaseCfPk("srcid", 21),orderSrlocaltion,null);
		
		HbaseTreeSet<String> orderlineAttribute=new HbaseTreeSet<String>();
		orderlineAttribute.add("olaid");//id
//		orderlineAttribute.add("orderline");
		
		RowKeyComposition rkcola=new RowKeyComposition(new MainHbaseCfPk("olaid", 21),orderlineAttribute,null);
		
		try {
			hda.creatTable("hbaseorderlinepromotion", new FamilyCond[]{new FamilyCond("linepromotion",rkc)
			,new FamilyCond("lineattribute",rkcola)
			,new FamilyCond("orderlinelocaltion",rkcosl)}, 36,"linepromotion", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropOrderlinePromotionTable() {
		try {
			hda.deleteTable("hbaseorderlinepromotion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建发货表
	 */
	@Test
	public void createDeliveries() {
		HbaseTreeSet<String> deliverydata=new HbaseTreeSet<String>();
		deliverydata.add("ddid");
//		deliverydata.add("myorder");
		deliverydata.add("trackingid");
		
		RowKeyComposition rkcDelivery=new RowKeyComposition(new MainHbaseCfPk("ddid", 21),deliverydata,null);
		HbaseLinkedHashSet<String> familyColsNeedIdx=new HbaseLinkedHashSet<String>(0);
//		familyColsNeedIdx.add("myorder");
//		familyColsNeedIdx.add("trackingid");
		rkcDelivery.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		//#############################################
		
		HbaseTreeSet<String> deliveryLinedata=new HbaseTreeSet<String>();
		deliveryLinedata.add("ddlid");
//		deliveryLinedata.add("mydelivery");
		
		RowKeyComposition rkcDeliveryLine=new RowKeyComposition(new MainHbaseCfPk("ddlid", 21),deliveryLinedata,null);
		
		try {
			hda.creatTable("hbasedelivery", new FamilyCond[]{new FamilyCond("delivery",rkcDelivery),
					new FamilyCond("deliveryline",rkcDeliveryLine)}, 36,"delivery", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropDeliveriesTable() {
		try {
			hda.deleteTable("hbasedelivery");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * omsintefacemutual 和 omsintefacemutualwms
	 */
	@Test
	public void createInferface() {
		HbaseTreeSet<String> omsintefacemutual=new HbaseTreeSet<String>();
		omsintefacemutual.add("outorderid");
		omsintefacemutual.add("bofdid");//id
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("bofdid", 21),omsintefacemutual,null);
		
		HbaseTreeSet<String> omsintefacemutualWms=new HbaseTreeSet<String>();
		omsintefacemutualWms.add("outorderid");
		omsintefacemutualWms.add("bofwdid");//id
		
		RowKeyComposition rkcwms=new RowKeyComposition(new MainHbaseCfPk("bofwdid", 21),omsintefacemutualWms,null);
		
		try {
			hda.creatTable("hbaseomsinterface", new FamilyCond[]{new FamilyCond("omsinterface",rkc)
			,new FamilyCond("omsinterfacewms",rkcwms)}, 36,"omsinterface", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropInferfaceTable() {
		try {
			hda.deleteTable("hbaseomsinterface");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createOrderdataSrlocaltions() {
		HbaseTreeSet<String> orderSrlocaltion=new HbaseTreeSet<String>();
		orderSrlocaltion.add("destid");
		orderSrlocaltion.add("srcid");
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("srcid", 21),orderSrlocaltion,null);
		
		try {
			hda.creatTable("hbaseordersrlocaltion", new FamilyCond[]{new FamilyCond("ordersrlocaltion",rkc)}, 36,"ordersrlocltion", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDropOrderdataSrlocaltionsTable() {
		try {
			hda.deleteTable("hbaseordersrlocaltion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建合单池表
	 */
	@Test
	public void createMergeOrderPool() {
		HbaseTreeSet<String> mergeOrderPool=new HbaseTreeSet<String>();
		mergeOrderPool.add("mopid");
//		mergeOrderPool.add("orderid");
		
		RowKeyComposition rkcMerge=new RowKeyComposition(new MainHbaseCfPk("mopid", 21),mergeOrderPool,null);
		
		try {
			hda.creatTable("hbasemergeorderpool", new FamilyCond[]{new FamilyCond("mergeorderpool",rkcMerge)}, 36,"mergeorderpool", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropMergeOrderPoolTable() {
		try {
			hda.deleteTable("hbasemergeorderpool");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * lack_order
	 */
	@Test
	public void createLackOrder() {
		HbaseTreeSet<String> lackorder=new HbaseTreeSet<String>();
		lackorder.add("loid");//id
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("loid", 21),lackorder,null);
		
		try {
			hda.creatTable("hbaselackorder", new FamilyCond[]{new FamilyCond("lackorder",rkc)}, 36,"lackorder", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDropLackOrderTable() {
		try {
			hda.deleteTable("hbaselackorder");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns 和 returnorderlines
	 */
	@Test
	public void createReturn() {
		HbaseTreeSet<String> returns=new HbaseTreeSet<String>();
		returns.add("rid");//id
		returns.add("returncode");
		returns.add("type");
		returns.add("oramount-value");
		returns.add("cramount-value");
		returns.add("state");
		returns.add("creationtime");
		returns.add("modifiedtime");
		returns.add("returnbackpayaccount");
		returns.add("returnbackreceiver");
		returns.add("returntype");
		returns.add("ordercategory");
		returns.add("outorderid");
		returns.add("oid");
		returns.add("basestore");
		returns.add("username");
		returns.add("shippingfirstname");
		returns.add("shad-mobilephone");
		returns.add("shad-phonenumber");
		returns.add("shad-countrysubentity");
		returns.add("shad-cityname");
		returns.add("shad-name");
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("rid", 21),returns,null);
		HbaseLinkedHashSet<String> familyColsNeedIdx=new HbaseLinkedHashSet<String>(9);
//		familyColsNeedIdx.add("order");
		familyColsNeedIdx.add("returncode");
		familyColsNeedIdx.add("outorderid");
		familyColsNeedIdx.add("oid");
		familyColsNeedIdx.add("username");
		familyColsNeedIdx.add("shippingfirstname");
		familyColsNeedIdx.add("shad-mobilephone");
		familyColsNeedIdx.add("shad-phonenumber");
		familyColsNeedIdx.add("creationtime");
		familyColsNeedIdx.add("modifiedtime");
		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		HbaseTreeSet<String> returnlines=new HbaseTreeSet<String>();
		
		returnlines.add("rlid");//id
		
		RowKeyComposition rkcLine=new RowKeyComposition(new MainHbaseCfPk("rlid", 21),returnlines,null);
		
		try {
			hda.creatTable("hbasereturn", new FamilyCond[]{new FamilyCond("return",rkc)
			,new FamilyCond("returnline",rkcLine)}, 36,"return", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropReturnTable() {
		try {
			hda.deleteTable("hbasereturn");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * busi_return_package_data
	 * busi_refund_only_data
	 */
	@Test
	public void createReturnPackage() {
		HbaseTreeSet<String> returnPackage = new HbaseTreeSet<String>();
//		returnPackage.add("myreturn");
		returnPackage.add("trackingid");
		returnPackage.add("rpid");//id
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("rpid", 21),returnPackage,null);
		HbaseLinkedHashSet<String> familyColsNeedIdx=new HbaseLinkedHashSet<String>(0);
//		familyColsNeedIdx.add("trackingid");
		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		HbaseTreeSet<String> refundOnly=new HbaseTreeSet<String>();
//		refundOnly.add("myreturn");
		refundOnly.add("refundonlytype");
		refundOnly.add("roid");//id
		
		RowKeyComposition rkcRefundOnly=new RowKeyComposition(new MainHbaseCfPk("roid", 21),refundOnly,null);
		HbaseLinkedHashSet<String> familyColsNeedIdxRefundOnly=new HbaseLinkedHashSet<String>(0);
//		familyColsNeedIdxRefundOnly.add("refundonlytype");
		rkcRefundOnly.setFamilyColsNeedIdx(familyColsNeedIdxRefundOnly);
		
		try {
			hda.creatTable("hbasereturnpackage", new FamilyCond[]{new FamilyCond("returnpackage",rkc)
			,new FamilyCond("refundonly",rkcRefundOnly)}, 36,"returnpackage", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropReturnPackageTable() {
		try {
			hda.deleteTable("hbasereturnpackage");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * busi_return_pick_order
	 * busi_return_pick_orderline
	 * 
	 */
	@Test
	public void createReturnPickOrder() {
		HbaseTreeSet<String> returnPickOrder = new HbaseTreeSet<String>();
//		returnPickOrder.add("trackingid");//原表字段名为expresscode，为与父表busi_return_package_data字段名一致，换做trackingid
		returnPickOrder.add("rpoid");//id
		
		RowKeyComposition rkc=new RowKeyComposition(new MainHbaseCfPk("rpoid", 21),returnPickOrder,null);
//		LinkedHashSet<String> familyColsNeedIdx=new LinkedHashSet<String>(1);
//		familyColsNeedIdx.add("trackingid");
//		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);
		
		//########################################
		HbaseTreeSet<String> returnPickOrderLine = new HbaseTreeSet<String>();
//		returnPickOrderLine.add("pickorderid");//原字段名orderid，为与orders表字段区分，这里改为pickorderid
		returnPickOrderLine.add("rpolid");//id
		
		RowKeyComposition rkcLine=new RowKeyComposition(new MainHbaseCfPk("rpolid", 21),returnPickOrderLine,null);
		
		try {
			hda.creatTable("hbasereturnpickorder", new FamilyCond[]{new FamilyCond("returnpickorder",rkc)
			,new FamilyCond("returnpickorderline", rkcLine)}, 36,"returnpickorder", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDropReturnPickOrderTable() {
		try {
			hda.deleteTable("hbasereturnpickorder");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
