package com.lppz.dubbo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.lppz.bean.DiamondBean;
import com.lppz.core.LppzPropertySourcesPlaceholderConfigurer;
import com.lppz.core.util.DiamondFileUtils;
import com.lppz.util.kafka.consumer.BaseKafkaConsumer;

/**
 * 微服务启动基类
 * @author licheng
 *
 */
public class BaseMicroStartup {
	private static final Logger logger = Logger.getLogger(BaseMicroStartup.class);
	
	public volatile static boolean flag=true;
	protected static DiamondBean diamondBean = null;
	
	static {
		try {
			diamondBean = DiamondFileUtils.getDiamondProperties();
		} catch (IOException e) {
			System.exit(-1);
		}
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
		DiamondFileUtils.loadProjProperties();
		DiamondFileUtils.closeClient();
		PropertySourcesPlaceholderConfigurer pc= new LppzPropertySourcesPlaceholderConfigurer();
		try {
			Resource resource = new ClassPathResource(DiamondFileUtils.DUBBO_PRO_FILE_PATH);
			pc.setLocation(resource);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return pc;
	}
	

	/**
	 * 从zk获取已启用的node及端口，获取未使用的端口
	 * @return 服务基础启动端口
	 * @throws Exception
	 */
//	private static Integer getValidPort() throws Exception {
//		String zkAddress = diamondBean.getZkAddress();
//		String proCode = diamondBean.getProjCode();
//		String proFile = diamondBean.getProfile();
//		List<String> nodeList = diamondBean.getNodeList();
//		List<Integer> nodePortList = diamondBean.getNodePortList();
//		Integer validPort = null;
//		String nodeName = null;
//		
//		if (CollectionUtils.isNotEmpty(nodeList) && CollectionUtils.isNotEmpty(nodePortList)) {
//			if (nodeList.size() != nodePortList.size()) {
//				throw new RuntimeException("node size diffence of port size");
//			}
//			CuratorFramework zkConn = CuratorFrameworkUtils.buildConnection(zkAddress);
//			String projZkPath = ZookeeperPath.ZK_SEPARATOR.getKey() +"diamond"
//			+ ZookeeperPath.ZK_SEPARATOR.getKey() + proFile
//			+ ZookeeperPath.ZK_SEPARATOR.getKey() + proCode;
//			try {
//				CuratorFrameworkUtils.createOfNotExist(zkConn,projZkPath);
//				List<String> nodes = zkConn.getChildren().forPath(projZkPath);
//				if(CollectionUtils.isEmpty(nodes)){
//					nodeName = nodeList.get(0);
//					validPort = nodePortList.get(0);
//					CuratorFrameworkUtils.createTempNode(projZkPath, nodeName, String.valueOf(validPort), zkConn);
//					return validPort;
//				}else{
//					Map<String,Integer> nodeMap = new HashMap<>();
//					String[] nodeMsg = null;
//					for (String node : nodes) {
//						nodeMsg = node.split(":");
//						nodeMap.put(nodeMsg[0], Integer.parseInt(nodeMsg[2]));
//					}
//					
//					for (int i = 0; i < nodeList.size(); i++) {
//						if (nodeMap.get(nodeList.get(i)) == -1) {
//							nodeName = nodeList.get(i);
//							validPort = nodePortList.get(i);
//							CuratorFrameworkUtils.createTempNode(projZkPath, nodeName, String.valueOf(validPort), zkConn);
//							return validPort;
//						}
//					}
//					//如果所有节点都被占用，排除异常
//					throw new DiamondNodeFullException();
//				}
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//		return null;
//	}

	protected static void startup(AnnotationConfigApplicationContext context, int sleepSecond){
		try {
			// startup
			context.start();
			logger.info(context.getApplicationName() + " server startup successfully.");
			while (flag) {
				Thread.sleep(sleepSecond * 1000);
			}
			context.destroy();
			BaseKafkaConsumer.pool.awaitTermination(10, TimeUnit.SECONDS);
			System.exit(0);
		} catch (Exception e) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			logger.error(sdf.format(new Date()) + " startup error", e);
			context.stop();
			System.exit(-1);
		}
	}
}