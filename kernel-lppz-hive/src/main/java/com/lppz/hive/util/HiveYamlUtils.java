package com.lppz.hive.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;


/**
 *
 */
public class HiveYamlUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(HiveYamlUtils.class);

	@SuppressWarnings("unchecked")
	public static Map<Object,Object> loadYaml(String yaml,boolean mark){
		Resource res=null;
	   if(mark)
	    res = new ClassPathResource(yaml);
	   else
		 res =new FileSystemResource(yaml);
		if(res==null||!res.exists()){
			return null;
		}
		Map<Object, Object> tempDataSources=null;
		try {
			tempDataSources = (Map<Object, Object>) new Yaml().load(res.getInputStream());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return tempDataSources;
	}
	
	public static void main(String[] args) {
//		System.out.println(2016-08-30 10:21:34);
	}
}
