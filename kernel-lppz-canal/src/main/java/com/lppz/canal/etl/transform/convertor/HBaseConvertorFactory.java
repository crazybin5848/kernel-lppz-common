package com.lppz.canal.etl.transform.convertor;

import com.lppz.canal.bean.TableBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class HBaseConvertorFactory {
	
	private static HBaseConvertorFactory instance;
	
	private HBaseConvertorFactory(){}

	public static HBaseConvertorFactory getInstance(){
		if (instance == null) {
			instance = new HBaseConvertorFactory();
		}
		return instance;
	}

	public AbstractHBaseConvertor getConvertor(TableBean tableBean, Jedis jedis, JedisCluster jedisCluster){
		if (tableBean.isRootTable()) {
			return new RootHBaseTableConvertor(tableBean, jedis, jedisCluster);
		}else if (tableBean.isLeafTable()) {
			return new LeafHBaseTableConvertor(tableBean, jedis, jedisCluster);
		}else{
			return new MiddleHBaseTableConvertor(tableBean, jedis, jedisCluster);
		}
	}

}
