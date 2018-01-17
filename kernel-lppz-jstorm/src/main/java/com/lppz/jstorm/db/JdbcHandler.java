package com.lppz.jstorm.db;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class JdbcHandler {
	public void handleInTrans(JdbcTemplate jt){}
}
