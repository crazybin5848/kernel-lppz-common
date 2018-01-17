package com.lppz.oms.kafka.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class InterfaceLogDto extends MyPageDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8203785492480460483L;
	private String in_messageId;
	
	/**
	 * 主键
	 */
	private String interfaceLogId;
	/**
	 * 发起方系统
	 */
	private String sourceType;//
	/**
	 * 接收方系统
	 */
	private String targetType;
	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 接口ID
	 */
	private String interfaceId;
	private String interfaceName;//接口名称
	/**
	 * 发送结果
	 * S 成功  F 失败
	 */
	private String resultFlag;
	/**
	 * 是否走PI通信
	 */
	private String piFlag;
	/**
	 * 处理开始时间
	 */
	private Date startDate;
	/**
	 * 处理结束时间
	 */
	private Date endDate;
	/**
	 * 接口日志异常明细表
	 */
	List<InterfaceLogDetailDto> detailList;

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
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the targetType
	 */
	public String getTargetType() {
		return targetType;
	}

	/**
	 * @param targetType the targetType to set
	 */
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the interfaceId
	 */
	public String getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the resultFlag
	 */
	public String getResultFlag() {
		return resultFlag;
	}

	/**
	 * @param resultFlag the resultFlag to set
	 */
	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	/**
	 * @return the piFlag
	 */
	public String getPiFlag() {
		return piFlag;
	}

	/**
	 * @param piFlag the piFlag to set
	 */
	public void setPiFlag(String piFlag) {
		this.piFlag = piFlag;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the detailList
	 */
	public List<InterfaceLogDetailDto> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList the detailList to set
	 */
	public void setDetailList(List<InterfaceLogDetailDto> detailList) {
		this.detailList = detailList;
	}

	/**
	 * @return the in_messageId
	 */
	public String getIn_messageId() {
		return in_messageId;
	}

	/**
	 * @param in_messageId the in_messageId to set
	 */
	public void setIn_messageId(String in_messageId) {
		this.in_messageId = in_messageId;
	}
	/**
	 * 验证接口日志调用方法判断
	 * @param dto
	 * @return
	 */
	public boolean validate(InterfaceLogDto dto){
		if(null == dto){
			return false;
		}
		if(null==dto.getInterfaceId()||null==dto.getMessageId()
				||null==dto.getPiFlag()||null==dto.getResultFlag()
				||null ==dto.getResultFlag() || null==dto.getSourceType()
				|| null==dto.getStartDate() || null == dto.getTargetType()
				|| null == dto.getInterfaceName()){
			return false;
		}
		return true;
	}
}
