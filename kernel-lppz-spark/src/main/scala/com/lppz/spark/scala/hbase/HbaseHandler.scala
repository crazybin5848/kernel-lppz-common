package com.lppz.spark.scala.hbase

import scala.collection.mutable.Map
import scala.collection.mutable.Set
import org.apache.commons.collections.CollectionUtils
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.client.Durability
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.SparkException
import org.apache.spark.sql.DataFrame
import com.lppz.spark.scala.jedis.JedisClientUtil
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger
import scala.beans.BeanProperty
import java.util.HashSet

class HbaseHandler extends Serializable {
  @transient lazy val LOG = Logger.getLogger(getClass.getName)
  import com.lppz.spark.scala.hbase.HbaseAbstractSparkConv
  import com.lppz.spark.scala.hbase.HbaseConnPool
  import com.lppz.spark.scala.hbase.HbaseSparkConvertor

  object instance extends Serializable {
    def createHBaseRecord(colfimaly: String, rowKey: String, values: Map[String, String], tablebname: String): (ImmutableBytesWritable, Put) = {
      val record = buildHbaseRc(colfimaly, rowKey, values)
      (new ImmutableBytesWritable(Bytes.toBytes(tablebname)), record)
    }

    private def buildHbaseRc(colfimaly: String, rowKey: String, values: Map[String, String]): Put = {
      val record = new Put(Bytes.toBytes(rowKey))
      val it = values.iterator
      while (it.hasNext) {
        val vls = it.next()
        try {
          val colname = vls._1.toString
          val colvalue = vls._2.toString
          record.add(Bytes.toBytes(colfimaly), Bytes.toBytes(colname), Bytes.toBytes(colvalue))
        } catch {
          case e: Exception => {
            val strMsg: StringBuilder = new StringBuilder(e.toString())
            strMsg.append(",rowkey:" + rowKey + ",colfimaly:" + colfimaly + "colname:" + vls._1.toString)
            LOG.error(strMsg.toString(), e)
            throw new SparkException(strMsg.toString(), e)
          }
        }
      }
      record
    }

    def buildSparkHBaseConf(sc: SparkContext, hbaseQuorum: String, hbasePort: String, tablebname: String) {
      sc.hadoopConfiguration.set("hbase.zookeeper.quorum", hbaseQuorum)
      sc.hadoopConfiguration.set("zookeeper.znode.parent", "/hbase")
      sc.hadoopConfiguration.set("hbase.zookeeper.property.clientPort", hbasePort);
      sc.hadoopConfiguration.setLong("hbase.rpc.timeout", 60000000);
      sc.hadoopConfiguration.setInt("hbase.client.scanner.timeout.period", 60000000);
      sc.hadoopConfiguration.setLong("hbase.client.scanner.caching", 30);
      sc.hadoopConfiguration.set(TableOutputFormat.OUTPUT_TABLE, tablebname)
      var job = new Job(sc.hadoopConfiguration)
      job.setOutputKeyClass(classOf[ImmutableBytesWritable])
      job.setOutputValueClass(classOf[Result])
      job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
      job.getConfiguration
      //    rdd1.map(
      //      x => {
      //        var put = new Put(Bytes.toBytes(x._1))
      //        put.add(Bytes.toBytes("f1"), Bytes.toBytes("c1"), Bytes.toBytes(x._2))
      //        (new ImmutableBytesWritable,put)
      //      }    
      //    ).saveAsNewAPIHadoopDataset(job.getConfiguration)
      //    sc.stop()   
    }

    def buildColArray(orginColumnArray: Array[String], excludeColumns: String): Array[String] = {
      val excludeCol: Array[String] = excludeColumns.split(",")
      val columnArray: Array[String] = new Array[String](orginColumnArray.length - excludeCol.length)
      var i = 0
      val exCludeset = Set[String]()
      excludeCol.foreach { ex => exCludeset += ex }
      orginColumnArray.foreach { x =>
        {
          if (!exCludeset.contains(x)) {
            columnArray.update(i, x)
            i += 1
          }
        }
      }
      columnArray
    }

    def saveRddRow2HBase(df: DataFrame, excludeColumns: String, convertor: HbaseSparkConvertor, hbaseQuorum: String, hbasePort: String, tablename: String, cf: String, partionNum: Integer, isWal: Boolean, jedisClusterYamlPath: String, jedisSentienlYamlPath: String, batchNum: Integer, transCoder: (java.util.HashMap[String, String], java.util.Map[String, String]) => java.util.HashMap[String, String]) {
      val rdd = df.rdd
      val colArray = buildColArray(df.columns, excludeColumns)
      rdd/*.repartition(partionNum)*/.foreachPartition { riter =>
        {
          if (riter != null && (!riter.isEmpty)) {
            var htable: HTable = null
            var hconn: Connection = null
            try {
              object InternalHbaseClient extends HbaseConnPool {
              }
              InternalHbaseClient.instance.buildHbaseTableConn(hbaseQuorum, hbasePort)
              hconn = InternalHbaseClient.instance.getConn
              val putList: java.util.List[Put] = new java.util.ArrayList[Put]
              var i = 1
              htable = hconn.getTable(TableName.valueOf(tablename)).asInstanceOf[HTable]
              htable.setAutoFlush(false, false)
              htable.setWriteBufferSize(8 * 1024 * 1024)
              object InternalRedisClient extends JedisClientUtil {
              }
              InternalRedisClient.instance.makeJedisCluster(jedisClusterYamlPath)
              InternalRedisClient.instance.makeJedis(jedisSentienlYamlPath)
              val jedisCluster = InternalRedisClient.instance.getJedisCluster
              val jedis = InternalRedisClient.instance.getJedisProxy
//              val len = riter.length
//              val exector = java.util.concurrent.Executors.newFixedThreadPool(if(len/batchNum>=20) 20 else len/batchNum)
//              val inc: AtomicInteger = new AtomicInteger(0)
              val kTable=HbaseSparkConvertor.doTransferTableKey
              val conv: HbaseAbstractSparkConv = convertor.asInstanceOf[HbaseAbstractSparkConv]
              conv.setJedisCluster(jedisCluster)
              conv.setJedis(jedis)
              val paramMap=conv.getParamMap()
              val ss=paramMap.get(kTable)
              val m=new HashSet[String]
              val btctb=tablename+":"+cf
              if(ss!=null){
                ss.split(",").foreach { x => m.add(x)}
              }
              val needTransfer=m.contains(btctb)
              riter.foreach {
                r =>
                  {
                    val xx = conv.convert(r, colArray,if(needTransfer) transCoder else null)
                    if (xx._1 != null) {
                      val record = buildHbaseRc(cf, xx._1, xx._2)
                      record.setDurability(Durability.ASYNC_WAL)
                      putList.add(record)
                    }
                    if (i % batchNum == 0) {
//                      class HbaseRun(@BeanProperty val pp: java.util.List[Put]) extends Runnable {
//                        override def run() {
//                          try {
//                            htable.put(pp)
//                            htable.flushCommits()
//                          } catch {
//                            case e: Exception => {
//                              LOG.error(e.getMessage, e)
//                              import util.control.Breaks._
//                              breakable {
//                                for (i <- 1 to 5) {
//                                  try {
//                                    htable.put(pp)
//                                    htable.flushCommits()
//                                    break
//                                  } catch {
//                                    case e: Exception => {
//                                      LOG.warn("retry fail,continue retry")
//                                    }
//                                  }
//                                }
//                                throw new SparkException(e.getMessage, e)
//                              }
//                            }
//                          } finally {
//                            inc.addAndGet(pp.size())
//                          }
//                        }
//                      }
//                      val pp: java.util.List[Put] = new ArrayList[Put]
//                      pp.addAll(putList)
//                      exector.execute(new HbaseRun(pp))
//                      putList.clear()
                      htable.put(putList)
                      htable.flushCommits()
                      putList.clear()
                    }
                    i += 1
                  }
              }
              if (CollectionUtils.isNotEmpty(putList)) {
                try {
                  htable.put(putList);
                } finally {
//                  inc.addAndGet(putList.size())
                }
                putList.clear();
              }
              htable.flushCommits()
//              import util.control.Breaks._
//              breakable {
//                while (true) {
//                  if (inc.get == len) {
//                    LOG.info("this partition put:" + len + " has merged to hbase!!!")
//                    exector.shutdown()
//                    break
//                  }
//                  Thread.sleep(10)
//                }
//              }
            } catch {
              case e: Exception => {
                LOG.error(e.getMessage, e)
                throw new SparkException(e.getMessage, e)
              }
            } finally {
              if (htable != null)
                htable.close()
              if (hconn != null)
                hconn.close()
            }
          }
        }
      }
    }
  }
}