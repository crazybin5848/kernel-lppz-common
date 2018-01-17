package com.lppz.core.configuration;

import java.io.IOException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@Import(DataSourceConfig.class)
public class TransactionMgrConfig {
	public static final String SUFFIXTRANS="transmgr";
	
	@Resource 
	private DataSource dataSource;
	
	@Bean(name="transactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager() throws IOException {
		//addTransBeanToApp();
		return new DataSourceTransactionManager(dataSource);
	}
	
}
