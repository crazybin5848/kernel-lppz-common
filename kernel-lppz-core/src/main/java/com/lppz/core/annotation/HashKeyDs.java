/**
 * Copyright 2006-2014 lppz.com. All rights reserved.
 * Support: http://www.lppz.com
 */
package com.lppz.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于标识数据源的注解
 * 
 * @author zoubin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface HashKeyDs
{
}
