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

public class OrderLine implements Serializable {
	public String getOrderlineid() {
		return orderlineid;
	}

	public void setOrderlineid(String orderlineid) {
		this.orderlineid = orderlineid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getPaymount() {
		return paymount;
	}

	public void setPaymount(String paymount) {
		this.paymount = paymount;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1139148241307719781L;
	private String orderlineid;
	private String sku;
	private String paymount;
	private List<OrderLineDetail> oldList=new ArrayList<OrderLineDetail>();
	public OrderLine(){}
	public List<OrderLineDetail> getOldList() {
		return oldList;
	}

	public void setOldList(List<OrderLineDetail> oldList) {
		this.oldList = oldList;
	}

	public OrderLine(String orderlineid, String sku, String paymount) {
		this.orderlineid = orderlineid;
		this.sku = sku;
		this.paymount = paymount;
	}
	
	public OrderLine build(ScanCascadeResult scr){
		if(scr==null)
		return this;
		this.orderlineid=Bytes.toString(scr.getSource().getRow()).split("_")[3];
		this.sku=scr.getSource().getQulifyerValueMap().get("sku");
		this.paymount=scr.getSource().getQulifyerValueMap().get("paymnt");
		if(!scr.getCascadeMap().isEmpty()){
			for(ScanCascadeResult sscr:scr.getCascadeMap().get("orderlinedetail")){
				OrderLineDetail old=new OrderLineDetail();
				this.oldList.add(old.build(sscr));
			}
		}
		return this;
	}
}
