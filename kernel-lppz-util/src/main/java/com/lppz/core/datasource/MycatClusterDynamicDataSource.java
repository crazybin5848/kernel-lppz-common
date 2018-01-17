package com.lppz.core.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MycatClusterDynamicDataSource extends DynamicDataSource
{
	@Override
	public void destory() throws IllegalArgumentException,
			IllegalAccessException {
		super.destory();
		try {
			childrenCache.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
//		pool.shutdown();
		client.close();		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4420858739523852273L;
	private static final Logger logger = LoggerFactory.getLogger(MycatClusterDynamicDataSource.class);

	@Override
	protected String generateRouteKey()
	{
		int num=1;
		if(!loadFactormasterMap.isEmpty()){
			num=loadFactormasterMap.keySet().toArray(new Integer[0])[java.util.concurrent.ThreadLocalRandom.current().nextInt(loadFactormasterMap.size())];
		}
		return nodePrefix+loadFactormasterMap.get(num);
	}
	
//	public LppzBasicDataSource fetchLoadDs()
//	{
//		int num=java.util.concurrent.ThreadLocalRandom.current().nextInt(loadweights.size());
//		return (LppzBasicDataSource) this.getTargetDataSources().get(nodePrefix+loadweights.get(num));
//	}
	
	private Map<Integer, Integer> loadFactormasterMap=new ConcurrentHashMap<Integer, Integer>();

//	private void setLoadFactormasterMap(Map<String, Integer> loadFactormasterMap) {
//		int k=1;
//		for(String master:loadFactormasterMap.keySet()){
//			Integer weight=loadFactormasterMap.get(master);
//			Integer load=k++;
//			for(int j=0;j<weight;j++){
//				loadweights.add(load);
//			}
//		}
//	}
	
	private String zkServerAddr;
	private String zkMycatClusterName;
	private String nodePrefix;
	private LppzBasicDataSource dsBean;
	private transient CuratorFramework client;
	private transient PathChildrenCache childrenCache;
	
	public MycatClusterDynamicDataSource(){}
	public MycatClusterDynamicDataSource(LppzBasicDataSource dsBean){
		this.dsBean=dsBean;
		this.zkServerAddr=dsBean.getZkServerAddr();
		this.zkMycatClusterName=dsBean.getZkMycatClusterName();
		this.nodePrefix=dsBean.getNodePrefix();
		if(StringUtils.isBlank(dsBean.getCatgroup()))
			throw new IllegalArgumentException("group can not be null when isCorbar is true");
		if(zkServerAddr==null)
			throw new IllegalArgumentException("zkServerAddr can not be null when isCorbar is true");
		if(zkMycatClusterName==null)
			throw new IllegalArgumentException("zkMycatClusterName can not be null when isCorbar is true");
		if(nodePrefix==null)
			throw new IllegalArgumentException("nodePrefix can not be null when isCorbar is true");
	}
	
	@Override
	public void afterPropertiesSet() {
		try {
			super.setTargetDataSources(new HashMap<Object,Object>());
			initWatchZkCluser();
//			Thread.sleep(3*1000);
			super.afterPropertiesSet();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		LppzBasicDataSource ds=null;
		try {
			ds=(LppzBasicDataSource)determineTargetDataSource();
			if(dsBean.isNeedLog())
			logger.info(ds.getDataSourceProperties().getProperty("url"));
			Connection conn=ds.getConnection();
			//sucessfully getConn from this dataSource,then reset ErrorCount to zero
			ds.setErrorCount(0);
			return conn;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			while(!loadFactormasterMap.isEmpty()){
				if(ds!=null){
					//connection lost meanwhile mycat process is dead,so remove the mycat node from this dynamic dataSource
					if(!telnetIsConnect(ds.getIp(),Integer.parseInt(ds.getPort()))){
//						loadFactormasterMap.remove(ds.getCurrentNode());
					}
					//maybe connection really timeout or connection not enough,but mycat process is still alive,retry continually 5 times at most
					else{
						if(ds.getErrorCount()==1000){
//							loadFactormasterMap.remove(ds.getCurrentNode());
						}
						else{
							ds.setErrorCount(ds.getErrorCount()+1);
						}
					}
				}
				return getConnection();
			}
		}
		return null;
	}
	
	private boolean telnetIsConnect(String ip, int port){
		boolean isConnect = false;
		int retrycount = 3;
		while (retrycount-- > 0) {
			TelnetClient client=connection(ip, port);
			if(client==null){
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
				}
				continue;
			}else{
				isConnect = true;
				try {
					client.disconnect();
				} catch (IOException e) {
				}
				break;
			}
		}
		return isConnect;
	}
	
	 private TelnetClient connection(String ip,int port)  
	    {  
	        try   
	        {  
	        		TelnetClient telnetClinet = new TelnetClient();  
	            telnetClinet.connect(ip, port);
	            return telnetClinet;
	        }   
	        catch (Exception e)   
	        {  
	            return null;  
	        }  
	    }
	
	private void initWatchZkCluser() throws Exception{
			client = CuratorFrameworkFactory.builder()
					.connectString(zkServerAddr)
					.sessionTimeoutMs(5000)
					.connectionTimeoutMs(20000)
					.retryPolicy(new ExponentialBackoffRetry(1000, 5))
					.build();
			client.start();
			List<String> listCat=client.getChildren().forPath("/mycat/" + zkMycatClusterName + "/"+dsBean.getCatgroup());
			if(CollectionUtils.isEmpty(listCat))
				throw new IllegalStateException("no node in "+zkMycatClusterName);
			for(String path:listCat){
				String[] data=new String(client.getData().forPath("/mycat/" + zkMycatClusterName + "/"+dsBean.getCatgroup()+"/"+path)).split(":");
				addNodeCat(data,false);
			}
			childrenCache = new PathChildrenCache(client, "/mycat/"+zkMycatClusterName+"/"+dsBean.getCatgroup(), true);
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
			childrenCache.getListenable().addListener(
				new PathChildrenCacheListener() {
					@Override
					public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
							throws Exception {
							switch (event.getType()) {
							case CHILD_ADDED:
								logger.info("CHILD_ADDED: " + event.getData().getPath());
								String[] data=new String(event.getData().getData()).split(":");
								addNodeCat(data,true);
								logger.info("TARGETDSMAP_UPDATED: " + data[0]+" ADDED");
								break;
							case CHILD_REMOVED:
								logger.warn("CHILD_REMOVED: " + event.getData().getPath());
								data=new String(event.getData().getData()).split(":");
								MycatClusterDynamicDataSource.this.getTargetDataSources().remove(data[0]);
								MycatClusterDynamicDataSource.super.afterPropertiesSet();
								loadFactormasterMap.remove(Integer.parseInt(data[0].replaceAll(dsBean.getNodePrefix(),"")));
								logger.warn("TARGETDSMAP_UPDATED: " + data[0]+" REMOVED");
								break;
							case CHILD_UPDATED:
//								System.out.println("CHILD_UPDATED: " + event.getData().getPath());
								break;
							default:
								break;
						}
					}
				}
			);
	}
	
	private void addNodeCat(String[] data,boolean mark) {
		LppzBasicDataSource newLmds=dsBean.buildInstance(data[1], data[2]);
		newLmds.setCurrentNode(Integer.parseInt(data[0].replaceAll(newLmds.getNodePrefix(),"")));
		if(mark){
			for(;;){
				Connection conn=null;
				try {
					conn=newLmds.getConnection();
					logger.info(newLmds+" has been added to cluster");
					break;
				} catch (SQLException e) {
					logger.warn(e.getMessage(),e);
				}
				finally{
					if(conn!=null){
						try {
							conn.close();
							conn=null;
						} catch (SQLException e) {
							logger.error(e.getMessage(),e);
						}
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
		MycatClusterDynamicDataSource.this.getTargetDataSources().put(data[0], newLmds);
		MycatClusterDynamicDataSource.super.afterPropertiesSet();
		loadFactormasterMap.put(newLmds.getCurrentNode(),newLmds.getCurrentNode());
	}
	
	public String getZkServerAddr() {
		return zkServerAddr;
	}
	public void setZkServerAddr(String zkServerAddr) {
		this.zkServerAddr = zkServerAddr;
	}
	public String getZkMycatClusterName() {
		return zkMycatClusterName;
	}
	public void setZkMycatClusterName(String zkMycatClusterName) {
		this.zkMycatClusterName = zkMycatClusterName;
	}
	public String getNodePrefix() {
		return nodePrefix;
	}
	public void setNodePrefix(String nodePrefix) {
		this.nodePrefix = nodePrefix;
	}

	public LppzBasicDataSource getDsBean() {
		return dsBean;
	}

	public void setDsBean(LppzBasicDataSource dsBean) {
		this.dsBean = dsBean;
	}
}