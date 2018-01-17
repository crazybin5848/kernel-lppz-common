package com.lppz.spark.scala

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext

/**
 * @author zoubin
 */
object HiveContextUtil {
  def getRDD(sc:SparkContext, sqlStr: String) = {
      val hiveContext = new HiveContext(sc)
      import hiveContext.implicits._
      import hiveContext.sql
      val d = sql(sqlStr)
      d
  }
  
  def execQuery(sc:SparkContext, sqlStr: String) = {
	  val hiveContext = new HiveContext(sc)
	  import hiveContext.implicits._
	  import hiveContext.sql
	  val d = sql(sqlStr)
	  d.collect()
  }
  
  def exec(sc:SparkContext, sqlStr: String) {
      val hiveContext = new HiveContext(sc)
      import hiveContext.implicits._
      import hiveContext.sql
      sql(sqlStr)
  }
  
  def exec(hc:HiveContext, sqlStr: String) {
	  import hc.implicits._
	  import hc.sql
	  sql(sqlStr)
  }
  
  def registerTmpTable(hc:HiveContext, sqlStr: String,tableName: String) {
	  import hc.implicits._
	  import hc.sql
	  sql(sqlStr).registerTempTable(tableName)
  }
  
  def registerTmpTable(sc:SparkContext, sqlStr: String,tableName: String) = {
	  val hiveContext = new HiveContext(sc)
	  import hiveContext.implicits._
	  import hiveContext.sql
	  sql(sqlStr).registerTempTable(tableName)
	  hiveContext
  }
}