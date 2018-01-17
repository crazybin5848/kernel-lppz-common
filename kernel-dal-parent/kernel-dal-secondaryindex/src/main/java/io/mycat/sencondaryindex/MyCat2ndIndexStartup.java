package io.mycat.sencondaryindex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.helpers.LogLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.lppz.configuration.es.EsClientConfiguration;
import com.lppz.core.configuration.DataSourceConfig;
import com.lppz.util.kafka.consumer.BaseKafkaConsumer;


/**
 * @author binzou
 */
@Configuration
@ImportResource({"classpath:/META-INF/kernel-lppz-dal-2ndindex.xml"})
@Import({DataSourceConfig.class,EsClientConfiguration.class})
public class MyCat2ndIndexStartup {
	static Logger logger = LoggerFactory.getLogger(MyCat2ndIndexStartup.class);
	private static AnnotationConfigApplicationContext context;
	public volatile static boolean flag=true;
	static {
		context = new AnnotationConfigApplicationContext(MyCat2ndIndexStartup.class);
	}
	
	@Bean
	public static PropertyPlaceholderConfigurer placehodlerConfigurer() {
		PropertyPlaceholderConfigurer pc= new PropertyPlaceholderConfigurer();
		try {
			Resource resource = new ClassPathResource("/META-INF/dubbo.properties");
			pc.setLocation(resource);
			pc.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return pc;
	}
	
	public static void main(String[] args) {
		try {
			// startup
			context.start();
			logger.info("kernel-lppz-dal-2ndindex server startup successfully.");
			while (flag) {
				Thread.sleep(10 * 1000);
			}
			BaseKafkaConsumer.pool.awaitTermination(10, TimeUnit.SECONDS);
			context.destroy();
			System.exit(0);
		} catch (Exception e) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			LogLog.error(sdf.format(new Date()) + " startup error", e);
			context.stop();
			System.exit(-1);
		}
	}
}