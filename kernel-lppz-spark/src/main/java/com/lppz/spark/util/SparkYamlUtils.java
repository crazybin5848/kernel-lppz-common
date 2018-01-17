package com.lppz.spark.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import com.lppz.spark.bean.SparkHiveSqlBean;


/**
 *
 */
public class SparkYamlUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(SparkYamlUtils.class);

	public static SparkHiveSqlBean loadYaml(String yaml,boolean mark){
		Resource res=null;
	   if(mark)
	    res = new ClassPathResource(yaml);
	   else
		 res =new FileSystemResource(yaml);
		if(res==null||!res.exists()){
			return null;
		}
		SparkHiveSqlBean bean=null;
		try {
			bean =  new Yaml().loadAs(res.getInputStream(), SparkHiveSqlBean.class);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return bean;
	}
	
	public static <T> T loadYaml(String yaml,boolean mark,Class<T> clazz){
		Resource res=null;
	   if(mark)
	    res = new ClassPathResource(yaml);
	   else
		 res =new FileSystemResource(yaml);
		if(res==null||!res.exists()){
			return null;
		}
		try {
			return  new Yaml().loadAs(res.getInputStream(), clazz);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
}