/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务日志注解
 *
 * @author luoyiqun@yihaodian.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BizLog {
	
	/**
	 * 日志类型
	 * @return
	 */
	public LogType logType();
	
	/**
	 * 应用名称,指引用backend-log的应用,来源于 App enmu 
	 */
	public App app(); 
	
	/**
	 * 参数记录方式:ALL:全部记录;NONE:全部不记录;INDEX:按顺序记录
	 * 
	 * @return ParamRecordorType
	 */
	public ParamRecorderType paramRecorderType() default ParamRecorderType.NONE;
	
	public int[] paramRecorderIndex() default {};
	
	/**
	 * 作为ref_code的参数的位置, 默认取第1个位置
	 * 
	 * @return int
	 */
	public int refCodeIndex() default 0;

	/**
	 * 如果ref_code参数不是普通类型,通过调用对象的getField()方法获取数据
	 * 
	 * @return String
	 */
	public String refCodeField() default "";
	
}
