package io.mycat.config.loader.zkprocess.zktoxml;

import io.mycat.MycatServer;
import io.mycat.MycatShutdown;
import io.mycat.backend.datasource.PhysicalDBNode;
import io.mycat.backend.datasource.PhysicalDBPool;
import io.mycat.backend.datasource.PhysicalDatasource;
import io.mycat.config.MycatConfig;
import io.mycat.config.loader.console.ZookeeperPath;
import io.mycat.config.loader.zkprocess.comm.ZkConfig;
import io.mycat.config.loader.zkprocess.comm.ZkParamCfg;
import io.mycat.config.loader.zkprocess.comm.ZookeeperProcessListen;
import io.mycat.config.loader.zkprocess.console.ZkNofiflyCfg;
import io.mycat.config.loader.zkprocess.entity.Property;
import io.mycat.config.loader.zkprocess.entity.Server;
import io.mycat.config.loader.zkprocess.parse.XmlProcessBase;
import io.mycat.config.loader.zkprocess.xmltozk.listen.ServerxmlTozkLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.BindatazkToxmlLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.EcacheszkToxmlLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.RuleszkToxmlLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.SchemaszkToxmlLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.SequenceTopropertiesLoader;
import io.mycat.config.loader.zkprocess.zktoxml.listen.ServerzkToxmlLoader;
import io.mycat.config.loader.zkprocess.zookeeper.process.ZkMultLoader;
import io.mycat.config.model.SchemaConfig;
import io.mycat.config.model.TableConfig;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.util.MysqlDmlUtil;

import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.util.kryo.KryoUtil;

/**
 * 将xk的信息转换为xml文件的操作 源文件名：ZktoxmlMain.java 文件版本：1.0.0 创建作者：liujun
 * 创建日期：2016年9月20日 修改作者：liujun 修改日期：2016年9月20日 文件描述：TODO 版权所有：Copyright 2016
 * zjhz, Inc. All Rights Reserved.
 */
public class ZktoXmlMain {

	/**
	 * 日志
	 * 
	 * @字段说明 LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZktoXmlMain.class);
	
	public static void main(String[] args) throws Exception {
		loadZktoFile();
		// System.out.println(Long.MAX_VALUE);
	}

	/**
	 * 将zk数据放到到本地 方法描述
	 * 
	 * @throws Exception
	 * @创建日期 2016年9月21日
	 */
	private static String myid;
	private static String custerName;
	private static String forceInit;
	public static String getMyid() {
		return myid;
	}

	public static void loadZktoFile() throws Exception {

		// 加载zk总服务
		ZookeeperProcessListen zkListen = new ZookeeperProcessListen();

		// 得到集群名称
		custerName = ZkConfig.getInstance().getValue(
				ZkParamCfg.ZK_CFG_CLUSTERID);
		// 得到基本路径
		String basePath = ZookeeperPath.ZK_SEPARATOR.getKey()
				+ ZookeeperPath.FLOW_ZK_PATH_BASE.getKey();
		basePath = basePath + ZookeeperPath.ZK_SEPARATOR.getKey() + custerName;
		zkListen.setBasePath(basePath);

		// 获得zk的连接信息
		CuratorFramework zkConn = buildConnection(ZkConfig.getInstance()
				.getValue(ZkParamCfg.ZK_CFG_URL));

		// 获得公共的xml转换器对象
		XmlProcessBase xmlProcess = new XmlProcessBase();

		// 加载以接收者
		new SchemaszkToxmlLoader(zkListen, zkConn, xmlProcess);

		// server加载
		new ServerzkToxmlLoader(zkListen, zkConn, xmlProcess);

		// rule文件加载
		new RuleszkToxmlLoader(zkListen, zkConn, xmlProcess);

		// 将序列配制信息加载
		new SequenceTopropertiesLoader(zkListen, zkConn, xmlProcess);

		// 进行ehcache转换
		new EcacheszkToxmlLoader(zkListen, zkConn, xmlProcess);

		// 将bindata目录的数据进行转换到本地文件
		new BindatazkToxmlLoader(zkListen, zkConn, xmlProcess);

		// 初始化xml转换操作
		xmlProcess.initJaxbClass();

		// 通知所有人
		zkListen.notifly(ZkNofiflyCfg.ZK_NOTIFLY_LOAD_ALL.getKey());

		myid = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_MYID);
		forceInit = ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_FORCEINIT);
		Server server = (Server) new ServerxmlTozkLoader(zkListen, zkConn,
				xmlProcess, false).notiflyProcess(false);
		String result=createTempNode("/mycat/" + custerName + "/line", myid, zkConn, server);
		if(result==null)
			throw new IllegalStateException(myid+" server has been exist pls wait and restart");
		if(zkConn.checkExists().forPath("/mycat/" + custerName +"/tmpidx")==null)
			zkConn.create().withMode(CreateMode.PERSISTENT).forPath("/mycat/" + custerName +"/tmpidx");
		String group=ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_CATGROUP);
		if(StringUtils.isBlank(group))
			group="default";
		if(zkConn.checkExists().forPath("/mycat/" + custerName +"/"+group)==null)
			zkConn.create().withMode(CreateMode.PERSISTENT).forPath("/mycat/" + custerName +"/"+group);
		result=createTempNode("/mycat/" + custerName + "/"+group, myid, zkConn, server);
		if(result==null)
			throw new IllegalStateException(myid+" server group has been exist pls wait and restart");
		zkListen.watchPath("/mycat/" + custerName + "/line", myid);
		// 加载watch
		loadZkWatch(zkListen.getWatchPath(), zkConn, zkListen);
	}

	private static void loadZkWatch(Set<String> setPaths,
			final CuratorFramework zkConn, final ZookeeperProcessListen zkListen)
			throws Exception {
		if (null != setPaths && !setPaths.isEmpty()) {
			for (String path : setPaths) {
				if (path.endsWith("schema")||path.equals("/mycat/" + custerName + "/line/"+myid)||path.endsWith("user")) {
					runWatch(zkConn, path, zkListen);
					LOGGER.info("ZktoxmlMain loadZkWatch path:" + path
							+ " regist success");
				}
			}
		}
	}

	/**
	 * 创建临时节点测试 方法描述
	 * 
	 * @param parent
	 * @param node
	 * @param zkConn
	 * @return 
	 * @throws Exception
	 * @创建日期 2016年9月20日
	 */
	private static String createTempNode(String parent, String node,
			final CuratorFramework zkConn, Server server) throws Exception {
		String path = ZKPaths.makePath(parent, node);
		String ipAddr = InetAddress.getLocalHost().getHostAddress();
		String port = null;
		for (Property prop : server.getSystem().getProperty()) {
			if ("serverPort".equals(prop.getName())) {
				port = prop.getValue();
				break;
			}
		}
		 try {
			return zkConn.create()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(path,
							(node + ":" + ipAddr + ":" + port + ":1").getBytes());
		} catch (Exception e) {
			int i=0;
			while(i++<13){
				try {
					LOGGER.warn(myid+" is exist,sleep 3s and then retry");
					Thread.sleep(3000);
					return zkConn.create()
							.withMode(CreateMode.EPHEMERAL)
							.forPath(path,
									(node + ":" + ipAddr + ":" + port + ":1").getBytes());
				} catch (Exception e1) {
				}
			}
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}

	/**
	 * 进行zk的watch操作 方法描述
	 * 
	 * @param zkConn
	 *            zk的连接信息
	 * @param path
	 *            路径信息
	 * @param zkListen
	 *            监控路径信息
	 * @throws Exception
	 * @创建日期 2016年9月20日
	 */
	private static void runWatch(final CuratorFramework zkConn, String path,
			final ZookeeperProcessListen zkListen) throws Exception {
		zkConn.getData().usingWatcher(new Watcher() {
//			ExecutorService pool = Executors.newCachedThreadPool();

			@Override
			public void process(WatchedEvent event) {
				LOGGER.info("ZktoxmlMain runWatch  process path receive event:"
						+ event);
				String path = ZookeeperPath.ZK_SEPARATOR.getKey()
						+ event.getPath();
				//shutdown Mycat
				if(path.equals("/mycat/" + custerName + "/line/"+myid)){
					  try {
						String ipPort =	new String(zkConn.getData().forPath("/mycat/" + custerName + "/line/"+myid));
						String[] tmpipp=ipPort.split(":");
						if(tmpipp[3].equals("0")){
							MycatShutdown.shutdown();
						}
					  } catch (Exception e) {
						  LOGGER.error(e.getMessage(),e);
					}
				}
				// 进行通知更新
				zkListen.notifly(path);
				MycatConfig config = MycatServer.getInstance().getConfig();
				MycatConfig newConfig = new MycatConfig(config.getDataHosts(),
						config.getDataNodes());
				int clusterSize=config.getDataHosts().values().size();
				String[] tmpIdArray=myid.split("_");
				try {
					boolean isMinid=false;
					List<String> list=zkConn.getChildren().forPath("/mycat/" + custerName + "/line");
					if(CollectionUtils.isNotEmpty(list)){
						Collections.sort(list);
						String minid=null;
						for(String znode:list){
						  String ipPort=	new String(zkConn.getData().forPath("/mycat/" + custerName + "/line/"+znode));
						  String[] tmpipp=ipPort.split(":");
						  TelnetClient client=connection(tmpipp[1], Integer.parseInt(tmpipp[2]));
						  if(client!=null){
							  client.disconnect();
							  minid=znode;
							  break;
						  }
						}
						if(myid.equals(minid)){
							isMinid=true;
							if (config.getSchemas() != null) {
								for (String key : config.getSchemas().keySet()) {
									SchemaConfig sc = config.getSchemas().get(key);
									if (sc.getTables() != null) {
										for (String tbName : sc.getTables().keySet()) {
											TableConfig tc = sc.getTables().get(tbName);
											if(!tc.isGlobalTable()){
												writeEsIdxKeySet2Path(sc.getName().toLowerCase(), tbName, zkConn);
											}
										}
									}
								}
							}
						}
					}
					int zkno=Integer.parseInt(tmpIdArray[tmpIdArray.length-1]);
					if(zkno>clusterSize||zkno<1){
						LOGGER.info("zkno:"+zkno+" is not in [1,"+clusterSize+"],wait to zkPath done...");
						checkZkConfigIsDone(config);
					}
					config.reload(newConfig.getUsers(), newConfig.getSchemas(),
							newConfig.getDataNodes(), newConfig.getDataHosts(),
							newConfig.getCluster(), newConfig.getFirewall(), false);
					LOGGER.info("ZktoxmlMain runWatch  process path receive event:"
							+ event + " notifly success");
					if(zkno>=1&&zkno<=clusterSize)
					handleSecondaryIdxChange(config,zkno,isMinid,zkConn);
					// 重新注册监听
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				try {
					runWatch(zkConn, path, zkListen);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}

			private void checkZkConfigIsDone(MycatConfig config) {
				if (config.getSchemas() != null) {
					for (String key : config.getSchemas().keySet()) {
						SchemaConfig sc = config.getSchemas().get(key);
						if (sc.getTables() != null) {
							for (String tbName : sc.getTables().keySet()) {
								TableConfig tc = sc.getTables().get(tbName);
								if (!tc.isGlobalTable()) {
									for (;;) {
										try {
											if (zkConn.checkExists().forPath("/mycat/"
																	+ custerName
																	+ "/tmpidx/"
																	+ sc.getName().toLowerCase()
																	+ "_"+ tbName.toLowerCase()) != null)
												break;
										} catch (Exception e) {
										}
									}
								}
							}
						}
					}
				}
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
			 
			private void handleSecondaryIdxChange(MycatConfig config, int zkno, boolean isMinid, CuratorFramework zkConn) {
				if (config.getSchemas() != null) {
					for (String key : config.getSchemas().keySet()) {
						SchemaConfig sc = config.getSchemas().get(key);
						if (sc.getTables() != null) {
							for (String tbName : sc.getTables().keySet()) {
								TableConfig tc = sc.getTables().get(tbName);
								if(!tc.isGlobalTable()){
									handleAddAndDel2ndIdx(sc.getName().toLowerCase(),tbName, tc,
											config.getDataNodes(),sc.getAllDataNodes(),zkno,isMinid,zkConn);
								}
							}
						}
					}
				}
			}

			@SuppressWarnings("unchecked")
			private void handleAddAndDel2ndIdx(final String schemaName,final String tbName,
					TableConfig tc, Map<String, PhysicalDBNode> mapNode, Set<String> scHosts, int zkno, boolean isMinid, CuratorFramework zkConn) {
				Set<String> esIdxKeySet=buildesIdxKeySet(schemaName, tbName,zkConn);
				final List<String> toBeAdded = (List<String>) CollectionUtils
						.subtract(tc.getSecondaryIndexKeySet(), esIdxKeySet);
				List<String> toBeRemoved = (List<String>) CollectionUtils
						.subtract(esIdxKeySet, tc.getSecondaryIndexKeySet());
				if (isMinid) {
					if (CollectionUtils.isNotEmpty(toBeRemoved)) {
						for (String col : toBeRemoved) {
							LppzEsComponent
									.getInstance()
									.deleteIndex(
											CatSecondaryEsIndexHandler.IDXTABLENAMEESPREFIX
													+ schemaName.toLowerCase()
													+ "-"
													+ tbName.toLowerCase()
													+ "-" + col + "-*");
						}
					}
				}
				if (CollectionUtils.isNotEmpty(toBeAdded)) {
					toBeAdded.add(tc.getPrimaryKey() + " as idxpk");
					if (StringUtils.isNotBlank(tc.getPartitionColumn()))
						toBeAdded.add(tc.getPartitionColumn()
								+ " as shardingPc");
					else if (StringUtils.isNotBlank(tc.getJoinKey()))
						toBeAdded.add(tc.getJoinKey() + " as shardingPc");
					else if (StringUtils.isNotBlank(tc.getRootParentKey()))
						toBeAdded.add(tc.getRootParentKey() + " as shardingPc");
					PhysicalDBNode[] ppArrray=mapNode.values().toArray(new PhysicalDBNode[0]);
					if(zkno>ppArrray.length||zkno<1)
						return;
					PhysicalDBNode node=ppArrray[zkno-1];
					PhysicalDBPool db = node.getDbPool();
					PhysicalDatasource ddb=db.getAllDataSources().iterator().next();
					MysqlDmlUtil.handleInsert2ndIdx2Es(schemaName,tbName, toBeAdded,ddb);
//					for (String kk : mapNode.keySet()) {
//						if(!scHosts.contains(kk))
//							continue;
//						PhysicalDBPool db = mapNode.get(kk).getDbPool();
//						PhysicalDatasource[] pdb=db.getAllDataSources().toArray(new PhysicalDatasource[0]);
//						final PhysicalDatasource ddb=pdb[zkno];
//						MysqlDmlUtil.handleInsert2ndIdx2Es(schemaName,tbName, toBeAdded,ddb);
////						for (final PhysicalDatasource ddb : db
////								.getAllDataSources()) {
////							MysqlDmlUtil.handleInsert2ndIdx2Es(schemaName,tbName, toBeAdded,
////									ddb);
//////							pool.execute(new Runnable() {
//////								@Override
//////								public void run() {
//////								}
//////							});
////						}
//					}
				}
			}

			private Set<String> buildesIdxKeySet(final String schemaName,
					final String tbName, CuratorFramework zkConn) {
				for (;;) {
					byte[] bb = null;
					String path = "/mycat/" + custerName + "/tmpidx/"
							+ schemaName.toLowerCase() + "_"
							+ tbName.toLowerCase();
					try {
						if (zkConn.checkExists().forPath(path) != null)
							bb = zkConn.getData().forPath(
									"/mycat/" + custerName + "/tmpidx/"
											+ schemaName.toLowerCase() + "_"
											+ tbName.toLowerCase());
						if (bb != null) {
							Set<String> esIdxKeySet = KryoUtil.kyroDeSeriLize(
									bb, Set.class);
							LOGGER.info("get esIdxKeySet from zk successfully!");
							return esIdxKeySet;
						}
						Thread.sleep(100);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
			
			private void writeEsIdxKeySet2Path(final String schemaName,
					final String tbName,CuratorFramework zkConn){
				Set<String> esIdxKeyIdxSet=null;
				if("0".equals(forceInit))
					esIdxKeyIdxSet=new LinkedHashSet<String>();
				else
					esIdxKeyIdxSet = LppzEsComponent.getInstance().getIndex(CatSecondaryEsIndexHandler.IDXTABLENAMEESPREFIX
											+schemaName.toLowerCase()+ "-" +tbName.toLowerCase() + "-");
					Set<String> esIdxKeySet = new LinkedHashSet<String>();
					for (String s : esIdxKeyIdxSet) {
						String[] tmpArray = s.split("-");
						esIdxKeySet.add(tmpArray[tmpArray.length - 2]);
					}
					byte[] bb=null;
					try {
						bb = KryoUtil.kyroSeriLize(esIdxKeySet, -1);
						if(zkConn.checkExists().forPath("/mycat/"+custerName+"/tmpidx/"+schemaName.toLowerCase()+"_"+tbName.toLowerCase())!=null)
							zkConn.delete().forPath("/mycat/"+custerName+"/tmpidx/"+schemaName.toLowerCase()+"_"+tbName.toLowerCase());
						String path = ZKPaths.makePath("/mycat/"+custerName+"/tmpidx", schemaName.toLowerCase()+"_"+tbName.toLowerCase());
						zkConn.create().withMode(CreateMode.EPHEMERAL).forPath(path, bb);
						LOGGER.info("create "+schemaName.toLowerCase()+"."+tbName.toLowerCase()+":esIdxKeySet to zk successfully!");
					} catch (Exception e) {
						LOGGER.error(e.getMessage(),e);
					}
			}
		}).inBackground().forPath(path);
	}

	private static CuratorFramework buildConnection(String url) {
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
				url, new ExponentialBackoffRetry(100, 6));

		// start connection
		curatorFramework.start();
		// wait 3 second to establish connect
		try {
			curatorFramework.blockUntilConnected(3, TimeUnit.SECONDS);
			if (curatorFramework.getZookeeperClient().isConnected()) {
				return curatorFramework.usingNamespace("");
			}
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}

		// fail situation
		curatorFramework.close();
		throw new RuntimeException("failed to connect to zookeeper service : "
				+ url);
	}
}
