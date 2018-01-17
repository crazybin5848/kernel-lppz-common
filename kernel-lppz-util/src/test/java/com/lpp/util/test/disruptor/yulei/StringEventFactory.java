package com.lpp.util.test.disruptor.yulei;

import com.lmax.disruptor.EventFactory;

public class StringEventFactory implements EventFactory<StringEvent> {

	@Override
	public StringEvent newInstance() {
		return new StringEvent();
	}

}
