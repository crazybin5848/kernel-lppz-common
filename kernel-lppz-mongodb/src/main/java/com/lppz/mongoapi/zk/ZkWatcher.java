package com.lppz.mongoapi.zk;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lppz.mongoapi.dao.MongoDao;
import com.lppz.util.curator.CuratorFrameworkUtils;
import com.lppz.util.curator.listener.ZookeeperProcessListen;

@Component
public class ZkWatcher {
	private static final Logger logger = LoggerFactory.getLogger(ZkWatcher.class);
	@Value("${dubbo.registry.address}")
	private String zkAddress;
	@Value("${dubbo.application.name}")
	private String applicationName;
	
	@Resource
	private MongoDao mongoDao;
	
	public static final String basePath = "/app/dict";
	
	
	@PostConstruct
	public void startWatcher(){
		logger.info("---------------------start zk watchor");
		// 加载zk总服务
		ZookeeperProcessListen zkListen = new ZookeeperProcessListen();
		
		// 得到基本路径
		zkListen.setBasePath(basePath);
		// 获得zk的连接信息
		String url = CuratorFrameworkUtils.analyzeConnect(zkAddress);
		CuratorFramework zkConn = CuratorFrameworkUtils.buildConnection(url);
		try {
			CuratorFrameworkUtils.createOfNotExist(zkConn, basePath);
		} catch (Exception e) {
			logger.error(String.format("创建节点异常，path=%s", basePath),e);
		}
		mongoDao.addListen(basePath, zkListen);
		
		// 加载watch
		try {
			loadZkWatch(basePath,zkConn, zkListen);
		} catch (Exception e) {
			logger.error(String.format("启动zk监控异常 path=%s",basePath),e);
		}
	}
	
	private void loadZkWatch(String path,
			final CuratorFramework zkConn, final ZookeeperProcessListen zkListen)
			throws Exception {
		if (StringUtils.isNotBlank(path)) {
			CuratorFrameworkUtils.nodeCache(zkConn, path, zkListen);
		}
	}
	
}
