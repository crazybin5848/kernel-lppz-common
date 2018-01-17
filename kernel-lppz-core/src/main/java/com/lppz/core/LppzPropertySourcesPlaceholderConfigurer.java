package com.lppz.core;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class LppzPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer{

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(LppzConstants.CONFIGSCANPACKAGE.equals(propertyName)){
			if(StringUtils.isBlank(propertyValue)){
				propertyValue="com.lppz.configuration.dubbo";
				return super.convertProperty(propertyName, propertyValue);
			}
		}
		return super.convertProperty(propertyName, propertyValue);
	}

	
}
