package com.lppz.canal.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseConnPool {
	private static final Logger logger = LoggerFactory.getLogger(HbaseConnPool.class);
	private Connection conn;
	
	private static HbaseConnPool connPool;
	
	private HbaseConnPool(){
		
	}
	
	public static HbaseConnPool getInstance(){
		if(connPool == null){
			connPool = new HbaseConnPool();
		}
		
		return connPool;
	}
	
	public void buildHbaseTableConn(String hbaseQuorum, String hbasePort){
	    if(conn==null){
	    	Configuration myConf = HBaseConfiguration.create();
	    	myConf.set("hbase.zookeeper.quorum", hbaseQuorum);
	    	myConf.set("hbase.zookeeper.property.clientPort", hbasePort);
	    	myConf.set("hbase.defaults.for.version.skip", "true");
	    	myConf.set("zookeeper.znode.parent", "/hbase");
	    	try {
				conn = ConnectionFactory.createConnection(myConf);
			} catch (IOException e) {
				logger.error("初始化hbase连接异常",e);
			} catch (Exception e) {
				logger.error("初始化hbase连接异常",e);
			}
	      }
	}
	
	public Connection getConnection(){
		return conn;
	}

}
