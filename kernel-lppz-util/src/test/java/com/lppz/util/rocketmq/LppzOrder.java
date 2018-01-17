package com.lppz.util.rocketmq;

import java.io.Serializable;

public class LppzOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2075382556137651904L;
	private String orderId;
	private Integer shardId;
	private String user;
	private String phone;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getShardId() {
		return shardId;
	}
	public void setShardId(Integer shardId) {
		this.shardId = shardId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return this.orderId+":"+this.phone+":"+this.user+":"+this.shardId;
	}
}
