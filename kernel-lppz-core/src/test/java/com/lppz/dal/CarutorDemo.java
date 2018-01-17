package com.lppz.dal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
/**
 * Curator事件监听
 * @author  huey
 * @version 1.0 
 * @created 2015-3-2
 */
public class CarutorDemo implements AutoCloseable{
	CuratorFramework client;
	PathChildrenCache childrenCache;
	ExecutorService pool = Executors.newFixedThreadPool(2);

	public void init() throws Exception{
		client = CuratorFrameworkFactory.builder()
				.connectString("192.168.37.243:2181,192.168.37.245:2181,192.168.37.246:2181")
				.sessionTimeoutMs(1000)
				.connectionTimeoutMs(3000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		client.start();
//		client.create()
//		.creatingParentsIfNeeded()
//		.forPath("/zk-huey/cnode", "hello".getBytes());
	/**
	 * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
	 */
	/**
	 * 监听数据节点的变化情况
	 */
//	final NodeCache nodeCache = new NodeCache(client, "/zk-huey/cnode", false);
//	nodeCache.start(true);
//	nodeCache.getListenable().addListener(
//		new NodeCacheListener() {
//			@Override
//			public void nodeChanged() throws Exception {
//				System.out.println("Node data is changed, new data: " + 
//					new String(nodeCache.getCurrentData().getData()));
//			}
//		}, 
//		pool
//	);
//	client.setData().forPath("/zk-huey/cnode", "world".getBytes());
		childrenCache = new PathChildrenCache(client, "/mycat/mycat-cluster-1/line", true);
		childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		childrenCache.getListenable().addListener(
			new PathChildrenCacheListener() {
				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
						throws Exception {
						switch (event.getType()) {
						case CHILD_ADDED:
							System.out.println("CHILD_ADDED: " + event.getData().getPath()+":::"+new String(event.getData().getData()));
							break;
						case CHILD_REMOVED:
							System.out.println("CHILD_REMOVED: " + event.getData().getPath());
							break;
						case CHILD_UPDATED:
							System.out.println("CHILD_UPDATED: " + event.getData().getPath());
							break;
						default:
							break;
					}
				}
			},
			pool
		);
	}
	
	public static void main(String[] args) throws Exception {
		CarutorDemo carutorDemo=new CarutorDemo();
		carutorDemo.init();
		boolean mark=true;
		while(mark){
			Thread.sleep(10 * 1000);
		}
		carutorDemo.close();
	}

	@Override
	public void close() throws Exception {
		System.out.println("I am close");
		childrenCache.close();
		pool.shutdown();
		client.close();		
	}
}