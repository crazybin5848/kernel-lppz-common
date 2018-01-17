package com.lppz.configuration.es;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.elasticsearch.client.support.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.lppz.elasticsearch.EsClientUtil;
import com.lppz.elasticsearch.LppzEsComponent;

@Configuration
public class EsClientConfiguration extends PathMatchingResourcePatternResolver {
	private static final int LOAD_FIRST_JEDIS_CONFIG_ONLY = 0;
	private static final Logger LOG = LoggerFactory.getLogger(EsClientConfiguration.class);
	
	@Bean(name = "transportClient",destroyMethod="close")
	public AbstractClient transportClient() throws IOException {
		Resource[] resources = null;
		AbstractClient client=null;
		try {
			resources = getResources("classpath*:/META-INF/es-cluster*.yaml");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return client;
		}
		if(!ArrayUtils.isEmpty(resources)){
			Resource clientRes = resources[LOAD_FIRST_JEDIS_CONFIG_ONLY];
			client = EsClientUtil.buildPoolClientProxy(clientRes.getInputStream());
		}
		LppzEsComponent.getInstance().setClient(client);
		return client;
	}
}
