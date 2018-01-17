package com.lppz.canal.init;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import com.lppz.configuration.dubbo.log.DubboKafkaProducerConfiguration;
import com.lppz.configuration.jedis.JedisConfiguration;
import com.lppz.core.configuration.DataSourceConfig;
import com.lppz.dubbo.BaseMicroStartup;

@Configuration
@PropertySource("classpath:/META-INF/dubbo.properties")
@ImportResource({"classpath:/META-INF/kernel-lppz-canal-spring.xml"})
@Import({DataSourceConfig.class,DubboKafkaProducerConfiguration.class,JedisConfiguration.class})
public class CanalClientStartup extends BaseMicroStartup {
	private static AnnotationConfigApplicationContext context;
	static {
		context = new AnnotationConfigApplicationContext(CanalClientStartup.class);
	}
	
	public static void main(String[] args) {
		startup(context, 10);
	}
}
