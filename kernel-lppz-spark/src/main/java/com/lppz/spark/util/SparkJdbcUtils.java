package com.lppz.spark.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.spark.bean.jdbc.JdbcDBTemplate;
import com.lppz.spark.bean.jdbc.JdbcTransactionTemplate;
import com.lppz.spark.scala.jdbc.JdbcSqlHandler;

public class SparkJdbcUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9207845675980386006L;

	private static Logger logger = LoggerFactory.getLogger(SparkJdbcUtils.class);
	
	public JdbcDBTemplate initDsAndStormTemplate(String dsYamlConfPath) {
		LppzBasicDataSource lb = buildBaseDsFromYaml(dsYamlConfPath);
		DataSource ds=build(lb);
		PlatformTransactionManager tx=new DataSourceTransactionManager(ds);
		JdbcTransactionTemplate stt=new JdbcTransactionTemplate(new JdbcTemplate(ds), tx);
		JdbcDBTemplate st=new JdbcDBTemplate(stt);
		return st;
	}

	protected DataSource build(LppzBasicDataSource lb) {
		return lb;
	}

	@SuppressWarnings("unchecked")
	protected LppzBasicDataSource buildBaseDsFromYaml(String dsYamlConfPath) {
		Map<Object, Object> targetDataSources=null;
		try {
			if (StringUtils.isNotBlank(dsYamlConfPath)) {
				if (new File(dsYamlConfPath).exists()) {
					targetDataSources = (Map<Object, Object>) new Yaml()
					.load(new FileSystemResource(dsYamlConfPath)
					.getInputStream());
				} 
			}
			else {
				InputStream in=SparkJdbcUtils.class.getResourceAsStream("/META-INF/ds-cluster.yaml");
				if(in!=null&&in.available()>0){
					targetDataSources = (Map<Object, Object>) new Yaml().load(in);
					in.close();
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		LppzBasicDataSource lb = (LppzBasicDataSource) targetDataSources
				.values().iterator().next();
		return lb;
	}
	
	public void mulitThreadExec(List<String> sql,JdbcDBTemplate st,int batchSize){
		if(null!=sql && sql.size()>0){
			ExecutorService tp = Executors.newCachedThreadPool();
			int total=sql.size();
			int totalPage=total % batchSize==0?total / batchSize:total / batchSize +1;
			
			AtomicInteger cnt=new AtomicInteger();
			
			for(int i=0;i<totalPage;i++){
				List<String> tmp=sql.subList(i*batchSize, (i+1)*batchSize>total?total:(i+1)*batchSize);
				tp.execute(new ExecThread(tmp,st,cnt));
			}
			
			while(cnt.get()!=totalPage){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			}
			tp.shutdown();
		}
	}
	
	class ExecThread implements Runnable{
		
		private List<String> tmp;
		
		private JdbcDBTemplate st;
		
		private AtomicInteger ai;
		
		public ExecThread(List<String> tmp,JdbcDBTemplate st,AtomicInteger ai){
			this.tmp=tmp;
			this.st=st;
			this.ai=ai;
		}

		@Override
		public void run() {
			try{
				JdbcSqlHandler handler=new JdbcSqlHandler(tmp.toArray(new String[]{}));
				st.doIntrans(handler);
			}catch(Exception ex){
				logger.error("batchUpdate has failure",ex);
				boolean needTry=true;
				if(ex instanceof UncategorizedSQLException){
					UncategorizedSQLException ee=(UncategorizedSQLException)ex;
					if(ee.getSQLException().getErrorCode()==1062){
						logger.error(ex.getMessage(),ex);
						logger.error("no need to retry");
						needTry=false;
					}
				}
				if(needTry){
					for(int i=1;i<21;i++){
						logger.info("jdbc batchUpdate retry no{}",i);
						try{
							JdbcSqlHandler handler=new JdbcSqlHandler(tmp.toArray(new String[]{}));
							st.doIntrans(handler);
							break;
						}catch(Exception e){
							logger.error("jdbc batchUpdate retry no{} has failure", i);
						}
					}
					logger.error("Here is the failure of 20 times sql :{}",JSON.toJSONString(tmp));
				}
			}finally{
				ai.getAndIncrement();
			}
		}
		
	}
	
	public static void main(String[] args){
//		StormDBTemplate st =initDsAndStormTemplate("C:\\Users\\romeo\\Desktop\\ds-cluster.yaml");
//		
//		HaNaJdbcHandler handler=new HaNaJdbcHandler(new String[]{
//				"upsert HDBMODEL.TAG_BY365_TEST values ('1122738702','30') where ZBPARTNE3='1122738702'",
//				"upsert HDBMODEL.TAG_BY365_TEST values ('1122738883','30') where ZBPARTNE3='1122738883'"
//		});
//		
//		st.doIntrans(handler);
		
		List<String> list=new ArrayList<>();
		
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		
		String[] temp=list.toArray(new String[]{});
		
		System.out.println(temp.length);
		
		for(String str:temp){
			System.out.println(str);
		}
	}
	
}
