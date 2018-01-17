package com.lpp.util.test.disruptor.yulei;

import com.lmax.disruptor.EventHandler;

public class StringEventHandler implements EventHandler<StringEvent> {

	@Override
	public void onEvent(StringEvent arg0, long arg1, boolean arg2) throws Exception {
		System.out.println(arg0.getMsg());
		System.out.println(Thread.currentThread().getId()+""+arg1);
	}

}
