package com.lppz.core.configuration;
import org.springframework.context.annotation.Configuration;

import com.lppz.core.inittb.BaseInitTableBean;

@Configuration("initAuthTableBean")
public class InitAuthTableBean extends BaseInitTableBean
{
	@Override
	protected void build() {
		super.resourceName = "auth_data_init_mysql.sql";
		super.TBName="authuser";
		super.component="LPPZAUTH";
	}
}
