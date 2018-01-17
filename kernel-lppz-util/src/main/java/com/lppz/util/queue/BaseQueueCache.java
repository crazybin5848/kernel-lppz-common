package com.lppz.util.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseQueueCache<T> implements QueueCache<T> {

	public abstract String[] generateVal(T... t);

	protected Map<T, List<T>> splitParams(List<T> params) {
		Map<T, List<T>> m = new HashMap<T, List<T>>();
		for (T t : params) {
			List<T> ts = m.get(t);
			if (ts == null) {
				ts = new ArrayList<T>();
				m.put(t, ts);
			}
			ts.add(t);
		}
		return m;
	}
}
