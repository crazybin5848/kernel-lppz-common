package com.lppz.dubbo.oms.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class ProductLink implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6119093166470909649L;

	private String id;

	 @JSONField (format="yyyy-MM-dd HH:mm:ss")  
    private Date creationtime;

    private String product;

    private String storecode;

    @JSONField (format="yyyy-MM-dd HH:mm:ss") 
    private Date modifiedtime;

    private String code;

    private String typecode;

    private String tenant;
    
    private Product productObject;

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

    public String getStorecode() {
        return storecode;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode == null ? null : storecode.trim();
    }

    public Date getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Date modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
    
}