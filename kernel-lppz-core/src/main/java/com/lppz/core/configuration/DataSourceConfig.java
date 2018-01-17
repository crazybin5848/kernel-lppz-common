package com.lppz.core.configuration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.core.datasource.LppzBasicDataSource;

@Configuration
@Import(InitAuthTableBean.class)
public class DataSourceConfig extends BaseMyBatisConfig{
	
	private static final String MASTER_DATASOURCE = "master1";
	private static final String MASTER_AUTH = "master-lppz-auth";
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);
	
	@javax.annotation.Resource(name="initAuthTableBean")
	private InitAuthTableBean initAuthTableBean;
	
//	private volatile static boolean isInit=false;
	
	private static Lock lock = new ReentrantLock();
	
	private static DataSource ds;
	@SuppressWarnings("unchecked")
	@Bean(name = "dataSource",destroyMethod="destory")
	public DataSource dataSource() throws IOException {
		if(ds!=null){
			return ds;
		}
		lock.lock();
		if(ds!=null){
			return ds;
		}
			
		try{
			//support multi-yaml configuration files 
			Resource[] resources = null;
			resources = getResources("classpath*:/META-INF/datasource-*test.yaml");
			if(ArrayUtils.isEmpty(resources)){			
				resources = getResources("classpath*:/META-INF/datasource*.yaml");
			}
			if(ArrayUtils.isEmpty(resources)){
				throw new IOException("Can't read any datasource.yaml...");
			}
			Map<Object, Object> targetDataSources = new LinkedHashMap<Object, Object>();
			for (Resource resource : resources) {
				Map<Object, Object> tempDataSources = (Map<Object, Object>) new Yaml().load(resource.getInputStream());
				if (!tempDataSources.isEmpty()) {
					targetDataSources.putAll(tempDataSources);
				}
			}
			LppzBasicDataSource lb = (LppzBasicDataSource) (targetDataSources.get(MASTER_DATASOURCE) == null
					? targetDataSources.values().iterator().next() : targetDataSources.get(MASTER_DATASOURCE));
			DynamicDataSource ds = new DynamicDataSource(lb, targetDataSources);
			ds.setConfigLocation(lb.getConfigLocation());
			ds.setMapperLocation(lb.getMapperLocation());
			ds.setBaseScanPackge(lb.getBaseScanPackge());
//			if(!ds.isCobar()){
			Resource authRes=getResource("classpath:/META-INF/ds-auth.yaml");
			AuthYamlDataSourceBean tmpauthDsObj;
			try {
				tmpauthDsObj = (AuthYamlDataSourceBean)new Yaml().load(authRes.getInputStream());
				LppzBasicDataSource authlb=buildAuthYamlProp(tmpauthDsObj,lb);
				targetDataSources.put(MASTER_AUTH, authlb);
			} catch (Exception e1) {
				LOG.warn("ds-auth.yaml not exist");
			}
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) app  
                .getAutowireCapableBeanFactory();  
			try {
				addMybatisBeanToApp(ds,acf);
				addTransBeanToApp(ds);
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
			}
//			}
			DataSourceConfig.ds=ds;
		}catch(Exception ex){
			LOG.error(ex.getMessage(),ex);
		}finally{
			lock.unlock();
		}
		return ds;
	}
	
	private LppzBasicDataSource buildAuthYamlProp(AuthYamlDataSourceBean aydb,
			LppzBasicDataSource lb) {
		for(Object s:lb.getDataSourceProperties().keySet()){
			aydb.getDataSourceProperties().put(s, lb.getDataSourceProperties().get(s));
		}
		String yaml=new Yaml().dumpAsMap(aydb);
		LppzBasicDataSource authlb=new Yaml().loadAs(yaml, LppzBasicDataSource.class);
		initAuthTableBean.setMultidataSource(authlb);
		initAuthTableBean.initData();
		return authlb;
	}
}
