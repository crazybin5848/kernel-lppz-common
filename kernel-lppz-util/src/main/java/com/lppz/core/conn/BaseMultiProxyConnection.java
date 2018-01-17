///*
// * [y] hybris Platform
// * 
// * Copyright (c) 2000-2015 hybris AG
// * All rights reserved.
// * 
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// */
//package com.lppz.core.conn;
//
//import java.sql.Array;
//import java.sql.Blob;
//import java.sql.CallableStatement;
//import java.sql.Clob;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.NClob;
//import java.sql.PreparedStatement;
//import java.sql.SQLClientInfoException;
//import java.sql.SQLException;
//import java.sql.SQLWarning;
//import java.sql.SQLXML;
//import java.sql.Savepoint;
//import java.sql.Statement;
//import java.sql.Struct;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//import com.zaxxer.hikari.proxy.ConnectionProxy;
//
//
///**
// *
// */
//public class BaseMultiProxyConnection implements Connection
//{
//
//	private ConnectionProxy proxconn;
//	/**
//	 * @return the proxconn
//	 */
//	public ConnectionProxy getProxconn() {
//		return proxconn;
//	}
//
//
//	/**
//	 * @throws IllegalAccessException 
//	 * @throws NoSuchFieldException 
//	 * 
//	 */
//	protected BaseMultiProxyConnection(final ConnectionProxy proxy) throws NoSuchFieldException, IllegalAccessException
//	{
//		this.proxconn = proxy;
//	}
//
//
//	@Override
//	public String nativeSQL(final String sql) throws SQLException
//	{
//		return proxconn.nativeSQL(sql);
//	}
//
//	@Override
//	public boolean getAutoCommit() throws SQLException
//	{
//		return proxconn.getAutoCommit();
//	}
//
//	@Override
//	public void commit() throws SQLException
//	{
//		proxconn.commit();
//	}
//
//	@Override
//	public void rollback() throws SQLException
//	{
//		proxconn.rollback();
//	}
//
//	@Override
//	public DatabaseMetaData getMetaData() throws SQLException
//	{
//		return proxconn.getMetaData();
//	}
//
//	@Override
//	public boolean isReadOnly() throws SQLException
//	{
//		return proxconn.isReadOnly();
//	}
//
//	@Override
//	public String getCatalog() throws SQLException
//	{
//		return proxconn.getCatalog();
//	}
//
//	@Override
//	public int getTransactionIsolation() throws SQLException
//	{
//		return proxconn.getTransactionIsolation();
//	}
//
//	@Override
//	public SQLWarning getWarnings() throws SQLException
//	{
//		return proxconn.getWarnings();
//	}
//
//	@Override
//	public void clearWarnings() throws SQLException
//	{
//		proxconn.clearWarnings();
//	}
//
//	@Override
//	public Map<String, Class<?>> getTypeMap() throws SQLException
//	{
//		return proxconn.getTypeMap();
//	}
//
//	@Override
//	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException
//	{
//		proxconn.setTypeMap(map);
//	}
//
//	@Override
//	public void setHoldability(final int holdability) throws SQLException
//	{
//		proxconn.setHoldability(holdability);
//	}
//
//	@Override
//	public int getHoldability() throws SQLException
//	{
//		return proxconn.getHoldability();
//	}
//
//	@Override
//	public Savepoint setSavepoint() throws SQLException
//	{
//		return proxconn.setSavepoint();
//	}
//
//	@Override
//	public Savepoint setSavepoint(final String name) throws SQLException
//	{
//		return proxconn.setSavepoint(name);
//	}
//
//	@Override
//	public void rollback(final Savepoint savepoint) throws SQLException
//	{
//		proxconn.rollback(savepoint);
//	}
//
//	@Override
//	public void releaseSavepoint(final Savepoint savepoint) throws SQLException
//	{
//		proxconn.releaseSavepoint(savepoint);
//	}
//
//	@Override
//	public Clob createClob() throws SQLException
//	{
//		return proxconn.createClob();
//	}
//
//	@Override
//	public Blob createBlob() throws SQLException
//	{
//		return proxconn.createBlob();
//	}
//
//	@Override
//	public NClob createNClob() throws SQLException
//	{
//		return proxconn.createNClob();
//	}
//
//	@Override
//	public SQLXML createSQLXML() throws SQLException
//	{
//		return proxconn.createSQLXML();
//	}
//
//	@Override
//	public void setClientInfo(final String name, final String value) throws SQLClientInfoException
//	{
//		proxconn.setClientInfo(name, value);
//	}
//
//	@Override
//	public void setClientInfo(final Properties properties) throws SQLClientInfoException
//	{
//		proxconn.setClientInfo(properties);
//	}
//
//	@Override
//	public String getClientInfo(final String name) throws SQLException
//	{
//		return proxconn.getClientInfo(name);
//	}
//
//	@Override
//	public Properties getClientInfo() throws SQLException
//	{
//		return proxconn.getClientInfo();
//	}
//
//	@Override
//	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException
//	{
//		return proxconn.createArrayOf(typeName, elements);
//	}
//
//	@Override
//	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException
//	{
//		return proxconn.createStruct(typeName, attributes);
//	}
//
//	@Override
//	public void setSchema(final String schema) throws SQLException
//	{
//		proxconn.setSchema(schema);
//	}
//
//	@Override
//	public String getSchema() throws SQLException
//	{
//		return proxconn.getSchema();
//	}
//
//	@Override
//	public void abort(final Executor executor) throws SQLException
//	{
//		proxconn.abort(executor);
//	}
//
//	@Override
//	public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException
//	{
//		proxconn.setNetworkTimeout(executor, milliseconds);
//	}
//
//	@Override
//	public int getNetworkTimeout() throws SQLException
//	{
//		return proxconn.getNetworkTimeout();
//	}
//
//
//	@Override
//	public <T> T unwrap(Class<T> iface) throws SQLException {
//		return proxconn.unwrap(iface);
//	}
//
//
//	@Override
//	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
//		return proxconn.isWrapperFor(paramClass);
//	}
//
//
//	@Override
//	public Statement createStatement() throws SQLException {
//		return proxconn.createStatement();
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString)
//			throws SQLException {
//		return proxconn.prepareStatement(paramString);
//	}
//
//
//	@Override
//	public CallableStatement prepareCall(String paramString)
//			throws SQLException {
//		return proxconn.prepareCall(paramString);
//	}
//
//
//	@Override
//	public void setAutoCommit(boolean autoCommit) throws SQLException {
//		proxconn.setAutoCommit(autoCommit);		
//	}
//
//
//	@Override
//	public void close() throws SQLException {
//		proxconn.close();		
//	}
//
//
//	@Override
//	public boolean isClosed() throws SQLException {
//		return proxconn.isClosed();
//	}
//
//
//	@Override
//	public void setReadOnly(boolean paramBoolean) throws SQLException {
//		proxconn.setReadOnly(paramBoolean);
//	}
//
//
//	@Override
//	public void setCatalog(String paramString) throws SQLException {
//		proxconn.setCatalog(paramString);		
//	}
//
//
//	@Override
//	public void setTransactionIsolation(int paramInt) throws SQLException {
//		proxconn.setTransactionIsolation(paramInt);		
//	}
//
//
//	@Override
//	public Statement createStatement(int paramInt1, int paramInt2)
//			throws SQLException {
//		return proxconn.createStatement(paramInt1, paramInt2);
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString,
//			int paramInt1, int paramInt2) throws SQLException {
//		return proxconn.prepareStatement(paramString,
//			 paramInt1,  paramInt2);
//	}
//
//
//	@Override
//	public CallableStatement prepareCall(String paramString, int paramInt1,
//			int paramInt2) throws SQLException {
//		return proxconn.prepareCall(paramString,  paramInt1,
//			 paramInt2);
//	}
//
//
//	@Override
//	public Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
//			throws SQLException {
//		return proxconn.createStatement(paramInt1,  paramInt2,  paramInt3);
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString,
//			int paramInt1, int paramInt2, int paramInt3) throws SQLException {
//		return proxconn.prepareStatement( paramString,
//				 paramInt1,  paramInt2,  paramInt3);
//	}
//
//
//	@Override
//	public CallableStatement prepareCall(String paramString, int paramInt1,
//			int paramInt2, int paramInt3) throws SQLException {
//		return proxconn.prepareCall(paramString,paramInt1,
//			paramInt2,paramInt3);
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString, int paramInt)
//			throws SQLException {
//		return proxconn.prepareStatement(paramString,paramInt);
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString,
//			int[] paramArrayOfInt) throws SQLException {
//		return proxconn.prepareStatement(paramString,paramArrayOfInt);
//	}
//
//
//	@Override
//	public PreparedStatement prepareStatement(String paramString,
//			String[] paramArrayOfString) throws SQLException {
//		return proxconn.prepareStatement(paramString, paramArrayOfString);
//	}
//
//
//	@Override
//	public boolean isValid(int timeout) throws SQLException {
//		return proxconn.isValid(timeout);
//	}
//
//
//
//}
