package com.lppz.spark.scala.streaming

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import com.alibaba.fastjson.JSONObject
import scala.beans.BeanProperty

class StreamHandlerVo(@BeanProperty val tableName:String,val buildRow :(JSONObject) => Row,val buildSchema :() => StructType) extends Serializable{
//  def buildRow(obj:JSONObject):Row
//  def buildSchema():StructType
}