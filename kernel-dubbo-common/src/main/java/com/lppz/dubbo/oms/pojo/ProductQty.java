package com.lppz.dubbo.oms.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class ProductQty implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4510479913517410778L;

	private String id;

	 @JSONField (format="yyyy-MM-dd HH:mm:ss") 
    private Date creationtime;

    private String product;

    private String bundleproduct;

    private Integer quatity;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    private String typecode;

    private String tenant;
    
    private Product productObject;
    
    private Product bundleProductObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(Date creationtime) {
        this.creationtime = creationtime;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getBundleproduct() {
        return bundleproduct;
    }

    public void setBundleproduct(String bundleproduct) {
        this.bundleproduct = bundleproduct == null ? null : bundleproduct.trim();
    }

    public Integer getQuatity() {
        return quatity;
    }

    public void setQuatity(Integer quatity) {
        this.quatity = quatity;
    }

    public Date getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Date modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode == null ? null : typecode.trim();
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant == null ? null : tenant.trim();
    }

	/**
	 * @return the productObject
	 */
	public Product getProductObject() {
		return productObject;
	}

	/**
	 * @param productObject the productObject to set
	 */
	public void setProductObject(Product productObject) {
		this.productObject = productObject;
	}

	/**
	 * @return the bundleProductObject
	 */
	public Product getBundleProductObject() {
		return bundleProductObject;
	}

	/**
	 * @param bundleProductObject the bundleProductObject to set
	 */
	public void setBundleProductObject(Product bundleProductObject) {
		this.bundleProductObject = bundleProductObject;
	}
    
}