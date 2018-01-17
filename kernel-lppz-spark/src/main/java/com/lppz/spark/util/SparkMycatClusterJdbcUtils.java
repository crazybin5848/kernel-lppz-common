package com.lppz.spark.util;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.core.datasource.MycatClusterDynamicDataSource;

public class SparkMycatClusterJdbcUtils extends SparkJdbcUtils{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7707845675980386006L;
	private static Logger logger = LoggerFactory.getLogger(SparkMycatClusterJdbcUtils.class);
	@Override
	protected DataSource build(LppzBasicDataSource lb) {
		MycatClusterDynamicDataSource ds=new MycatClusterDynamicDataSource(lb);
		ds.afterPropertiesSet();
		return ds;
	}
}
