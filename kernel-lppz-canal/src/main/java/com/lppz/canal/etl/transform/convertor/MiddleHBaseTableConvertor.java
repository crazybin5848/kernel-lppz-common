package com.lppz.canal.etl.transform.convertor;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.canal.bean.TableBean;
import com.lppz.spark.bean.JedisHbaseBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class MiddleHBaseTableConvertor extends AbstractHBaseConvertor {

	private static final long serialVersionUID = -4172581976443333632L;
	private static final Logger logger = LoggerFactory.getLogger(MiddleHBaseTableConvertor.class);

	public MiddleHBaseTableConvertor(TableBean tableBean, Jedis jedis, JedisCluster jedisCluster) {
		super(tableBean, jedis, jedisCluster);
	}

	@Override
	public Map<String, String> queryRowKeyQulifierAndValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix(Map<String, String> changeRows, RowKeyComposition rkc) {
		JedisHbaseBean jedisBean=getJedisHBaseBeanFromJedis(changeRows);
		if (jedisBean == null) {
			return null;
		}
		return jedisBean.getPrefix();
	}
	
	@Override
	public void saveRedis(Map<String, String> qulifierAndValue,
			String prefix, RowKeyComposition rkc, StringBuilder sb, JedisHbaseBean... jedisBeans) {
		JedisHbaseBean jedisBean = jedisBeans[0];
		appendCascadeRowKeyByJedis(jedisBean, sb);
		String relateKey = getRelateKey();
		if(relateKey!=null)
		qulifierAndValue.remove(relateKey);
		if(jedisBean.getParentId()!=null){
			String[] pp=jedisBean.getOrgParentId().split(Constants.QSPLITTER);
			qulifierAndValue.put(pp[0], pp[1]);
		}
		String[] mm=jedisBean.getOrgMainId().split(Constants.QSPLITTER);
		qulifierAndValue.put(mm[0], mm[1]);
		try {
			saveMiddleHbaseRedis(qulifierAndValue, prefix, jedisBean,rkc);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	protected void appendCascadeRowKeyByJedis(JedisHbaseBean jedisBean,
			StringBuilder sb) {
		if(jedisBean.getParentId()!=null)
		sb.append(Constants.SPLITTER).append(jedisBean.getParentId());
		sb.append(Constants.SPLITTER).append(jedisBean.getMainId());
	}
	
	private void saveMiddleHbaseRedis(Map<String, String> qulifierAndValue,
			String prefix, JedisHbaseBean jedisBean, RowKeyComposition rkc) throws IOException {
		String mid=jedisBean.getMainId();
		String mainIdColumn = getMainIdColumn();
		String parentId=new StringBuilder(mainIdColumn).append(Constants.QSPLITTER)
		.append(rkc.getMainHbaseCfPk().buidFixedLengthNumber(qulifierAndValue.get(mainIdColumn))).toString();
		JedisHbaseBean jjhba=new JedisHbaseBean(prefix,mid,parentId);
		jjhba.setOrgMainId(jedisBean.getOrgMainId());
		jjhba.setOrgParentId(new StringBuilder(mainIdColumn).append(Constants.QSPLITTER).append(qulifierAndValue.get(mainIdColumn)).toString());
		saveJedisHbase(qulifierAndValue, jjhba);
	}

}
