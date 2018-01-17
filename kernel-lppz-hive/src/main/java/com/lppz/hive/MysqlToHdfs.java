package com.lppz.hive;

import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.hive.bean.HivePartionCol;
import com.lppz.hive.bean.HiveSqoopBean;
import com.lppz.hive.bean.HiveSqoopSingleBean;
import com.lppz.hive.bean.Rdbms2HDfsBean;
import com.lppz.hive.util.HiveUtil;
import com.lppz.hive.util.HiveYamlUtils;

public class MysqlToHdfs {
	private static final Logger LOG = LoggerFactory.getLogger(MysqlToHdfs.class);

    public static void main(String[] args) throws Exception {
    			doMain(args);
//    		final String[] args1=new String[]{"/Users/zoubin/Documents/binlppzsap/kernel-lppz-common/kernel-lppz-hive/src/main/resources/META-INF/rdbms2hive.yaml","partionVal,;'2016-09-01'","param1,;1,;param2,;100"};
//    		Thread t1=new Thread(new Runnable(){
//				@Override
//				public void run() {
//					try {
//						doMain(args1);
//					} catch (SQLException | InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//    		});
//    		final String[] args2=new String[]{"/Users/zoubin/Documents/binlppzsap/kernel-lppz-common/kernel-lppz-hive/src/main/resources/META-INF/rdbms2hive2.yaml","param1,;101,;param2,;103"};
//    		Thread t2=new Thread(new Runnable(){
//				@Override
//				public void run() {
//					try {
//						doMain(args2);
//					} catch (SQLException | InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//    		});
//    		t1.start();
//    		t2.start();
//    		t1.join();
//    		t2.join();
//    		System.out.println("all over");
    }

	private static void doMain(String[] args) throws SQLException,
			InterruptedException {
		Map<Object,Object> map=null;
		if(args.length>0)
		map=HiveYamlUtils.loadYaml(args[0],false);
		else
		map=HiveYamlUtils.loadYaml("/META-INF/rdbms2hive.yaml",true);
		for(Object o:map.values()){
			HiveSqoopBean hsb=(HiveSqoopBean)o;
			if(CollectionUtils.isNotEmpty(hsb.getSourceList()))
			for(Rdbms2HDfsBean hrb:hsb.getSourceList()){
				HiveSqoopSingleBean bean=new HiveSqoopSingleBean();
				bean.setConfigBean(hsb.getConfigBean());
				bean.setSourceBean(hrb);
				if(CollectionUtils.isNotEmpty(bean.getSourceBean().getHpcList())){
					String[] ss=args[args.length-2].split(",;");
					int k=0;
					for(HivePartionCol hpc:bean.getSourceBean().getHpcList()){
						String val=hpc.getValue().replaceAll("#"+ss[k]+"#", ss[k+1]);
						k+=2;
						hpc.setValue(val);
					}
				}
				singleLoadData(bean,args);
			}
		}
	}

	private static void singleLoadData(HiveSqoopSingleBean bean,String[] args) throws SQLException,
			InterruptedException {
		    		String tableName=bean.getSourceBean().getHivetableName();
		    		if(bean.getSourceBean().isMode()){
		    			LOG.info("recreate hive table："+tableName);
		    			HiveUtil.createHiveTableFromRDBMS(bean);
				}
				String sql=bean.getSourceBean().getSql();
				LOG.info(sql);
				if(sql.contains("#")){
					if(args.length<1)
						throw new SQLException("need params to exec sql");
					String[] parmas=args[args.length-1].split(",;");
					for(int i=0;i<parmas.length;i+=2){
						sql=sql.replaceAll("#"+parmas[i]+"#", parmas[i+1]);
					}
					LOG.info(sql);
					bean.getSourceBean().setSql(sql);
				}
//				String[] ss=sql.split("where",2);
//				sql=ss[0]+" where ${CONDITIONS} ";
//				if(ss.length==2)
//					sql+=" and "+ss[1];
//				bean.getSourceBean().setSql(sql);
				Map<String,Object> map=HiveUtil.mysql2hdfs(bean);
				SqoopClient client =(SqoopClient) map.get("client");
				long jobId =(long) map.get("jobId");
				while(true){
					MSubmission ms=client.getJobStatus(jobId);
					if(ms.getStatus().isRunning()){
						LOG.info(bean.getSourceBean().getHiveschema()+"."+tableName+":progress : "
								+ String.format("%.2f %%", ms.getProgress() * 100));
					}
					else{
						LOG.info("JOB has been executed... ...status："+ms.getStatus());
						break;
					}
					Thread.sleep(2000);
				}
				try {
					HiveUtil.loadData2Hive(bean);
					LOG.info(tableName+" load successfully！");
				} catch (Exception e) {
					LOG.error(e.getMessage(),e);
					HiveUtil.clearloadHive(bean);
				}
				finally{
					HiveUtil.deleteSqoopJobAndLinks(map);
					LOG.info("delete job success！");
				}
	}
}