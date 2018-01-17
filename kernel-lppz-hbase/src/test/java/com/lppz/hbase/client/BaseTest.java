package com.lppz.hbase.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import com.lppz.hbase.client.configuration.HbaseClientConfiguration;

public class BaseTest {
	public void initConf(){
//		Configuration conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.property.clientPort", "2181");  
//			conf.set("hbase.master", "10.6.25.96:600000");  
//		conf.set("hbase.zookeeper.quorum", "Bingo-06-04:2181,Bingo-06-05:2181,Bingo-06-06:2181,Bingo-06-07:2181,Bingo-06-08:2181");
//		conf.set("hbase.zookeeper.quorum", "hanode1:2181,hanode2:2181,hanode3:2181");
//		conf.set("hbase.zookeeper.quorum", "centos7MRBP");
//		conf.setLong("hbase.rpc.timeout", 1000);
//		conf.setInt(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD,1000);
//		conf.setLong("hbase.client.scanner.caching", 10);
		try {
			HbaseClientConfiguration.getInstance().init("/Users/zoubin/Documents/scalawork/kernel-lppz-common/kernel-lppz-hbase/src/test/resources/hbase-lppz-client.yaml");
//			AbstractHbaseClient.init(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		long length=7;
		for(;;){
			String num=Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits() % length)+1,36);		
			System.out.println(num);
		}
	}
}