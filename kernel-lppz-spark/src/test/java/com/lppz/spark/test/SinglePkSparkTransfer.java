//package com.lppz.spark.test;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.spark.SparkContext;
//import org.apache.spark.sql.Row;
//
//import com.lppz.spark.bean.HivePartionCol;
//import com.lppz.spark.bean.Rdbms2HDfsBean;
//import com.lppz.spark.bean.SparkHiveSqlBean;
//import com.lppz.spark.bean.SparkMysqlDmlBean;
//import com.lppz.spark.bean.SparkSqlConfigBean;
//import com.lppz.spark.scala.MysqlSpark;
//import com.lppz.spark.util.SparkHiveUtil;
//import com.lppz.spark.util.SparkYamlUtils;
//
//public class SinglePkSparkTransfer {
//
//	public static void main(String[] args) throws IOException {
//		args=new String[]{"mysql2hive.yaml","local[8]","partionVal,;2016-09-07","test"};
//		if (args.length == 0)
//			throw new IOException("need yaml config");
//		SparkHiveSqlBean bean = SparkYamlUtils.loadYaml(args[0], true);
//		String mode = args.length == 1 ? "local" : args[1];
//		parse(bean, args);
////		doCreateHiveTable(bean, mode);
//		String fileDir = dofetchMysqlFromSparkToLocal(bean.getConfigBean(),bean.getMysqlBean(),
//				mode,bean.getSourcebean().getHdfsUrl(),bean.getSourcebean().getHpcList().get(0).getValue(),bean.getSourcebean().getHiveschema(),bean.getSourcebean().getHivetableName());
////		doLoadLocal2Hive(bean.getSourcebean(), mode, fileDir);
//	}
//
//	private static void doLoadLocal2Hive(Rdbms2HDfsBean hivebean, String mode,
//			String fileDir) {
//		SparkHiveUtil.doLoad2Hive(hivebean, mode, fileDir,null,true);
//	}
//
//	private static void parse(SparkHiveSqlBean shsb, String[] args) {
//		String url=shsb.getConfigBean().getRdbmsjdbcUrl().replaceAll("#schema#", shsb.getConfigBean().getSchema());
//		shsb.getConfigBean().setRdbmsjdbcUrl(url);
//		if (CollectionUtils.isNotEmpty(shsb.getSourcebean().getHpcList())) {
//			String[] ss = args[args.length - 2].split(",;");
//			int k = 0;
//			for (HivePartionCol hpc : shsb.getSourcebean().getHpcList()) {
//				String val = hpc.getValue().replaceAll("#" + ss[k] + "#",
//						ss[k + 1]);
//				k += 2;
//				hpc.setValue(val);
//			}
//		}
//		String sql = shsb.getMysqlBean().getSql();
//		if (sql.contains("#")) {
//			String[] parmas = args[args.length - 1].split(",;");
//			for (int i = 0; i < parmas.length; i += 2) {
//				sql = sql.replaceAll("#" + parmas[i] + "#", parmas[i + 1]);
//			}
//			shsb.getMysqlBean().setSql(sql);
//		}
//	}
//
//	private static void doCreateHiveTable(SparkHiveSqlBean bean, String mode) {
//		if (bean.getSourcebean().isMode()) {
//			try {
//				SparkHiveUtil.createHiveTableFromRDBMS(bean, mode,null);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private static String dofetchMysqlFromSparkToLocal(
//			SparkSqlConfigBean config,SparkMysqlDmlBean bean, String mode, String hdfsUrl, String partion,String hiveSchema,String hiveTbName) throws IOException {
//		MysqlSpark mysql = new MysqlSpark();
//		String fileDir = "/tmp/mysqldata/" + config.getSchema() + "/"
//				+ bean.getTableName()+"-"+SparkHiveUtil.now();
//		String appName = "load mysqltable " + config.getSchema() + ":"
//				+ bean.getTableName() + SparkHiveUtil.now();
//		SparkContext sc=null;
//		if (bean.getTotal4Once() != null) {
//			Long tt=bean.getTotal4Once();
////			List<Row> maxIdList=mysql.getMysqlAgg(appName, mode, config,bean,"max");
//			int max=10;
//			sc=mysql.buildSc(appName, mode);
//			for (long i = 0;i<=max; i += tt) {
//				bean.setOffset(i);
//				bean.setTotal4Once(tt + i);
//				Map<String,String> map=mysql.getMysqlListAndSave2Hdfs(appName, mode,hiveSchema,hiveTbName,hdfsUrl,partion,config, bean,sc,"2015-08-01",true);
//				System.out.println();
//				//List<Row> list = mysql.getMysqlList(appName, mode, config,bean,sc);
////				if(CollectionUtils.isEmpty(list))
////					continue;
////				FileNioUtil.writeWithMappedByteBuffer(fileDir, bean.getOffset()
////								+ "_" + bean.getTotal4Once(), list);
//			}
//		} else {
//			sc=mysql.buildSc(appName, mode);
//			List<Row> list = mysql.getMysqlList(appName, mode, config,bean,sc);
//			if(CollectionUtils.isEmpty(list))
//				return fileDir;
////			FileNioUtil.writeWithMappedByteBuffer(fileDir, null, list);
//		}
//		sc.stop();
//		return fileDir;
//	}
//}