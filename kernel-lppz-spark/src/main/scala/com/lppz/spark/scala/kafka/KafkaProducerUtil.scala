package com.lppz.spark.scala.kafka

import com.lppz.spark.kafka.SparkExportKafkaProducerConfiguration
import com.lppz.spark.kafka.SparkExportProducer
import org.apache.log4j.Logger
import com.lppz.spark.kafka.SparkExportStringProducer

class KafkaProducerUtil extends Serializable{
object instance extends Serializable {
  @transient private var producer: SparkExportProducer = null
  @transient private var producerString: SparkExportStringProducer = null
  @transient lazy val LOG=Logger.getLogger(getClass.getName)

  def buildKafkaProducer(kafkaBrokerPath: String):Unit = {
    if(producer==null){
      producer = SparkExportKafkaProducerConfiguration.createProducer(kafkaBrokerPath)
       val hook = new Thread {
          override def run = {
            producer.destroy()
        		  producer.close()
          }
        }
        sys.addShutdownHook(hook.run)
    }
   }
  
  def buildStringKafkaProducer(kafkaBrokerPath: String):Unit = {
    if(producerString==null){
      producerString = SparkExportKafkaProducerConfiguration.createStringProducer(kafkaBrokerPath)
       val hook = new Thread {
          override def run = {
            producerString.destroy()
        		  producerString.close()
          }
        }
        sys.addShutdownHook(hook.run)
    }
   }
  def getProducer: SparkExportProducer = {
    assert(producer != null)
    producer
  }
  
  def getStringProducer: SparkExportStringProducer = {
    assert(producerString != null)
    producerString
  }
}
}