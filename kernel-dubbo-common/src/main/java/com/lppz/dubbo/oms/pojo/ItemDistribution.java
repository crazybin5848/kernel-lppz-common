package com.lppz.dubbo.oms.pojo;

public class ItemDistribution implements java.io.Serializable{
	public final static long serialVersionUID = 38224369L;

	private String itemId;

	private BaseStore store;

	private String storeName;

	private String orgnizationId;

	private String orgnizationName;

	private String itemName;

	private int percentage;

	private int threshhold;

	private int atp;

	private boolean warningSent = false;

	private boolean isBundle = false;

	private String updateTime;

	private int bundleQty;

	
	public ItemDistribution(){}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getItemId()
	{
		return itemId;
	}

	/**
	* sets 
	*
	*/
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}

	/**
	* gets 
	*
	* @returns BaseStore
	*/
	public BaseStore getStore()
	{
		return store;
	}

	/**
	* sets 
	*
	*/
	public void setStore(BaseStore store)
	{
		this.store = store;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStoreName()
	{
		return storeName;
	}

	/**
	* sets 
	*
	*/
	public void setStoreName(String storeName)
	{
		this.storeName = storeName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrgnizationId()
	{
		return orgnizationId;
	}

	/**
	* sets 
	*
	*/
	public void setOrgnizationId(String orgnizationId)
	{
		this.orgnizationId = orgnizationId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrgnizationName()
	{
		return orgnizationName;
	}

	/**
	* sets 
	*
	*/
	public void setOrgnizationName(String orgnizationName)
	{
		this.orgnizationName = orgnizationName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getItemName()
	{
		return itemName;
	}

	/**
	* sets 
	*
	*/
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getPercentage()
	{
		return percentage;
	}

	/**
	* sets 
	*
	*/
	public void setPercentage(int percentage)
	{
		this.percentage = percentage;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getThreshhold()
	{
		return threshhold;
	}

	/**
	* sets 
	*
	*/
	public void setThreshhold(int threshhold)
	{
		this.threshhold = threshhold;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getAtp()
	{
		return atp;
	}

	/**
	* sets 
	*
	*/
	public void setAtp(int atp)
	{
		this.atp = atp;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getWarningSent()
	{
		return warningSent;
	}

	/**
	* sets 
	*
	*/
	public void setWarningSent(boolean warningSent)
	{
		this.warningSent = warningSent;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getIsBundle()
	{
		return isBundle;
	}

	/**
	* sets 
	*
	*/
	public void setIsBundle(boolean isBundle)
	{
		this.isBundle = isBundle;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getUpdateTime()
	{
		return updateTime;
	}

	/**
	* sets 
	*
	*/
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getBundleQty()
	{
		return bundleQty;
	}

	/**
	* sets 
	*
	*/
	public void setBundleQty(int bundleQty)
	{
		this.bundleQty = bundleQty;
	}

	@Override
	public String toString() {
		return "ItemDistribution [itemId=" + itemId + ", store=" + store
				+ ", storeName=" + storeName + ", orgnizationId="
				+ orgnizationId + ", orgnizationName=" + orgnizationName
				+ ", itemName=" + itemName + ", percentage=" + percentage
				+ ", threshhold=" + threshhold + ", atp=" + atp
				+ ", warningSent=" + warningSent + ", isBundle=" + isBundle
				+ ", updateTime=" + updateTime + ", bundleQty=" + bundleQty
				+ "]";
	}
}
