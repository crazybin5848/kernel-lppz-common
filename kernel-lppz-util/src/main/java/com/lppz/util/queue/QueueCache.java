package com.lppz.util.queue;

import java.util.List;

public interface QueueCache<T> {
	public List<String> takeCache(int maxElements, T... t);

	public void pushCache(final List<T> params);

	public void removeBak(String value, T... t);

	public int size(T... t);

	public void initQueue(T... t);
}
