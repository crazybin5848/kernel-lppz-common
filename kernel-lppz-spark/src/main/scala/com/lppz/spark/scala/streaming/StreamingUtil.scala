package com.lppz.spark.scala.streaming

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Time
import org.apache.spark.streaming.kafka.KafkaUtils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

/**
 * @author zoubin
 */
object StreamingUtil {
  def make(sc:SparkContext, seconds:Long,zkServer:String,consumerGroup:String,topicMap:Map[String, Int],d:DoStreaming) {
      val ssc = new StreamingContext(sc, Seconds(seconds))
       val kafkaStream = KafkaUtils.createStream(
          ssc, 
          zkServer, 
          consumerGroup, 
          topicMap, 
          StorageLevel.MEMORY_AND_DISK_SER)
      .map(x => x._2)
     kafkaStream.foreachRDD((rdd: RDD[String], time: Time)=>{
          d.doBiz(rdd,time)
      })
     ssc.start()
     ssc.awaitTermination()
  }
}