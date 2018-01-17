/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具类
 *
 * @author xuwencai@lppz.com
 * @version 1.0
 */
public class BizLogUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(BizLogUtil.class);
	
	/**
	 * 获取远程IP
	 *
	 * @param HttpServletRequest request
	 * @return String
	 */	
	public static String getRemoteAddress(HttpServletRequest request){
		if (request.getHeader("x-forwarded-for") != null) {
			return request.getHeader("x-forwarded-for");
        } else {
        	return request.getRemoteAddr();
        }
	}
	
	/**
	 * 获取远程IP
	 * 使用hedwig export的服务可以调用该方法获取远程IP
	 * 
	 * @return String
	 */		
	public static String getRemoteAddress(){
		//TODO
		return null;
//		return HedwigContextUtil.getAttribute(PropKeyConstants.HOST_IP, "UNKNOW").toString();
	}
	
	/**
	 * 获取本机IP
	 * 
	 * @return
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	public static String getLocalAddress(){
		try{
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && (ip instanceof Inet4Address)) {
						return ip.getHostAddress();
					}
				}
			}
			// windows平台获取IP
			if (ip == null) {
				ip = InetAddress.getLocalHost();
			}
			return ip.getHostAddress();	
		}catch (SocketException e) {
			logger.error("{}", e);
		}catch (UnknownHostException e){
			logger.error("{}", e);
		}
		return null;
	}	
}
