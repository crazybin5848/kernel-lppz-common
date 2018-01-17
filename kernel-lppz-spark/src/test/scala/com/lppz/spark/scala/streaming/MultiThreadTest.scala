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
import java.util.concurrent.atomic.AtomicInteger
import java.util.UUID
import java.util.ArrayList

object MultiThreadTest {
  def main(args: Array[String]) = {
    val exector= java.util.concurrent.Executors.newFixedThreadPool(100) 
    val inc:AtomicInteger=new AtomicInteger(0)
    class HbaseRun(@BeanProperty val putList:java.util.List[Int]) extends Runnable{
                  def this()=this(new ArrayList[Int]())
                        override def run(){
                           try{
                             val it=putList.iterator()
                             while(it.hasNext()){
                               val x=it.next()
                               println(Thread.currentThread().getId+":"+x)
                             }
                           }finally{
                             inc.addAndGet(putList.size())
                           }
                        }
                      }
    val size=10000
    val ll=new ArrayList[Int]()
    import util.control.Breaks._
    breakable {
    for(i <- 1 to size){
      ll.add(i)
//      if(i%10==0){
//        break
//      }
//      if(i%100==0){
//        val putList:java.util.List[Int]=new ArrayList[Int]
//        putList.addAll(ll)
//        exector.execute(new HbaseRun(putList))
//        ll.clear()
//      }
    }
    throw new Exception("fuck")
    }
//     throw new Exception("fuck")
    println("break")
    while(true){
      if(inc.get==size){
        println("it's over")
        System.exit(0)
      }
       Thread.sleep(10)
    }
}
}

