package com.lppz.dubbo.oms.pojo;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.alibaba.fastjson.annotation.JSONField;
import com.lppz.serialize.ProductDeserializer;
import com.lppz.serialize.ProductSerializer;
@JsonDeserialize(using = ProductDeserializer.class) 
@JsonSerialize(using = ProductSerializer.class) 
public class Product implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6322551733429135567L;

	private String id;

    private Integer isgift;

    private String status;

    private Double floorprice;

    private Integer isbundle;

    private String productcode;

    private String barcode;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")  
    private Date creationtime;

    private String supercategories;

    private String productid;

    private String name;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    private Double refprice;

    private String typecode;

    private String tenant;
    
    private String unit;
    
    private List<ProductLink> productLinks;
    
    private List<ProductQty> productQties;
    
    private String materialtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getIsgift() {
        return isgift;
    }

    public void setIsgift(Integer isgift) {
        this.isgift = isgift;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Double getFloorprice() {
        return floorprice;
    }

    public void setFloorprice(Double floorprice) {
        this.floorprice = floorprice;
    }

    public Integer getIsbundle() {
        return isbundle;
    }

    public void setIsbundle(Integer isbundle) {
        this.isbundle = isbundle;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode == null ? null : productcode.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public Date getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(Date creationtime) {
        this.creationtime = creationtime;
    }

    public String getSupercategories() {
        return supercategories;
    }

    public void setSupercategories(String supercategories) {
        this.supercategories = supercategories == null ? null : supercategories.trim();
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Date modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public Double getRefprice() {
        return refprice;
    }

    public void setRefprice(Double refprice) {
        this.refprice = refprice;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the productLinks
	 */
	public List<ProductLink> getProductLinks() {
		return productLinks;
	}

	/**
	 * @param productLinks the productLinks to set
	 */
	public void setProductLinks(List<ProductLink> productLinks) {
		this.productLinks = productLinks;
	}

	/**
	 * @return the productQties
	 */
	public List<ProductQty> getProductQties() {
		return productQties;
	}

	/**
	 * @param productQties the productQties to set
	 */
	public void setProductQties(List<ProductQty> productQties) {
		this.productQties = productQties;
	}

	public String getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(String materialtype) {
		this.materialtype = materialtype;
	}
}