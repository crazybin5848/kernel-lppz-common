/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.core.inittb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class InitDBMySqlUtil implements InitDBSqlUtil
{
	private static InitDBMySqlUtil instance=new InitDBMySqlUtil();
	private InitDBMySqlUtil(){}
	public static InitDBMySqlUtil getInstance(){
		return instance;
	}
	public static String[] JDBC_METADATA_TABLE_TYPES = {"TABLE"};
	
	private static final Logger log = LoggerFactory.getLogger(InitDBMySqlUtil.class);

	public void executeSchemaResource(DataSource dataSource, String resourceName, InputStream inputStream,String component,String chractorset)
	{
		String sqlStatement = null;
		String exceptionSqlStatement = null;
		String operation="init";
		try
		{
			Connection connection = dataSource.getConnection();
			Exception exception = null;
			byte[] bytes = IOUtils.toByteArray(inputStream);
			String ddlStatements = new String(bytes,chractorset);
			BufferedReader reader = new BufferedReader(new StringReader(ddlStatements));
			String line = readNextTrimmedLine(reader);
			while (line != null)
			{
				if (line.startsWith("# "))
				{
					log.debug(line.substring(2));
				}
				else if (line.startsWith("-- "))
				{
					log.debug(line.substring(3));
				}
				else if (line.length() > 0)
				{
					if (line.endsWith(";"))
					{
						sqlStatement = addSqlStatementPiece(sqlStatement, line.substring(0, line.length() - 1));
						Statement jdbcStatement = connection.createStatement();
						try
						{
							// no logging needed as the connection will log it
							log.debug("SQL: {}", sqlStatement);
							jdbcStatement.execute(sqlStatement);
							jdbcStatement.close();
						}
						catch (Exception e)
						{
							if (exception == null)
							{
								exception = e;
								exceptionSqlStatement = sqlStatement;
							}
							log.error("problem during schema statement {}", operation+" "+ sqlStatement, e);
						}
						finally
						{
							sqlStatement = null;
						}
					}
					else
					{
						sqlStatement = addSqlStatementPiece(sqlStatement, line);
					}
				}

				line = readNextTrimmedLine(reader);
			}

			if (exception != null)
			{
				throw exception;
			}

			
			log.debug("db schema {} for component {} successful", operation, component);

		}
		catch (Exception e)
		{
			throw new RuntimeException("couldn't " + operation + " db schema: " + exceptionSqlStatement, e);
		}
	}


	public boolean isTablePresent(DataSource dataSource,String tableName)
	{
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet tables = null;
			String catalog = connection.getCatalog();
			String schema = connection.getSchema();

			try
			{
				tables = databaseMetaData.getTables(catalog, schema, tableName, JDBC_METADATA_TABLE_TYPES);
				return tables.next();
			}
			finally
			{
				try
				{
					tables.close();
				}
				catch (Exception e)
				{
					log.error("Error closing meta data tables", e);
				}
			}

		}
		catch (Exception e)
		{
			throw new RuntimeException("couldn't check if tables are already present using metadata: " + e.getMessage(), e);
		}
	}

	private static String readNextTrimmedLine(BufferedReader reader) throws IOException
	{
		String line = reader.readLine();
		if (line != null)
		{
			line = line.trim();
		}
		return line;
	}

	private static String addSqlStatementPiece(String sqlStatement, String line)
	{
		if (sqlStatement == null)
		{
			return line;
		}
		return sqlStatement + " \n" + line;
	}
}
