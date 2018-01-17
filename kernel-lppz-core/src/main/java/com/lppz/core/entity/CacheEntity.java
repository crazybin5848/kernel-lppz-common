package com.lppz.core.entity;

import java.io.Serializable;
import java.util.List;

import com.lppz.core.entity.DalEntity;

public class CacheEntity implements Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DalEntity entity;
	private String entityName;
	public String getEntityName() {
		return entityName;
	}
	private transient List<Index> listIndex;
	public DalEntity getEntity() {
		return entity;
	}
	public List<Index> getListIndex() {
		return listIndex;
	}
	public CacheEntity(DalEntity entity,List<Index> listIndex,String entityName){
		this.entity=entity;
		this.entityName=entityName;
		this.listIndex=listIndex;
	}
}
