package com.lppz.dubbo.oms.init;

import com.lppz.dubbo.oms.init.enums.BaseEnum;

public class BaseEnumBuilder implements BaseEnum{

	/**
	 * 
	 */
	private static final long serialVersionUID = 447759305832990684L;
	private String type;
	public BaseEnumBuilder(){
	}
	public BaseEnumBuilder(String type){
		this.type=type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
