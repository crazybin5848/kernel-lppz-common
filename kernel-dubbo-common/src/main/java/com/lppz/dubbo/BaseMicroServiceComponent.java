package com.lppz.dubbo;

import com.lppz.util.MicroServiceUtils;

public abstract class BaseMicroServiceComponent {

	protected void shutdown(){
		String url=generateCloseRestUrl();
		
		MicroServiceUtils.shutdown(true, url);
	}
	
	protected abstract String generateCloseRestUrl();
}
