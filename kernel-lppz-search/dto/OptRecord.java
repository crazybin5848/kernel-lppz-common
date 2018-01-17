package com.lppz.oms.kafka.dto;

import java.util.Date;

public class OptRecord extends MyPageDto{
	private String funcname;
	private String optType;
	private String userId;
	private String userName;
	private String ipAddr;
	private String beforMsg;
	private String afterMsg;
	private String remark;
	private Date optTime;
	/**
	 * @return the funcname
	 */
	public String getFuncname() {
		return funcname;
	}
	/**
	 * @param funcname the funcname to set
	 */
	public void setFuncname(String funcname) {
		this.funcname = funcname;
	}
	/**
	 * @return the optType
	 */
	public String getOptType() {
		return optType;
	}
	/**
	 * @param optType the optType to set
	 */
	public void setOptType(String optType) {
		this.optType = optType;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}
	/**
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	/**
	 * @return the beforMsg
	 */
	public String getBeforMsg() {
		return beforMsg;
	}
	/**
	 * @param beforMsg the beforMsg to set
	 */
	public void setBeforMsg(String beforMsg) {
		this.beforMsg = beforMsg;
	}
	/**
	 * @return the afterMsg
	 */
	public String getAfterMsg() {
		return afterMsg;
	}
	/**
	 * @param afterMsg the afterMsg to set
	 */
	public void setAfterMsg(String afterMsg) {
		this.afterMsg = afterMsg;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the optTime
	 */
	public Date getOptTime() {
		return optTime;
	}
	/**
	 * @param optTime the optTime to set
	 */
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
	
}
