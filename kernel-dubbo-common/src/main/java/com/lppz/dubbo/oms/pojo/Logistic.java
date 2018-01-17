package com.lppz.dubbo.oms.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Logistic implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6625341185286919355L;

	private String id;

	@JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

    private String name;

    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

    private String code;

    private String typecode;

    private String tenant;

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
}