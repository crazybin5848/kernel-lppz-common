package com.lppz.oms.kafka.event;

import java.io.Serializable;


/**
 *
 */
public class KafkaDalMsgType implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 848171064908771528L;
	public KafkaDalMsgType(){
	}
	 public enum KafkaDalMsgTypeEnum {
		 TableConfig,SchemaConfig,ClusterConfig,RuleConfig,DBHostConfig,UserConfig;
	 }

	private KafkaDalMsgTypeEnum ienum;
	public KafkaDalMsgTypeEnum getIenum() {
		return ienum;
	}

	public void setIenum(KafkaDalMsgTypeEnum ienum) {
		this.ienum = ienum;
	}

}
