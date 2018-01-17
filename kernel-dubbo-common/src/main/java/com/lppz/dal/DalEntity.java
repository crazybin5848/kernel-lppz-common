package com.lppz.dal;

import java.io.Serializable;
import java.util.Map;

public class DalEntity<T> implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameter;
	private T obj;
	private String dalKey;
	private int dalTsSize;
	public Map<String, Object> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}

	public DalEntity(T obj, String dalKey) {
		this.dalKey = dalKey;
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public String getDalKey() {
		return dalKey;
	}

	public void setDalKey(String dalKey) {
		this.dalKey = dalKey;
	}

	public int getDalTsSize() {
		return dalTsSize;
	}

	public void setDalTsSize(int dalTsSize) {
		this.dalTsSize = dalTsSize;
	}
}
