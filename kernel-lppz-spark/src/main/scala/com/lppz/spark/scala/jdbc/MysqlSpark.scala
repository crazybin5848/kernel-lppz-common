package com.lppz.spark.scala.jdbc

import java.util.Properties
import org.apache.commons.lang3.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import com.lppz.spark.bean.SparkMysqlDmlBean
import com.lppz.spark.bean.SparkSqlConfigBean
import com.lppz.spark.util.HadoopPutMergeUtil
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem

/**
 * @author zoubin
 */
class MysqlSpark {

  def getMysqlList(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, sc: SparkContext) = {
    val query = buildDfByIdJdbc(appName, mode, config, bean, sc)
    query.collectAsList()
  }

  def buildDfByIdJdbc(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, sc: SparkContext) = {
    val lowerBound = bean.getOffset
    val upperBound = bean.getTotal4Once
    val numPartitions = bean.getNumPartitions
    val url = config.getRdbmsjdbcUrl
    val prop = new Properties()
    prop.put("user", config.getRdbmsjdbcUser)
    prop.put("password", config.getRdbmsjdbcPasswd)
    val sqlContext = new SQLContext(sc)
    val jdbcDF =if(StringUtils.isNotEmpty(bean.getPartitionColumn))
    sqlContext.read.jdbc(url, bean.getTableName, bean.getPartitionColumn, lowerBound, upperBound, numPartitions, prop)
    else
    sqlContext.read.format("jdbc").option("user", config.getRdbmsjdbcUser)
    .option("url", config.getRdbmsjdbcUrl)
    .option("password", config.getRdbmsjdbcPasswd)
    .option("dbtable", config.getSchema+"."+bean.getTableName)
    .option("driver", config.getRdbmsdbDriver).load()
    
    //    jdbcDF.collect().foreach(println)
    //    sqlContext.applySchema(jdbcDF, schema)
    jdbcDF.createOrReplaceTempView(bean.getTableName)
    var sql = bean.getSql
    if (bean.getTotal4Once != null && StringUtils.isNotBlank(bean.getPartitionColumn)) {
      if (sql.contains("where")) {
        sql = sql.concat(" and ").concat(bean.getPartitionColumn).concat(" between ").concat(String.valueOf(bean.getOffset))
          .concat(" and ").concat(String.valueOf(bean.getTotal4Once))
      } else {
        sql = sql.concat(" where ").concat(bean.getPartitionColumn).concat(" between ").concat(String.valueOf(bean.getOffset))
          .concat(" and ").concat(String.valueOf(bean.getTotal4Once))
      }
    }
    
    jdbcDF.sqlContext.sql(sql)
  }

  def getMysqlListArrayDF(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, sc: SparkContext) = {
    val numPartitions = bean.getNumPartitions
    val url = config.getRdbmsjdbcUrl
    val prop = new Properties()
    prop.put("user", config.getRdbmsjdbcUser)
    prop.put("password", config.getRdbmsjdbcPasswd)
    val sqlContext = new SQLContext(sc)
    val jdbcDF = sqlContext.read.jdbc(url, bean.getTableName, bean.getColArray, prop)
    jdbcDF.createOrReplaceTempView(bean.getTableName)
    jdbcDF
  }

  def mergeHdfsFile(hdfs:FileSystem,destFileName: String, fileName: String,hdfsUrl: String) {
    HadoopPutMergeUtil.putMerge(hdfs, fileName, destFileName)
    hdfs.delete(new Path(fileName),true)
  }
  
  def getMysqlListArray(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, sc: SparkContext) = {
    val numPartitions = bean.getNumPartitions
    val url = config.getRdbmsjdbcUrl
    val prop = new Properties()
    prop.put("user", config.getRdbmsjdbcUser)
    prop.put("password", config.getRdbmsjdbcPasswd)
    val sqlContext = new SQLContext(sc)
    val jdbcDF = sqlContext.read.jdbc(url, bean.getTableName, bean.getColArray, prop)
    jdbcDF.createOrReplaceTempView(bean.getTableName)
    jdbcDF.collectAsList()
  }

  def buildQuery(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, sc: SparkContext) = {
    val lowerBound = 1
    val upperBound = 100000000000000l
    val numPartitions = 3
    val url = config.getRdbmsjdbcUrl
    val prop = new Properties()
    prop.put("user", config.getRdbmsjdbcUser)
    prop.put("password", config.getRdbmsjdbcPasswd)
    val sqlContext = new SQLContext(sc)
    val jdbcDF = sqlContext.read.jdbc(url, bean.getTableName, bean.getPartitionColumn, lowerBound, upperBound, numPartitions, prop)
    jdbcDF.createOrReplaceTempView(bean.getTableName)
    val sql = bean.getSql
    jdbcDF.sqlContext.sql(sql)
  }

  def buildSc(appName: String, mode: String) = {
    val conf = new SparkConf();
    conf.setMaster(mode);
    conf.setAppName(appName);
    conf.set("spark.driver.maxResultSize", "5G")
    new SparkContext(conf)
  }

  def getMysqlCount(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean) = {
    val sc = buildSc(appName, mode)
    try {
      val query = buildQuery(appName, mode, config, bean, sc)
      query.count
    } finally {
      sc.stop()
    }
  }

  def getMysqlAgg(appName: String, mode: String, config: SparkSqlConfigBean, bean: SparkMysqlDmlBean, aggCmd: String) = {
    val sc = buildSc(appName, mode)
    try {
      val query = buildQuery(appName, mode, config, bean, sc)
      query.agg((bean.getPartitionColumn, aggCmd)).collectAsList()
    } finally {
      sc.stop()
    }
  }
}