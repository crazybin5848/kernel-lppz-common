package com.lppz.util.disruptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.lmax.disruptor.EventHandler;

public class BaseEventHandler<U, T> implements EventHandler<T> {
	public BaseHandler<U> getHandler() {
		return handler;
	}

	public void setHandler(BaseHandler<U> handler) {
		this.handler = handler;
	}

	public List<U> getBaseList() {
		return baseList;
	}

	protected List<U> baseList = new ArrayList<U>();
	protected BaseHandler<U> handler;
	private static final Logger logger = Logger.getLogger(BaseEventHandler.class);

	@Override
	public void onEvent(T event, long sequence, boolean endOfBatch)
			throws Exception {
			if (CollectionUtils.isNotEmpty(baseList)) {
				if (handler != null) {
					logger.debug("baseList:"+baseList.size());
					handler.handle(baseList);
				}
			}
	}
}