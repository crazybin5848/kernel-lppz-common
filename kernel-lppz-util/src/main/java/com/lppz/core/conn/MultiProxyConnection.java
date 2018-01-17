//package com.lppz.core.conn;
//
//import java.sql.Connection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.zaxxer.hikari.proxy.ConnectionProxy;
//
//
///**
// *
// */
//public class MultiProxyConnection extends BaseMultiProxyConnection
//{
//	/**
//	 * 
//	 */
//	private static MultiProxyConnection conn;
//
//	public static MultiProxyConnection getInstance()
//	{
//		return conn;
//	}
//
////	public MultiProxyConnection(final ConnectionProxy proxy) throws NoSuchFieldException, IllegalAccessException
////	{
////		super(proxy);
////	}
//
//	private final Map<Object,Connection> multiconn = Collections.synchronizedMap(new HashMap<Object,Connection>());
//
//	/**
//	 * @return the multiconn
//	 */
//	public Map<Object, Connection> getMulticonn() {
//		return multiconn;
//	}
//
//
//}
