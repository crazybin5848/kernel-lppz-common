package com.lppz.mongoapi.util;

import org.apache.http.util.Asserts;

public class EsIndexUtils {
	
	public static int APP_SEARCH_MOD = 5;
	
	public static String getSurffixByMode(String value, int mod){
		Asserts.notNull(value, "es pk value");
		return String.valueOf(Math.abs(value.hashCode())%mod + 1);
	}
	
	public static String buildId(String id) {
		int hashCode = Math.abs(id.hashCode());
		return hashCode % 6 + id;
	}

}
