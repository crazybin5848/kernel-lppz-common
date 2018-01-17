package com.lppz.oms.kafka.dto;

import java.io.Serializable;

public class InterfaceLogDetailDto extends MyPageDto implements Serializable{

	private static final long serialVersionUID = 3412834436977198470L;
	/**
	 * 主键
	 */
	private String interfaceLogDetailId;
	/**
	 * 接口日志ID
	 */
	private String interfaceLogId;
	/**
	 * 行号
	 */
	private String lineNo;
	/**
	 * 异常代码
	 */
	private String errorKey;
	/**
	 * 异常原因
	 */
	private String message;
	/**
	 * @return the interfaceLogDetailId
	 */
	public String getInterfaceLogDetailId() {
		return interfaceLogDetailId;
	}
	/**
	 * @param interfaceLogDetailId the interfaceLogDetailId to set
	 */
	public void setInterfaceLogDetailId(String interfaceLogDetailId) {
		this.interfaceLogDetailId = interfaceLogDetailId;
	}
	/**
	 * @return the interfaceLogId
	 */
	public String getInterfaceLogId() {
		return interfaceLogId;
	}
	/**
	 * @param interfaceLogId the interfaceLogId to set
	 */
	public void setInterfaceLogId(String interfaceLogId) {
		this.interfaceLogId = interfaceLogId;
	}
	/**
	 * @return the lineNo
	 */
	public String getLineNo() {
		return lineNo;
	}
	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	/**
	 * @return the errorKey
	 */
	public String getErrorKey() {
		return errorKey;
	}
	/**
	 * @param errorKey the errorKey to set
	 */
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
