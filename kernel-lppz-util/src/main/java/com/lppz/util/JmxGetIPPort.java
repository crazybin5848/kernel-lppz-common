package com.lppz.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JmxGetIPPort {
	private static final Logger logger = LoggerFactory.getLogger(JmxGetIPPort.class);
	public static final String ERRORIPPORT="-1:8080";
	public static String getIPPort()
		{
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objs;
			try {
				objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
				        Query.match(Query.attr("protocol"), Query.value("*")));
			    String hostname = InetAddress.getLocalHost().getHostName();
			    InetAddress[] addresses = InetAddress.getAllByName(hostname);
			    for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
			        ObjectName obj = i.next();
			        String port = obj.getKeyProperty("port");
			        for (InetAddress addr : addresses) {
			            String ip=addr.getHostAddress();
						if (ip.equalsIgnoreCase("127.0.0.1") || ip.equalsIgnoreCase("localhost"))
							continue;
							return ip+":"+port;
						}
			        }
			} catch (Exception  e) {
				logger.error(e.getMessage(),e);
			}
			return ERRORIPPORT;
		}
	
	public static String getMainIPPort()
	{
		String port="";
		if((port=DubboPropertiesUtils.getKey("dubbo.rest.port"))==null)
			return ERRORIPPORT;
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			InetAddress[] addresses = InetAddress.getAllByName(hostname);
			for (InetAddress addr : addresses) {
				String ip = addr.getHostAddress();
				if (ip.equalsIgnoreCase("127.0.0.1")
						|| ip.equalsIgnoreCase("localhost"))
					continue;
				return ip + ":" + port;
			}
		} catch (Exception  e) {
			logger.error(e.getMessage(),e);
		}
		return ERRORIPPORT;
	}
}