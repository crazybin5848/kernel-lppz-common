package com.lppz.spark.scala.streaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time

trait DoStreaming extends Serializable{
  def doBiz(rdd: RDD[String], time: Time)
}