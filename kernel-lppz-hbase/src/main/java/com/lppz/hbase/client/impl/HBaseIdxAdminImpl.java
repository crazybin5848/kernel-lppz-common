package com.lppz.hbase.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseIdxParam;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;

import com.alibaba.fastjson.JSON;
import com.lppz.hbase.client.HBaseIdxAdminInterface;
import com.lppz.hbase.client.configuration.BaseHbaseClientConfig;

public class HBaseIdxAdminImpl extends BaseHbaseClientConfig implements HBaseIdxAdminInterface{

	@Override
	public HBaseDDLResult addIdx(String tableName, String idxName,
			String familyName, List<String> colnameList,
			ArrayList<HbaseDataType> orderByList) throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, idxName, familyName, colnameList,orderByList);
		return doDDL("/services/hbaseIdxddl/addIdx", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult dropIdx(String tableName, String idxName,
			String familyName) throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, idxName, familyName,null);
		return doDDL("/services/hbaseIdxddl/dropIdx", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult addIdxTable(String tableName, String familyName,
			String column) throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, null, familyName, column);
		return doDDL("/services/hbaseIdxddl/addIdxTable", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult dropIdxTable(String tableName, String familyName,
			String column) throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, null, familyName, column);
		return doDDL("/services/hbaseIdxddl/dropIdxTable", JSON.toJSONString(param));
	}

	@Override
	public HBaseDDLResult getIdx(String tableName, String familyName)
			throws Exception {
		HBaseIdxParam param=new HBaseIdxParam().build(tableName, null, familyName, null);
		return doDDL("/services/hbaseIdxddl/getIdx", JSON.toJSONString(param));
	}

}
