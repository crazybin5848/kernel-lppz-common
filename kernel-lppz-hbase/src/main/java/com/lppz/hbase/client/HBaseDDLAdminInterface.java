package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.HbaseClientInterface;
import org.apache.hadoop.hbase.client.coprocessor.model.FamilyCond;
import org.apache.hadoop.hbase.client.coprocessor.model.HbaseOp;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseIdxParam;

public interface HBaseDDLAdminInterface extends HbaseClientInterface {
	public HBaseDDLResult creatTable(String tableName,
			FamilyCond[] familyCrArray, int regionNum, String mainFamilyName,
			boolean isCompress,int... es2total) throws Exception;

	public HBaseDDLResult deleteTable(String tableName) throws Exception;

	public HBaseDDLResult modifyTableFamily(String tableName, HbaseOp op,
			FamilyCond fc) throws Exception;

	public HBaseDDLResult modifyTableCoprocessor(String tableName, HbaseOp op,
			String coprocessorName) throws Exception;
	
	public HBaseDDLResult getTableDesc(String tableName,String familyName) throws Exception;

}
