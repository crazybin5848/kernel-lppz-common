package com.lppz.oms.kafka.dto;

import java.text.SimpleDateFormat;
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
	private String optTime;
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
	public String getOptTime() {
		return optTime;
	}
	/**
	 * @param optTime the optTime to set
	 */
	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	public void buildTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(optTime!=null){
				this.optTime=format.format(new Date(Long.parseLong(optTime)));
			}
		} catch (NumberFormatException e) {
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OptRecord [funcname=");
		builder.append(funcname);
		builder.append(", optType=");
		builder.append(optType);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", ipAddr=");
		builder.append(ipAddr);
		builder.append(", beforMsg=");
		builder.append(beforMsg);
		builder.append(", afterMsg=");
		builder.append(afterMsg);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", optTime=");
		builder.append(optTime);
		builder.append("]");
		return builder.toString();
	}
	
}
