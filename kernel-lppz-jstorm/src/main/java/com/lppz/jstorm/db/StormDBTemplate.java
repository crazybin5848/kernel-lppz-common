package com.lppz.jstorm.db;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class StormDBTemplate {
	private StormTransactionTemplate stt;

	public StormDBTemplate(StormTransactionTemplate stt) {
		this.stt = stt;
	}

	public void doIntrans(final JdbcHandler handler) {
		stt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				handler.handleInTrans(stt.getJt());
				// stt.getJt().update("insert into users (username) values ('xiaxin');",x);
			}
		});
	}
}
