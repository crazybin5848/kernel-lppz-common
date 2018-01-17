package com.lppz.mongoapi.enums;

public enum OpResultEnums {
	SUCCESS(1,"success"),FAIL(0,"fail"),EXCEPTION(-1,"exception");
	
	private int value;
	private String desc;
	
	private OpResultEnums(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static OpResultEnums convertOf(int value){
		OpResultEnums[] values = values();
		for (OpResultEnums result : values) {
			if (result.getValue()==value) {
				return result;
			}
		}
		return null;
	}

}
