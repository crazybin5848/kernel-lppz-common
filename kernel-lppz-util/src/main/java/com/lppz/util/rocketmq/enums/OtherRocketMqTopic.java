package com.lppz.util.rocketmq.enums;

/**
*
* rocketMq topic 名称
*/
public enum OtherRocketMqTopic {

	SPARKEXPORT("SparkExport", "SparkExport"),
	LPPZBIZEXCEOTION("LppzBizException", "业务异常"),
	CANCALMSG("CancalMsg","cancal消息");
	
	private String id;
	private String name;

	/**
	 *
	 */
	private OtherRocketMqTopic(final String id, final String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
