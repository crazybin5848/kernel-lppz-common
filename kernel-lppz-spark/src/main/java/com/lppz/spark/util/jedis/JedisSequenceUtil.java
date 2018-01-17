package com.lppz.spark.util.jedis;

import redis.clients.jedis.Jedis;
public class JedisSequenceUtil {
	private final static String SEQPREFIX = "seq";
	public static final String delimitor=":::";
	public static final String idxPrefix="IDX";
	public static final String tbPrefix="DATA";

	private final static String SEQINDEX = "seq:::index";
	private Jedis jedis;
	private static JedisSequenceUtil instance=new JedisSequenceUtil();
	private JedisSequenceUtil(){}
	public static JedisSequenceUtil getInstance(){
		return instance;
	}

	private String buildKey(String key) {
		return new StringBuilder(SEQPREFIX).append(delimitor).append(key).toString();
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	public long incrSequence(String seqname, Long... step) throws Exception {
		return jedis==null?-1l:jedis.hincrBy(SEQINDEX,buildKey(seqname),
					step == null || step.length == 0 ? 1 : step[0]);
	}
	public Jedis getJedis() {
		return jedis;
	}
}