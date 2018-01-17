package com.lppz.configuration.dubbo.ws;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
//import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.lppz.configuration.dubbo.log.DubboKafkaProducerConfiguration;
import com.lppz.configuration.dubbo.log.DubboLoggerProducer;
import com.lppz.dubbox.ws.BaseDubboWsUtil;
import com.lppz.dubbox.ws.DubboWsReferBean;

@Configuration
@Import(DubboKafkaProducerConfiguration.class)
public class DubboWsConfiguration extends PathMatchingResourcePatternResolver {
	private static final Logger LOG = LoggerFactory.getLogger(DubboWsConfiguration.class);
	
	@Resource 
	private ApplicationConfig application;
	@Resource 
	private RegistryConfig registry;
	@Resource
	private DubboLoggerProducer producer;
	
	@Bean(name = "dubboWsReferBean")
	public DubboWsReferBean dubboWsReferBean() throws Exception {
		org.springframework.core.io.Resource[] resources = null;
		DubboWsReferBean dwrb = null;
		try {
			resources = getResources("classpath*:/META-INF/ws-dubbo*.yaml");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return dwrb;
		}
		if(!ArrayUtils.isEmpty(resources)){
			org.springframework.core.io.Resource dubbowsRes = resources[0];
			@SuppressWarnings("unchecked")
			Map<String, DubboWsReferBean> dwbeanMap = (Map<String, DubboWsReferBean>) new Yaml().load(dubbowsRes.getInputStream());
			for(String key:dwbeanMap.keySet()){
				DubboWsReferBean db=dwbeanMap.get(key);
				db.setApplication(application);
				db.setRegistry(registry);
				db.setClazz(Class.forName(key));
			}
			BaseDubboWsUtil.getInstance().setDwbeanMap(dwbeanMap);
			BaseDubboWsUtil.getInstance().initMap();
		}
		return dwrb;
	}

}
