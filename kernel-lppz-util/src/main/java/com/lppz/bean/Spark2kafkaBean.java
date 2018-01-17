package com.lppz.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import com.lppz.util.kryo.KryoUtil;

public class Spark2kafkaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1068081149385929168L;
	/**
	 * 
	 */
	private String esId;
	private String esIndex;
	private String esType;
	private Map<String,String> resultMap;
	public String getEsId() {
		return esId;
	}
	public void setEsId(String esId) {
		this.esId = esId;
	}
	public String getEsIndex() {
		return esIndex;
	}
	public void setEsIndex(String esIndex) {
		this.esIndex = esIndex;
	}
	public String getEsType() {
		return esType;
	}
	public void setEsType(String esType) {
		this.esType = esType;
	}
	public Map<String, String> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
	}
	
	public byte[] converByteByKryo() throws IOException{
			return KryoUtil.kyroSeriLize(this, -1);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Spark2kafkaBean [esId=");
		builder.append(esId);
		builder.append(", esIndex=");
		builder.append(esIndex);
		builder.append(", esType=");
		builder.append(esType);
		builder.append(", resultMap=");
		builder.append(resultMap);
		builder.append("]");
		return builder.toString();
	}
	
}
