package com.lppz.util.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import com.lppz.util.jedis.cluster.JedisSequenceUtil;

public abstract class RedisQueueCache<T> extends BaseQueueCache<T> {
	private Map<T, String> srckey;
	private Map<T, String> dstkey;

	@Override
	public List<String> takeCache(int maxElements, T... t) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < maxElements; i++) {
			String str = JedisSequenceUtil.getInstance().getJedis().rpoplpush(generateSrckey(t), generateDstkey(t));
			if (str==null||str.length()==0)
				break;
			list.add(str);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushCache(List<T> params) {
		JedisSequenceUtil.getInstance().getJedis().lpush(srckey.get(params.get(0)), generateVal((T[]) params.toArray()));
	}

	@Override
	public int size(T... t) {
		return JedisSequenceUtil.getInstance().getJedis().llen(generateSrckey(t)).intValue();
	}

	@Override
	public void removeBak(String value, T... t) {
		JedisSequenceUtil.getInstance().getJedis().lrem(generateDstkey(t), 1, value);
	}

	@Override
	public void initQueue(T... t) {
		for (T tempT : t) {
			srckey.put(tempT, generateSrckey(tempT));
			srckey.put(tempT, generateDstkey(tempT));
		}
	}

	private String generateSrckey(T... t) {
		return t == null ? srckey.values().toArray(new String[0])[0] : srckey.get(t[0]);

	}

	private String generateDstkey(T... t) {
		return t == null ? dstkey.values().toArray(new String[0])[0] : dstkey.get(t[0]);

	}
	protected abstract String generateSrckey(T t);

	protected abstract String generateDstkey(T t);

}
