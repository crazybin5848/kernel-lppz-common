package com.lppz.core.configuration;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.core.datasource.MycatClusterDynamicDataSource;

public class BaseMyBatisConfig extends PathMatchingResourcePatternResolver
		implements ApplicationContextAware {
	protected ConfigurableApplicationContext app;
	public static final String SUFFIXSQLMAP = "sqlsessionfactorybean";
	public static final String SUFFIXSCAN = "scanmybatis";
	public static final String SQLMAP_BEAN_CLASS = "org.mybatis.spring.SqlSessionFactoryBean";
	public static final String SCANMAPPER_BEAN_CLASS = "org.mybatis.spring.mapper.MapperScannerConfigurer";

	protected void addMybatisBeanToApp(DynamicDataSource ds,
			DefaultListableBeanFactory acf) throws Exception {
		boolean isMybatis= null != ds.getConfigLocation() && null != ds.getMapperLocation();
		Map<Object, Object> mapCustom = ds.getTargetDataSources();
		Iterator<Object> iter = mapCustom.keySet().iterator();
		while (iter.hasNext()) {
			String beanKey = (String) iter.next();
			LppzBasicDataSource lb = (LppzBasicDataSource) mapCustom
					.get(beanKey);
			if (!lb.isCobar()&&isMybatis) {
				dynamicRegisMybatisBean(acf, beanKey, lb,false);
			} else {
				MycatClusterDynamicDataSource mcds = new MycatClusterDynamicDataSource(
						lb);
				mcds.afterPropertiesSet();
				mapCustom.put(beanKey, mcds);
				if(isMybatis)
				dynamicRegisMybatisBean(acf, beanKey, mcds,true);
			}
		}
	}

	private void dynamicRegisMybatisBean(DefaultListableBeanFactory acf,
			String beanKey, DataSource lb,boolean isCoBar) throws IOException,
			Exception {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder
				.rootBeanDefinition(SQLMAP_BEAN_CLASS);
		bdb.getBeanDefinition().setAttribute("id", beanKey + SUFFIXSQLMAP);
		bdb.addPropertyValue("dataSource", lb);
		if(!isCoBar){
			LppzBasicDataSource lbs=(LppzBasicDataSource)lb;
			bdb.addPropertyValue("configLocation",
					getResource(lbs.getConfigLocation()));
			bdb.addPropertyValue("mapperLocations",
					getResources(lbs.getMapperLocation()));
			acf.registerBeanDefinition(beanKey + SUFFIXSQLMAP,
					bdb.getBeanDefinition());
			dynamicRegisterMybatisScanner(acf, beanKey, lbs);
		}
		else{
			MycatClusterDynamicDataSource mcds=(MycatClusterDynamicDataSource)lb;
			bdb.addPropertyValue("configLocation",
					getResource(mcds.getDsBean().getConfigLocation()));
			bdb.addPropertyValue("mapperLocations",
					getResources(mcds.getDsBean().getMapperLocation()));
			acf.registerBeanDefinition(beanKey + SUFFIXSQLMAP,
					bdb.getBeanDefinition());
			dynamicRegisterMybatisScanner(acf, beanKey, mcds.getDsBean());
		}
	}

	protected void dynamicRegisterMybatisScanner(
			DefaultListableBeanFactory acf, String beanKey,
			LppzBasicDataSource lb) throws Exception {
		BeanDefinitionBuilder bdb2 = BeanDefinitionBuilder
				.rootBeanDefinition(SCANMAPPER_BEAN_CLASS);
		bdb2.getBeanDefinition().setAttribute("id", beanKey + SUFFIXSCAN);
		bdb2.addPropertyValue("sqlSessionFactoryBeanName", beanKey
				+ SUFFIXSQLMAP);
		bdb2.addPropertyValue("basePackage", lb.getBaseScanPackge());
		acf.registerBeanDefinition(beanKey + SUFFIXSCAN,
				bdb2.getBeanDefinition());
		MapperScannerConfigurer scaner = (MapperScannerConfigurer) app
				.getBean(beanKey + SUFFIXSCAN);
		scaner.afterPropertiesSet();
		scaner.postProcessBeanDefinitionRegistry(acf);
	}

	protected void addTransBeanToApp(DataSource dataSource) {
		if (dataSource instanceof DynamicDataSource) {
			DynamicDataSource ds = (DynamicDataSource) dataSource;
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) app
					.getAutowireCapableBeanFactory();
			String TRANS_BEAN_CLASS = "org.springframework.jdbc.datasource.DataSourceTransactionManager";
			BeanDefinitionBuilder bdb;
			Map<Object, Object> mapCustom = ds.getTargetDataSources();
			Iterator<Object> iter = mapCustom.keySet().iterator();
			while (iter.hasNext()) {
				String beanKey = (String) iter.next();
				bdb = BeanDefinitionBuilder.rootBeanDefinition(TRANS_BEAN_CLASS);
				bdb.getBeanDefinition().setAttribute("id",
						beanKey + TransactionMgrConfig.SUFFIXTRANS);
				bdb.addPropertyValue("dataSource", mapCustom.get(beanKey));
				acf.registerBeanDefinition(beanKey
						+ TransactionMgrConfig.SUFFIXTRANS,
						bdb.getBeanDefinition());
				DataSourceTransactionManager dtm = (DataSourceTransactionManager) app
						.getBean(beanKey + TransactionMgrConfig.SUFFIXTRANS);
				dtm.afterPropertiesSet();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext app)
			throws BeansException {
		this.app = (ConfigurableApplicationContext) app;
	}
}