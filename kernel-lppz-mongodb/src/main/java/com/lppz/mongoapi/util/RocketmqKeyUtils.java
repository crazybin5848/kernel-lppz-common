package com.lppz.mongoapi.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lppz.mongoapi.bean.DictModel;
import com.lppz.util.UUIDUtils;

/**
 * rocketmq的key获取工具类
 * 主要根据传入参数和字典表的配置获取业务主键作为Rocketmq的key
 * @author licheng
 *
 */
public class RocketmqKeyUtils {
	
	private static Map<String, DictModel> dictMap = new HashMap<>();
	
	@SuppressWarnings("rawtypes")
	public static String getKeyFromParams(Map params, String table){
		String key = null;
		String pk = null;
		//TODO save update delete 操作结构差异测试并调整
		if (dictMap != null && !dictMap.isEmpty()) {
			DictModel dict = dictMap.get(table);
			if (dict != null) {
				pk = dict.getPk();
				if (StringUtils.isNotBlank(pk)) {
					key = convertString(params.get(pk));
				}
			}
		}
		
		if (StringUtils.isBlank(key)) {
			if (params == null || params.isEmpty()) {				
				key = UUIDUtils.createUUId();
			}else{
				key = String.valueOf(Math.abs(params.hashCode()));
			}
		}
		return key;
	}

	private static String convertString(Object object) {
		if (object != null) {
			return String.valueOf(object);
		}
		return null;
	}
	
}
