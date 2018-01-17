package com.lppz.spark.scala.rocketmq

import com.lppz.spark.bean.SparkExportDmlBean
import scala.beans.BeanProperty
import com.lppz.spark.scala.HiveContextUtil
import org.apache.spark.sql.DataFrame
import scala.util.control.Breaks
import org.apache.spark.SparkContext
import org.apache.log4j.Logger

class SparkExportRocketMqBean (@BeanProperty var sqlBean: SparkExportDmlBean) extends Serializable{
  @transient lazy val LOG=Logger.getLogger(getClass.getName)
  def this() = this(null)

  def buildSparkSqlRdd(sc: SparkContext,sqlStr: String): DataFrame = {
    HiveContextUtil.exec(sc, "use " + sqlBean.getSchema())
    HiveContextUtil.getRDD(sc, sqlStr)
  }

  def loopExec(sc: SparkContext,nameAddr:String, jedisClusterYamlPath:String, month:String) {
    var i: Long = 0
    val loop = new Breaks;
    loop.breakable {
      while (true) {
        val endNo = i + sqlBean.getTotal4Once;
        val sqlStr = sqlBean.getSql.replace("#month#", month).replace("#start#", String.valueOf(i))
          .replace("#end#", String.valueOf(endNo))
        val df = buildSparkSqlRdd(sc,sqlStr)
        LOG.debug("---------" + df + "   " + df.rdd + " " + df.rdd.isEmpty());
        if (df != null && df.rdd != null && (!df.rdd.isEmpty())) {
          object InternalKafkaCli extends SparkRocketMqHandler {}
          InternalKafkaCli.instance.send2RocketMq(df, nameAddr, jedisClusterYamlPath, sqlBean.getPartionNum, sqlBean.getEsIndex, sqlBean.getEsType, sqlBean.getDateColumnNames)
           LOG.info("rocketmq has send msgs bwtween "+i+","+endNo)
           i = endNo
        } else {
          loop.break
        }
//         loop.break//TODO test once
      }
    }
  }
}