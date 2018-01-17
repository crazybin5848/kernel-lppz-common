package com.lppz.hbase.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.HbaseClientInterface;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseIdxParam;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.HbaseDataType;

public interface HBaseIdxAdminInterface extends HbaseClientInterface {
	public HBaseDDLResult addIdx(String tableName,String idxName,String familyName,List<String> colnameList,ArrayList<HbaseDataType> orderByList) throws Exception;

	public HBaseDDLResult dropIdx(String tableName,String idxName,String familyName) throws Exception;

	public HBaseDDLResult addIdxTable(String tableName,String familyName,String column) throws Exception;

	public HBaseDDLResult dropIdxTable(String tableName,String familyName,String column) throws Exception;
	
	public HBaseDDLResult getIdx(String tableName,String familyName) throws Exception;
}
