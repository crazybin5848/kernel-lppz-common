package com.lppz.core.inittb;

public abstract interface InitTableInterface {
	public void initData();
	public static String[] JDBC_METADATA_TABLE_TYPES = {"TABLE"};
}
