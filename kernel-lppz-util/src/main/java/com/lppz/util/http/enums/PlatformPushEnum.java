package com.lppz.util.http.enums;
/**
 * 
 * Each platform could own enum at each level
 * Initial DangDang as an example,pls append JD,TAOBAO and so on .
 *
 */
public enum PlatformPushEnum {
	DPUSHEPROUCTENUM("updateMultiItemsStatus",".xml","sign",true),
	DPUSHSENDENUM("sendGoods",".xml","sign",true),
	DPUSHSTOCKENUM("multiItemsStock",".xml","sign",true);
	private String fileid;
	private String format;
	private String signKey;
	private boolean isUpperCase;
	PlatformPushEnum(String fileid,String format,String signKey,boolean isUpperCase){
		this.fileid=fileid;
		this.format=format;
		this.signKey=signKey;
		this.isUpperCase=isUpperCase;
	}
	public String fileid(){
		return fileid;
	}
	public String format(){
		return format;
	}
	public String signKey(){
		return signKey;
	}
	public boolean isUpperCase(){
		return isUpperCase;
	}
}
