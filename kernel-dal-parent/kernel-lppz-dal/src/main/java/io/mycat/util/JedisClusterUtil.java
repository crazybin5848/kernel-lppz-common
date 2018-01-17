package io.mycat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

import com.lppz.configuration.jedis.BaseJedisClusterConfiguration;

public class JedisClusterUtil extends BaseJedisClusterConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterUtil.class);
	private JedisCluster jedisCluster;
    private JedisClusterUtil(){}    
    private static JedisClusterUtil instance=new JedisClusterUtil();
    public static JedisClusterUtil getInstance(){
    		return instance;
    }
	public void buildJedisCluster(String home)  {
		if(jedisCluster!=null)
			return;
		try {
			FileInputStream stream=new FileInputStream(new File(home+"/conf/jedis-cluster.yaml"));
			jedisCluster = getJedisCluster(stream);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	public JedisCluster getJedisCluster()  {
		return jedisCluster;
	}
	@Override
	public JedisCluster reloadJedisCluster() {
		// TODO Auto-generated method stub
		return null;
	}
}