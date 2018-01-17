package com.lppz.spark.rdbms;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class JdbcHandler {
	
	public abstract void handleInTrans(JdbcTemplate jt);
}
