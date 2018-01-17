package com.lppz.spark.scala.hbase

import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory

class HbaseConnPool extends Serializable{
object instance extends Serializable {
  @transient private var hconn:Connection = null

  def buildHbaseTableConn(hbaseQuorum: String, hbasePort: String):Unit = {
    if(hconn==null){
      val myConf = HBaseConfiguration.create()
        myConf.set("hbase.zookeeper.quorum", hbaseQuorum)
        myConf.set("hbase.zookeeper.property.clientPort", hbasePort)
        myConf.set("hbase.defaults.for.version.skip", "true")
        myConf.set("zookeeper.znode.parent", "/hbase")
        hconn = ConnectionFactory.createConnection(myConf)
//        val hook = new Thread {
//          override def run = 
//            if (hconn != null) 
//            hconn.close()
//        }
//        sys.addShutdownHook(hook.run)
    }
   }
  def getConn: Connection = {
    assert(hconn != null)
    hconn
  }
}
}