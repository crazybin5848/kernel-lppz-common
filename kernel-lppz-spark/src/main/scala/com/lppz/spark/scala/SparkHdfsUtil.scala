package com.lppz.spark.scala
  import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import org.apache.hadoop.fs.Path._
import java.net.URI
/**
 * @author zoubin
 */
class SparkHdfsUtil {
/*
 * 需求：用scala操作hdfs，但是又不想用RDD。
 * http://bleibinha.us/blog/2013/09/accessing-the-hadoop-distributed-filesystem-hdfs-with-scala
 * http://www.linuxidc.com/Linux/2014-04/100545.htm HDFS——如何列出目录下的所有文件
 */
    def mvFromLocal2Hdfs(HDFS:String,src:Array[Path],dest:Path) {
      val conf = new Configuration()
      //val hdfsCoreSitePath = new Path("core-site.xml")
     // val hdfsHDFSSitePath = new Path("hdfs-site.xml")     
      //conf.addResource(hdfsCoreSitePath)
      //conf.addResource(hdfsHDFSSitePath)
      println(conf)//Configuration: core-default.xml, core-site.xml
      //根据这个输出,在这个程序进来之前,conf已经被设置过了
      
      //目前我知道,定位具体的hdfs的位置,有两种方式
      //一种是在conf配置,一个域名可以绑定多个ip.我们通过这个域名来定位hdfs.
      //另一种是在调用FileSystem.get时指定一个域名或者一个ip,当然仅限一个.
      
//      val fileSystem = FileSystem.get(conf)
//      val HDFS="hdfs://hamaster:9000";
      val fileSystem = getFileSystem(HDFS)
      //如果conf设置了hdfs的host和port,此处可以不写
      //hadoop的配置都是一层一层的,后面的会覆盖前面的.
      
      //String HDFS="hdfs://localhost:9000";
      //FileSystem hdfs = FileSystem.get(URI.create(HDFS),conf);
      //这种写法 只能用一个ip或者域名了.不推荐.
     fileSystem.moveFromLocalFile(src, dest)      
     fileSystem.close()
    }
    
    def getFileSystem(HDFS:String) = {
    	val conf = new Configuration()
    	FileSystem.get(URI.create(HDFS),conf);
    }
}