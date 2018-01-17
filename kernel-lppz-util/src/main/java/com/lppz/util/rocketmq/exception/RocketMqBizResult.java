package com.lppz.util.rocketmq.exception;

import java.io.Serializable;

public class RocketMqBizResult<R> implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 5221277011150806188L;
	private R r;
	private String topic;
	private int level=0;
	private String tag;
	public R getR() {
		return r;
	}
	public void setR(R r) {
		this.r = r;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
