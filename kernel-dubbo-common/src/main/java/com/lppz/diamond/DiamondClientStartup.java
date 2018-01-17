package com.lppz.diamond;

import java.io.IOException;

import com.lppz.core.util.DiamondFileUtils;

/**
 * 加载配置中心配置生成配置文件启动类
 * @author licheng
 *
 */
public class DiamondClientStartup {
	
	public static void main(String[] args) throws IOException {
		loadDiamond(null);
	}
	
	public static void localLoadDiamond(String projectName) throws IOException{
		loadDiamond(projectName);
	}
	
	private static void loadDiamond(String projectName) throws IOException{
		DiamondFileUtils.getDiamondProperties();
		DiamondFileUtils.loadProjProperties(projectName);
		DiamondFileUtils.closeClient();
	}

}
