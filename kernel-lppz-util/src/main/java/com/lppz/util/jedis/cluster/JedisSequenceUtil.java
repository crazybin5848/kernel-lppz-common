package com.lppz.util.jedis.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import redis.clients.jedis.Jedis;
public class JedisSequenceUtil {
	private final static String SEQPREFIX = "seq";
	public static final String delimitor=":::";
	public static final String idxPrefix="IDX";
	public static final String tbPrefix="DATA";
	public static final Integer retryCount=5;
	private static final Logger LOG = LoggerFactory.getLogger(JedisSequenceUtil.class);


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
	public Long incrSequence(String seqname, Long... step) throws Exception {
		long num=0l;
		int count=0;
		try {
			num=jedis==null?-1l:jedis.hincrBy(SEQINDEX,buildKey(seqname),
					step == null || step.length == 0 ? 1 : step[0].longValue());
			return num;
		} catch (Exception e) {
			while (count < retryCount) {
				count++;
				try {
					Thread.sleep(100);
					num=jedis==null?-1l:jedis.hincrBy(SEQINDEX,buildKey(seqname),
							step == null || step.length == 0 ? 1 : step[0].longValue());
					return num;
				} catch (Exception e2) {
					LOG.warn("redisIncry操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public Jedis getJedis() {
		return jedis;
	}
}