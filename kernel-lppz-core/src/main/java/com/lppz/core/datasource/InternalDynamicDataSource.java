package com.lppz.core.datasource;
public class InternalDynamicDataSource extends DynamicDataSource
{
	@Override
	protected String generateRouteKey()
	{
		return DynamicDataSourceHolder.getIntenal();
	}


}