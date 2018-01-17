package com.lppz.hbase.client.model;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;

public class ScanOrderedKV {
	public ScanOrderedKV() {
	}

	public ScanOrderedKV(String qulifier, String value,CompareOp op) {
		this.qulifier = qulifier;
		this.value = value;
		this.op = op;
	}

	public CompareOp getOp() {
		return op;
	}

	public void setOp(CompareOp op) {
		this.op = op;
	}

	private CompareOp op;
	private String qulifier;
	private String value;

	public String getQulifier() {
		return qulifier;
	}

	public void setQulifier(String qulifier) {
		this.qulifier = qulifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
