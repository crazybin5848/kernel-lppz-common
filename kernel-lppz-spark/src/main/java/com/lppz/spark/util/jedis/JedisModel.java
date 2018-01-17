package com.lppz.spark.util.jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JedisModel {
	private String indexkey;
	private String[] indexVal;

	public JedisModel(String indexey, String[] indexVal) {
		this.indexkey = indexey;
		this.indexVal = indexVal;
	}

	public String buildJedisKey(SparkJedisCluster jedisCluster) {
		try {
			StringBuilder sb = new StringBuilder(JedisSequenceUtil.tbPrefix)
					.append(JedisSequenceUtil.delimitor).append(indexkey)
					.append(JedisSequenceUtil.delimitor);
			int i = 1;
			for (String ival : indexVal) {
				sb.append(ival);
				if (i++ != indexVal.length) {
					sb.append(JedisSequenceUtil.delimitor);
				}
			}
			return sb.toString();
		} finally {
			if (jedisCluster != null) {
				jedisCluster.sadd(new StringBuilder(JedisSequenceUtil.idxPrefix).
						append(JedisSequenceUtil.delimitor).append(indexkey).toString(),
						build(indexVal));
			}
		}
	}
	
	public String buildJedisIdxKey() {
		return new StringBuilder(
				JedisSequenceUtil.idxPrefix)
				.append(JedisSequenceUtil.delimitor).append(indexkey)
				.toString();
	}

	public String[] getJedisKeyFromIdx(SparkJedisCluster jedisCluster) {
		Set<String> set = jedisCluster.smembers(buildJedisIdxKey());
		List<String> keys = new ArrayList<String>(set.size());
		for (String s : set) {
			keys.add(new StringBuilder(JedisSequenceUtil.tbPrefix)
					.append(JedisSequenceUtil.delimitor).append(indexkey)
					.append(JedisSequenceUtil.delimitor).append(s).toString());
		}
		return  keys.toArray(new String[0]);
	}

	public String build(String[] indexVal2) {
		String ss = "";
		int i = 1;
		for (String s : indexVal) {
			ss += s;
			if (i++ != indexVal.length)
				ss += JedisSequenceUtil.delimitor;
		}
		return ss;
	}

	public String getIndexey() {
		return indexkey;
	}

	public void setIndexey(String indexey) {
		this.indexkey = indexey;
	}

	public String[] getIndexVal() {
		return indexVal;
	}

	public void setIndexVal(String[] indexVal) {
		this.indexVal = indexVal;
	}

}
