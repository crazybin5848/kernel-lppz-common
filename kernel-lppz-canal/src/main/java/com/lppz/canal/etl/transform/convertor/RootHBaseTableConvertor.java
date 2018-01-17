package com.lppz.canal.etl.transform.convertor;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.lppz.canal.bean.TableBean;
import com.lppz.spark.bean.JedisHbaseBean;

public class RootHBaseTableConvertor extends AbstractHBaseConvertor {
	
	private static final Logger logger = LoggerFactory.getLogger(RootHBaseTableConvertor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2342581567191241854L;

	public RootHBaseTableConvertor(TableBean tableBean, Jedis jedis, JedisCluster jedisCluster) {
		super(tableBean, jedis, jedisCluster);
	}


	@Override
	public Map<String, String> queryRowKeyQulifierAndValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix(Map<String, String> qulifierAndValue, RowKeyComposition rkc) {
		String prefix = null;
		String mainId=rkc.getMainHbaseCfPk().buidFixedLengthNumber(qulifierAndValue.get(rkc.getMainHbaseCfPk().getOidQulifierName()));
		String kk=getTableName()+"--"+getFamilyName()+"--"+mainId;
		if(getJedisCluster().exists(kk)){
			prefix=getJedisCluster().get(kk);
		}
		else{
			prefix=buildSequencePrefix();
			getJedisCluster().set(kk, prefix);
		};
		return prefix;
	}
	
	@Override
	public void saveRedis(Map<String, String> qulifierAndValue,
			String prefix, RowKeyComposition rkc, StringBuilder sb, JedisHbaseBean... jedisBean) {
		try {
			saveRootHbaseRedis(prefix, qulifierAndValue,rkc);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	protected void saveRootHbaseRedis(String prefix,
			Map<String, String> qulifierAndValue, RowKeyComposition rkc)
			throws IOException {
		String mainIdColumn = getMainIdColumn();
		String mid=new StringBuilder(mainIdColumn).append(Constants.QSPLITTER)
				.append(rkc.getMainHbaseCfPk().buidFixedLengthNumber(qulifierAndValue.get(mainIdColumn))).toString();
		JedisHbaseBean jhbb=new JedisHbaseBean(prefix,mid,null);
		jhbb.setOrgMainId(new StringBuilder(getMainIdColumn()).append(Constants.QSPLITTER).append(qulifierAndValue.get(mainIdColumn)).toString());
		saveJedisHbase(qulifierAndValue, jhbb);
	}
	
	public String buildSequencePrefix() {
		String tableName = getTableName();
		String regionNum=getMapCache().get(tableName).getRegionNum();
		Map<String,String> regionMap=getMapCache().get(tableName).getRegionMap();
		String kk=generateSequencePrefixRowKey(regionNum==null?1:Integer.parseInt(regionNum),regionMap);
		if(kk==null)
			throw new IllegalStateException("lack of meta Table data:"+tableName);
		return kk;
	}
	
	private String generateSequencePrefixRowKey(int length, Map<String, String> regionMap) {
		return generateSequencePrefixRowKey(length, regionMap, getTableName(), getFamilyName());
	}
	
	private String generateSequencePrefixRowKey(int length, Map<String, String> regionMap, String tableName, String familyName) {
		if(length>36||length<1)
			throw new IllegalStateException(tableName+"'s pre region num can not gt 36 or le 1"); 
		String x=Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits() % length)+1,36);
		StringBuilder sb=new StringBuilder("10".equals(x)?"0":x);
		String hkey=regionMap.get(sb.toString());
		if(hkey==null)
			hkey=regionMap.get("");
		Long num=getJedis().hincrBy(tableName+"_"+familyName, hkey, 1);
		String ssnum="";
		try {
			ssnum = HbaseUtil.addZeroForNum(Long.toString(num,36), 8);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		sb.append(ssnum);
		return sb.toString();
	}

}
