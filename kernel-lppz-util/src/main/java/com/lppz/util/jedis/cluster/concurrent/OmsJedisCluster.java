package com.lppz.util.jedis.cluster.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.lppz.util.jedis.cluster.JedisSequenceUtil;
public class OmsJedisCluster extends JedisCluster{
	private static final Logger LOG = LoggerFactory.getLogger(OmsJedisCluster.class);
	private int retryNum = 5;
	private int depayMillis = 100;
	@Override
	public String set(String key, String value) {
		int count = 0;
		try {
			return super.set(key, value);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.set(key, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public Long incrBy(String key, long integer) {
		int count = 0;
		try {
			return super.incrBy(key, integer);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.incrBy(key, integer);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public Long hset(String key, String field, String value) {
		int count = 0;
		try {
			return super.hset(key, field, value);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hset(key, field, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public Long hset(JedisModel key, String field, String value) {
		int count = 0;
		try {
			return super.hset(key.buildJedisKey(this), field, value);
		} catch (Exception e) {
			super.srem(JedisSequenceUtil.idxPrefix+JedisSequenceUtil.delimitor+key.getIndexey(), key.build(key.getIndexVal()));
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hset(key.buildJedisKey(this), field, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String[] hget(JedisModel key, String field) {
		int count = 0;
		try {
			String[] kk=key.getJedisKeyFromIdx(this);
			String[] ss=new String[kk.length];
			int i=0;
			for(String k:kk){
				ss[i++]=super.hget(k, field);
			}
			return ss;
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					String[] kk=key.getJedisKeyFromIdx(this);
					String[] ss=new String[kk.length];
					int i=0;
					for(String k:kk){
						ss[i++]=super.hget(k, field);
					}
					return ss;
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public String hget(String key, String field) {
		int count = 0;
		try {
			return super.hget(key, field);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hget(key, field);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		int count = 0;
		try {
			return super.hmset(key, hash);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hmset(key, hash);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String hmset(JedisModel key, Map<String, String> hash) {
		int count = 0;
		try {
			return super.hmset(key.buildJedisKey(this), hash);
		} catch (Exception e) {
			super.srem(JedisSequenceUtil.idxPrefix+JedisSequenceUtil.delimitor+key.getIndexey(), key.build(key.getIndexVal()));
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hmset(key.buildJedisKey(this), hash);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		int count = 0;
		try {
			return super.hmget(key, fields);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hmget(key, fields);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public List<String[]> hmget(JedisModel key, String... fields) {
		int count = 0;
		try {
			String[] kk=key.getJedisKeyFromIdx(this);
			List<String[]> ss=new ArrayList<String[]>(kk.length);
			for(String k:kk){
				ss.add(super.hmget(k, fields).toArray(new String[0]));
			}
			return ss;
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					String[] kk=key.getJedisKeyFromIdx(this);
					List<String[]> ss=new ArrayList<String[]>(kk.length);
					for(String k:kk){
						ss.add(super.hmget(k, fields).toArray(new String[0]));
					}
					return ss;
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public Map<String,String[]> hmgetInxVal(JedisModel key, String... fields) {
		int count = 0;
		try {
			String[] kk=key.getJedisKeyFromIdx(this);
			List<String[]> ss=new ArrayList<String[]>(kk.length);
			Map<String,String[]> m = new HashMap<String, String[]>(kk.length);
			for(String k:kk){
				String[] vals = k.split(JedisSequenceUtil.delimitor);
				m.put(vals[vals.length -1], super.hmget(k, fields).toArray(new String[0]));
				ss.add(super.hmget(k, fields).toArray(new String[0]));
			}
			return m;
		} catch (Exception e) {
			while (count <= retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					String[] kk=key.getJedisKeyFromIdx(this);
					List<String[]> ss=new ArrayList<String[]>(kk.length);
					Map<String,String[]> m = new HashMap<String, String[]>(kk.length);
					for(String k:kk){
						String[] vals = k.split(JedisSequenceUtil.delimitor);
						m.put(vals[vals.length -1], super.hmget(k, fields).toArray(new String[0]));
						ss.add(super.hmget(k, fields).toArray(new String[0]));
					}
					return m;
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		int count = 0;
		try {
			return super.hincrBy(key, field, value);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hincrBy(key, field, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Long hdel(String key, String... field) {
		int count = 0;
		try {
			return super.hdel(key, field);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hdel(key, field);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		int count = 0;
		try {
			return super.hgetAll(key);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hgetAll(key);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public Map<String, String> hgetAll(JedisModel key) {
		int count = 0;
		String k = null;
		try {
			k =key.buildJedisKey(null);
			return super.hgetAll(k);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.hgetAll(k);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	

	@Override
	public List<String> mget(String... keys) {
		int count = 0;
		try {
			return super.mget(keys);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.mget(keys);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String mset(String... keysvalues) {
		int count = 0;
		try {
			return super.mset(keysvalues);
		} catch (Exception e) {
			while (count <= retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.mset(keysvalues);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

//	public OmsJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout,
//			int maxRedirections, GenericObjectPoolConfig poolConfig) {
//		super(jedisClusterNode, timeout, maxRedirections, poolConfig);
//		for(HostAndPort ha:jedisClusterNode){
//			jedisGroup.add(new Jedis(ha.getHost(),ha.getPort()));
//		}
//	}
	public OmsJedisCluster(Set jedisClusterNode, Integer timeout,
			Integer maxRedirections, GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, maxRedirections, poolConfig);
		for(HostAndPort ha:(Set<HostAndPort>)jedisClusterNode){
			jedisGroup.add(new Jedis(ha.getHost(),ha.getPort()));
		}
	}
	
	private List<Jedis> jedisGroup=new ArrayList<Jedis>();

	public List<Jedis> getJedisGroup() {
		return jedisGroup;
	}
	
	public Set<String> keys(final String pattern) {
		Set<String> s = new HashSet<String>();
		for (Jedis j : jedisGroup) {
			Set<String> sj = null;
			try {
				sj = j.keys(pattern);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			if (sj != null)
				s.addAll(sj);
		}
		return s;
	}
	
	public String setex(byte[] key, int seconds, byte[] value){
		int count = 0;
		try {
			return super.setex(key, seconds, value);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.setex(key, seconds, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String setex(String key, int seconds, String value){
		int count = 0;
		try {
			return super.setex(key, seconds, value);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.setex(key, seconds, value);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String get(String key){
		int count = 0;
		try {
			return super.get(key);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.get(key);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error("redis get exception", e);
		}
		return null;
	}
	
	public byte[] get(byte[] key){
		int count = 0;
		try {
			return super.get(key);
		} catch (Exception e) {
			while (count < retryNum) {
				count++;
				try {
					Thread.sleep(depayMillis);
					return super.get(key);
				} catch (Exception e2) {
					LOG.warn("redis操作失败，开始第{}次重试", count + 1);
				}
			}
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
}
