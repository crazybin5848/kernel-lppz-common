package com.lppz.util.disruptor.sender;

public interface IDal2ndSender<U> {
	public void destory();

	public void sendMsgOrderly(U dto,Object key,String... params);

	public void sendMsg(U dto, String... params);
}
