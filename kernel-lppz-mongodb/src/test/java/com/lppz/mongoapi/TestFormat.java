package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import ch.qos.logback.classic.Logger;

public class TestFormat {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(TestFormat.class);

	public static void main(String[] args) {
//		String arrayField = "arrayField1";
//		long modifiedCount = 1L;
//		String pk = "pk1";
//		String table = "table1";
//		String pkValue = "pkvalue1";
//		Object value = new Long(1);
//		logger.warn(String.format("pull操作改变的数据大于一条,table={},pk={},pkValue={},arrayField={},value={},pullNumber={}"), table, pk, pkValue, arrayField, value, modifiedCount);
//		System.out.println(String.format("pull操作改变的数据大于一条,table=%s,pk=%s,pkValue=%s,arrayField=%s,value=%s,pullNumber=%s",table, pk, pkValue, arrayField, value, modifiedCount));
		
		List<String> ss =new ArrayList<>();
		ss.add("xxx11111");
		ss.add("xxx11111");
		System.out.println(JSON.toJSONString(ss));
	}
}
