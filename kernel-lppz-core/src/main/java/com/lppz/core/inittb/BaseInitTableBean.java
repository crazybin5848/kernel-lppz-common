package com.lppz.core.inittb;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 */
public abstract class BaseInitTableBean implements InitTableInterface
{
	protected String DB_TYPE="mysql";
	protected String resourceName;
	protected String TBName;
	protected String component;
	protected InitDBSqlUtil initDbsqlUtil=InitDBMySqlUtil.getInstance();
	protected DataSource multidataSource;
	protected String charactorSet="UTF-8";
	
	public void setMultidataSource(DataSource multidataSource) {
		this.multidataSource = multidataSource;
	}

	private static final Logger logger = LoggerFactory.getLogger(BaseInitTableBean.class);

	public void initData()
	{
		build();
		if (!initDbsqlUtil.isTablePresent(multidataSource,TBName))
		{
			logger.info("start load data from file[" + resourceName + "]");
			org.springframework.core.io.Resource fileRource = new ClassPathResource(resourceName); 
			InputStream is = null;
			try 
			{
				is = fileRource.getInputStream();
			} catch (IOException e) {
				logger.error("file cannot be found [" + resourceName + "]", e);
			}
			initDbsqlUtil.executeSchemaResource(multidataSource, resourceName, is, component,charactorSet);
			logger.info(component+" data init completed.");
		}
		else
		{
			logger.info(component+" data already created.");
		}
	}

	protected abstract void build();

}
