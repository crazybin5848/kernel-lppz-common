/*
 * Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work. Neither this
 * material nor any portion hereof may be copied or distributed without the
 * express written consent of Home Box Office, Inc. This material also contains
 * proprietary and confidential information of Home Box Office, Inc. and its
 * suppliers, and may not be used by or disclosed to any person, in whole or in
 * part, without the prior written consent of Yi Hao Dian, Inc.
 */
package com.lppz.oms.kafka.domain;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.oms.kafka.annotation.BizLog;
import com.lppz.oms.kafka.annotation.ParamRecorderType;
import com.lppz.oms.kafka.constant.LogKeyword;
import com.lppz.oms.kafka.dto.BizLogDto;

/**
 * 业务日志领域对象
 * 
 * @author luoyiqun@yihaodian.com
 * @version 1.0
 */
public class BackendBizLog {

	private String className; // 类名
	private String methodName; // 方法名
	private long costTime; //方法调用耗时，单位ms
	private String localIp;
	private String remoteIp;
	private int exceptionFlag; // 异常标识,0:没有异常;1:有异常
	private String param;// 参数信息,超过4000字符拆成多条记录
	private String refCode; // 业务主键,可以是soId,userId
	private Object result; // 调用结果,超过4000字符被截断
	private Exception exception; // 异常信息,超过4000字符被截断

	private BizLog bizLogAnnotation;

	public static final int maxInfoLength = 4000; // 一条记录的最大长度

	private static final Logger logger = LoggerFactory.getLogger(BackendBizLog.class);

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param methodName The methodName to set.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @param costTime The costTime to set.
	 */
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	/**
	 * @param exceptionFlag The exceptionFlag to set.
	 */
	public void setExceptionFlag(int exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	/**
	 * @param param The param to set.
	 */
	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * @param refCode The refCode to set.
	 */
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	/**
	 * @param result The result to set.
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @param exception The exception to set.
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @param bizLogAnnotation The bizLogAnnotation to set.
	 */
	public void setBizLogAnnotation(BizLog bizLogAnnotation) {
		this.bizLogAnnotation = bizLogAnnotation;
	}

	/**
	 * @param localIp The localIp to set.
	 */
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	/**
	 * @param remoteIp The remoteIp to set.
	 */
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	/**
	 * @return Returns the bizLogAnnotation.
	 */
	public BizLog getBizLogAnnotation() {
		return bizLogAnnotation;
	}

	/**
	 * 是否记录所有参数 ，以bizLogAnnotation.paramRecorderType()为准
	 * 
	 * @return true/false
	 */
	public boolean isRecordAllParam() {
		if (bizLogAnnotation == null) {
			return false;
		}
		return bizLogAnnotation.paramRecorderType() == ParamRecorderType.ALL;
	}

	/**
	 * 是否所有参数都不记录 因为bizLogAnnotation.paramRecorderType()默认为NONE,
	 * 所以先判断paramRecorderIndex是否为空
	 * 
	 * @return true/false
	 */
	public boolean isRecordNoneParam() {
		if (bizLogAnnotation == null) {
			return false;
		}
		if (bizLogAnnotation.paramRecorderIndex() != null && bizLogAnnotation.paramRecorderIndex().length > 0) {
			return false;
		}
		return bizLogAnnotation.paramRecorderType() == ParamRecorderType.NONE;
	}

	public BizLogDto convertToDto() {
		BizLogDto dto = new BizLogDto();
		if (bizLogAnnotation != null) {
			dto.setAppName(bizLogAnnotation.app().getDesc());
			dto.setAppId(bizLogAnnotation.app().getId()); // 应用id
			dto.setLogType(bizLogAnnotation.logType().getDesc());
		}
		dto.setParamInfo(param);
		dto.setClassName(className);
		dto.setMethodName(methodName);
		dto.setCostTime(costTime);		
		dto.setExceptionFlag(exceptionFlag);		
		dto.setRefCode(refCode);
		dto.setRemoteIp(remoteIp);
		dto.setLocalIp(localIp);
		dto.setKeyword(LogKeyword.ANNOTATION);
		dto.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//Gson gson = new Gson();
		//JSON.toJSON(this.param);
		if (this.param != null) {
			dto.setParamInfo(JSON.toJSONString(this.param));
		}

		if (this.exception != null) {
			dto.setExceptionFlag(1);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			try{
				exception.printStackTrace(pw);	
			}finally{
				try {
					sw.close();
				}
				catch (IOException e) {
					logger.error("{}", e);
				}
				pw.close();
			}			
			String s = sw.toString();
			if (s.length() > maxInfoLength) {
				s = s.substring(0, maxInfoLength);
			}
			dto.setExceptionInfo(s);
		}
		if (this.result != null) {
			String s = JSON.toJSONString(result);
			if (s.length() > maxInfoLength) {
				s = s.substring(0, maxInfoLength);
			}
			dto.setResultInfo(s);
		}

		return dto;
	}

	/**
	 * 获取refCode
	 * 
	 * @param Object[] args 方法参数列表
	 */
	public String getRefCode(Object[] args){
		if(args == null || args.length <= 0){
			return null;
		}
		int index = bizLogAnnotation.refCodeIndex();
		if(index >= args.length){ //非法index,超过了args.length
			return null;
		}
		Object o = args[index];		
		if(o == null){
			return null;
		}
		if(o instanceof String){
			return o.toString();
		}
		if(o instanceof Integer || o instanceof Long || o instanceof Double){
			return String.valueOf(o);
		}
		if(o instanceof Map){
			//@refCodeField作为Map的key
			String key = bizLogAnnotation.refCodeField();
			if(key == null || "".equals(key.trim())){
				return null;
			}
			Object value = ((Map) o).get(key); 
			return value.toString(); 
		}
		//非基础类型，需要调用dto.getXXX方法获取对应的值
		String field = bizLogAnnotation.refCodeField();
		if(field == null || field.trim().length() <= 0){
			return null;
		}
		
		Method method;
		try {
			method = o.getClass().getMethod(getFieldMethodName(field), (Class[])null);
			Object refValue = method.invoke(o, (Object[])null);
			if(refValue == null){
				return null;
			}
			return refValue.toString();
		}catch (SecurityException e) {
			logger.error("{}", e);
		}catch (NoSuchMethodException e) {
			logger.error("{}", e);
		}catch (IllegalArgumentException e) {
			logger.error("{}", e);
		}catch (IllegalAccessException e) {
			logger.error("{}", e);
		}catch (InvocationTargetException e) {
			logger.error("{}", e);
		}
		
		return null;
	}
	
	/**
	 * 获取get方法方法名
	 * @param refCodeFiled
	 * @return
	 */
	private String getFieldMethodName(String refCodeFiled){
		String s = refCodeFiled.trim();
		return "get" + s.substring(0, 1).toUpperCase() + s.substring(1,s.length());
	}
}
