package com.lppz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <b>模块描述	: </b>字符类型数据处理工具类<br/>
 * <b>使用描述	: </b><br/>
 * <b>JDK版本	: </b>JDK 1.6<br/>
 */
public final class StringUtils {
	
	private StringUtils(){}

	/**
	 * 功能描述：是否为空白,包括null和""
	 * 
	 * @param str String
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 功能描述：去掉字符串中重复的字符串
	 * @param str 原字符串
	 * @param strSplit 重复的字符串
	 * @return String 返回去掉重复子字符串后的字符串
	 */
	public static String removeSameString(String str,String strSplit) {
		Set<String> mLinkedSet = new LinkedHashSet<String>();
		String[] strArray = str.split(strSplit);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strArray.length; i++) {
			if (!mLinkedSet.contains(strArray[i])) {
				mLinkedSet.add(strArray[i]);
				sb.append(strArray[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 判断字符串是否为null或""，如果是则返回"";
	 * @param string String
	 * @return String
	 */
	public static String getString(String string){
		String s = "";
		if (!isBlank(string)) {
			s = string;
		}
		return s;
	}
	
	/**
	 * 功能描述：在页面上直接显示文本内容，替换小于号，空格，回车，TAB
	 * 
	 * @param str
	 *            String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlShow(String str) {
		if (str == null) {
			return null;
		}
		String strTmp = "";
		strTmp = replace("<", "&lt;", str);
		strTmp = replace(" ", "&nbsp;", strTmp);
		strTmp = replace("\r\n", "<br/>", strTmp);
		strTmp = replace("\n", "<br/>", strTmp);
		strTmp = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", strTmp);
		return str;
	}
	
	/**
	 * 功能描述：替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null){
			return null;
		}
		StringBuffer str = new StringBuffer("");
		int index = -1;
		String sourceTmp = source;
		while ((index = sourceTmp.indexOf(from)) != -1) {
			str.append(sourceTmp.substring(0, index) + to);
			sourceTmp = sourceTmp.substring(index + from.length());
			index = sourceTmp.indexOf(from);
		}
		str.append(sourceTmp);
		return str.toString();
	}
	
	public static String convertStreamToString(InputStream is) {   
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		     StringBuilder sb = new StringBuilder();   
		        String line = null;   
		        try {   
		            while ((line = reader.readLine()) != null) {   
		                sb.append(line + "\n");   
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		                is.close();   
		            } catch (IOException e) {   
		                e.printStackTrace();   
		            }   
		        }   
		        return sb.toString();   
		    } 
}
