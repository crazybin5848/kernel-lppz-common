/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.annotation;

/**
 * 参数记录方式
 * 
 * ALL:全部记录
 * NONE:全部不记录(默认方式)
 * INDEX:按顺序记录(第一个参数index=0)
 * 
 * @author luoyiqun@yihaodian.com
 * @version 1.0
 */
public enum ParamRecorderType {
	ALL,
	NONE,
	INDEX
}
