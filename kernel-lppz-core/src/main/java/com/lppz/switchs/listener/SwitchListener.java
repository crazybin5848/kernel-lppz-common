package com.lppz.switchs.listener;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.core.datasource.DynamicDataSource;
import com.lppz.util.DubboPropertiesUtils;

public class SwitchListener {
	
	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
	
	private static Map<String, Boolean> switchMap = new HashMap<String, Boolean>();
	
	private static String projectName;
	
	private static String zkAddress;
	
	private static String path;
	
	private static CuratorFramework curator;
	
	private static SwitchLock switchLock = new SwitchLock();
	
	protected static void start(String projectNameP, String zkAddressP) {
		//init
		projectName = projectNameP;
		zkAddress = zkAddressP;
		path = "/switchs/use/" + projectName;
		
		try{
			String connect = getConnect();
			//logger.info(String.format("load values, projectName: %s, zkAddress: %s", projectName, zkAddress));
			curator = getClient(connect);
			initNode(curator, projectName);

			setLogUseListenter(curator, path);
		}catch(Exception e) {
			e.printStackTrace();
			if(curator != null) curator.close();
		}
	}
	
	private static void initNode(CuratorFramework curator, String projectName) throws Exception {
		Stat logStat = curator.checkExists().forPath("/switchs");
		if(logStat == null){
			ZKPaths.mkdirs(curator.getZookeeperClient().getZooKeeper(), "/switchs");
		}
		
		Stat useStat = curator.checkExists().forPath("/switchs/use");
		if(useStat == null){
			ZKPaths.mkdirs(curator.getZookeeperClient().getZooKeeper(), "/switchs/use");
		}
		
		Stat projectStat = curator.checkExists().forPath(path);
		if(projectStat == null){
			ZKPaths.mkdirs(curator.getZookeeperClient().getZooKeeper(), path);
			//curator.create().withMode(CreateMode.PERSISTENT).forPath(path, "true".getBytes());
		}
	}

	private static CuratorFramework getClient(String connect) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);  
        CuratorFramework client = CuratorFrameworkFactory.newClient(connect, retryPolicy);  
        client.start();  
		return client;
	}
	
	@SuppressWarnings("resource")
    private static void setLogUseListenter(CuratorFramework client, final String path) throws Exception{  
		TreeCache treeCache = new TreeCache(client, path);  
        //设置监听器和处理过程  
        treeCache.getListenable().addListener(new TreeCacheListener() {  
            @Override  
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {  
                ChildData data = event.getData();  
                if(data !=null){  
                	String str = new String(data.getData());
                	String node = data.getPath().replaceAll(path + "/", "");
                	Boolean flag = switchMap.get(node);
                	if("false".equals(str)) {
                		flag = false;
                	}
                	if("true".equals(str)) {
                		flag = true;
                	}
                	if(!path.equals(data.getPath())) {
	                    switch (event.getType()) {  
	                    case NODE_ADDED:  
	                    	switchMap.put(node, flag);
	                    	logger.info(String.format("mode add, path: %s, value: %s", data.getPath(), str));
	                        break;  
	                    case NODE_REMOVED:
	                    	switchMap.remove(node);
	                    	logger.info(String.format("mode remove, path: %s, value: %s", data.getPath(), str));
	                        break;  
	                    case NODE_UPDATED:  
	                    	switchMap.put(node, flag);
	                    	logger.info(String.format("mode update, path: %s, value: %s", data.getPath(), str));
	                        break;  
	                          
	                    default:  
	                        break;  
	                    }
                	}
                }else{  
                	logger.info( "data is null : "+ event.getType());
                }
            }
        });  
        //开始监听  
        treeCache.start();  
    }
	
	//解析zk -> ip:port,ip:port
	private static String getConnect() {
		String connect = "";
		StringBuilder tmpUrlBuilder = new StringBuilder();
		String[] zkUrls = null;
		String[] tmpZkUrls = null;
		String tmpUrl = null;
		if (StringUtils.isNotBlank(zkAddress)) {
			zkUrls = zkAddress.split(",");
			for (int i = 0; i < zkUrls.length; i++) {
				tmpUrl = zkUrls[i];
				if (i == 0) {
					tmpUrl = tmpUrl.replaceAll("(zookeeper://)|(backup=)", "");
					tmpZkUrls = tmpUrl.split("\\?");
					for (int j = 0; j < tmpZkUrls.length; j++) {
						tmpUrlBuilder.append(tmpZkUrls[j]).append(",");
					}
				} else {
					tmpUrlBuilder.append(tmpUrl).append(",");
				}
			}
			tmpUrlBuilder.deleteCharAt(tmpUrlBuilder.length() - 1);
			connect = tmpUrlBuilder.toString();
		}
		return connect;
	}

	/**
	 * 获取该项目节点下的开关值,node 自定义
	 * @param node
	 * @return
	 */
	public static boolean isOff(String node) {
		if(StringUtils.isEmpty(node)) { 
			logger.warn("node is must not null.");
			return false;
		}
		
		Boolean use = switchMap.get(node);
		if(use == null) {
			try {
				curator.create().withMode(CreateMode.PERSISTENT).forPath(String.format("%s/%s", path, node), 
						"true".getBytes());
				return false;
			} catch (Exception e) {
				switchLock.lock();
				try{
					loopMap: for(int i=0; i<100; i++) {
						use = switchMap.get(node);
						if(use != null) {
							break loopMap;
						}else {
							Thread.sleep(1);
						}
					}
				}catch(Exception ex) {
					logger.error("wait map flush error.", ex);
				}finally {
					switchLock.unlock();
				}
			}
		}
		//自旋100ms，仍然得不到值时，返回true
		if(use == null) {
			logger.warn("try 100 time and cost 100ms, can't load data from zk listener.");
			return false;
		}
		return !use;
	}
	
	private static void buildPath() {
		path = "/switchs/use/" + projectName;
		// TODO Auto-generated method stub
	}

	/**
	 * 设置开关node为true, node自定义
	 * @param node
	 */
	public static void setOn(String node) {
		setData(node, "true".getBytes());
	}
	
	/**
	 * 设置开关node为false, node自定义
	 * @param node
	 */
	public static void setOff(String node) {
		setData(node, "false".getBytes());
	}
	
	private static void setData(String node, byte[] bytes) {
		try {
			curator.setData().forPath(String.format("%s/%s", path, node), bytes);
		} catch (Exception e) {
			try {
				curator.create().withMode(CreateMode.PERSISTENT).forPath(String.format("%s/%s", path, node), 
						bytes);
			} catch (Exception e1) {
				logger.error("zk curator set data error.", e);
			}
		}
	}
	
	public static Map<String, Boolean> switchMap() {
		return switchMap;
	}
    
}
