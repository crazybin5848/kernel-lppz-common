package io.mycat.backend.jdbc;

import io.mycat.config.model.DBHostConfig;
import io.mycat.config.model.DataHostConfig;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariJDBCDatasource extends JDBCDatasource implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3303749281606047184L;
	private HikariDataSource ds;
	public HikariJDBCDatasource() {
		this(null,null,false);
	}
	public HikariJDBCDatasource(DBHostConfig config, DataHostConfig hostConfig, boolean isReadNode) {
		super(config, hostConfig, isReadNode);
		build(config); 
	}

	private void build(DBHostConfig config) {
		HikariConfig hconfig=new HikariConfig();
		hconfig.setDataSourceClassName(config.getDataSourceClassName());
		Properties dsProperties=new Properties();
		dsProperties.setProperty("url", config.getUrl());
		dsProperties.setProperty("user", config.getUser());
		dsProperties.setProperty("password", config.getPassword());
		hconfig.setDataSourceProperties(dsProperties);
		hconfig.setAutoCommit(true);
		hconfig.setConnectionTimeout(config.getConnectionTimeout());
		hconfig.setIdleTimeout(config.getIdleTimeout());
		hconfig.setMaxLifetime(config.getMaxLifetime());
		hconfig.setMaximumPoolSize(config.getMaxCon());
		hconfig.setMinimumIdle(config.getMinCon());
		ds=new HikariDataSource(hconfig);
	}
	@Override
	public boolean testConnection(String schema) throws IOException {
		boolean isConnected = false;	
		Connection connection = null;
		Statement statement = null;
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();			
			if (connection != null && statement != null) {
				isConnected = true;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			if (statement != null) {
				try { statement.close(); } catch (SQLException e) {}
			}
			
			if (connection != null) {
				try { connection.close(); } catch (SQLException e) {}
			}
		}		
		return isConnected;
	}
	
	@Override
    public Connection getConnection() throws SQLException {
		Connection connection = ds.getConnection();
		String initSql=getHostConfig().getConnectionInitSql();
		if (initSql != null && !"".equals(initSql)) {
			Statement statement = null;
			try {
				statement = connection.createStatement();
				statement.execute(initSql);
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
		}
		return connection;
    }
	
	public void evictConn(Connection connection){
		ds.evictConnection(connection);
	}
}