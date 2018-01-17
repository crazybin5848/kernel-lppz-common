package com.lppz.oms.kafka.dto;

import java.util.Date;

public class ProductSynDto extends MyPageDto{
	private String productId;//商品编码
	private String productName;//商品名称
	private String productCode;//商家编码
	private String productPrice;//商品参考价格
	private String lowesPrice;//最低价格
	private Date synDate;//同步时间
	private String extend1;//扩展字段
	private String extend2;
	private String extend3;
	private String extend4;
	private String extend5;
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the productPrice
	 */
	public String getProductPrice() {
		return productPrice;
	}
	/**
	 * @param productPrice the productPrice to set
	 */
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	/**
	 * @return the lowesPrice
	 */
	public String getLowesPrice() {
		return lowesPrice;
	}
	/**
	 * @param lowesPrice the lowesPrice to set
	 */
	public void setLowesPrice(String lowesPrice) {
		this.lowesPrice = lowesPrice;
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
	/**
	 * @return the extend4
	 */
	public String getExtend4() {
		return extend4;
	}
	/**
	 * @param extend4 the extend4 to set
	 */
	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}
	/**
	 * @return the extend5
	 */
	public String getExtend5() {
		return extend5;
	}
	/**
	 * @param extend5 the extend5 to set
	 */
	public void setExtend5(String extend5) {
		this.extend5 = extend5;
	}
	
}
