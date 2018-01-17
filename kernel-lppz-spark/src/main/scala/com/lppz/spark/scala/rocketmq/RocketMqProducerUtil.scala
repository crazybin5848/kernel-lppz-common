package com.lppz.spark.scala.rocketmq

import org.apache.log4j.Logger
import com.lppz.spark.rocketmq.SparkExportRocketMqProducer

class RocketMqProducerUtil extends Serializable{
object instance extends Serializable {
  @transient private var producer: SparkExportRocketMqProducer = null
  @transient lazy val LOG=Logger.getLogger(getClass.getName)

  def buildRocketMqProducer(nameAddr: String):Unit = {
    if(producer==null){
      producer = new SparkExportRocketMqProducer(nameAddr)
       val hook = new Thread {
          override def run = {
        		  producer.close()
          }
        }
        sys.addShutdownHook(hook.run)
    }
   }
  
  def getProducer: SparkExportRocketMqProducer = {
    assert(producer != null)
    producer
  }
}
}