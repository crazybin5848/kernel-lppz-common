package com.lppz.dubbo.oms.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseStore implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6426903116950825570L;

	private String id;
    
    private String orgnizationId;

    private Integer enableWarning;

    private Double floorprice;

    private String storeAccount;

    private Integer invertoryUpdateActive;

    private BigDecimal version;

    private String outCode;

    private Integer orderRetrieveActive;

    private Integer isactive;

    private String description;

    private String name;

    private Double refprice;

    private Integer enableAutoSplit;

    private Integer islbpmode;

    private Integer connectStatus;

    private String storeCode;

    private String code;

    private String storeType;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    private Integer orderRetrieveInterval;

    private String channel;

    public int threadNum;
    
    private Integer crmneed;

	private Integer selfrun; 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(String orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public Integer getEnableWarning() {
		return enableWarning;
	}

	public void setEnableWarning(Integer enableWarning) {
		this.enableWarning = enableWarning;
	}

	public Double getFloorprice() {
        return floorprice;
    }

    public void setFloorprice(Double floorprice) {
        this.floorprice = floorprice;
    }

    public String getStoreAccount() {
        return storeAccount;
    }

    public void setStoreAccount(String storeAccount) {
        this.storeAccount = storeAccount == null ? null : storeAccount.trim();
    }

    public Integer getInvertoryUpdateActive() {
		return invertoryUpdateActive;
	}

	public void setInvertoryUpdateActive(Integer invertoryUpdateActive) {
		this.invertoryUpdateActive = invertoryUpdateActive;
	}

	public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}

	public Integer getOrderRetrieveActive() {
		return orderRetrieveActive;
	}

	public void setOrderRetrieveActive(Integer orderRetrieveActive) {
		this.orderRetrieveActive = orderRetrieveActive;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRefprice() {
		return refprice;
	}

	public void setRefprice(Double refprice) {
		this.refprice = refprice;
	}

	public Integer getEnableAutoSplit() {
		return enableAutoSplit;
	}

	public void setEnableAutoSplit(Integer enableAutoSplit) {
		this.enableAutoSplit = enableAutoSplit;
	}

	public Integer getIslbpmode() {
		return islbpmode;
	}

	public void setIslbpmode(Integer islbpmode) {
		this.islbpmode = islbpmode;
	}

	public Integer getConnectStatus() {
		return connectStatus;
	}

	public void setConnectStatus(Integer connectStatus) {
		this.connectStatus = connectStatus;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public Date getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}

	public Date getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	public Integer getOrderRetrieveInterval() {
		return orderRetrieveInterval;
	}

	public void setOrderRetrieveInterval(Integer orderRetrieveInterval) {
		this.orderRetrieveInterval = orderRetrieveInterval;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public Integer getCrmneed() {
		return crmneed;
	}

	public void setCrmneed(Integer crmneed) {
		this.crmneed = crmneed;
	}

	public Integer getSelfrun() {
		return selfrun;
	}

	public void setSelfrun(Integer selfrun) {
		this.selfrun = selfrun;
	}
}