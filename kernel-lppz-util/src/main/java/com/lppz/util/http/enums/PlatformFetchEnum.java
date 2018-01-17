package com.lppz.util.http.enums;
/**
 * 
 * Each platform could own enum at each level
 * Initial DangDang as an example,pls append JD,TAOBAO and so on .
 *
 */
public enum PlatformFetchEnum {
	DFETCHORDERENUM("p","pageSize","sign"),
	YOUZANFETCHORDERENUM("page_no","page_size","sign");
	private String  pageNo;
	private String pageSize;
	private String sign;
	PlatformFetchEnum(String pageNo,String pageSize,String sign){
		this.pageNo=pageNo;
		this.pageSize=pageSize;
		this.sign=sign;
	
	}
	public String pageNoName(){
		return pageNo;
	}
	public String pageSizeName(){
		return pageSize;
	}
	public String sign(){
		return sign;
	}

	
}
