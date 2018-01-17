/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.lppz.oms.kafka.event;

import java.io.Serializable;


/**
 *
 */
public class KafkaLppzEvent implements Serializable
{
	@Override
	public String toString() {
		return ienum.name()+":"+ipPort;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 848171064908771528L;
	public KafkaLppzEvent(String ipPort,InitComponentEnum ienum){
		this.ipPort=ipPort;
		this.ienum=ienum;
	}
	public KafkaLppzEvent(){
	}
	 public enum InitComponentEnum {
		 Product,Area,Basestore,Logistic,StockRoomLocation;
	 }

	private InitComponentEnum ienum;
	public InitComponentEnum getIenum() {
		return ienum;
	}

	public void setIenum(InitComponentEnum ienum) {
		this.ienum = ienum;
	}

	private String ipPort;


	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
}
