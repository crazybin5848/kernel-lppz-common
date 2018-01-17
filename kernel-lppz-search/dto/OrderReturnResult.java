package com.lppz.oms.kafka.dto;

import java.util.Date;

public class OrderReturnResult extends MyPageDto{
	private String orderId;
	private String seq;
	private String result;
	private String resultFlag;
	private String send;
	private String receive;
	private Date receiveTime;
	private String outSys;
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
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
	 * @return the send
	 */
	public String getSend() {
		return send;
	}
	/**
	 * @param send the send to set
	 */
	public void setSend(String send) {
		this.send = send;
	}
	/**
	 * @return the receive
	 */
	public String getReceive() {
		return receive;
	}
	/**
	 * @param receive the receive to set
	 */
	public void setReceive(String receive) {
		this.receive = receive;
	}
	/**
	 * @return the receiveTime
	 */
	public Date getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return the outSys
	 */
	public String getOutSys() {
		return outSys;
	}
	/**
	 * @param outSys the outSys to set
	 */
	public void setOutSys(String outSys) {
		this.outSys = outSys;
	}
	
	
	
}
