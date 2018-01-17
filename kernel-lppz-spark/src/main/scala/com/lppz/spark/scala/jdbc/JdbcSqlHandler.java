package com.lppz.spark.scala.jdbc;

import java.io.Serializable;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lppz.spark.rdbms.JdbcHandler;

public class JdbcSqlHandler extends JdbcHandler implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6700351908316288563L;
	private String[] sqls;
	
	public JdbcSqlHandler(){}

	public JdbcSqlHandler(String[] sqls){
		this.sqls=sqls;
	}

	@Override
	public void handleInTrans(JdbcTemplate jt){
		jt.batchUpdate(sqls);
	}

	public String[] getSqls() {
		return sqls;
	}

	public void setSqls(String[] sqls) {
		this.sqls = sqls;
	}
}
