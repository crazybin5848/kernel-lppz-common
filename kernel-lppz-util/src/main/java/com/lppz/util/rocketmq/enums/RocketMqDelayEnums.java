package com.lppz.util.rocketmq.enums;

/**
*
* rocketMq 延迟消费消息的延迟时间枚举类
* 由于rocketmq消息延迟只支持level参数，不支持精确设置，这个枚举类用于规范和转换delay级别
*/
public enum RocketMqDelayEnums {

	//1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	OneSecond(1, "1s")
	,FiveSecond(2, "5s")
	,TenSecond(3,"10s")
	,ThirtySecond(4,"30s")
	,OneMin(5,"1m")
	,TwoMin(6,"2m")
	,ThreeMin(7,"3m")
	,FourMin(8,"4m")
	,FiveMin(9,"5m")
	,SixMin(10,"6m")
	,SevenMin(11,"7m")
	,EightMin(12,"8m")
	,NineMin(13,"9m")
	,TenMin(14,"10m")
	,TwentyMin(15,"20m")
	,ThirtyMin(16,"30m")
	,OneHour(17,"1h")
	,TwoHour(18,"2h");
	
	private int level;
	private String des;

	/**
	 *
	 */
	private RocketMqDelayEnums(final int level, final String des) {
		this.level = level;
		this.des = des;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
