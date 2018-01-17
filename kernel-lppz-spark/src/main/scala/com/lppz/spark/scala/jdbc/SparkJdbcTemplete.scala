package com.lppz.spark.scala.jdbc

import org.apache.log4j.Logger
import com.lppz.spark.util.SparkJdbcUtils
import com.lppz.spark.bean.jdbc.JdbcDBTemplate
import com.lppz.core.datasource.LppzBasicDataSource
import java.util.ArrayList

class SparkJdbcTemplete extends Serializable{
  @transient lazy val LOG=Logger.getLogger(getClass.getName)
  @transient private var st:JdbcDBTemplate=null
  @transient private var util:SparkJdbcUtils=null
  
  object instance extends Serializable {
    def buildJdbcTemplete(dataSourcePath:String){
      util=new SparkJdbcUtils()
      st=util.initDsAndStormTemplate(dataSourcePath)
    }
    
    def getSt()={
      assert(st != null)
      st
    }
    
    def mulitExec(list:ArrayList[String],batchSize:Integer){
      assert(st != null)
      util.mulitThreadExec(list, st, batchSize)
    }
  }
}