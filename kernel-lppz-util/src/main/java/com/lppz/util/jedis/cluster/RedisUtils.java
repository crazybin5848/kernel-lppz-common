package com.lppz.util.jedis.cluster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.set.SetParams;

import com.lppz.constant.RedisConstants;

public class RedisUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	/**
	 * 判断key是否存在
	 * 如果存在，返回true，并移除key；
	 * 否则返回false
	 * @param key
	 * @return
	 */
	public static Boolean isExistDel(JedisCluster jedisCluster,String key){
		if(jedisCluster != null){
			Long result = (Long)jedisCluster.eval(RedisConstants.KEY_EXIST_AND_DEL_LUA_SCRIPT,1, key);
			if (result == 1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return null;
	}
	
	/**
	 * 上锁
	 * @param jedisCluster
	 * @param lockKey
	 * @return
	 */
	public static Boolean lockOff(JedisCluster jedisCluster,String lockKey, String lockValue){
		if(jedisCluster != null){
			lockValue = getOrderLockValue(lockValue);
			Long result = jedisCluster.setnx(lockKey, lockValue);
//			Long result = (Long)jedisCluster.eval(RedisConstants.LOCK_OFF_LUA_SCRIPT,1, lockKey, lockValue);
			if (result == 1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return null;
	}
	
	/**
	 * 解锁 
	 * @param jedisCluster
	 * @param lockKey
	 * @return
	 */
	public static Boolean unlock(JedisCluster jedisCluster,String lockKey){
		//TODO key不存在是否返回成功？
		if(jedisCluster != null){
			Long result = jedisCluster.del(lockKey);
			if (result == 1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return null;
	}
	
	/**
	 * 设置有超时时间的key-value
	 * @param jedisCluster
	 * @param key
	 * @param value
	 * @param expireSeconds
	 * @return
	 */
	public static void setKeyExpire(JedisCluster jedisCluster,String key, String value, long expireSeconds){
		if (jedisCluster != null) {
			jedisCluster.eval(RedisConstants.SET_EXPIRE_LUA_SCRIPT, 1, key,value,String.valueOf(expireSeconds));
		}
	}

	/**
	 * 批量设置系统锁
	 * 后续节点可以覆盖之前的锁
	 * @param jedisCluster
	 * @param keys
	 */
	public static void systemLockOffs(JedisCluster jedisCluster, Map<String,String> locks) {
		if (jedisCluster != null && locks != null) {
			Set<Entry<String, String>> set = locks.entrySet();
			for (Entry<String, String> entry : set) {
				jedisCluster.set(String.format(RedisConstants.ORDER_LOCK_FORMAT, entry.getKey()), entry.getValue());
			}
		}
	}
	
	public static void unSystemLocks(JedisCluster jedisCluster, Set<String> locks){
		if (jedisCluster != null && locks != null) {
			for (String entry : locks) {
				jedisCluster.del(entry);
			}
		}
	}
	
	public static Boolean existKey(JedisCluster jedisCluster,String key){
		if(jedisCluster != null){
			//由于jedis的exists存在性能问题（检查过期时间），使用get方法判断key是否存在
			String value = jedisCluster.get(key);
			if(StringUtils.isBlank(value)){
				return Boolean.FALSE;
			}else{
				return Boolean.TRUE;
			}
		}
		return null;
	}
	
	/**
	 * 获取一定范围内的自增值
	 * @param jedisCluster
	 * @param key
	 * @param max
	 * @param min
	 * @return
	 */
	public static Long getIncKeyWithRange(JedisCluster jedisCluster,String key, long min, long max){
		if(jedisCluster != null){
			List<String> params = new ArrayList<>();
			params.add(String.valueOf(min));
			params.add(String.valueOf(max));
			return Long.valueOf((String)jedisCluster.eval(RedisConstants.RANGE_INCR_LUA_SCRIPT, Arrays.asList(key), params));
		}
		return null;
	}

	public static Long getIncKeyWithRangeGreaterThenMinValue(
			JedisCluster jedisCluster, String key, long minValue) {
		if(jedisCluster != null){
			return Long.valueOf((String)jedisCluster.eval(RedisConstants.GREATER_INCR_LUA_SCRIPT, Arrays.asList(key), Arrays.asList(String.valueOf(minValue))));
		}
		return null;
	}
	
	

	public static boolean checkLock(JedisCluster jedisCluster, String key, String topic){
		if(checkJedisExists(jedisCluster)){
			try {
				Long result = jedisCluster.setnx(key, getOrderLockValue(topic));
				if(result == 1){
					return true;
				}
			} catch (Exception e) {
				logger.error("检查redis锁异常", e);
				throw e;
			}
		}else{
			throw new RuntimeException("jedisCluster is null");
		}
		return false;
	}
	
	public static boolean checkJedisExists(JedisCluster jedisCluster){
		if (jedisCluster == null) {
			logger.warn("jedisCluster is null ");
			return false;
		}
		return true;
	}
	
	public static String getOrderLockKey(String key){
		return String.format(RedisConstants.ORDER_LOCK_FORMAT, key);
	}
	
	public static String getOrderLockValue(String value){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY:MM:dd HH:mm:ss");
		return value + "/" + sdf.format(new Date());
	}

	public static Boolean lockOffExprie(JedisCluster jedisCluster,
			String orderid, String value, int expireSeconds) {
		String result = jedisCluster.set(getOrderLockKey(orderid), value, SetParams.setParams().ex(expireSeconds).nx());
		if("OK".equals(result)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
