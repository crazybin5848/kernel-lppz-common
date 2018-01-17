package com.lppz.mycat.monitor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * JSch工具类，用于远程操作linux服务器和上传文件
 * @author licheng
 *
 */
public class JSchUtil {
	private static Logger logger = LoggerFactory.getLogger(JSchUtil.class);
	private  Channel channel;
	private  Session session;
	
	public  Channel getChannel(String user, String psw, String host, int port, String channelType, int timeout){
		try {
			JSch jsch=new JSch();
			session = jsch.getSession(user, host, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(psw);
			session.setTimeout(timeout);
			session.connect();
			channel = session.openChannel(channelType);
		} catch (JSchException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return channel;
	}
	
	public  void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
