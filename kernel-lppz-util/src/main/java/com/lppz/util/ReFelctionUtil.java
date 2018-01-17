package com.lppz.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReFelctionUtil
{
	private static final Logger LOG = LoggerFactory.getLogger(ReFelctionUtil.class);
	@SuppressWarnings("unchecked")
	public static <T> T getDynamicObj(Class<?> clazz,String name,Object instance)  {
		try {
			Field ff = clazz.getDeclaredField(name);
			ff.setAccessible(true);
			return (T)ff.get(instance);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public static void setFinalValue(Object obj,Field field, Object newValue) {
	      try {
			field.setAccessible(true);
			  Field modifiersField = Field.class.getDeclaredField("modifiers");
			  modifiersField.setAccessible(true);
			  modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			  field.set(obj, newValue);
			  modifiersField.setAccessible(false);  
			  field.setAccessible(false);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(),e);
		}  
	   }
	
	
	public static Object dynamicInvokePriMethods(Class<?> clazz,final String tbName, final Class<?>[] types,Object instance, final Object... args)
	{
		try {
			Method method = clazz.getDeclaredMethod(tbName, types);
			method.setAccessible(true);
			return method.invoke(instance, args);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
}
