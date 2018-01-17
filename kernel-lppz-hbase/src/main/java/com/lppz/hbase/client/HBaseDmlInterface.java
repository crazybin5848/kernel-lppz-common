package com.lppz.hbase.client;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.HbaseClientInterface;
import org.apache.hadoop.hbase.client.coprocessor.model.TableDmlModel;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;

import redis.clients.jedis.JedisCluster;

public interface HBaseDmlInterface extends HbaseClientInterface{
	public void insertData(TableDmlModel tdm,JedisCluster jedis) throws IOException;
	
	public void insertBatchDataWithSamePrefix(List<List<TableDmlModel>> tdmList,JedisCluster jedis) throws IOException;
	public void deleteAllColumn(String tableName,String familyName,String rowKey)
			throws IOException;
	public void updateData(String tableName, CascadeCell cell)
			throws IOException;
	public void updateBatchData(String tableName, List<CascadeCell> cellList)
			throws IOException;
	
}
