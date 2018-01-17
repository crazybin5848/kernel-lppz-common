package com.lppz.oms.kafka.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LppzExBo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String desc;
	private String stackTrace;
	private String ip;
	private String text1;
	private String text2;
	private String createtime;
	private Long id;
	private int sendflag;
	private Map<String,Object> params;
	private String paramStrings;
	public String getParamStrings() {
		return paramStrings;
	}
	public void setParamStrings(String paramStrings) {
		this.paramStrings = paramStrings;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSendflag() {
		return sendflag;
	}
	public void setSendflag(int sendflag) {
		this.sendflag = sendflag;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public void buildTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(createtime!=null){
				this.createtime=format.format(new Date(Long.parseLong(createtime)));
			}
		} catch (NumberFormatException e) {
		}
	}
	
	public void convert2paramString(){
		StringBuilder sb=new StringBuilder("");
		if(params!=null&&!params.isEmpty()){
			for(String k:params.keySet()){
				sb.append(k).append("=").append(params.get(k)).append(",");
			}
			paramStrings=sb.substring(0,sb.length()-1);
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LppzExBo [name=");
		builder.append(name);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", stackTrace=");
		builder.append(stackTrace);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", text1=");
		builder.append(text1);
		builder.append(", text2=");
		builder.append(text2);
		builder.append(", createtime=");
		builder.append(createtime);
		builder.append(", id=");
		builder.append(id);
		builder.append(", sendflag=");
		builder.append(sendflag);
		builder.append(", params=");
		builder.append(params);
		builder.append(", paramStrings=");
		builder.append(paramStrings);
		builder.append("]");
		return builder.toString();
	}
	
	
}
