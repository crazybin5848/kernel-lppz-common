package com.lppz.hive.test;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MFromConfig;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MLink;
import org.apache.sqoop.model.MLinkConfig;
import org.apache.sqoop.model.MSubmission;
import org.apache.sqoop.model.MToConfig;
import org.apache.sqoop.submission.counter.Counter;
import org.apache.sqoop.submission.counter.CounterGroup;
import org.apache.sqoop.submission.counter.Counters;
import org.apache.sqoop.validation.Status;

public class HDFSToMysql {
    public static void main(String[] args) {
        sqoopTransfer();
    }
    public static void sqoopTransfer() {
        //初始化
        String url = "http://10.6.30.37:12000/sqoop/";
        SqoopClient client = new SqoopClient(url);
        deleteAllJobAndLink(client);
        MLink fromLink = client.createLink("generic-jdbc-connector");
        fromLink.setName("JDBC connector");
        fromLink.setCreationUser("admln");
        MLinkConfig fromLinkConfig = fromLink.getConnectorLinkConfig();
        fromLinkConfig.getStringInput("linkConfig.connectionString").setValue("jdbc:mysql://10.6.30.35/test");
        fromLinkConfig.getStringInput("linkConfig.jdbcDriver").setValue("com.mysql.jdbc.Driver");
        fromLinkConfig.getStringInput("linkConfig.username").setValue("root");
        fromLinkConfig.getStringInput("linkConfig.password").setValue("KTqHDMg8r3q1w");
        Status fromStatus = client.saveLink(fromLink);
        if(fromStatus.canProceed()) {
         System.out.println("创建JDBC Link成功，ID为: " + fromLink.getPersistenceId());
        } else {
         System.out.println("创建JDBC Link失败");
        }
        
        //创建一个源链接 HDFS
        MLink toLink = client.createLink("hdfs-connector");
        toLink.setName("HDFS connector");
        toLink.setCreationUser("admln");
        MLinkConfig toLinkConfig = toLink.getConnectorLinkConfig();
        toLinkConfig.getStringInput("linkConfig.uri").setValue("hdfs://10.6.30.37:9000/");
        Status toStatus = client.saveLink(toLink);
        if(toStatus.canProceed()) {
         System.out.println("创建HDFS Link成功，ID为: " + toLink.getPersistenceId());
        } else {
         System.out.println("创建HDFS Link失败");
        }
        
        long fromLinkId = fromLink.getPersistenceId();
        long toLinkId = toLink.getPersistenceId();
        //创建一个任务
        MJob job = client.createJob(fromLinkId, toLinkId);
        job.setName("mysql to hdfs job");
        job.setCreationUser("admln");
        //设置源链接任务配置信息
        MFromConfig fromJobConfig = job.getFromJobConfig();
//        Link configuration:
//        	linkConfig.jdbcDriver,JDBC Driver Class : null
//        	linkConfig.connectionString,JDBC Connection String : null
//        	linkConfig.username,Username : null
//        	linkConfig.password,Password : null
//        	linkConfig.jdbcProperties,JDBC Connection Properties : null
//
//        	From database configuration:
//        	fromJobConfig.schemaName,Schema name : null
//        	fromJobConfig.tableName,Table name : null
//        	fromJobConfig.sql,Table SQL statement : null
//        	fromJobConfig.columns,Table column names : null
//        	fromJobConfig.partitionColumn,Partition column name : null
//        	fromJobConfig.allowNullValueInPartitionColumn,Null value allowed for the partition column : null
//        	fromJobConfig.boundaryQuery,Boundary query : null
//
//        	To database configuration:
//        	toJobConfig.schemaName,Schema name : null
//        	toJobConfig.tableName,Table name : null
//        	toJobConfig.sql,Table SQL statement : null
//        	toJobConfig.columns,Table column names : null
//        	toJobConfig.stageTableName,Stage table name : null
//        	toJobConfig.shouldClearStageTable,Should clear stage table : null
//        fromJobConfig.getStringInput("fromJobConfig.schemaName").setValue("test");
//        fromJobConfig.getStringInput("fromJobConfig.tableName").setValue("omsstock");
        fromJobConfig.getStringInput("fromJobConfig.partitionColumn").setValue("id");
        fromJobConfig.getStringInput("fromJobConfig.sql").setValue("select * from omsstock where ${CONDITIONS} and id =111");
//        fromJobConfig.getStringInput("fromJobConfig.boundaryQuery").setValue("select * from omsstock limit 10");
        
        //创建目的地链接任务配置信息
//        toJobConfig.outputFormat,Output format : null
//        toJobConfig.compression,Compression format : null
//        toJobConfig.customCompression,Custom compression format : null
//        toJobConfig.outputDirectory,Output directory : null
        MToConfig toJobConfig = job.getToJobConfig();
//        toJobConfig.getStringInput("toJobConfig.outputFormat").setValue("\t");
        toJobConfig.getStringInput("toJobConfig.outputDirectory").setValue("/tmp/hive/data/omsstock.data");
        // set the driver config values
        //MDriverConfig driverConfig = job.getDriverConfig();
        //driverConfig.getStringInput("throttlingConfig.numExtractors").setValue("3");//这句还没弄明白
        Status status = client.saveJob(job);
        if(status.canProceed()) {
         System.out.println("JOB创建成功，ID为: "+ job.getPersistenceId());
        } else {
         System.out.println("JOB创建失败。");
        }
        
        //启动任务
        long jobId = job.getPersistenceId();
        MSubmission submission = client.startJob(jobId);
        System.out.println("JOB提交状态为 : " + submission.getStatus());
        while(submission.getStatus().isRunning() && submission.getProgress() != -1) {
          System.out.println("进度 : " + String.format("%.2f %%", submission.getProgress() * 100));
          //三秒报告一次进度
          try {
            Thread.sleep(3000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("JOB执行结束... ...");
        System.out.println("Hadoop任务ID为 :" + submission.getExternalJobId());
        Counters counters = submission.getCounters();
        if(counters != null) {
          System.out.println("计数器:");
          for(CounterGroup group : counters) {
            System.out.print("\t");
            System.out.println(group.getName());
            for(Counter counter : group) {
              System.out.print("\t\t");
              System.out.print(counter.getName());
              System.out.print(": ");
              System.out.println(counter.getValue());
            }
          }
        }
//        if(submission.getExternalLink() != null) {
//          System.out.println("JOB执行异常，异常信息为 : " +submission.getError());
//        }
        System.out.println("HDFS通过sqoop传输数据到MySQL统计执行完毕");
    }
	private static void deleteAllJobAndLink(SqoopClient client) {
		for(MJob job:client.getJobs()){
//			client.stopJob(job.getPersistenceId());
			client.enableJob(job.getPersistenceId(), false);
			client.deleteJob(job.getPersistenceId());
		}
		for(MLink link:client.getLinks()){
			client.enableLink(link.getPersistenceId(), false);
			client.deleteLink(link.getPersistenceId());
		}
	}
}