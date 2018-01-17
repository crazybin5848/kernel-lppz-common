package com.lppz.canal.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

public class MapUtils {
	
	/**
	 * 从集合中获取指定字段值
	 * @param columnNames
	 * @param columns
	 * @return
	 */
	public static Map<String, String> getValueInList(List<Map<String,String>> columns, String... columnNames) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if(columnNames == null || columnNames.length == 0 || CollectionUtils.isEmpty(columns)){
			return null;
		}
		for (Map<String,String> column : columns) {
			for(String columnName : columnNames){
				resultMap.put(columnName, column.get(columnName));
			}
		}
		return resultMap;
	}

}
