package com.lppz.spark.scala.jedis

import org.springframework.core.io.FileSystemResource

import com.lppz.spark.util.jedis.SparkJedisCluster
import com.lppz.spark.util.jedis.SparkJedisClusterUtil

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisCluster

class JedisClientUtil extends Serializable {
  object instance extends Serializable {
    @transient private var jedis: Jedis = null
    @transient private var jedisCluster: SparkJedisCluster = null
    def makeJedis(jedisYamlPath: String): Unit = {
      if (jedis == null) {
        jedis = SparkJedisClusterUtil.buildJedisSentinelProxy(new FileSystemResource(jedisYamlPath).getInputStream())
//        val hook = new Thread {
//          override def run = jedis.close()
//        }
//        sys.addShutdownHook(hook.run)
      }
    }

    def makeJedisCluster(jedisClusterYamlPath: String): Unit = {
      if (jedisCluster == null) {
        jedisCluster = SparkJedisClusterUtil.getJedisCluster(new FileSystemResource(jedisClusterYamlPath).getInputStream())
        val hook = new Thread {
          override def run = jedisCluster.close()
        }
        sys.addShutdownHook(hook.run)
      }
    }
    def getJedisProxy: Jedis = {
      assert(jedis != null)
      jedis
    }
    def getJedisCluster: SparkJedisCluster = {
      assert(jedisCluster != null)
      jedisCluster
    }
  }
}