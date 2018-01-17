package com.lppz.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class HttpEncodeUtils {
	public static String md5Signature(Map<String, String> params,String appScecret,boolean upperCase) {
		String result = null;
		StringBuffer orgin = getBeforeSign(params, new StringBuffer(appScecret));
		if (orgin == null)
			return result;

		// secret last
		orgin.append(appScecret);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));

		} catch (Exception e) {
			throw new java.lang.RuntimeException("sign error !");
		}
		if(upperCase){
			result=result.toUpperCase();
		}
		return result;
	}
	/**
	 * 二进制转字符串
	 */
	private static String byte2hex(byte[] b) {

		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString();
	}
	
		private static StringBuffer getBeforeSign(Map<String, String> params,
			StringBuffer orgin) {
		if (params == null)
			return null;
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(params);
		Iterator<String> iter = treeMap.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			orgin.append(name).append(params.get(name));
		}

		return orgin;
	}

}
