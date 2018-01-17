package io.mycat.sencondaryindex.model;

import java.io.Serializable;

public class Dal2ndIdxKafkaModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5766275687549628151L;
	private String dto;
	private String esId;
	private String param;
	public String getDto() {
		return dto;
	}
	public void setDto(String dto) {
		this.dto = dto;
	}
	public String getEsId() {
		return esId;
	}
	public void setEsId(String esId) {
		this.esId = esId;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public Dal2ndIdxKafkaModel(){}
	public Dal2ndIdxKafkaModel(String dto,String param,String esId){
		this.dto=dto;
		this.param=param;
		this.esId=esId;
	}
}