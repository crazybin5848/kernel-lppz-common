package com.lppz.mycat.monitor.trigger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.mycat.monitor.model.DsBean;
import com.lppz.mycat.monitor.model.NodeModel;
import com.lppz.mycat.monitor.utils.ShellUtil;
import com.lppz.mycat.monitor.utils.ZkConfig;
import com.lppz.mycat.monitor.utils.ZkParamCfg;


public class MycatNodeMonitorTrigger {
	
	private Logger logger = LoggerFactory.getLogger(MycatNodeMonitorTrigger.class);
	
	private int expireSeconds = 30;
	
	public MycatNodeMonitorTrigger() {
		String expireSecondStr = ZkConfig.getInstance().getValue(ZkParamCfg.RESTART_EXPIRE_SECOND);
		if (StringUtils.isNotBlank(expireSecondStr)) {
			try {
				expireSeconds = Integer.parseInt(expireSecondStr);
			} catch (Exception e) {
				logger.error("获取校验服务超时时间异常", e);
			}
		}
	}
	
	public void checkMycatNodes(CuratorFramework zkConn){
		Map<String,NodeModel> nodeMap = new HashMap<>();
		Map<String,NodeModel> restartMap = new HashMap<>();
		String[] nodesArray = null;
		String[] modelPers = null;
		NodeModel tmpmodel = null;
		String serverAddress = null;
		String mycatuser = ZkConfig.getInstance().getValue(ZkParamCfg.MYCATUSER);
		String mycatpwd = ZkConfig.getInstance().getValue(ZkParamCfg.MYCATPWD);
		String schema = ZkConfig.getInstance().getValue(ZkParamCfg.SCHEMA);
		
		String mycatNodes = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_MYCAT_NODES);
		if (StringUtils.isNotBlank(mycatNodes)) {
			nodesArray = mycatNodes.split(",");
			for (String node : nodesArray) {
				modelPers = node.split(":");
				if (modelPers.length == 5) {
					tmpmodel = new NodeModel(modelPers[0], modelPers[1], Integer.valueOf(modelPers[2]), modelPers[3], modelPers[4]);
					nodeMap.put(modelPers[0], tmpmodel);
				}
			}
		
			String mycatNodesPath = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_MYCAT_NODE_PATH);
			GetChildrenBuilder children = zkConn.getChildren();
			try {
				List<String> livenodes = children.forPath(mycatNodesPath);
				Set<Entry<String, NodeModel>> nodeEntrys = nodeMap.entrySet();
				if (livenodes != null && !livenodes.isEmpty()) {
					for (Entry<String, NodeModel> node : nodeEntrys) {
						tmpmodel = node.getValue();
						serverAddress = tmpmodel.getIp() + ":" + tmpmodel.getPort();
						if (livenodes.contains(node.getKey())) {
							if (!isConnectable(tmpmodel.getIp(), tmpmodel.getPort(), mycatuser, mycatpwd,schema, tmpmodel.getNodeName(),false)) {
								restartMycat(tmpmodel,restartMap);
							}
//							else{
//								restartMap.remove(serverAddress);
//							}
						}else{
							restartMycat(tmpmodel,restartMap);
						}
					}
				}else{
					for (Entry<String, NodeModel> node : nodeEntrys) {
						restartMycat(node.getValue(),restartMap);
					}
				}
				for (String addr : restartMap.keySet()) {
					NodeModel model = restartMap.get(addr);
					int waitCount=0;
					long start=System.currentTimeMillis();
					for (;;) {
						if (isConnectable(model.getIp(), model.getPort(),
								mycatuser, mycatpwd, schema,
								model.getNodeName(),true)) {
							logger.info("重启服务{}完成", model);
							break;
						}
						if(waitCount==1000){
							logger.error(model.toString()+"等待服务超时："+(System.currentTimeMillis()-start)/1000+"s");
							break;
						}
						logger.warn("等待服务{}重启完成。。。", model);
						Thread.sleep(3000);
						waitCount++;
					}
				}
			} catch (Exception e) {
				logger.error("读取mycat节点信息异常",e);
			}finally{
//				try {
////					zkConn.close();
//					zkConn.getZookeeperClient().close();
//				} catch (Exception e) {
//					logger.error("关闭zk连接异常",e);
//				}
			}
		}
	}
	Map<DsBean,LppzBasicDataSource> mapDs=new HashMap<DsBean,LppzBasicDataSource>();
	private boolean isConnectable(String ip, int port, String user, String password, String schema, String nodeName,boolean ischeck){
		boolean isConnect = false;
		String sql = ZkConfig.getInstance().getValue(ZkParamCfg.TESTSQL);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
//		String url = null;
//		try {
//			Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"); // 加载mysq驱动
//		} catch (ClassNotFoundException e) {
//			logger.error("驱动加载错误", e);
//		}
		try {
//			url = "jdbc:mysql://" + ip + ":" + port	+ "/"+schema+"?useConfigs=maxPerformance";
//			conn = DriverManager.getConnection(url, user, password);
			DsBean bean=new DsBean(ip,port,schema,user,password);
			LppzBasicDataSource ds=mapDs.get(bean);
			if(ds==null){
				ds=LppzBasicDataSource.buildDefaultInstance(ip, String.valueOf(port), 10, 2, schema,user, password);
				mapDs.put(bean, ds);
			}		
			conn=ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);// 执行sql语句
			if (rs.next()) {
				logger.info("mycat is alive ip {} port {} node {}",ip, port, nodeName);
				isConnect = true;
			}
		} catch (SQLException e) {
			if(!ischeck)
			logger.error("数据库连接错误", e);
		}catch (Exception e) {
			if(!ischeck)
			logger.error("数据库连接错误", e);
		}
		finally{
			// 关闭数据库
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("数据库关闭错误", e);
			}
		}
		return isConnect;
	}
	
	private boolean telnetIsConnect(String ip, int port){
		boolean isConnect = false;
		int retrycount = 10;
		while (retrycount-- > 0) {
			TelnetClient client=connection(ip, port);
			if(client==null){
				try {
					Thread.sleep(3000L);
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

	private void restartMycat(NodeModel model, Map<String, NodeModel> restartMap) {
		String serverAddress = model.getIp() + ":" + model.getPort();
//		NodeModel restartModle = restartMap.get(serverAddress);
//		if (restartModle != null && notExpire(restartModle.getRestartTime())) {
//			logger.warn("服务启动未完成等待服务启动完成 {}", restartModle);
//			return;
//		}
		restartMap.put(serverAddress, model);
		logger.warn("开始重启服务 {}", model);
		model.setRestartTime(new Date());
		String restartServer = "sh restartDal " + model.getPort();
		try {
			ShellUtil.exec(model.getIp(), model.getLinuxuser(), model.getLinuxpwd(), 22, restartServer);
			Thread.sleep(1000);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		logger.warn("正在重启服务 {}", model);
	}

	private boolean notExpire(Date restartTime) {
		boolean notExpire = true;
		if (restartTime == null) {
			return notExpire;
		}
		Calendar calender = Calendar.getInstance();
		Date now = calender.getTime();
		calender.setTime(restartTime);
		calender.add(Calendar.SECOND, expireSeconds);
		Date expireTime =  calender.getTime();
		if (now.after(expireTime)) {
			notExpire = false;
		}
		return notExpire;
	}

}
