package com.lppz.canal.init;


import com.alibaba.dubbo.config.annotation.Service;
import com.lppz.dubbo.MicroServiceDestoryInterface;
import com.lppz.dubbo.micro.MicroServiceDestory;

//@Service(protocol = "rest", timeout = 100000)
public class CanalClientDestory extends MicroServiceDestory implements MicroServiceDestoryInterface{

	public static void main(String[] args) {
		CanalClientDestory destory = new CanalClientDestory();
		destory.shutdown();
	}
}