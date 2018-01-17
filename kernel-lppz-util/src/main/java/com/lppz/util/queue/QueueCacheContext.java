package com.lppz.util.queue;

import java.util.List;

import com.lppz.util.jedis.cluster.JedisSequenceUtil;

public abstract class QueueCacheContext<T> implements QueueCache<T> {

	private BlockingQueueCache<T> blockingQueueCache;
	private RedisQueueCache<T> redisQueueCache;
	private BaseQueueCache<T> baseQueueCache;

	@Override
	public List<String> takeCache(int maxElements, T... ts) {
		//从不同队列取值
		return blockingQueueCache.size() > 0 ? blockingQueueCache.takeCache(maxElements, ts) : redisQueueCache.takeCache(maxElements, ts);
	}

	@Override
	public void pushCache(final List<T> strs) {
		baseQueueCache.pushCache(strs);
	}

	public void removeBak(String value, T... t) {
		baseQueueCache.removeBak(value, t);
	}

	public int size(T... ts) {
		return baseQueueCache.size();
	}

	public void initQueue(T... t) {
		blockingQueueCache.initQueue(t);
		redisQueueCache.initQueue(t);
	}

	public void setBlockingQueueCache(BlockingQueueCache<T> blockingQueueCache) {
		this.blockingQueueCache = blockingQueueCache;
	}

	public void setRedisQueueCache(RedisQueueCache<T> redisQueueCache) {
		this.redisQueueCache = redisQueueCache;
	}

	public void setBaseQueueCache(BaseQueueCache<T> baseQueueCache) {
		this.baseQueueCache = baseQueueCache;
	}

	
}
