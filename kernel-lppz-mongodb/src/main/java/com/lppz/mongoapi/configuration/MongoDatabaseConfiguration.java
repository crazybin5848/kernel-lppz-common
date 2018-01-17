package com.lppz.mongoapi.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import com.lppz.mongoapi.bean.MongoConfigBean;
import com.lppz.mongoapi.client.LppzMongoClient;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

//@Configuration
public class MongoDatabaseConfiguration extends PathMatchingResourcePatternResolver implements ApplicationContextAware {
	private ConfigurableApplicationContext app;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.app=(ConfigurableApplicationContext)applicationContext;
	}
	
	private MongoClient mongoClient;
	
//	@Bean(name = "lppzMongoClient",destroyMethod="close")
	public LppzMongoClient buliderMongoDbDao() throws IOException{
		Resource clientRes =null;
		
		Resource[] resources = getResources("classpath*:/META-INF/mongodb-cluster*.yaml");
		
		if(null!=resources && resources.length>0){
			clientRes = resources[0];
		}else{
			return null;
		}
		MongoConfigBean config=new Yaml().loadAs(clientRes.getInputStream(),MongoConfigBean.class);
		MongoClientOptions options = MongoClientOptions.builder()  
	             .connectionsPerHost(config.getPoolSize())  
	             .maxWaitTime(config.getMaxWaitTime())  
	             .socketTimeout(config.getSocketTimeout())  
	             .maxConnectionLifeTime(config.getMaxConnectionLifeTime()) 
	             .connectTimeout(config.getConnectTimeout()).build();
		
		List<ServerAddress> seeds=new ArrayList<>();
		for(Properties prop:config.getServerAddress()){
			ServerAddress seed=new ServerAddress(prop.getProperty("host"), Integer.parseInt(prop.getProperty("port")));
			
			seeds.add(seed);
		}
		mongoClient = new MongoClient(seeds,options);
		return new LppzMongoClient(config.getDb(), mongoClient);
	}

}
