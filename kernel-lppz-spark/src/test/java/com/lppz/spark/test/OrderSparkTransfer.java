package com.lppz.spark.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Row;

import com.lppz.spark.bean.SparkHiveSqlBean;
import com.lppz.spark.bean.SparkMysqlDmlBean;
import com.lppz.spark.bean.SparkSqlConfigBean;
import com.lppz.spark.scala.jdbc.MysqlSpark;
import com.lppz.spark.util.FileNioUtil;
import com.lppz.spark.util.SparkHiveUtil;
import com.lppz.spark.util.SparkYamlUtils;

public class OrderSparkTransfer {
	public static void main(String[] args) throws IOException {
		args=new String[]{"./multimysql2hive.yaml","local[8]","partionVal,;2016-09-07","test"};
		if (args.length == 0)
			throw new IOException("need yaml config");
		SparkHiveSqlBean bean = SparkYamlUtils.loadYaml(args[0], true);
		String mode = args.length == 1 ? "local" : args[1];
		SparkHiveUtil.parse(bean, args);
		dofetchMysqlFromSparkToLocal(bean.getConfigBean(), bean.getMysqlBean(), mode);
	}
	
	private static String dofetchMysqlFromSparkToLocal(
			SparkSqlConfigBean config,SparkMysqlDmlBean bean, String mode) throws IOException {
		MysqlSpark mysql = new MysqlSpark();
		String fileDir = "/tmp/mysqldata/" + config.getSchema() + "/"
				+ bean.getTableName()+"-"+SparkHiveUtil.now();
		String appName = "load mysqltable " + config.getSchema() + ":"
				+ bean.getTableName() + SparkHiveUtil.now();
		SparkContext sc=null;
		if (bean.getTotal4Once() != null) {
			Long tt=bean.getTotal4Once();
//			List<Row> maxIdList=mysql.getMysqlAgg(appName, mode, config,bean,"max");
			int max=100000;//maxIdList.get(0).getInt(0);
//			maxIdList=mysql.getMysqlAgg(appName, mode, config,bean,"min");
			int min=1;//maxIdList.get(0).getInt(0);
			sc=mysql.buildSc(appName, mode);
			for (long i = min;i<=max; i += tt) {
				bean.setOffset(i);
				bean.setTotal4Once(tt + i);
				List<Row> list = mysql.getMysqlList(appName, mode, config,bean,sc);
				if(CollectionUtils.isEmpty(list))
					continue;
				Map<String,Object> map=SparkHiveUtil.buildString(list,50);
				StringBuilder sb = (StringBuilder) map.get("sb");
				FileNioUtil.writeWithMappedByteBuffer(fileDir, bean.getOffset()
								+ "_" + bean.getTotal4Once(), sb);
				List<String> oidList=(List<String>)map.get("listOid");
				bean.buildSplitArray(oidList);
				bean.setTableName("orderlines");
				List<Row> ListOlines =mysql.getMysqlListArray(appName, mode, config, bean, sc);
				map=SparkHiveUtil.buildString(ListOlines,-1);
				sb = (StringBuilder) map.get("sb");
				FileNioUtil.writeWithMappedByteBuffer(fileDir, bean.getOffset()
						+ "_" + bean.getTotal4Once(), sb);
			}
		} else {
			sc=mysql.buildSc(appName, mode);
			List<Row> list = mysql.getMysqlList(appName, mode, config,bean,sc);
			if(CollectionUtils.isEmpty(list))
				return fileDir;
			Map<String,Object> map=SparkHiveUtil.buildString(list,50);
			StringBuilder sb = (StringBuilder) map.get("sb");
			FileNioUtil.writeWithMappedByteBuffer(fileDir, null, sb);
		}
		sc.stop();
		return fileDir;
	}
}