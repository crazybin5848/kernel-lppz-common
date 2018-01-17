package com.lppz.dubbo.monitor.disruptor;

import com.alibaba.dubbo.common.URL;


public class DubboInvokeEvent { 
    private URL statistics;

	public URL getStatistics() {
		return statistics;
	}

	public void setStatistics(URL statistics) {
		this.statistics = statistics;
	}
} 