package com.lppz.hbase.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.coprocessor.Constants;

public class PartRowKeyString {
	private ScanOrderedKV[][] partrowKey;

	public PartRowKeyString() {
	}

	public PartRowKeyString(ScanOrderedKV[][] partrowKey) {
		this.partrowKey = partrowKey;
	}

	public ScanOrderedKV[][] getPartrowKey() {
		return partrowKey;
	}

	public String[] buildpartKey() {
		if (partrowKey == null || partrowKey.length == 0)
			return null;
		List<String> str = new ArrayList<String>();
		for (ScanOrderedKV[] p : partrowKey) {
			str.add(build(p));
		}
		return str.toArray(new String[str.size()]);
	}

	private String build(ScanOrderedKV[] p) {
		StringBuilder sb = new StringBuilder("");
		Map<String,String> tree=new TreeMap<String,String>();
		for (ScanOrderedKV s : p) {
			tree.put(s.getQulifier(), s.getValue());
		}
		for(String q:tree.keySet()){
			String xz=tree.get(q);
			sb.append(Constants.REGSPLITER).append(q).append(Constants.QSPLITTER)
			.append(xz).append(Constants.REGSPLITER);
		}
		return sb.toString();
	}
}
