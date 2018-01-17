package com.lppz.util.rocketmq.enums;

/**
*
* rocketMq producer类型
*/
public enum RocketMqProducerTypeEnums {

	ORDERLY("ORDERLY")
	,CONCURRENCY("CONCURRENCY");
	
	private String des;

	/**
	 *
	 */
	private RocketMqProducerTypeEnums(final String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
