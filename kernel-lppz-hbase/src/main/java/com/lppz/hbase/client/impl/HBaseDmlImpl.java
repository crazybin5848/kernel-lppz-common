package com.lppz.hbase.client.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.exception.HbasePutException;
import org.apache.hadoop.hbase.client.coprocessor.model.CoprocessorPut;
import org.apache.hadoop.hbase.client.coprocessor.model.TableDmlModel;
import org.apache.hadoop.hbase.client.coprocessor.model.scan.CascadeCell;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

import com.lppz.hbase.client.HBaseDmlInterface;
import com.lppz.hbase.client.configuration.BaseHbaseClientConfig;

@SuppressWarnings("deprecation")
public class HBaseDmlImpl extends BaseHbaseClientConfig implements HBaseDmlInterface{
	private static final Logger logger = LoggerFactory.getLogger(HBaseDmlImpl.class);
	public void insertData(TableDmlModel tdm,JedisCluster jedis) throws IOException {
		HTableInterface table = null;
			try {
				table = hTablePool.getTable(Bytes.toBytes(tdm.getTableName()));
				Put put = genPut(tdm,jedis);
				table.put(put);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
				throw e;
			}
			finally {
				table.close();
			}
	}

	public void insertBatchDataWithSamePrefix(List<List<TableDmlModel>> tdmList,JedisCluster jedis) throws IOException {
		HTableInterface table = hTablePool.getTable(Bytes.toBytes(tdmList
				.get(0).get(0).getTableName()));
		try {
			if(tdmList==null||tdmList.isEmpty()||tdmList.get(0).isEmpty()) return;
				List<Put> list=new ArrayList<Put>();
				for(List<TableDmlModel> tl:tdmList){
					List<Put> lp = generatePrefixList(table,jedis,tl);
					list.addAll(lp);
				}
				table.put(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		finally {
			table.close();
		}
	}
	
	
	private List<Put> generatePrefixList(HTableInterface table,JedisCluster jedis,List<TableDmlModel> tdmList){
		List<Put> list = new ArrayList<Put>(tdmList.size());
		TableDmlModel td=tdmList.get(0);
		if(mapCache==null)
			mapCache=super.initMap();
		td.setDesc(mapCache.get(table.getName().getNameAsString()));
		String prefix=!StringUtils.isBlank(td.getPrefix())?td.getPrefix():
			jedis==null?td.buildRandomPrefix():td.buildSequencePrefix(jedis);
		for (TableDmlModel tdm : tdmList)
			try {
				list.add(genPut(tdm,jedis,prefix));
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		return list;
	}
	
	public void deleteAllColumn(String tableName,String familyName,String rowKey)
			throws IOException {
		HTableInterface table = hTablePool.getTable(Bytes.toBytes(tableName));
		Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
		deleteAll.addFamily(familyName.getBytes());
		deleteAll.setAttribute(Constants.IDXHBASETABLEDEL,"".getBytes());
		try {
			table.delete(deleteAll);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		finally{
			table.close();
		}
	}

	public void updateData(String tableName, CascadeCell cell)
			throws IOException {
		HTableInterface table = hTablePool.getTable(Bytes.toBytes(tableName));
		Put put = genUpdatePut(cell);
		try {
			table.put(put);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		finally{
			table.close();
		}
	}

	
	public void updateBatchData(String tableName, List<CascadeCell> cellList)
			throws IOException {
		HTableInterface table = hTablePool.getTable(Bytes.toBytes(tableName));
		List<Put> list = new ArrayList<Put>();
		for (CascadeCell cell : cellList)
			list.add(genUpdatePut(cell));
		try {
			table.put(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		finally{
			table.close();
		}
	}	

	private Put genUpdatePut(CascadeCell cell) throws HbasePutException {
		CoprocessorPut pp=new CoprocessorPut(Bytes.toString(cell.getFamily()),Bytes.toString(cell.getRow()),
				cell.getQulifyerValueMap());
		Put put=pp.buildPut();
		put.setAttribute(Constants.HBASEUPDATEROW, "".getBytes());
		return put;
	}

	private Put genPut(TableDmlModel tdm,JedisCluster jedis,String... prefix) throws IOException {
		tdm.setDesc(mapCache.get(tdm.getTableName()));
		CoprocessorPut pp=new CoprocessorPut(tdm.getFamiliName(),tdm.buildRowKey(jedis,prefix),
				tdm.getQulifierAndValue());
		Put put=pp.buildPut();
		if (tdm.getMapCell() != null)
			put.setAttribute(Constants.HBASECASCADEROW,HbaseUtil.kyroSeriLize(tdm.getMapCell(), -1));
		return put;
	}
}