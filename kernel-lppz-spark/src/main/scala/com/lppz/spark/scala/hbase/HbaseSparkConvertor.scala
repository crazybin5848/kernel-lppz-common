package com.lppz.spark.scala.hbase

import org.apache.spark.sql.Row
import scala.collection.mutable.Map 

trait HbaseSparkConvertor extends Serializable{
  def convert(row:Row,cols:Array[String],transferCode : (java.util.HashMap[String,String],java.util.Map[String,String]) => java.util.HashMap[String,String]):(String,Map[String,String])
}

object HbaseSparkConvertor{
   def doTransferTableKey="doTransferTables"
   def importTableKey="importTables";
}