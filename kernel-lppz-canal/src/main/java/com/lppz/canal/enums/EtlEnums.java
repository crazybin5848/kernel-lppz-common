package com.lppz.canal.enums;


public enum EtlEnums {

	ETRACT(1, "ETRACT"), TRANSFORM(2, "TRANSFORM"), LOAD(3, "LOAD");

	private int value;
	private String name;

	EtlEnums(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static EtlEnums valueOf(int value) {
	      switch (value) {
	        case 1: return ETRACT;
	        case 2: return TRANSFORM;
	        case 3: return LOAD;
	        default: return null;
	      }
	    }
}
