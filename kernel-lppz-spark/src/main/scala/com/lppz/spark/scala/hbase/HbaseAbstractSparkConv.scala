package com.lppz.spark.scala.hbase

import java.util.HashMap
import java.util.HashSet

import scala.beans.BeanProperty
import scala.collection.mutable.Map

import org.apache.hadoop.hbase.client.coprocessor.util.HbaseStringUtil
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import com.lppz.spark.util.jedis.SparkJedisCluster

import redis.clients.jedis.Jedis

abstract class HbaseAbstractSparkConv(@BeanProperty protected var jedisCluster:SparkJedisCluster,@BeanProperty protected var jedis:Jedis,@BeanProperty protected var paramMap:java.util.Map[String,String]) extends HbaseSparkConvertor{
  @transient lazy val LOG=Logger.getLogger(getClass.getName)
def convert(r:Row,cols:Array[String],transferCoder : (java.util.HashMap[String,String],java.util.Map[String,String]) => java.util.HashMap[String,String]):(String,Map[String,String]) = {
    val map=Map[String,String]()
    cols.foreach { c => {
     val col:String = HbaseStringUtil.formatString(c.asInstanceOf[String])
//      val col:String=c.asInstanceOf[String]
//      .replaceAll("_","-")
//      .replaceAll("::", "\\;;").replaceAll("\\|", "\\!").replaceAll("\\#", "\\$").toLowerCase()
      val v=r.get(r.fieldIndex(c.asInstanceOf[String]))
      val value:String=if(v==null) "NULL" else {
        val vv = String.valueOf(v)
        if("".equals(vv.trim())){
            "NULL"
        }else{
          HbaseStringUtil.formatStringVal(vv)
//          vv.replaceAll("_","\\$-\\$").replaceAll("::", "\\;;").replaceAll("\\|", "\\!!").replaceAll("\\#", "\\$")
          }
      }
      val cv=(col,value)
      map+=cv
    } 
    }
    val mmamp:java.util.HashMap[String,String]=convert(map)
    try {
      val bamp=if(transferCoder!=null) transferCoder(mmamp,paramMap) else mmamp;
      val rowKey=buildRowKey(bamp)
      (rowKey,convert(bamp))
    } catch {
      case t: Throwable => throw t
    } 
  }

  private def convert(map:Map[String,String]):java.util.HashMap[String,String]={
    val it=map.iterator
    val ttMap=new java.util.HashMap[String,String]
    while(it.hasNext){
      val tt=it.next()
      ttMap.put(tt._1, tt._2)
    }
    ttMap
  }
  
  private def convert(map:java.util.HashMap[String,String]):Map[String,String]={
	  val it=map.keySet().iterator()
			val ttMap=Map[String,String]()
				while(it.hasNext){
					val key=it.next()
					val value=map.get(key)
					val cv=(key,value)
					ttMap+=cv
				}
	    ttMap
}
  
  def buildRowKey(map:java.util.HashMap[String,String]):String
}