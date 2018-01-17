package com.lppz.constant;

public class RedisConstants {
	public static final String ORDER_LOCK_FORMAT = "O_LCK::%s";
	
	public static final String ORDER_SEND_LOCK_FORMAT = "O_SEND_LCK::%s::%s";
	
	public static final String LPPZBATCHOMS_GENERATESEQUENCENO = "LPPZBATCHOMS:::GENERATESEQUENCENO";
	
	public static final String DEFAULT_VALUE = "DEFAULT_VALUE";
	
	public static final long DEFAULT_MIN_INTR_VALUE = 20000000000000L;
	
	public static final String EXPIRE_SECOND_KEY = "ExpireSecond";
	
	/**
	 * 检测key是否存在脚本，如果对应key存在，删除key返回1，否则返回0
	 */
	public static final String KEY_EXIST_AND_DEL_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "redis.call('del',KEYS[1]) "
			+ "end "
			+ "return res";
	
	/**
	 * 上锁脚本，如果key不存在，设置key，返回1；否则返回0
	 */
	public static final String LOCK_OFF_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "return 0 "
			+ "else "
			+ "redis.call('set',KEYS[1],ARGV[1]) "
			+ "return 1 "
			+ "end";
	
	/**
	 * 如果键不存在，设置键值，然后设置超时时间
	 */
	public static final String SET_EXPIRE_LUA_SCRIPT = "local res = redis.call('setnx',KEYS[1], ARGV[1]) "
			+ "redis.call('expire',KEYS[1] , ARGV[2])";
	
	/**
	 * 解除系统锁，
	 * 如果传入的键存在且值相同，删除锁并返回1
	 * 如果间存在但值不同，说明其他节点更新了系统锁，返回2
	 * 否则返回0
	 */
	public static final String UN_LOCK_SYSTEM_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "local val = redis.call('get',KEYS[1]) "
			+ "if(val==ARGV[1]) then "
			+ "redis.call('del',KEYS[1]) "
			+ "return 1 "
			+ "end "
			+ "return 2 "
			+ "end "
			+ "return 0";
	
	/**
	 * 系统锁定
	 * 如果用户没有认领，设置系统锁（覆盖已有系统锁），返回成功
	 * 否则返回失败
	 */
	public static final String LOCK_SYSTEM_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "return 0 "
			+ "end "
			+ "redis.call('set',KEYS[2],ARGV[1]) "
			+ "return 1 ";
	
	/**
	 * 人工锁定
	 * 如果没有人认领，并且没有系统锁，设置人工锁，返回成功
	 * 否则返回失败
	 */
	public static final String LOCK_USER_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "return 0 "
			+ "else "
			+ "res = redis.call('get',KEYS[2]) "
			+ "if res then "
			+ "return 0 "
			+ "else "
			+ "redis.call('set',KEYS[1],ARGV[1]) "
			+ "return 1 "
			+ "end "
			+ "end ";
	
	/**
	 * 如果key不存在，添加值为最小值（第一个参数）的key，否则获取key当前值，如果值小于最大值（第二个参数），值加1 否则返回-1，最后返回key值
	 */
	public static final String RANGE_INCR_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "local val = redis.call('get',KEYS[1]) "
			+ "local sub = ARGV[2]-val "
			+ "if sub > 0 then "
			+ "redis.call('incr',KEYS[1]) "
			+ "else "
			+ "return '-1' "
			+ "end "
			+ "else "
			+ "redis.call('set',KEYS[1],ARGV[1]) "
			+ "end "
			+ "return redis.call('get',KEYS[1])";
	
	/**
	 * 如果key不存在，添加值为最小值的key并返回，否则获取值加1
	 */
	public static final String GREATER_INCR_LUA_SCRIPT = "local res = redis.call('get',KEYS[1]) "
			+ "if res then "
			+ "redis.call('incr',KEYS[1]) "
			+ "else "
			+ "redis.call('set',KEYS[1],ARGV[1]) "
			+ "end "
			+ "return redis.call('get',KEYS[1])";

}
