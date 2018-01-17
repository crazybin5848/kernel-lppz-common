package com.lppz.hive;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MFromConfig;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MLink;
import org.apache.sqoop.model.MLinkConfig;
import org.apache.sqoop.model.MSubmission;
import org.apache.sqoop.model.MToConfig;
import org.apache.sqoop.validation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.hive.bean.Hdfs2RdbmsBean;
import com.lppz.hive.util.HiveUtil;
import com.lppz.hive.util.HiveYamlUtils;

public class HdfsToMysql {
	private static final Logger LOG = LoggerFactory.getLogger(HdfsToMysql.class);

    public static void main(String[] args) throws Exception {
		Map<Object,Object> map=null;
		if(args.length>0)
		map=HiveYamlUtils.loadYaml(args[0],false);
		else
		map=HiveYamlUtils.loadYaml("/META-INF/hdfs2Rdbms.yaml",true);
		for(Object o:map.values()){
			Hdfs2RdbmsBean bean=(Hdfs2RdbmsBean)o;
			if(args.length==2)
			bean.setDfsInputDir(args[1]);
			hdfs2mysql(bean);
		}
    }
    
    public static void hdfs2mysql(Hdfs2RdbmsBean bean) {
		String url = bean.getConfigBean().getSqoopServerUrl();//"http://10.6.30.37:12000/sqoop/";
		SqoopClient client = new SqoopClient(url);
		HiveUtil.deleteAllJobAndLink(client);
		MLink fromLink = client.createLink("hdfs-connector");
		fromLink.setName("HDFS connector");
		fromLink.setCreationUser("admln");
		MLinkConfig fromLinkConfig = fromLink.getConnectorLinkConfig();
		fromLinkConfig.getStringInput("linkConfig.uri").setValue(
				bean.getConfigBean().getSqoopHdfsUrl());
		Status fromStatus = client.saveLink(fromLink);
		if (fromStatus.canProceed()) {
			LOG.info("create HDFS Link success，ID: "
					+ fromLink.getPersistenceId());
		} else {
			LOG.error("create HDFS Link fail");
		}

		MLink toLink = client.createLink("generic-jdbc-connector");
		toLink.setName("JDBC connector");
		toLink.setCreationUser("admln");
		MLinkConfig toLinkConfig = toLink.getConnectorLinkConfig();
		toLinkConfig.getStringInput("linkConfig.connectionString").setValue(
				bean.getConfigBean().getRdbmsjdbcUrl());
		toLinkConfig.getStringInput("linkConfig.jdbcDriver").setValue(
				bean.getConfigBean().getRdbmsdbDriver());
		toLinkConfig.getStringInput("linkConfig.username").setValue(bean.getConfigBean().getRdbmsjdbcUser());
		toLinkConfig.getStringInput("linkConfig.password").setValue(bean.getConfigBean().getRdbmsjdbcPasswd());
		Status toStatus = client.saveLink(toLink);
		if (toStatus.canProceed()) {
			LOG.info("create JDBC Link success，ID: "
					+ fromLink.getPersistenceId());
		} else {
			LOG.error("create JDBC Link fail");
		}
		
		long fromLinkId = fromLink.getPersistenceId();
		long toLinkId = toLink.getPersistenceId();
		MJob job = client.createJob(fromLinkId, toLinkId);
		job.setName("hdfs to mysql job");
		job.setCreationUser("admln");
		MFromConfig fromJobConfig = job.getFromJobConfig();
		fromJobConfig.getStringInput("fromJobConfig.inputDirectory").setValue(bean.getDfsInputDir());
		MToConfig toJobConfig = job.getToJobConfig();
		toJobConfig.getStringInput("toJobConfig.schemaName").setValue(bean.getRdbmsschemaName());
		toJobConfig.getStringInput("toJobConfig.tableName").setValue(bean.getRdbmstableName());
		Status status = client.saveJob(job);
		if (status.canProceed()) {
			LOG.info("JOB create success，ID: " + job.getPersistenceId());
		} else {
			LOG.error("JOB create fail");
		}

		// 启动任务
		long jobId = job.getPersistenceId();
		client.startJob(jobId);
		while(true){
			MSubmission ms=client.getJobStatus(jobId);
			if(ms.getStatus().isRunning()){
				LOG.info(bean.getRdbmsschemaName()+":"+bean.getRdbmstableName()+":progress : "
						+ String.format("%.2f %%", ms.getProgress() * 100));
			}
			else{
				LOG.info("JOB exec end... ...status："+ms.getStatus());
				HiveUtil.deleteJobAndLink(client, jobId, fromLinkId, toLinkId);
//				try {
//					clearHdfsData(bean);
//					LOG.info("clear hdfs import dir："+bean.getDfsInputDir()+" success!");
//				} catch (SQLException e) {
//					LOG.error(e.getMessage(),e);
//				}
				LOG.info("delete job success！");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}
    
    private static void clearHdfsData(Hdfs2RdbmsBean bean) throws SQLException {
		 Statement stmt = HiveUtil.buildHiveStatement(bean.getConfigBean());
		 String filepath = bean.getDfsInputDir();
		 String sql = "dfs -rmr -skipTrash " + filepath ;
		 stmt.execute(sql);
	 }
    
}