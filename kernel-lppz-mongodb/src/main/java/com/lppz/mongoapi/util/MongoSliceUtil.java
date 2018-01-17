package com.lppz.mongoapi.util;

import java.util.HashMap;
import java.util.Map;

public class MongoSliceUtil {

	public static Map<Integer, long[]> getSliceMap(long[][] arrayIndexs,
			int skip, int pageSize) {
		if (skip + pageSize > arrayIndexs[arrayIndexs.length - 1][1])
			pageSize = (int) arrayIndexs[arrayIndexs.length - 1][1] - skip;
		long[] in = new long[] { skip, pageSize };
		long head = arrayIndexs[arrayIndexs.length - 1][1] - in[0];
		long[] xin = { head - in[1], head };
		Map<Integer, long[]> mm = new HashMap<Integer, long[]>(2);
		int i = 0;
		for (long[] x : arrayIndexs) {
			i++;
			if (xin[1] < x[0] || xin[0] > x[1])
				continue;
			// 包含
			if (in(xin[0], x)) {
				if (in(xin[1], x)) {
					long[] tmp = { xin[0] - x[0], xin[1] - xin[0] };
					mm.put(i, tmp);
					break;
				}
				if (xin[1] > x[1] && xin[0] != x[1]) {
					long tmpSkip = xin[0] - x[0];
					long limit = x[1] - x[0] - tmpSkip;
					long[] tmp = { tmpSkip, limit };
					mm.put(i, tmp);
					xin[0] = x[1];
					continue;
				}
			}
		}
		return mm;
	}

	public static void main(String[] args) {
		long[] x1 = { 0, 20 };
		long[] x2 = { 20, 40 };
		long[] x3 = { 40, 60 };
		long[] x4 = { 60, 80 };
		long[] x5 = { 80, 90 };
		long[][] xx = new long[][] { x1, x2, x3, x4, x5 };
		// long[][] xx=new long[][]{x1,x2};
		int skip = 20;
		int pageSize = 15;
		if (skip + pageSize > xx[xx.length - 1][1])
			pageSize = (int) xx[xx.length - 1][1] - skip;
		long[] in = { skip, pageSize };
		long head = xx[xx.length - 1][1] - in[0];
		long[] xin = { head - in[1], head };
		Map<Integer, long[]> mm = new HashMap<Integer, long[]>(2);
		int i = 0;
		for (long[] x : xx) {
			i++;
			if (xin[1] < x[0] || xin[0] > x[1])
				continue;
			// 包含
			if (in(xin[0], x)) {
				if (in(xin[1], x)) {
					long[] tmp = { xin[0] - x[0], xin[1] - xin[0] };
					mm.put(i, tmp);
					break;
				}
				if (xin[1] > x[1] && xin[0] != x[1]) {
					long tmpSkip = xin[0] - x[0];
					long limit = x[1] - x[0] - tmpSkip;
					long[] tmp = { tmpSkip, limit };
					mm.put(i, tmp);
					xin[0] = x[1];
					continue;
				}
			}
		}
		for (int x : mm.keySet()) {
			System.out.println(x);
			System.out.println(mm.get(x)[0] + ":" + mm.get(x)[1]);
		}
	}

	private static boolean in(long xin, long[] x) {
		return xin >= x[0] && xin <= x[1];
	}
}
