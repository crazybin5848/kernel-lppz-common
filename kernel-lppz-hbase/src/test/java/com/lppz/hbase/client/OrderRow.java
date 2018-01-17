/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lppz.hbase.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.coprocessor.model.scan.ScanCascadeResult;
import org.apache.hadoop.hbase.util.Bytes;

public class OrderRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2135607482521590960L;
	private String orderId;
	private List<OrderLine> orderLineList=new ArrayList<OrderLine>();
	private List<OrderShipment> ordershipmentList=new ArrayList<OrderShipment>();
	private String uid;
	private String baseStoreId;
	private String outorderId;
	public String getOutorderId() {
		return outorderId;
	}
	public void setOutorderId(String outorderId) {
		this.outorderId = outorderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<OrderLine> getOrderLineList() {
		return orderLineList;
	}
	public void setOrderLineList(List<OrderLine> orderLineList) {
		this.orderLineList = orderLineList;
	}
	public List<OrderShipment> getOrdershipmentList() {
		return ordershipmentList;
	}
	public void setOrdershipmentList(List<OrderShipment> ordershipmentList) {
		this.ordershipmentList = ordershipmentList;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBaseStoreId() {
		return baseStoreId;
	}
	public void setBaseStoreId(String baseStoreId) {
		this.baseStoreId = baseStoreId;
	}
	
	public OrderRow build(ScanCascadeResult scr){
		if(scr==null)
		return this;
		String[] row=Bytes.toString(scr.getSource().getRow()).split("_");
		this.uid=row[2];
		this.orderId=scr.getSource().getQulifyerValueMap().get("oid");
		this.baseStoreId=scr.getSource().getQulifyerValueMap().get("storeid");
		this.outorderId=scr.getSource().getQulifyerValueMap().get("outorderid");
		if(!scr.getCascadeMap().isEmpty()){
			for(ScanCascadeResult sscr:scr.getCascadeMap().get("orderline")){
				OrderLine ol=new OrderLine();
				this.orderLineList.add(ol.build(sscr));
			}
			for(ScanCascadeResult sscr:scr.getCascadeMap().get("ordershipment")){
				OrderShipment os=new OrderShipment();
				this.ordershipmentList.add(os.build(sscr));
			}
		}
		return this;
	}
}
