package com.lppz.util;

import java.util.UUID;

/**
 * <b>模块描述	: </b>UUID处理工具类<br/>
 * <b>使用描述	: </b><br/>
 * <b>JDK版本	: </b>JDK 1.6<br/>
 */
public final class UUIDUtils {

	private UUIDUtils(){}
	
	/**
	 * 生成UUID
	 * @return String
	 */
	public static String createUUId() {
		UUID uuid = UUID.randomUUID();
		return StringUtils.removeSameString(uuid.toString(), "-");
	}
}
