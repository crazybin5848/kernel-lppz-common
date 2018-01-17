package com.lppz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormatUtils {
	private static final Logger logger = LoggerFactory.getLogger(DateFormatUtils.class);
	
	public static final String DEFAULT_DATETIME_FORMAT_SEC  = "yyyy-MM-dd HH:mm:ss";
	
	public static Date parsDate(final String date) throws ParseException
	{
		String pattern = null;
		final String pattern1 = "yyyy-MM-dd HH:mm:ss";
		final String pattern2 = "MM dd yyyy  h:mm";
		final String pattern3 = "MM dd yyyy h:mm";
		final String pattern4 = "yyyy-MM-dd";

		final String s1 = "((19|20)\\d{2})-(0?[1-9]|1[0-2])-(0?[1-9]|[123]\\d) (0?\\d|1\\d|2[0-3]):(0?\\d|[1-5]\\d):(0?\\d|[1-5]\\d)";
		final String s2 = "(0?[1-9]|1[0-2]) (0?[1-9]|[123]\\d) ((19|20)\\d{2})  (0?\\d|[12]\\d):(0?\\d|[1-5]\\d)[AaPp][Mm]";
		final String s3 = "(0?[1-9]|1[0-2]) (0?[1-9]|[123]\\d) ((19|20)\\d{2}) (0?\\d|[12]\\d):(0?\\d|[1-5]\\d)[AaPp][Mm]";
		final String s4 = "((19|20)\\d{2})-(0?[1-9]|1[0-2])-(0?[1-9]|[123]\\d)";
		if (StringUtils.isBlank(date))
		{
			throw new RuntimeException("时间为空");
		}

		if (date.matches(s1))
		{
			pattern = pattern1;
		}
		else if (date.matches(s2))
		{
			pattern = pattern2;
		}
		else if (date.matches(s3))
		{
			pattern = pattern3;
		}
		else if (date.matches(s4))
		{
			pattern = pattern4;
		}
		else
		{
			logger.error("parsDate format error date : {}", date);
			throw new RuntimeException("时间格式不正确");
		}

		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}
	
	public static String getNowDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_SEC);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
