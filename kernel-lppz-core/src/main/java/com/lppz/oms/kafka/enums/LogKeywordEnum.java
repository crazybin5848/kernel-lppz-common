package com.lppz.oms.kafka.enums;

/**
 * 业务日志关键字枚举类
 * @author licheng
 *
 */
public enum LogKeywordEnum {
	/**
	 * 订单日志关键字
	 */
	ORDERLOG("order"),
	/**
	 * 方法注解日志关键字
	 */
	ANNOTATION("annotation"),
	/**
	 * 接口日志关键字
	 */
	INTERFACELOG ("interface"),
	/**
	 * 工作流关键字
	 */
	ORDERWORKFLOW ("workflow"),
	/**
	 * 订单报缺关键字
	 */
	LACKORDER ("lackorder"),
	/**
	 * 发货关键字
	 */
	OMS2WMS("send_wms_return_result"),
	/**
	 * 库存同步日志关键字
	 */
	STOCKSYN("stocksyn"),
	/**
	 * 操作修改记录关键字
	 */
	OPTRECORD("optrecord"),
	/**
	 * 商品同步日志关键字
	 */
	PRODUCTSYN("productsyn"),
	
	/**
	 * 异常信息
	 */
	LPPZEXCEPTION("lppzException");
	
	private LogKeywordEnum(String name) {
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
