package com.lppz.elasticsearch.disruptor.scroll;

import java.io.Serializable;

public class EsScrollModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8044207044244425857L;
	private String index;
	private String type;
	private String termFiled;
	public String getTermFiled() {
		return termFiled;
	}
	public void setTermFiled(String termFiled) {
		this.termFiled = termFiled;
	}
	private String termValue;
	
	public EsScrollModel(){}
	public EsScrollModel(String index,String type,String termFiled,String termValue){
		this.index=index;
		this.type=type;
		this.termFiled=termFiled;
		this.termValue=termValue;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTermValue() {
		return termValue;
	}
	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}
}