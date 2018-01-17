/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.datasource;

/**
 * DynamicDataSourceHolder存储当前线程使用的数据源
 * 
 * @author zoubin
 */
public class DynamicDataSourceHolder
{
	private static final ThreadLocal<String> holder = new ThreadLocal<String>();
	private static final ThreadLocal<String> holderintenal = new ThreadLocal<String>();
	private static final ThreadLocal<String> holderactiviti = new ThreadLocal<String>();

	/**
	 * 绑定当前线程数据源
	 * 使用完成后必须调用remove()方法删除
	 * 
	 * @param name
	 */
	public static void set(final String name)
	{
		holder.set(name);
	}

	/**
	 * 获取当前线程的数据源的名称
	 * 
	 * @return String
	 */
	public static String get()
	{
		return holder.get();
	}

	/**
	 * 删除与当前线程绑定的数据源名称
	 */
	public static void remove()
	{
		holder.remove();
	}

	public static void setIntenal(final String name)
	{
		holderintenal.set(name);
	}

	/**
	 * 获取当前线程的数据源的名称
	 * 
	 * @return String
	 */
	public static String getIntenal()
	{
		return holderintenal.get();
	}

	/**
	 * 删除与当前线程绑定的数据源名称
	 */
	public static void removeIntenal()
	{
		holderintenal.remove();
	}
	
	public static void setActiviti(final String name)
	{
		holderactiviti.set(name);
	}

	/**
	 * 获取当前线程的数据源的名称
	 * 
	 * @return String
	 */
	public static String getActiviti()
	{
		return holderactiviti.get();
	}

	/**
	 * 删除与当前线程绑定的数据源名称
	 */
	public static void removeActiviti()
	{
		holderactiviti.remove();
	}
}
