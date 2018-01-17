package com.lppz.hbase.client.impl;

import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.HbaseOp;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseCrTableParam;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseIdxParam;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseModifiedParam;

import com.alibaba.fastjson.JSON;
import com.lppz.hbase.client.HBaseDDLAdminInterface;
import com.lppz.hbase.client.configuration.BaseHbaseClientConfig;

public class HBaseDDLAdminImpl extends BaseHbaseClientConfig implements
		HBaseDDLAdminInterface {

	@Override
	public HBaseDDLResult creatTable(String tableName,
			FamilyCond[] familyCrArray, int regionNum, String mainFamilyName,
			boolean isCompress,int... es2total) throws Exception {
		HBaseCrTableParam param = new HBaseCrTableParam().build(tableName,
				familyCrArray, regionNum, mainFamilyName, isCompress,es2total);
		return doDDL("/services/hbaseddl/crTb", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult deleteTable(String tableName) throws Exception {
		return doDDL("/services/hbaseddl/delTb", tableName);
	}

	@Override
	public HBaseDDLResult modifyTableFamily(String tableName, HbaseOp op,
			FamilyCond fc) throws Exception {
		HBaseModifiedParam param=new HBaseModifiedParam().build(tableName, op, fc, null);
		return doDDL("/services/hbaseddl/updateTbFamily", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult modifyTableCoprocessor(String tableName, HbaseOp op,
			String coprocessorName) throws Exception {
		HBaseModifiedParam param=new HBaseModifiedParam().build(tableName, op, null, coprocessorName);
		return doDDL("/services/hbaseddl/updateTbCoprocessor", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult getTableDesc(String tableName, String familyName)
			throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, null, familyName, null);
		return doDDL("/services/hbaseddl/getTableDesc", JSON.toJSONString(param));
	}
}