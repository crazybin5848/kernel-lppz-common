package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.MainHbaseCfPk;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseLinkedHashSet;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseTreeSet;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.lppz.hbase.client.impl.HBaseDDLAdminImpl;

public class HBaseCreateTablesTest3 extends BaseTest {
	HBaseDDLAdminImpl hda;
	boolean isCompress = true;

	@Before
	public void init() {
		hda = new HBaseDDLAdminImpl();
		super.initConf();
	}

	@Ignore
	@Test
	public void hbaseorderCreate() {
		HbaseTreeSet<String> omsorder = new HbaseTreeSet<String>();

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

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("orderid", 21), omsorder, null);

		HbaseLinkedHashSet<String> familyColsNeedIdx = new HbaseLinkedHashSet<String>(14);
		familyColsNeedIdx.add("outorderid");
		familyColsNeedIdx.add("orderid");
		familyColsNeedIdx.add("basestore");
		familyColsNeedIdx.add("issuedate");
		familyColsNeedIdx.add("paymentdate");
		familyColsNeedIdx.add("username");
		familyColsNeedIdx.add("shippingfirstname");
		familyColsNeedIdx.add("shad-mobilephone");
		familyColsNeedIdx.add("shad-phoneNumber");
		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);

		// #####################################################
		HbaseTreeSet<String> orderlineSet = new HbaseTreeSet<String>();
		orderlineSet.add("olid");
		orderlineSet.add("skuid");

		RowKeyComposition rkcLine = new RowKeyComposition(new MainHbaseCfPk("olid", 21), orderlineSet, null);
		HbaseLinkedHashSet<String> familyColsNeedIdxLine = new HbaseLinkedHashSet<String>(1);
		familyColsNeedIdxLine.add("skuid");
		rkcLine.setFamilyColsNeedIdx(familyColsNeedIdxLine);

		try {
			hda.creatTable("hbaseorder",
					new FamilyCond[] { new FamilyCond("order", rkc), new FamilyCond("orderline", rkcLine) }, 36,
					"order", isCompress, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasepaymentinfoCreate() {
		HbaseTreeSet<String> paymentinfo = new HbaseTreeSet<String>();
		paymentinfo.add("payid");// id

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("payid", 21), paymentinfo, null);

		HbaseTreeSet<String> promotioninfo = new HbaseTreeSet<String>();
		promotioninfo.add("pid");// id

		RowKeyComposition rkcPromotion = new RowKeyComposition(new MainHbaseCfPk("pid", 21), promotioninfo, null);

		try {
			hda.creatTable("hbasepaymentinfo", new FamilyCond[] { new FamilyCond("paymentinfo", rkc),
					new FamilyCond("promotioninfo", rkcPromotion) }, 36, "paymentinfo", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasepromotioninfoCreate() {
		HbaseTreeSet<String> orderlinePromotion = new HbaseTreeSet<String>();
		orderlinePromotion.add("olpid");// id改为olpid
		// orderlinePromotion.add("orderline");

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("olpid", 21), orderlinePromotion, null);

		HbaseTreeSet<String> orderSrlocaltion = new HbaseTreeSet<String>();
		orderSrlocaltion.add("srcid");
		orderSrlocaltion.add("destid");
		RowKeyComposition rkcosl = new RowKeyComposition(new MainHbaseCfPk("srcid", 21), orderSrlocaltion, null);

		HbaseTreeSet<String> orderlineAttribute = new HbaseTreeSet<String>();
		orderlineAttribute.add("olaid");// id
		// orderlineAttribute.add("orderline");

		RowKeyComposition rkcola = new RowKeyComposition(new MainHbaseCfPk("olaid", 21), orderlineAttribute, null);

		try {
			hda.creatTable(
					"hbaseorderlinepromotion", new FamilyCond[] { new FamilyCond("linepromotion", rkc),
							new FamilyCond("lineattribute", rkcola), new FamilyCond("orderlinelocaltion", rkcosl) },
					36, "linepromotion", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasedeliveryeCreate() {
		HbaseTreeSet<String> deliverydata = new HbaseTreeSet<String>();
		deliverydata.add("ddid");
		// deliverydata.add("myorder");
		deliverydata.add("trackingid");

		RowKeyComposition rkcDelivery = new RowKeyComposition(new MainHbaseCfPk("ddid", 21), deliverydata, null);
		HbaseLinkedHashSet<String> familyColsNeedIdx = new HbaseLinkedHashSet<String>(3);
		// familyColsNeedIdx.add("myorder");
		familyColsNeedIdx.add("trackingid");
		rkcDelivery.setFamilyColsNeedIdx(familyColsNeedIdx);

		// #############################################

		HbaseTreeSet<String> deliveryLinedata = new HbaseTreeSet<String>();
		deliveryLinedata.add("ddlid");
		// deliveryLinedata.add("mydelivery");

		RowKeyComposition rkcDeliveryLine = new RowKeyComposition(new MainHbaseCfPk("ddlid", 21), deliveryLinedata,
				null);

		try {
			hda.creatTable("hbasedelivery", new FamilyCond[] { new FamilyCond("delivery", rkcDelivery),
					new FamilyCond("deliveryline", rkcDeliveryLine) }, 36, "delivery", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasereturnCreate() {
		HbaseTreeSet<String> returns = new HbaseTreeSet<String>();
		returns.add("rid");// id
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
		returns.add("orderid");
		returns.add("basestore");
		returns.add("username");
		returns.add("shippingfirstname");
		returns.add("shad-mobilephone");
		returns.add("shad-phonenumber");
		returns.add("shad-countrysubentity");
		returns.add("shad-cityname");
		returns.add("shad-name");

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("rid", 21), returns, null);
		HbaseLinkedHashSet<String> familyColsNeedIdx = new HbaseLinkedHashSet<String>(8);
		familyColsNeedIdx.add("returncode");
		familyColsNeedIdx.add("creationtime");
		familyColsNeedIdx.add("modifiedtime");
		familyColsNeedIdx.add("outorderid");
		familyColsNeedIdx.add("orderid");
		familyColsNeedIdx.add("username");
		familyColsNeedIdx.add("shippingfirstname");
		familyColsNeedIdx.add("shad-mobilephone");
		familyColsNeedIdx.add("shad-phoneNumber");
		HbaseTreeSet<String> returnlines = new HbaseTreeSet<String>();

		returnlines.add("rlid");// id

		RowKeyComposition rkcLine = new RowKeyComposition(new MainHbaseCfPk("rlid", 21), returnlines, null);

		try {
			hda.creatTable("hbasereturn",
					new FamilyCond[] { new FamilyCond("return", rkc), new FamilyCond("returnline", rkcLine) }, 36,
					"return", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasereturnpackageCreate() {
		HbaseTreeSet<String> returnPackage = new HbaseTreeSet<String>();
		// returnPackage.add("myreturn");
		returnPackage.add("trackingid");
		returnPackage.add("rpid");// id

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("rpid", 21), returnPackage, null);
		HbaseLinkedHashSet<String> familyColsNeedIdx = new HbaseLinkedHashSet<String>(2);
		familyColsNeedIdx.add("trackingid");
		rkc.setFamilyColsNeedIdx(familyColsNeedIdx);

		HbaseTreeSet<String> refundOnly = new HbaseTreeSet<String>();
		// refundOnly.add("myreturn");
		refundOnly.add("refundonlytype");
		refundOnly.add("roid");// id

		RowKeyComposition rkcRefundOnly = new RowKeyComposition(new MainHbaseCfPk("roid", 21), refundOnly, null);
		HbaseLinkedHashSet<String> familyColsNeedIdxRefundOnly = new HbaseLinkedHashSet<String>(2);
		familyColsNeedIdxRefundOnly.add("refundonlytype");
		rkcRefundOnly.setFamilyColsNeedIdx(familyColsNeedIdxRefundOnly);

		try {
			hda.creatTable("hbasereturnpackage", new FamilyCond[] { new FamilyCond("returnpackage", rkc),
					new FamilyCond("refundonly", rkcRefundOnly) }, 36, "returnpackage", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void hbasereturnpickCreate() {
		HbaseTreeSet<String> returnPickOrder = new HbaseTreeSet<String>();
		// returnPickOrder.add("trackingid");//原表字段名为expresscode，为与父表busi_return_package_data字段名一致，换做trackingid
		returnPickOrder.add("rpoid");// id

		RowKeyComposition rkc = new RowKeyComposition(new MainHbaseCfPk("rpoid", 21), returnPickOrder, null);
		// LinkedHashSet<String> familyColsNeedIdx=new LinkedHashSet<String>(1);
		// familyColsNeedIdx.add("trackingid");
		// rkc.setFamilyColsNeedIdx(familyColsNeedIdx);

		// ########################################
		HbaseTreeSet<String> returnPickOrderLine = new HbaseTreeSet<String>();
		// returnPickOrderLine.add("pickorderid");//原字段名orderid，为与orders表字段区分，这里改为pickorderid
		returnPickOrderLine.add("rpolid");// id

		RowKeyComposition rkcLine = new RowKeyComposition(new MainHbaseCfPk("rpolid", 21), returnPickOrderLine, null);

		try {
			hda.creatTable("hbasereturnpickorder", new FamilyCond[] { new FamilyCond("returnpickorder", rkc),
					new FamilyCond("returnpickorderline", rkcLine) }, 36, "returnpickorder", isCompress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testCreateTable() {
		System.out.println("start...");
		/** orders orderlines **/
		// hbaseorderCreate;
		/** paymentinfo shipments **/
		// hbasepaymentinfoCreate();
		/** busi_promotion_info busi_orderline_promotion_info **/
		// hbasepromotioninfoCreate();
		/** busi_lp_deliverye_data busi_lp_deliverye_line_data **/
		// hbasedeliveryeCreate();
		/** returns returnorderlines **/
		// hbasereturnCreate();
		/** busi_return_package_data busi_refund_only_data **/
		// hbasereturnpackageCreate();
		/** busi_return_pick_order busi_return_pick_orderline **/
		// hbasereturnpickCreate();
		System.out.println("end.");
	}

	@Ignore
	@Test
	public void testDropTable() {
		String[] tableName = new String[] { "hbaseorder", "hbaseppaymentinfo", "hbasepromotioninfo",
				"hbasedeliveryedata", "hbasereturn", "hbasereturnpackage", "hbasereturnpick" };
		for (int i = 0; i < tableName.length; i++) {
			try {
				hda.deleteTable(tableName[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
