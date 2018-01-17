package com.lppz.switchs.listener;

import java.util.concurrent.atomic.AtomicBoolean;

public class SwitchLock {

	AtomicBoolean state = new AtomicBoolean(false);
	
	public void lock() {
		while(true) {
			while(state.get()) {
				;
			}
			if(!state.getAndSet(true)) {
				return ;
			}
		}
	}
	
	public void unlock() {
		state.set(false);
	}
	
}
