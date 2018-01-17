/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
 */
package com.lppz.configuration.biz.interceptor;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lppz.oms.kafka.annotation.BizLog;
import com.lppz.oms.kafka.domain.BackendBizLog;
import com.lppz.oms.kafka.service.ExtraInfoService;
import com.lppz.util.kafka.producer.KafkaProducer;

/**
 * AOP拦截
 *
 * @version 1.0
 */
@Aspect
@Component
public class BizLogInterceptor implements MethodInterceptor {
	private ExtraInfoService extraInfoService;

	@Resource(name="bizLogProducer")
	private KafkaProducer<BackendBizLog> bizLogProducer;
	/**
	 * @param hostService
	 *            The hostService to set.
	 */
	public void setExtraInfoService(ExtraInfoService extraInfoService) {
		this.extraInfoService = extraInfoService;
	}

	/*
	 * 业务日志拦截器
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!invocation.getMethod().isAnnotationPresent(BizLog.class)) {
			return invocation.proceed();
		}
		BackendBizLog log = new BackendBizLog();
		try {
			log.setBizLogAnnotation(invocation.getMethod().getAnnotation(
					BizLog.class));
			log.setMethodName(invocation.getMethod().getName());
			log.setClassName(invocation.getMethod().getDeclaringClass()
					.getName());
			log.setParam(getParamInfo(invocation, log));
		} catch (Exception e) {

		}

		String tempRefCode = null;
		if (extraInfoService != null) {
			log.setRemoteIp(extraInfoService.getRemoteAddress());
			log.setLocalIp(extraInfoService.getLocalAddress());
			tempRefCode = extraInfoService.getRefCode();
		}
		// extraInfoService.getRefCode()为调用方实现的获取业务主键方法，如果为空，通过注解取值
		if (tempRefCode != null && tempRefCode.trim().length() > 0) {
			log.setRefCode(tempRefCode);
		} else {
			log.setRefCode(log.getRefCode(invocation.getArguments()));
		}

		Object result = null;
		Exception ex = null;
		Error er = null;
		try {
			long begin = System.currentTimeMillis();
			result = invocation.proceed();
			log.setCostTime(System.currentTimeMillis() - begin);
			log.setResult(result);
		} catch (Exception e) {
			ex = e;
			log.setException(ex);
		} catch (Error e) {
			er = e;
		}

		bizLogProducer.sendMsg(log);

		if (ex != null) {
			throw ex;
		}
		if (er != null) {
			throw er;
		}

		return result;
	}

	/**
	 * 获取参数内容
	 */
	private String getParamInfo(MethodInvocation invocation,
			BackendBizLog bizLog) {
		if (bizLog.isRecordNoneParam()) {
			return null;
		}
		Object[] args = invocation.getArguments(); // 获取所有参数
		if (args == null || args.length <= 0) {
			return null;
		}
		int len = args.length;
		StringBuilder sb = new StringBuilder();
		//Gson gson = new Gson();

		// 记录所有参数
		if (bizLog.isRecordAllParam()) {
			for (int i = 0; i < len; i++) {
				sb.append("arg[").append(i).append("]:");
				sb.append(JSON.toJSONString(args[i])).append(";");
			}
			return sb.toString();
		}
		// 记录指定index的参数
		BizLog bizLogAnnotation = bizLog.getBizLogAnnotation();
		int[] paramIndex = bizLogAnnotation.paramRecorderIndex();
		if (paramIndex == null || paramIndex.length <= 0) {
			return null;
		}

		for (int i : paramIndex) {
			if (i >= len) {
				continue;
			}
			sb.append("arg[").append(i).append("]:");
			sb.append(JSON.toJSONString(args[i])).append(";");
		}
		return sb.toString();
	}

}