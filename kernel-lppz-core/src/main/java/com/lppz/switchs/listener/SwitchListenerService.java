package com.lppz.switchs.listener;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SwitchListenerService {

	@Value("${dubbo.application.name}")
	String projectName;
	
	@Value("${dubbo.registry.address}")
	String zkAddress;
	
	@PostConstruct
	public void logListenerInit() {
		SwitchListener.start(projectName, zkAddress);
	}
	
}
