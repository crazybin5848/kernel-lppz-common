package com.lppz.canal.etl.transform.convertor;

import java.util.Map;

import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;

import com.lppz.canal.bean.TableBean;
import com.lppz.spark.bean.JedisHbaseBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class LeafHBaseTableConvertor extends AbstractHBaseConvertor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1543382468028716336L;

	public LeafHBaseTableConvertor(TableBean tableBean, Jedis jedis, JedisCluster jedisCluster) {
		super(tableBean, jedis,
				jedisCluster);
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
			String prefix, RowKeyComposition rkc, StringBuilder sb,
			JedisHbaseBean... jedisBean) {
		// TODO Auto-generated method stub
		
	}

}
