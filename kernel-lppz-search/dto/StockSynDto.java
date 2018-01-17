package com.lppz.oms.kafka.dto;

import java.util.Date;

public class StockSynDto extends MyPageDto{

	private String productId;//产品编码
	private String whouse;//出库id或事业部id
	private String stockType;//库存类型 1:事业部，2：实物
	private String inOutFlag;//进出方式 1：进，2：出
	private String synWay;//同步方式 1:增量，2：全量
	private String incrementSynWay;//增量同步方式 1：增，2：减
	private int synQuantity;//同步数量
	private Date synDate;//同步日期
	private String extend1;//扩展字段
	private String extend2;//扩展字段
	private String extend3;//扩展字段

	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the whouse
	 */
	public String getWhouse() {
		return whouse;
	}
	/**
	 * @param whouse the whouse to set
	 */
	public void setWhouse(String whouse) {
		this.whouse = whouse;
	}
	/**
	 * @return the stockType
	 */
	public String getStockType() {
		return stockType;
	}
	/**
	 * @param stockType the stockType to set
	 */
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}
	/**
	 * @return the inOutFlag
	 */
	public String getInOutFlag() {
		return inOutFlag;
	}
	/**
	 * @param inOutFlag the inOutFlag to set
	 */
	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}
	/**
	 * @return the synWay
	 */
	public String getSynWay() {
		return synWay;
	}
	/**
	 * @param synWay the synWay to set
	 */
	public void setSynWay(String synWay) {
		this.synWay = synWay;
	}
	/**
	 * @return the incrementSynWay
	 */
	public String getIncrementSynWay() {
		return incrementSynWay;
	}
	/**
	 * @param incrementSynWay the incrementSynWay to set
	 */
	public void setIncrementSynWay(String incrementSynWay) {
		this.incrementSynWay = incrementSynWay;
	}
	/**
	 * @return the synQuantity
	 */
	public int getSynQuantity() {
		return synQuantity;
	}
	/**
	 * @param synQuantity the synQuantity to set
	 */
	public void setSynQuantity(int synQuantity) {
		this.synQuantity = synQuantity;
	}
	/**
	 * @return the synDate
	 */
	public Date getSynDate() {
		return synDate;
	}
	/**
	 * @param synDate the synDate to set
	 */
	public void setSynDate(Date synDate) {
		this.synDate = synDate;
	}
	/**
	 * @return the extend1
	 */
	public String getExtend1() {
		return extend1;
	}
	/**
	 * @param extend1 the extend1 to set
	 */
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	/**
	 * @return the extend2
	 */
	public String getExtend2() {
		return extend2;
	}
	/**
	 * @param extend2 the extend2 to set
	 */
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	/**
	 * @return the extend3
	 */
	public String getExtend3() {
		return extend3;
	}
	/**
	 * @param extend3 the extend3 to set
	 */
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	
	
}
