/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.aspect.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.lppz.core.annotation.HashKeyInsertDs;
import com.lppz.core.annotation.HashKeyListDs;
import com.lppz.core.annotation.HashKeyListInserDs;
import com.lppz.core.aspect.BaseDataSourceAspect;
import com.lppz.core.datasource.DynamicDataSourceHolder;
import com.lppz.core.entity.DalEntity;
import com.lppz.core.util.DalUtil;


/**
 * @author zoubin
 * 
 */
@Aspect
@Component
public class DalShardingDataSourceAspect extends BaseDataSourceAspect implements Ordered
{
	private static final Logger logger = LoggerFactory.getLogger(DalShardingDataSourceAspect.class);
//	@Resource(name="defaultOmsJedisCluster")
//	private OmsJedisClusterInterface omsJedisClusterInterface;
	ExecutorService pool = Executors.newCachedThreadPool();

//	@Resource(name = "dalDataSourceRWAspect")
//	private IDalDataSourceAspect daldsrwAspect;
//	 @Pointcut("@annotation(com.lppz.dal.annotation.Dal)") 
//	public void dalAspect() {} 
	/**
	 */
	private void before(final JoinPoint point)
	{
		try
		{
//			point.getThis().getClass().getInterfaces().getClass().getAnnotation(annotationClass)
			String key = null;
			int i = 0;
			final Object[] params = point.getArgs();
			final Method method = ((MethodSignature) point.getSignature()).getMethod();
			for (final Annotation[] aa : method.getParameterAnnotations())
			{
				if (aa.length == 0)
				{
					i++;
					continue;
				}

				final Annotation a = aa[0];
				if (a.annotationType() == HashKeyInsertDs.class)
				{
					if (params[i] instanceof DalEntity)
					{
						final DalEntity d = (DalEntity) params[i];
						key = DalUtil.generate(d.getDalKey(), d.getDalTsSize());
						break;
//								handleAfterPersist(d);
					}
				}
			}
			if (key == null)
			{
				setDynamicDsKey(point, key);
				return;
			}
			setDynamicDsKey(point, key);
			logger.debug("set datasource before do something");
		}
		catch (final Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param point
	 * @param key
	 */
	private void setDynamicDsKey(final JoinPoint point, final String key)
	{
		DynamicDataSourceHolder.set(key);
//		daldsrwAspect.setInterlKey(point);
	}

	public Object around(final ProceedingJoinPoint pjp) throws Throwable
	{
		final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		final HashKeyListDs hashKeyListDs = method.getAnnotation(HashKeyListDs.class);
		if (hashKeyListDs != null)
		{
			final Object reval = handleBatchInsert(pjp, method);
//			if (reval instanceof String && "-1".equals(reval))
//			{
//				return handleListAll(pjp, method);
//			}
//			else
//			{
				return reval;
//			}
		}
		return handleSingleRW(pjp,method);
	}

	/**
	 * @param pjp
	 * @param method
	 * @throws Throwable
	 */
	private Object handleBatchInsert(final ProceedingJoinPoint pjp, final Method method) throws Throwable
	{
//		Object retVal = null;
//		int numint = 0;
//		long numlong = 0l;
		int i = 0;
		final Object[] params = pjp.getArgs();
		final Map<String, List<DalEntity>> m = new HashMap<String, List<DalEntity>>();
		for (final Annotation[] aa : method.getParameterAnnotations())
		{
			if (aa.length == 0)
			{
				i++;
				continue;
			}
			final Annotation a = aa[0];
			if (a.annotationType() == HashKeyListInserDs.class)
			{
				if (params[i] instanceof List)
				{
					final List<?> l = (List<?>) params[i];
					for (final Object o : l)
					{
						if (o instanceof DalEntity)
						{
							DalEntity d=(DalEntity)o;
							final String dsk = DalUtil.generate(d.getDalKey(),d.getDalTsSize());
							List<DalEntity> ll = (List<DalEntity>) m.get(dsk);
							if (ll == null)
							{
								ll = new ArrayList<DalEntity>();
							}
							ll.add(d);
							m.put(dsk, ll);
						}
					}

					for (final String key : m.keySet())
					{
						final List<DalEntity> lk= m.get(key);
						final Object[] paramas = params.clone();
						paramas[i] = lk;
						pool.execute(new Runnable(){
							@Override
							public void run() {
								setDynamicDsKey(pjp, key);
								try {
									pjp.proceed(paramas);
//									handleAfterPersist(lk);
								} catch (Throwable e) {
									logger.error(e.getMessage(), e);
								}
								finally{
									after();
								}
							}
						});
//						if (method.getReturnType() == int.class || method.getReturnType() == Integer.class)
//						{
//							numint += (Integer) retVal;
//						}
//						else if (method.getReturnType() == long.class || method.getReturnType() == Long.class)
//						{
//							numlong += (Long) retVal;
//						}
						
					}
//					if (method.getReturnType() == int.class || method.getReturnType() == Integer.class)
//					{
//						return numint;
//					}
//					else if (method.getReturnType() == long.class || method.getReturnType() == Long.class)
//					{
//						return numlong;
//					}
//					else
//					{
//						return null;
//					} 
					return null;
				}
			}
		}
		return "-1";
	}

//	protected void handleAfterPersist(List<DalEntity> lk) {
//		List<CacheEntity> listDto=omsJedisClusterInterface.convertRouteRedisKeys(lk);
//		omsJedisClusterInterface.handleRouteRedisKeys(listDto);
//	}

	/**
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	private Object handleSingleRW(final ProceedingJoinPoint pjp, final Method method) throws Throwable
	{
		Object retVal=null;
		try {
			before(pjp);
			retVal = pjp.proceed();
//			int i = 0;
//			final Object[] params = pjp.getArgs();
//			for (final Annotation[] aa : method.getParameterAnnotations())
//			{
//				if (aa.length == 0)
//				{
//					i++;
//					continue;
//				}
//				final Annotation a = aa[0];
//				if (a.annotationType() == HashKeyInsertDs.class)
//				{
//					if (params[i] instanceof DalEntity)
//					{
//						final DalEntity d = (DalEntity) params[i];
////						handleAfterPersist(d);
//					}
//				}
//			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally{
			after();
		}
		return retVal;
	}

//	protected void handleAfterPersist(DalEntity d) {
//		CacheEntity cto=omsJedisClusterInterface.convertRouteRedisKey(d);
//		omsJedisClusterInterface.handleRouteRedisKeys(cto);
//	}

//	/**
//	 * @param pjp
//	 * @param method
//	 * @param numint
//	 * @param numlong
//	 * @return
//	 * @throws Throwable
//	 */
//	@SuppressWarnings("unchecked")
//	private Object handleListAll(final ProceedingJoinPoint pjp, final Method method) throws Throwable
//	{
//		Object retVal;
//		int numint = 0;
//		long numlong = 0l;
//		final List<Object> list = new ArrayList<Object>();
//		for (final Object key : targetDataSourcesMap.keySet())
//		{
//			setDynamicDsKey(pjp, (String) key);
//			retVal = pjp.proceed();
//			after();
//			if (retVal != null)
//			{
//				if (method.getReturnType() == List.class)
//				{
//					list.addAll((List<Object>) retVal);
//				}
//				else if (method.getReturnType() == int.class || method.getReturnType() == Integer.class)
//				{
//					numint += (Integer) retVal;
//				}
//				else if (method.getReturnType() == long.class || method.getReturnType() == Long.class)
//				{
//					numlong += (Long) retVal;
//				}
//				else
//				{
//					throw new DalReturnTypeException("HashKeyListDs annotation method must "
//							+ "return List or int or long type,should not return " + method.getReturnType());
//				}
//			}
//		}
//		if (method.getReturnType() == List.class)
//		{
//			return list;
//		}
//		else if (method.getReturnType() == int.class || method.getReturnType() == Integer.class)
//		{
//			return numint;
//		}
//		else if (method.getReturnType() == long.class || method.getReturnType() == Long.class)
//		{
//			return numlong;
//		}
//		throw new DalReturnTypeException("HashKeyListDs annotation method must "
//				+ "return List or int or long type,should not return " + method.getReturnType());
//	}

	@Override
	public int getOrder() {
		return 1;
	}
}
