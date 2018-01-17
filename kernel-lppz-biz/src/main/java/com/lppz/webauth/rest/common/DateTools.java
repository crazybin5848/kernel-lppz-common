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
package com.lppz.webauth.rest.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * 时间工具类
 */
public class DateTools
{
	// 日期格式，年份，例如：2004，2008
	public static final String YYYY = "yyyy";

	// 日期格式，年份和月份，例如：200707，200808
	public static final String YYYYMM = "yyyyMM";

	// 日期格式，年月日，例如：20050630，20080808
	public static final String YYYYMMDD = "yyyyMMdd";

	// 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	// 日期格式，年月日时分秒，例如：20001230120000，20080808200808
	public static final String YYYYMMDDHHMISS = "yyyyMMddHHmmss";

	// 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开，
	// 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
	public static final String YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

	public static final SimpleDateFormat defaultDateformat = new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS);
	static
	{
		defaultDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * 字符串转换为日期
	 *
	 * @return Date
	 */
	public static Date strToDate(final String strDate, final String format)
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try
		{
			date = dateFormat.parse(strDate);
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 按照默认的格式将指定的字符串解析成Date类型
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(final String strDate)
	{
		Date date = null;
		try
		{
			date = defaultDateformat.parse(strDate);
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换为字符串
	 *
	 * @return String
	 */
	public static String dateToStr(final Date date, final String format)
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return dateFormat.format(date);
	}

	/**
	 * 将给定的时间字符串转换成指定的时间格式
	 * strDate的格式必须为yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String strDateToStr(final String strDate, final String format)
	{
		String str = "";
		final Date date = strToDate(strDate);
		if (date != null)
		{
			str = dateToStr(date, format);
		}
		return str;
	}

	/**
	 * 将指定的时间转换成默认时间格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String dateToStr(final Date date)
	{
		if (date == null)
		{
			return "";
		}
		return defaultDateformat.format(date);
	}

	/**
	 * 根据给定格式得到当前日期时间
	 *
	 * @return 符合格式要求的日期字符串 返回格式
	 */
	public static String getDate(final String fmt)
	{
		final Date myDate = new Date(System.currentTimeMillis());
		final SimpleDateFormat sDateformat = new SimpleDateFormat(fmt);
		sDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return sDateformat.format(myDate);
	}

	/**
	 * 获取当前时间的缺省格式字符串
	 *
	 * @return
	 */
	public static String getCurrentDate()
	{
		return getDate(YYYY_MM_DD_HH_MI_SS);
	}

	/**
	 * 按照缺省格式格式化时间对象
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(final Date date)
	{
		return defaultDateformat.format(date);
	}
}
