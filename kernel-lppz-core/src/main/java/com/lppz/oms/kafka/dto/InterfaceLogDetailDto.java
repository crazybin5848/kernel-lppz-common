package com.lppz.oms.kafka.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	private String startDate;
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void buildTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(startDate!=null){
				this.startDate=format.format(new Date(Long.parseLong(startDate)));
			}
		} catch (NumberFormatException e) {
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InterfaceLogDetailDto [interfaceLogDetailId=");
		builder.append(interfaceLogDetailId);
		builder.append(", interfaceLogId=");
		builder.append(interfaceLogId);
		builder.append(", lineNo=");
		builder.append(lineNo);
		builder.append(", errorKey=");
		builder.append(errorKey);
		builder.append(", message=");
		builder.append(message);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append("]");
		return builder.toString();
	}

}
