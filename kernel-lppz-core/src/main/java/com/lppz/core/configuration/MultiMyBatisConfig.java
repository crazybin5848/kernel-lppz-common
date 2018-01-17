package com.lppz.core.configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.core.datasource.LppzBasicDataSource;

@Configuration
@Import(DataSourceConfig.class)
public class MultiMyBatisConfig extends BaseMyBatisConfig{
	@Resource 
	private DataSource dataSource;
	
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) app  
                .getAutowireCapableBeanFactory();  
		if(dataSource instanceof DynamicDataSource){
		DynamicDataSource ds=(DynamicDataSource)dataSource;
		SqlSessionFactoryBean ssfb=new SqlSessionFactoryBean();
		ssfb.setDataSource(dataSource);
		ssfb.setConfigLocation(getResource(ds.getConfigLocation()));
		ssfb.setMapperLocations(getResources(ds.getMapperLocation()));
//		if(ds.isCobar())
//		dynamicRegisterMybatisScanner(acf, "sqlSessionFactory", (LppzBasicDataSource) ds.getDefaultTargetDataSource());
//		else
//		addMybatisBeanToApp(ds,acf);
		return ssfb;
		}
		return null;
	}
}
