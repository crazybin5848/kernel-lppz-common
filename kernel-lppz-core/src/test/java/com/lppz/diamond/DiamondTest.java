package com.lppz.diamond;

import java.io.IOException;

import com.lppz.bean.DiamondBean;
import com.lppz.core.util.DiamondFileUtils;

public class DiamondTest {
	
	public static void main(String[] args) throws IOException {
		DiamondBean diamondBean = new DiamondBean();
		diamondBean.setProfile("build");
		diamondBean.setProjCode("edep-lppz-provider");
		diamondBean.setServerAddress("192.168.37.247");
		diamondBean.setServerPort(8283);
		DiamondFileUtils.setDiamondProperties(diamondBean);
		DiamondFileUtils.loadProjProperties();
	}

}
