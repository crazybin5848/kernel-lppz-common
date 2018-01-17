package com.lppz.util.disruptor;


public interface BaseErrorHandler<U> {

	void handler(U u);
	int getRetryCount();
	boolean isLogInfo();
}
