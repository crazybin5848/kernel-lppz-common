package com.lppz.elasticsearch.result;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.lppz.util.LppzConstants;

public class SearchResult implements Serializable{
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5798435752189786582L;

	public Class<?> getClazz() {
		return clazz;
	}

	private Object source;
	private String index;
	private String type;
	private String id;
	private String version;
	private String timestamp;
	private Class<?> clazz;

	public Object getSource() {
		return source;
	}

	public String getIndex() {
		return index;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void build(Map<String, Object> map) {
		buildSource(map);
		buildOther(map);
	}

	private void buildOther(Map<String, Object> map) {
		index = (String) map.get("__index");
		type = (String) map.get("__type");
		id = (String) map.get("__id");
		version = (String) map.get("@version");
		timestamp = (String) map.get("@timestamp");
	}

	private void buildSource(Map<String, Object> map) {
		String classType = (String) map.get("__type");
		if (classType == null)
			return;
		try {
			clazz = Class.forName(classType);
			source = clazz.newInstance();
			List<Field> list=Lists.newArrayList();
			getAllClassFields(clazz,list);
			buildDTO(source,list,map);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void getAllClassFields(Class<?> clazz,List<Field> list) {
		for(Field f:clazz.getDeclaredFields()){
			list.add(f);
		}
		if(clazz.getSuperclass()==Object.class){
			return;
		}
		getAllClassFields(clazz.getSuperclass(),list);
	}
	
	private void buildDTO(Object source,List<Field> ff,Map<String,Object> map) throws IllegalAccessException{
		for (Field f : ff) {
			try {
				if (f.getName().equals(LppzConstants.seriname))
					continue;
				f.setAccessible(true);
				f.set(source, map.get(f.getName()));
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
				if (f.getType() == Long.class || f.getType() == long.class) {
					f.set(source,
							Double.valueOf(map.get(f.getName()).toString()).longValue());
				}
				if (f.getType() == Double.class
						|| f.getType() == double.class) {
					f.set(source,
							Double.valueOf(map.get(f.getName()).toString()));
				}
				if (f.getType() == Integer.class
						|| f.getType() == int.class) {
					f.set(source,
							Double.valueOf(map.get(f.getName()).toString()).intValue());
				}
			}
		}
	}
}
 