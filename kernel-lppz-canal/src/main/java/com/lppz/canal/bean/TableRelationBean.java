package com.lppz.canal.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class TableRelationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7550935061353445027L;
	
	private Map<String, TableBean> tables;

	public Map<String, TableBean> getTables() {
		return tables;
	}

	public void setTables(Map<String, TableBean> tables) {
		this.tables = tables;
	}
	
	public static void main(String[] args) {
		
		TableRelationBean rbean = new TableRelationBean();
		rbean.setTables(new HashMap<String, TableBean>());
		
		TableBean bean = null;
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("ordersharding");
		bean.setPrimaryKey("id");
		bean.setForeignKey(null);
		bean.setParentTalbeName(null);
		bean.setParentTalbePrimaryKey(null);
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orders");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("ordersharding");
		bean.setParentTalbePrimaryKey("orderid");
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		bean.getRelationKeys().add("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orderlines");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orderlinequantities");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderline");
		bean.setParentTalbeName("orderlines");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orderlineattributes");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderline");
		bean.setParentTalbeName("orderlines");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orderlinedata_locationroles");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderline");
		bean.setParentTalbeName("orderlines");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_orderline_promotion_info");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderline");
		bean.setParentTalbeName("orderlines");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("shipments");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("paymentinfo");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_promotion_info");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("busi_omsinterfacemutual_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("orderdata_srlocationids");
		bean.setPrimaryKey("id");
		bean.setForeignKey("srcid");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_merge_order_pool_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_lp_deliverye_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_lp_deliverye_line_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("mydelivery");
		bean.setParentTalbeName("busi_lp_deliverye_data");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("returns");
		bean.setPrimaryKey("id");
		bean.setForeignKey("order");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("id");
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("returnorderlines");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myreturn");
		bean.setParentTalbeName("returns");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_refund_only_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myreturn");
		bean.setParentTalbeName("returns");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_return_package_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myreturn");
		bean.setParentTalbeName("returns");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_return_pick_order");
		bean.setPrimaryKey("id");
		bean.setForeignKey(null);
		bean.setParentTalbeName(null);
		bean.setParentTalbePrimaryKey(null);
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsext");
		bean.setTableName("busi_return_pick_orderline");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("busi_return_pick_order");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("busi_omsinterfacemutual_wms_data");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("busi_lack_order");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderid");
		bean.setParentTalbeName("orders");
		bean.setParentTalbePrimaryKey("orderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_order");
		bean.setPrimaryKey("id");
		bean.setForeignKey(null);
		bean.setParentTalbeName(null);
		bean.setParentTalbePrimaryKey(null);
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		bean.getRelationKeys().add("outorderid");
		bean.getRelationKeys().add("omsid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_order_line");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("temp_order");
		bean.setParentTalbePrimaryKey("omsid");
		bean.setRelationKeys(new ArrayList<String>());
		bean.getRelationKeys().add("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_order_line_promotion_info");
		bean.setPrimaryKey("id");
		bean.setForeignKey("orderline");
		bean.setParentTalbeName("temp_order_line");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_payment_info");
		bean.setPrimaryKey("id");
		bean.setForeignKey("myorder");
		bean.setParentTalbeName("temp_order");
		bean.setParentTalbePrimaryKey("omsid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("catch_order_err_msg");
		bean.setPrimaryKey("id");
		bean.setForeignKey("outorderid");
		bean.setParentTalbeName("temp_order");
		bean.setParentTalbePrimaryKey("outorderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_return_order");
		bean.setPrimaryKey("id");
		bean.setForeignKey("outorderid");
		bean.setParentTalbeName("temp_order");
		bean.setParentTalbePrimaryKey("outorderid");
		rbean.getTables().put(bean.getTableName(), bean);
		
		bean = new TableBean();
		bean.setSchemaName("omsedb");
		bean.setTableName("temp_return_order_line");
		bean.setPrimaryKey("id");
		bean.setForeignKey("tempreturnorder");
		bean.setParentTalbeName("temp_return_order");
		bean.setParentTalbePrimaryKey("id");
		rbean.getTables().put(bean.getTableName(), bean);
		Yaml yaml = new Yaml();
		System.out.println(yaml.dump(rbean));
	}
	
}
