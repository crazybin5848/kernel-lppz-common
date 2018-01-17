package com.lppz.hbase.client;

import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanCascadeResult;
import org.apache.hadoop.hbase.util.Bytes;

public class OrderShipment {
private String addr;
private String pcode;
public String getAddr() {
	return addr;
}
public void setAddr(String addr) {
	this.addr = addr;
}
public String getPcode() {
	return pcode;
}
public void setPcode(String pcode) {
	this.pcode = pcode;
}
public OrderShipment build(ScanCascadeResult scr){
	if(scr==null)
		return this;
	this.addr=scr.getSource().getQulifyerValueMap().get("addr");
	this.pcode=scr.getSource().getQulifyerValueMap().get("pcode");
	return this;
}
}
