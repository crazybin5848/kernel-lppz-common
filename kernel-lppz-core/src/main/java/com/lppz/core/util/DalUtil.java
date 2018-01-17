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
package com.lppz.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.lppz.core.annotation.DalDsRW.DalDB;
import com.lppz.core.exceptions.DalMultiThreadExecException;


/**
 *
 */
public class DalUtil
{
	private static final Logger LOG = LoggerFactory.getLogger(DalUtil.class);

	public static List<String> getRouteKeyList(final int num)
	{
		final List<String> list = new LinkedList<String>();
		for (int i = 0; i < num; i++)
		{
			final String code = generateRouteKey();
			list.add(code);
		}
		return list;
	}

	/**
	 * @return
	 */
	public static String generateRouteKey() {
		final String code = String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()));
		return code;
	}

	public static void multiThreadExec(final int threadNum, final int dataNum, final IDalCommonCallInterFace facade,
			final Object... args)
	{
		final Long time = System.currentTimeMillis();
		final CountDownLatch threadSignal = new CountDownLatch(threadNum);
		final Executor executor = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++)
		{
			executor.execute(new Runnable()
			{
				private final CountDownLatch threadsSignal = threadSignal;

				@Override
				public void run()
				{
					LOG.info(Thread.currentThread().getName() + "开始...");
					LOG.info("开始了线程：：：：" + threadsSignal.getCount());
					try
					{
						facade.batchInsertOrUpdate(DalUtil.getRouteKeyList(dataNum), args);
						
					}
					catch (final Exception e)
					{
						LOG.error(e.getMessage(), e);
						new DalMultiThreadExecException(e);
					}
					threadsSignal.countDown();
					LOG.info(Thread.currentThread().getName() + "结束. 还有" + threadsSignal.getCount() + " 个线程");
				}
			});
		}
		try
		{
			threadSignal.await();
		}
		catch (final InterruptedException e)
		{
			LOG.error(e.getMessage(), e);
			throw new DalMultiThreadExecException(e);
		}
		LOG.info(Thread.currentThread().getName() + "+++++++结束.");
		LOG.info("总耗时：" + (System.currentTimeMillis() - time) + "毫秒");
	}

	/**
	 * @return
	 */
	public static String generateRW(final DalDB dalDB)
	{
		return DalDB.Read == dalDB ? "slave" : "master";
	}

	/**
	 * @return
	 */
	public static String generate(final String key, final int size)
	{
		return "lppzds" + Long.parseLong(key) % size;
	}
	
	public static void release(DataSource d){
		final ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(d);
		if (conHolder != null)
		{
			conHolder.released();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDynamicObj(Class<?> clazz,String name,Object instance) throws NoSuchFieldException,
			IllegalAccessException {
		Field ff=clazz.getDeclaredField(name);
		ff.setAccessible(true);
		return (T)ff.get(instance);
	}
	
	public static void setFinalValue(Object obj,Field field, Object newValue) throws Exception {
	      field.setAccessible(true);
	      Field modifiersField = Field.class.getDeclaredField("modifiers");
	      modifiersField.setAccessible(true);
	      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	      field.set(obj, newValue);
	      modifiersField.setAccessible(false);  
          field.setAccessible(false);  
	   }
	
	@SuppressWarnings({ "static-access" })
	public static Object getProxyInstance(java.lang.reflect.Proxy proxy)
			throws NoSuchFieldException, IllegalAccessException, Exception {
		Object c=proxy.getInvocationHandler(proxy);
		AdvisedSupport advised=(AdvisedSupport)getDynamicObj(c.getClass(),"advised",c);
		Object obj=advised.getTargetSource().getTarget();
		return obj;
	}
	
	public static Object dynamicInvokePriMethods(Class<?> clazz,final String tbName, final Class<?>[] types,Object instance, final Object... args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Method method;
		method = clazz.getDeclaredMethod(tbName, types);
		method.setAccessible(true);
		return method.invoke(instance, args);
	}
	
	public static Field getField(String fieldname, Class clazz)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldname);
		field.setAccessible(true);
		return field;
	}
	
//	public static void setFinalValueForMO(Object mo, String fName, Object fValue) throws Exception
//	{
//		if (mo instanceof Proxy)
//		{
//			DefaultGenericManagedObjectHandler gmo = (DefaultGenericManagedObjectHandler) getField("h", Proxy.class).get(mo);
//			DefaultManagedObject managed = (DefaultManagedObject) (getField("managed",DefaultGenericManagedObjectHandler.class).get(gmo));
//			TypeHandler typeHandler = (TypeHandler) (getField("typeHandler", DefaultManagedObject.class).get(managed));
//			Attribute attribute = typeHandler.getManagedObjectType().getDeclaredAttribute(fName);
//			
//			Field isFinal = getField("isFinal", AttributeModifier.class);
//			setFinalValue(attribute.getModifierInfo(), isFinal, fValue);
//		}
//	}
}
