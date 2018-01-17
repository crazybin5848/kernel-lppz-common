//package com.lppz.core.inittb;
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
///**
// *
// */
//@Component("initEDBTableBean")
//public class InitEDBTableBean extends BaseInitTableBean implements InitializingBean
//{
//	@Resource(name="multiedbdataSource")
//	public void setMultidataSource(DataSource multidataSource) {
//		this.multidataSource = multidataSource;
//	}
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		initData();
//	}
//
//	@Override
//	protected void build() {
//		super.resourceName = "tempoms_data_init_" + DB_TYPE + ".sql";
//		super.TBName="temp_order";
//		super.component="OMSedBA";
//	}
//}
