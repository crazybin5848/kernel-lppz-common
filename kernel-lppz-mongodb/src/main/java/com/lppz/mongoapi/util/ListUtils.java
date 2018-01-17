package com.lppz.mongoapi.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
	
	public static <T>List<List<T>> splitArray(List<T> srcList, int length) {
		List<List<T>> resultList = new ArrayList<>();
		if (srcList == null || srcList.size() == 0 || length < 1) {  
			return null;  
		}  
			  
		int size = srcList.size();  
		int count = (size + length - 1) / length;  
		
		for (int i = 0; i < count; i++) {  
			List<T> subList = srcList.subList(i * length, ((i + 1) * length > size ? size : length * (i + 1)));  
			resultList.add(subList);  
		}  
			  
		return resultList;
	}

}
