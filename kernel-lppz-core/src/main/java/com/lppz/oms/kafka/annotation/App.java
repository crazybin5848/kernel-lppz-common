/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 调用App 枚举
 * 
 * @author xuwencai@lppz.com
 * @version 1.0
 */
public enum App {
	
	ORDER_CANCEL(1, "order-cancel", "取消订单"),
	ORDER_UPDATE(2, "order-update", "修改订单"),
	ORDER_RETURN(3, "order-return", "订单退货"),
	ORDER_MERGE(4, "order-merge", "合并订单"),
	ORDER_SPLIT(5, "order-split", "拆分订单"),
	ORDER_DRAWBACK(6, "order-drawback", "退款"),
	
	ORDER_FLOW(7,"order-flow","订单工作流"),
	COMMON_COMPONENT(8,"common-component","公共组件"),
	OUT_ORDER_MERGE_POOL(9, "out_order_merge_pool", "出合单池"),
	ORDER_SOURCING(10, "order-sourcing", "寻源"),
	ORDER_REPLENISH(11, "order-replenish", "补发单"),
	DISTRIBUT_COUNT_ORDER(12, "disCountOrder", "分销汇总订单"),
	DISTRIBUT_RETURN_ORDER(13, "disReturnOrder", "分销退货订单"),
	TEMPORDER(14, "tempOrder", "临时订单"),
	order_claim(15, "orderClaim", "认领");
	private static Map<Integer, App> map = new HashMap<Integer, App>();
	static{
		for(App app : App.values()){
			map.put(app.getId(), app);
		}
	}
	
	/**
	 * 根据id获取app
	 */	
	public static App getById(Integer id){
		return map.get(id);
	}
	
	public static List<App> getAll(){
		List<App> result = new ArrayList<App>();
		for(App app : App.values()){
			result.add(app);
		}
		return result;
	}
	
	private Integer id;
	private String name;
	private String desc;
	
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	private App(Integer id, String name, String desc){
		this.id = id;
		this.name = name;
		this.desc = desc;
	} 
}
