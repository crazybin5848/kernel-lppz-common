package com.lppz.util.disruptor;

import java.util.List;

public interface BaseHandler<U> {
	public void handle(List<U> u);
}
