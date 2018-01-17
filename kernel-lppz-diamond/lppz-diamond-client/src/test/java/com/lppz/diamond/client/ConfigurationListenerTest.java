/**        
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.    
 */    
package com.lppz.diamond.client;

import com.lppz.diamond.client.event.ConfigurationEvent;
import com.lppz.diamond.client.event.ConfigurationListener;

/**
 * Create on @2013-8-28 @下午10:05:12 
 * @author bsli@ustcinfo.com
 */
public class ConfigurationListenerTest implements ConfigurationListener {

	@Override
	public void configurationChanged(ConfigurationEvent event) {
		System.out.println(event.getType().name() + " " + event.getPropertyName() + " " + event.getPropertyValue());
	}

}
