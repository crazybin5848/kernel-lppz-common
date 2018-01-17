package com.lppz.util.rocketmq.enums;

/**
*
* rocketMq topic 名称
*/
public enum OtherRocketMqConsumerGroup
{

	SPARKEXPORT("SparkExportConsumerGroup", "SparkExportConsumerGroup"),
	LPPZBIZEXCEOTION("LppzBizExceptionConsermerGroup", "业务异常"),
	CANCALMSG("CancalMsgConsumerGroup","cancal消息");
	private String id;
	private String name;

	/**
	 *
	 */
	private OtherRocketMqConsumerGroup(final String id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

}