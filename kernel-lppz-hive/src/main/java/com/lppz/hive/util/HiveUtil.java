package com.lppz.hive.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MFromConfig;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MLink;
import org.apache.sqoop.model.MLinkConfig;
import org.apache.sqoop.model.MToConfig;
import org.apache.sqoop.validation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.hive.bean.HivePartionCol;
import com.lppz.hive.bean.HiveSqoopConfigBean;
import com.lppz.hive.bean.HiveSqoopSingleBean;

public class HiveUtil {
	private static final Logger LOG = LoggerFactory.getLogger(HiveUtil.class);

	 private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	public static String getHiveSqlFromMysql(HiveSqoopSingleBean bean) {
		Connection conn = null;
		String sql;
		String url = bean.getConfigBean().getRdbmsjdbcUrl();//HivePropertiesUtils.getKey("hive.rdbms.jdbcUrl");
		try {
			Class.forName(bean.getConfigBean().getRdbmsdbDriver());
			conn = DriverManager.getConnection(url, bean.getConfigBean().getRdbmsjdbcUser(), bean.getConfigBean().getRdbmsjdbcPasswd());
			Statement stmt = conn.createStatement();
			String tableName=bean.getSourceBean().getSqltableName();
			sql = "desc " + tableName + ";";
			ResultSet rs = stmt.executeQuery(sql);
			StringBuilder ddlSql = new StringBuilder("create table "
					+ bean.getSourceBean().getHivetableName() + "(");
			while (rs.next()) {
				ddlSql.append(rs.getString(1)).append(" ");
				String type = rs.getString(2);
				ddlSql.append(convert(type)).append(",");
			}
			String s = ddlSql.substring(0, ddlSql.length() - 1) + ")";
			if(CollectionUtils.isNotEmpty(bean.getSourceBean().getHpcList())){
				s+="\r\n PARTITIONED BY(";
				int k=0;
				for(HivePartionCol hpc:bean.getSourceBean().getHpcList()){
					s+=hpc.getCol()+" "+hpc.getType();
					if(k++<bean.getSourceBean().getHpcList().size()-1)
						s+=",";
				}
				s+=")";
			}
			s += "\r\n row format delimited fields terminated by '\\t'";
			return s;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		return null;
	}

	private static String convert(String type) {
		type = type.toLowerCase();
		if (type.contains("bit") || type.contains("tinyint"))
			return "tinyint";
		if (type.contains("smallint"))
			return "smallint";
		if (type.contains("mediumint") || type.contains("int")
				|| type.contains("integer"))
			return "int";
		if (type.contains("bigint"))
			return "bigint";
		if (type.contains("decimal"))
			return type;
		if (type.contains("double") || type.contains("numeric"))
			return "double";
		if (type.contains("float"))
			return "float";
		if (type.contains("varchar"))
			return type;
		if (type.contains("char"))
			return type;
		if (type.contains("text"))
			return "string";
		if (type.contains("binary"))
			return "binary";
		if (type.contains("datetime") || type.contains("timestamp"))
			return "timestamp";
		if (type.contains("date"))
			return "date";
		return type;
	}

	public static void deleteSqoopJobAndLinks(Map<String, Object> map) {
		SqoopClient client =(SqoopClient) map.get("client");
		long jobId =(long) map.get("jobId");
		long fromLinkId =(long) map.get("fromLinkId");
		long toLinkId =(long) map.get("toLinkId");
		HiveUtil.deleteJobAndLink(client, jobId, fromLinkId, toLinkId);
	}
	
	public static Map<String, Object> mysql2hdfs(HiveSqoopSingleBean bean) {
		String url = bean.getConfigBean().getSqoopServerUrl();
		SqoopClient client = new SqoopClient(url);
//		deleteAllJobAndLink(client);
		String namesufix=Integer.toString(Math.abs((int)UUID.randomUUID().getLeastSignificantBits()),36);
		MLink fromLink = client.createLink("generic-jdbc-connector");
		fromLink.setName("JDBC connector"+namesufix);
		fromLink.setCreationUser("admln");
		MLinkConfig fromLinkConfig = fromLink.getConnectorLinkConfig();
		fromLinkConfig.getStringInput("linkConfig.connectionString").setValue(
				bean.getConfigBean().getRdbmsjdbcUrl());
		fromLinkConfig.getStringInput("linkConfig.jdbcDriver").setValue(
				bean.getConfigBean().getRdbmsdbDriver());
		fromLinkConfig.getStringInput("linkConfig.username").setValue(bean.getConfigBean().getRdbmsjdbcUser());
		fromLinkConfig.getStringInput("linkConfig.password").setValue(bean.getConfigBean().getRdbmsjdbcPasswd());
		Status fromStatus = client.saveLink(fromLink);
		if (fromStatus.canProceed()) {
			LOG.info("create JDBC Link success，ID: "
					+ fromLink.getPersistenceId());
		} else {
			LOG.error("create JDBC Link fail");
		}
		MLink toLink = client.createLink("hdfs-connector");
		toLink.setName("HDFS connector"+namesufix);
		toLink.setCreationUser("admln");
		MLinkConfig toLinkConfig = toLink.getConnectorLinkConfig();
		toLinkConfig.getStringInput("linkConfig.uri").setValue(
				bean.getConfigBean().getSqoopHdfsUrl());
		Status toStatus = client.saveLink(toLink);
		if (toStatus.canProceed()) {
			LOG.info("create HDFS Link success，ID: "
					+ toLink.getPersistenceId());
		} else {
			LOG.error("create HDFS Link fail");
		}

		long fromLinkId = fromLink.getPersistenceId();
		long toLinkId = toLink.getPersistenceId();
		MJob job = client.createJob(fromLinkId, toLinkId);
		job.setName("mysql to hdfs job"+namesufix);
		job.setCreationUser("admln");
		MFromConfig fromJobConfig = job.getFromJobConfig();
		fromJobConfig.getStringInput("fromJobConfig.partitionColumn").setValue(
				bean.getSourceBean().getPk());
//		fromJobConfig.getStringInput("fromJobConfig.allowNullValueInPartitionColumn").setValue(
//				"true");
		fromJobConfig.getStringInput("fromJobConfig.sql").setValue(
				bean.getSourceBean().getSql());
		MToConfig toJobConfig = job.getToJobConfig();
		toJobConfig.getStringInput("toJobConfig.outputDirectory").setValue(
				"/tmp/hive/data/"+bean.getSourceBean().getHivetableName()+".data/");
		Status status = client.saveJob(job);
		if (status.canProceed()) {
			LOG.info("JOB Created Success，ID: " + job.getPersistenceId());
		} else {
			LOG.error("JOB Created fail");
		}

		long jobId = job.getPersistenceId();
		client.startJob(jobId);
		Map<String,Object> map=new HashMap<String,Object>(4);
		map.put("client", client);
		map.put("jobId", jobId);
		map.put("fromLinkId", fromLinkId);
		map.put("toLinkId", toLinkId);
		return map;
	}

	public static void deleteJobAndLink(SqoopClient client, long jobId,
			long fromLinkId, long toLinkId) {
		client.clearCache();
		while(true){
		try {
			client.deleteJob(jobId);
			break;
		} catch (Exception e) {
			client.stopJob(jobId);
		}
		}
		client.deleteLink(fromLinkId);
		client.deleteLink(toLinkId);
	}

	public static void deleteAllJobAndLink(SqoopClient client) {
		for (MJob job : client.getJobs()) {
			client.clearCache();
			try {
				client.enableJob(job.getPersistenceId(), false);
				client.deleteJob(job.getPersistenceId());
			} catch (Exception e) {
				client.stopJob(job.getPersistenceId());
				client.enableJob(job.getPersistenceId(), false);
				client.deleteJob(job.getPersistenceId());
			}
		}
		for (MLink link : client.getLinks()) {
			client.enableLink(link.getPersistenceId(), false);
			client.deleteLink(link.getPersistenceId());
		}
	}
	
	 public static void createHiveTableFromRDBMS(HiveSqoopSingleBean bean) throws SQLException {
		 Statement stmt = buildHiveStatement(bean.getConfigBean());
	     String ddl=bean.getSourceBean().getDdl();
		String crSql=StringUtils.isNotBlank(ddl)?ddl:HiveUtil.getHiveSqlFromMysql(bean);
	    String tableName=bean.getSourceBean().getHivetableName();
		stmt.execute("use "+bean.getSourceBean().getHiveschema());
		stmt.execute("drop table if exists " + tableName);
	    stmt.execute(crSql);
	  }
	 
	 public static void loadData2Hive(HiveSqoopSingleBean bean) throws SQLException {
	   Statement stmt = buildHiveStatement(bean.getConfigBean());
	    String tableName=bean.getSourceBean().getHivetableName();
		String filepath = "/tmp/hive/data/"+tableName+".data/*";
	    String sql = "load data inpath '" + filepath + "' OVERWRITE into table " + tableName;
	    if(CollectionUtils.isNotEmpty(bean.getSourceBean().getHpcList())){
	    		sql+=" PARTITION (";
	    		int k=0;
	    		for(HivePartionCol hpc:bean.getSourceBean().getHpcList()){
	    			sql+=hpc.getCol()+"="+hpc.getValue();
	    			if(k++<bean.getSourceBean().getHpcList().size()-1)
	    				sql+=",";
	    		}
	    		sql+=")";
	    }
	    LOG.info(sql);
	    stmt.execute("use "+bean.getSourceBean().getHiveschema());
	    stmt.execute(sql);
	  }
	 
	 public static void clearloadHive(HiveSqoopSingleBean bean) throws SQLException {
		 Statement stmt = buildHiveStatement(bean.getConfigBean());
		 String tableName=bean.getSourceBean().getHivetableName();
		 String filepath = "/tmp/hive/data/"+tableName+".data";
		 String sql = "dfs -rmr -skipTrash " + filepath ;
		 stmt.execute(sql);
	 }
	 
	 public static Statement buildHiveStatement(HiveSqoopConfigBean bean) throws SQLException {
		try {
	      Class.forName(driverName);
	    } catch (ClassNotFoundException e) {
	      LOG.error(e.getMessage(),e);
	      System.exit(1);
	    }
	    Connection con = DriverManager.getConnection(bean.getHive2jdbcUrl(), bean.getHive2jdbcUser(),bean.getHive2jdbcPasswd());
	    Statement stmt = con.createStatement();
		return stmt;
	}
}