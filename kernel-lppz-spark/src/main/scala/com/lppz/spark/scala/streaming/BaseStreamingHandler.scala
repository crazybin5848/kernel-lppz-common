package com.lppz.spark.scala.streaming

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.Time
import org.apache.spark.sql.DataFrame
import com.alibaba.fastjson.JSON
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.Row
import java.util.TreeSet
import com.alibaba.fastjson.JSONObject
import java.util.TreeMap

class BaseStreamingHandler(streamVoArray:Array[StreamHandlerVo],sql:String,handle : (DataFrame) => Unit) extends DoStreaming{
  def doBiz(rdd: RDD[String], time: Time){
     val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
     val sparkSession=sqlContext.sparkSession
      import sparkSession.implicits._
      rdd.cache()
      streamVoArray.foreach { vo:StreamHandlerVo => 
           val rowRDD = rdd.map { x => 
             val obj:JSONObject=JSON.parseObject(x)
             vo.buildRow(obj)
           }
         val schema = vo.buildSchema()
         val logDataFrame = sqlContext.sparkSession.createDataFrame(rowRDD,schema)
         logDataFrame.registerTempTable(vo.getTableName())
     }
      try {
        val logCountsDataFrame = sqlContext.sql(sql)
        handle(logCountsDataFrame)
      } catch {
        case t: Throwable => t.printStackTrace() 
      }
     }
}

 object SQLContextSingleton {
  @transient  private var instance: SQLContext = _
  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}