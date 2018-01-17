package com.lppz.dubbo.oms.init;

import java.io.Serializable;

import com.lppz.dubbo.oms.init.enums.AreaEnum;
import com.lppz.dubbo.oms.init.enums.BaseEnum;
import com.lppz.dubbo.oms.init.enums.BaseStoreEnum;
import com.lppz.dubbo.oms.init.enums.LogisticEnum;
import com.lppz.dubbo.oms.init.enums.ProductEnum;
import com.lppz.dubbo.oms.init.enums.StockRoomLocationEnum;

public class CacheParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8939168467675773519L;
	private String key;
	private BaseEnum type;
	private String clazz;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public BaseEnum getType() {
		return type;
	}
	public void setType(BaseEnum type) {
		this.type = type;
		if(type!=null)
		this.clazz=type.getClass().getName();
	}
	
	public CacheParam(String key,BaseEnum type){
		this.key=key;
		this.type=type;
		if(type!=null)
		this.clazz=type.getClass().getName();
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public CacheParam(){
	}
	
	public BaseEnum build(){
		if(clazz!=null&&type instanceof BaseEnumBuilder){
			BaseEnumBuilder beb=(BaseEnumBuilder)type;
			if(clazz.equals(AreaEnum.class.getName())){
				return AreaEnum.valueOf(beb.getType());
			}
			if(clazz.equals(BaseStoreEnum.class.getName())){
				return BaseStoreEnum.valueOf(beb.getType());
			}
			if(clazz.equals(LogisticEnum.class.getName())){
				return LogisticEnum.valueOf(beb.getType());
			}
			if(clazz.equals(ProductEnum.class.getName())){
				return ProductEnum.valueOf(beb.getType());
			}
			if(clazz.equals(StockRoomLocationEnum.class.getName())){
				return StockRoomLocationEnum.valueOf(beb.getType());
			}
		}
		return type;
	}
}