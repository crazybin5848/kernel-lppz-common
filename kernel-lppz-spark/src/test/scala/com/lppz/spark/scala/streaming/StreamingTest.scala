package com.lppz.spark.scala.streaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time

import com.lppz.spark.scala.jdbc.MysqlSpark
import java.sql.DriverManager
import com.alibaba.fastjson.JSON
import scala.beans.BeanProperty
import org.apache.spark.sql.types.StructType
import com.alibaba.fastjson.JSONObject
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.Row

object StreamingTest {
  def main(args: Array[String]) = {
    val sc = new MysqlSpark().buildSc("streamingTask", "local[8]")
    sc.setLogLevel("ERROR")
    val seconds=10
    val zkServer="hanode1:2181,hanode2:2181,hanode3:2181,hanode4:2181,hanode5:2181"
    val consumerGroup="consumeStreaming"
    val topicMap=Map[String, Int]("fuckStreaming" -> 0,"fuckStreaming" -> 1,"fuckStreaming" -> 2,"fuckStreaming" -> 3,"fuckStreaming" -> 4)
    val d = new BaseStreamingHandler(
      Array(new StreamHandlerVo("DapLog",(obj)=>{
        val day:String=obj.get("day").asInstanceOf[String]
         val ip:String=obj.get("ip").asInstanceOf[String]
         val cookieid:String=obj.get("cookieid").asInstanceOf[String]
        Row(day,ip,cookieid)
      },()=>{StructType(  
        Seq(  
        StructField("day",StringType,true),            
        StructField("ip",StringType,true),            
        StructField("cookieid",StringType,true)       
      ))})),
      "select count(1),ip from DapLog group by ip",
      (df) => {
        df.show()
      }
      )
    StreamingUtil.make(sc, seconds, zkServer, consumerGroup, topicMap, d)
}
}

case class DapLog(@BeanProperty val day:String,@BeanProperty val ip:String,@BeanProperty val cookieid:String)