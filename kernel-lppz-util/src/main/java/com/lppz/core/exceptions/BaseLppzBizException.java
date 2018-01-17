package com.lppz.core.exceptions;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.lppz.bean.RollbackMsgs;
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLppzBizException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6231055223611382899L;

	private String key;
	
	private String keySource;
	
	private String module;
	
	private String compensationUrl;
	
	private Map<String,Object> compensationMsgMap;
	
	@Deprecated
	private RollbackMsgs rollbackMsgs;

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKeySource() {
		return keySource;
	}
	
	public void setKeySource(String keySource) {
		this.keySource = keySource;
	}
	
	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	
	@Deprecated
	public RollbackMsgs getRollbackMsgs() {
		return rollbackMsgs;
	}

	@Deprecated
	public void setRollbackMsgs(RollbackMsgs rollbackMsgs) {
		this.rollbackMsgs = rollbackMsgs;
	}
	
	public String getCompensationUrl() {
		return compensationUrl;
	}

	public void setCompensationUrl(String compensationUrl) {
		this.compensationUrl = compensationUrl;
	}

	public Map<String, Object> getCompensationMsgMap() {
		return compensationMsgMap;
	}

	public void setCompensationMsgMap(Map<String, Object> compensationMsgMap) {
		this.compensationMsgMap = compensationMsgMap;
	}

	public BaseLppzBizException() {
	}
	
	public BaseLppzBizException(String message) {
		super(message);
	}
	
	public BaseLppzBizException(Throwable cause) {
		super(cause);
	}
	
	public BaseLppzBizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseLppzBizException(final String message, final String key, final String keySource, final String module, Map<String, Object> compensationMsgMap, final Throwable cause)
	{
		super(message, cause);
		this.setKey(key);
		this.setKeySource(keySource);
		this.setModule(module);
		this.setCompensationMsgMap(compensationMsgMap);
	}
	
	public BaseLppzBizException(String key, String keySource, String module,
			Throwable cause) {
		super(cause);
		this.setKey(key);
		this.setKeySource(keySource);
		this.setModule(module);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseLppzBizException [key=");
		builder.append(key);
		builder.append(", keySource=");
		builder.append(keySource);
		builder.append(", module=");
		builder.append(module);
		builder.append(", compensationUrl=");
		builder.append(compensationUrl);
		builder.append(", compensationMsgMap=");
		builder.append(compensationMsgMap);
		builder.append(", rollbackMsgs=");
		builder.append(rollbackMsgs);
		builder.append("]");
		return builder.toString();
	}
}
