package io.mycat.sencondaryindex.kafka.producer;

import io.mycat.sencondaryindex.model.SchemaConfig;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.fastjson.JSONObject;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.spark.bean.jdbc.JdbcDBTemplate;
import com.lppz.spark.bean.jdbc.JdbcTransactionTemplate;
import com.lppz.spark.rdbms.JdbcHandler;
import com.lppz.util.disruptor.BaseErrorHandler;

@SuppressWarnings("rawtypes")
public class KafkaErrorDBHandler implements BaseErrorHandler<EsModel>{
	private int reTryCount;
	private boolean isLogInfo;
	private SchemaConfig sc;
	private JdbcDBTemplate st;
	private static final Logger logger = LoggerFactory.getLogger(KafkaErrorDBHandler.class);

	public KafkaErrorDBHandler(DataSource ds, SchemaConfig sc){
		PlatformTransactionManager tx=new DataSourceTransactionManager(ds);
		JdbcTransactionTemplate stt=new JdbcTransactionTemplate(new JdbcTemplate(ds), tx);
		this.st=new JdbcDBTemplate(stt);
		this.sc=sc;
	}
	
	@Override
	public void handler(EsModel u) {
		if(EsDMlEnum.Update.equals(u.getEnumType())){
			String[] tmp=u.getId().split("-");
			String shardValue=tmp[0];
			String colName=tmp[1];
			String pkValue=tmp[2];
			String[] tmpIdx=u.getIndex().split("-");
			String tbName=tmpIdx[4];
			JSONObject jsonObject = JSONObject.parseObject((String)u.getJsonSource()); 
			String idxValue=jsonObject.getString("idxValue");
			String pk=sc.getTables().get(tbName).getPrimaryKey();
			String secKeys=sc.getTables().get(tbName).getSecondaryIndexKeys();
			String shardKey=buildShardKey(tbName);
			final StringBuilder sql=new StringBuilder("insert into ").append(tbName).append("(").append(pk).append(",");
			if(!pk.equals(shardKey)&&shardKey!=null){
				sql.append(shardKey).append(",");
			}
			for(String key:secKeys.split("::")){
				if(!key.equals(colName)){
					sql.append(key).append(",");
				}
			}
			sql.append(colName).append(") values('").append(pkValue).append("',");
			if(!pk.equals(shardKey)&&shardKey!=null){
				sql.append("'").append(shardValue).append("',");
			}
			for(String key:secKeys.split("::")){
				if(!key.equals(colName)){
					sql.append("null").append(",");
				}
			}
			sql.append("'").append(idxValue).append("')");
			try {
				st.doIntrans(new JdbcHandler(){
					@Override
					public void handleInTrans(JdbcTemplate jt) {
						try {
							jt.update(sql.toString());
						} catch (DataAccessException e) {
							throw e;
						}
					}
				});
			} catch (DataAccessException e) {
				logger.warn(e.getMessage(),e);
				logger.info("compensate data successfully");
				throw e;
			}
		}
	}

	private String buildShardKey(String tbName) {
		if(sc.getTables().get(tbName).getJoinKey()!=null)
		return sc.getTables().get(tbName).getJoinKey();
		return sc.getTables().get(tbName).getRoot_id();
	}
	
	public void handleInsert2ndIdx2Es(String schemaName, String tbName,
			List<String> toBeAdded) {

	}
	
	@Override
	public int getRetryCount() {
		return reTryCount;
	}

	@Override
	public boolean isLogInfo() {
		return isLogInfo;
	}

	public int getReTryCount() {
		return reTryCount;
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

	public void setLogInfo(boolean isLogInfo) {
		this.isLogInfo = isLogInfo;
	}
}
