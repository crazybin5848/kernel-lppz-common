package com.lppz.canal.etl.transform.convertor;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.client.coprocessor.util.HbaseStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.lppz.canal.bean.TableBean;
import com.lppz.spark.bean.HBaseMeta;
import com.lppz.spark.bean.JedisHbaseBean;
import com.lppz.util.kryo.KryoUtil;

public abstract class AbstractHBaseConvertor implements Serializable{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractHBaseConvertor.class);
	private JedisCluster jedisCluster;
	private Jedis jedis;
	public AbstractHBaseConvertor(TableBean tableBean,Jedis jedis, JedisCluster jedisCluster) {
		this.parentFamilyName=tableBean.getParentFamilyName();
		this.isRootHbase=tableBean.isRootTable();
		this.isLeaf=tableBean.isLeafTable();
		this.familyName=tableBean.getHbaseCFName();
		this.tableName=tableBean.getHbaseTableName();
		this.mainIdColumn=tableBean.getMainIdColumn();
		this.colList=tableBean.getColList();
		this.relateKey=tableBean.getForeignKey();
		this.jedis = jedis;
		this.jedisCluster = jedisCluster;
		init();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 112312312344L;
	private Boolean isRootHbase;
	private String mainIdColumn;
	private Boolean isLeaf;
	private String colList;
	private String tableName;
	private String familyName;
	private String parentFamilyName;
	private String relateKey;
	private static Map<String,RowKeyComposition> mapColCache;
	private static Map<String,HBaseMeta> mapCache;
	
	@SuppressWarnings({ "unchecked"})
	private synchronized void init() {
		if(mapColCache==null){
			byte[] mapColByte=getHData("mapHbaseColCache", "mapColCache");
			byte[] mapCacheByte=getHData("mapHbaseColCache", "mapCacheByte");
			if(mapColByte!=null){
				try {
					mapColCache=KryoUtil.kyroDeSeriLize(mapColByte,Map.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(mapCacheByte!=null){
				try {
					mapCache=KryoUtil.kyroDeSeriLize(mapCacheByte,Map.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(mapColCache!=null&&mapCache!=null)
				return;
		}
	}
	
	private byte[] getHData(String key, String field){
		return jedis.hget(key.getBytes(), field.getBytes());
	}
	
	public abstract Map<String,String> queryRowKeyQulifierAndValues();
	public abstract String getPrefix(Map<String,String> changeRows, RowKeyComposition rkc);
	public abstract void saveRedis(Map<String, String> qulifierAndValue, String prefix,
			RowKeyComposition rkc, StringBuilder sb, JedisHbaseBean ... jedisBean);
	
	public String buildRowKey(Map<String, String> qulifierAndValue){
		StringBuilder sb;
		try {
			JedisHbaseBean jedisBean=null;
			RowKeyComposition rkc=mapColCache.get(this.tableName+":::"+this.familyName);
			String prefix= getPrefix(qulifierAndValue, rkc);
			sb = new StringBuilder(prefix).append(Constants.REGTABLEHBASEPREFIIX);
			if(rkc.getOrderBy()!=null){
				if(!qulifierAndValue.containsKey(rkc.getOrderBy().getQulifier())){
					qulifierAndValue.putAll(queryRowKeyQulifierAndValues());
				}
				if (!qulifierAndValue.containsKey(rkc.getOrderBy().getQulifier())) {
					throw new IllegalStateException("Col Value Map must contain:"+rkc.getOrderBy().getQulifier());
				}
				String orderByValue=rkc.getOrderBy().build(qulifierAndValue.get(rkc.getOrderBy().getQulifier()));
				if(orderByValue!=null)
					sb.//append(rkc.getOrderBy().getQulifier()).append(Constants.QSPLITTERORDERBY).
					append(orderByValue).append(Constants.QLIFIERSPLITTER);
			}
			TreeSet<String> colKeysForRow=rkc.getFamilyColsForRowKey();
			int i=0;
			for(String q:colKeysForRow){
				String v=qulifierAndValue.get(q);
				if(v==null) throw new IllegalStateException("Col Value Map must contain:"+q); 
				if(rkc.getMainHbaseCfPk().getOidQulifierName().equals(q)){
					String mainId=rkc.getMainHbaseCfPk().buidFixedLengthNumber(v);
					sb.append(q).append(Constants.QSPLITTER).append(mainId);
				}
				else
				sb.append(q).append(Constants.QSPLITTER).append(v);
				if(i++<colKeysForRow.size()-1)
					sb.append(Constants.SPLITTER);
			}
			
			saveRedis(qulifierAndValue, prefix,rkc, sb, jedisBean);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			throw e;
		}
		return sb.toString();
	}

	protected void saveJedisHbase(Map<String, String> qulifierAndValue,
			JedisHbaseBean jjhba) throws IOException {
		for(String col:this.colList.split(",")){
		    String jedisKey=new StringBuilder("")
						.append(this.familyName)
						.append(":").append(qulifierAndValue.get(col))
						.toString();
		    jedisCluster.set(jedisKey.getBytes(), KryoUtil.kyroSeriLize(jjhba, -1));
		}
	}
	
	protected JedisHbaseBean getJedisHBaseBeanFromJedis(Map<String,String> rowMap) {
		String val=rowMap.get(this.relateKey);
		String jedisKey=new StringBuilder("")
				.append(this.parentFamilyName)
				.append(":").append(val).toString();
		byte[] jhb=jedisCluster.get(jedisKey.getBytes());
		if(jhb==null)
			return null;
		try {
			JedisHbaseBean jhbb=KryoUtil.kyroDeSeriLize(jhb, JedisHbaseBean.class);
//			if(jhbb.getMainId()==null)
//				jhbb.setMainId(val);
			return jhbb;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public Map<String,String> convert(Map<String,String> columnAndValue){
		Map<String,String> cvResultMap = new HashMap<>();
		for (Entry<String, String> entry : columnAndValue.entrySet()) {
			cvResultMap.put(HbaseStringUtil.formatString(entry.getKey()), HbaseStringUtil.formatStringVal(entry.getValue()));
		}
		
		return cvResultMap;
	}
	
	public Boolean getIsRootHbase() {
		return isRootHbase;
	}
	public void setIsRootHbase(Boolean isRootHbase) {
		this.isRootHbase = isRootHbase;
	}
	public String getMainIdColumn() {
		return mainIdColumn;
	}
	public void setMainIdColumn(String mainIdColumn) {
		this.mainIdColumn = mainIdColumn;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getColList() {
		return colList;
	}
	public void setColList(String colList) {
		this.colList = colList;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getParentFamilyName() {
		return parentFamilyName;
	}
	public void setParentFamilyName(String parentFamilyName) {
		this.parentFamilyName = parentFamilyName;
	}
	public String getRelateKey() {
		return relateKey;
	}
	public void setRelateKey(String relateKey) {
		this.relateKey = relateKey;
	}
	public Map<String, RowKeyComposition> getMapColCache() {
		return mapColCache;
	}
	public Map<String, HBaseMeta> getMapCache() {
		return mapCache;
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public Jedis getJedis() {
		return jedis;
	}
	
}

