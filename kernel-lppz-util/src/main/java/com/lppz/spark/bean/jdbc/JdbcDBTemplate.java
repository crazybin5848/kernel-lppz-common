package com.lppz.spark.bean.jdbc;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.util.Assert;

import com.lppz.core.datasource.LppzBasicDataSource;
import com.lppz.core.datasource.MycatClusterDynamicDataSource;
import com.lppz.spark.rdbms.JdbcHandler;

public class JdbcDBTemplate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3982233228483908874L;
	private JdbcTransactionTemplate stt;
	
	public JdbcTransactionTemplate getStt() {
		return stt;
	}

	public void setStt(JdbcTransactionTemplate stt) {
		this.stt = stt;
	}

	public JdbcDBTemplate(JdbcTransactionTemplate stt) {
		this.stt = stt;
	}
	
	public void close(){
		Assert.notNull(stt);
		Assert.notNull(stt.getJt());
		DataSource dds=stt.getJt().getDataSource();
		if(dds instanceof LppzBasicDataSource){
			LppzBasicDataSource ds=(LppzBasicDataSource)dds;
			Assert.notNull(ds);
			ds.close();
		}
		if(dds instanceof MycatClusterDynamicDataSource){
			MycatClusterDynamicDataSource ds=(MycatClusterDynamicDataSource)dds;
			Assert.notNull(ds);
			try {
				ds.destory();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void doIntrans(final JdbcHandler handler) {
		stt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				handler.handleInTrans(stt.getJt());
			}
		});
	}
}
