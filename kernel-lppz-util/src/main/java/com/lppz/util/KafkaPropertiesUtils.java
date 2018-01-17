
package com.lppz.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 *
 */
public class KafkaPropertiesUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(KafkaPropertiesUtils.class);
	public static final String KAFKACLUSTERSIZE = "kafkaclustersize";

	public static String getKey(final String keystr,String...home) throws IOException
	{
		String restr = null;
		try
		{
			Resource resource=null;
			if(home==null||home.length==0){
				resource = new ClassPathResource("kafka.properties");
			}
			else{
				resource= new FileSystemResource(home[0]+File.separator+"kafka.properties");
			}
			final Properties props = PropertiesLoaderUtils.loadProperties(resource);
			restr = props.getProperty(keystr);
		}
		catch (final IOException e)
		{
//			LOG.error(e.getMessage(), e);
			throw e;
		}
		return restr;
	}
	
	public static Integer getClusterSize(String...home)
	{
		String restr = null;
		try
		{
			Resource resource=null;
			if(home==null||home.length==0){
				resource = new ClassPathResource("kafka.properties");
			}
			else{
				resource= new FileSystemResource(home[0]+File.separator+"kafka.properties");
			}
			final Properties props = PropertiesLoaderUtils.loadProperties(resource);
			restr = props.getProperty(KAFKACLUSTERSIZE);
			return restr==null?10:Integer.parseInt(restr);
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return 10;
	}
}
