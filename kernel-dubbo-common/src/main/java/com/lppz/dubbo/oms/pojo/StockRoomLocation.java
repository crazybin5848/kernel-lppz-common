package com.lppz.dubbo.oms.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;


public class StockRoomLocation implements Serializable{
	private static final long serialVersionUID = 1371704217684386732L;

	private String locationId;

	private String description;

	private int priority;

	private String storeName;

	private String taxAreaId;

	private int absoluteInventoryThreshold;

	private int percentageInventoryThreshold;

	private boolean usePercentageThreshold;

	private boolean active = true;

	@JSONField (format="yyyy-MM-dd HH:mm:ss") 
	private Date creationTime;

	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Date modifiedTime;

	private Set<String> baseStores = new HashSet<String>();

	private Set<String> shipToCountriesCodes = new HashSet<String>();

	private List<Logistic> logistics = new ArrayList<Logistic>();

	private List<ProductCategory> productCategories = new ArrayList<ProductCategory>();

	private String recMan;

	private String recTel;

	private String wmsName;

	private boolean sendSwitch;

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * @return the taxAreaId
	 */
	public String getTaxAreaId() {
		return taxAreaId;
	}

	/**
	 * @param taxAreaId the taxAreaId to set
	 */
	public void setTaxAreaId(String taxAreaId) {
		this.taxAreaId = taxAreaId;
	}

	public int getAbsoluteInventoryThreshold() {
		return absoluteInventoryThreshold;
	}

	public void setAbsoluteInventoryThreshold(int absoluteInventoryThreshold) {
		this.absoluteInventoryThreshold = absoluteInventoryThreshold;
	}

	public int getPercentageInventoryThreshold() {
		return percentageInventoryThreshold;
	}

	public void setPercentageInventoryThreshold(int percentageInventoryThreshold) {
		this.percentageInventoryThreshold = percentageInventoryThreshold;
	}

	public boolean isUsePercentageThreshold() {
		return usePercentageThreshold;
	}

	public void setUsePercentageThreshold(boolean usePercentageThreshold) {
		this.usePercentageThreshold = usePercentageThreshold;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Set<String> getBaseStores() {
		return baseStores;
	}

	public void setBaseStores(Set<String> baseStores) {
		this.baseStores = baseStores;
	}

	public Set<String> getShipToCountriesCodes() {
		return shipToCountriesCodes;
	}

	public void setShipToCountriesCodes(Set<String> shipToCountriesCodes) {
		this.shipToCountriesCodes = shipToCountriesCodes;
	}

	public List<Logistic> getLogistics() {
		return logistics;
	}

	public void setLogistics(List<Logistic> logistics) {
		this.logistics = logistics;
	}

	public List<ProductCategory> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(List<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	public String getRecMan() {
		return recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
	}

	public String getRecTel() {
		return recTel;
	}

	public void setRecTel(String recTel) {
		this.recTel = recTel;
	}

	public String getWmsName() {
		return wmsName;
	}

	public void setWmsName(String wmsName) {
		this.wmsName = wmsName;
	}

	public boolean isSendSwitch() {
		return sendSwitch;
	}

	public void setSendSwitch(boolean sendSwitch) {
		this.sendSwitch = sendSwitch;
	}
}
