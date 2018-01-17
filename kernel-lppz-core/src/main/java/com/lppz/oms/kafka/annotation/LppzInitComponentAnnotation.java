package com.lppz.oms.kafka.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lppz.oms.kafka.event.KafkaLppzEvent.InitComponentEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited 
public @interface LppzInitComponentAnnotation {
	public InitComponentEnum initenum() ;
}
