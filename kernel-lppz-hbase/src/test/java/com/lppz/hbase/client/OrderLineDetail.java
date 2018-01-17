package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanCascadeResult;
import org.apache.hadoop.hbase.util.Bytes;

public class OrderLineDetail {
private String desc;
private String stock;
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}
public String getStock() {
	return stock;
}
public void setStock(String stock) {
	this.stock = stock;
}

public OrderLineDetail build(ScanCascadeResult scr){
	if(scr==null)
		return this;
	this.desc=scr.getSource().getQulifyerValueMap().get("desc");
	this.stock=scr.getSource().getQulifyerValueMap().get("stock");
	return this;
}
}
