package com.lppz.util.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BlockingQueueCache<T> extends BaseQueueCache<T> {
	private final Map<T, BlockingQueue<String>> msgCache = new HashMap<T, BlockingQueue<String>>();

	@Override
	public List<String> takeCache(int maxElements, T... t) {
		List<String> list = new ArrayList<String>();
		generateQueue(t).drainTo(list, maxElements);
		return list;
	}

	@SuppressWarnings("unchecked")
	private BlockingQueue<String> generateQueue(T... t) {
		return t==null?msgCache.values().toArray(new  LinkedBlockingQueue[0])[0]: msgCache.get(t[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushCache(List<T> params) {
		Map<T, List<T>> m = splitParams(params);
		for (T t : m.keySet()) {
			generateQueue(t).addAll(Arrays.asList(generateVal((T[]) m.get(t).toArray())));
		}
	}

	@Override
	public int size(T... t) {
		return generateQueue(t).size();
	}

	@Override
	public void removeBak(String value, T... t) {

	}

	@Override
	public void initQueue(T... t){
		for(T tempT:t){
			BlockingQueue<String> bq = new  LinkedBlockingQueue<String>();
			msgCache.put(tempT, bq);
		}
	}
	
}
