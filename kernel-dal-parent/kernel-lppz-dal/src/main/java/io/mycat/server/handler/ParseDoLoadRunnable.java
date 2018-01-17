package io.mycat.server.handler;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.druid.sql.ast.SQLExpr;

public class ParseDoLoadRunnable implements Runnable {

	private List<String[]> listRow;
	private ServerLoadDataInfileHandler serverLoadDataInfileHandler;
	private List<SQLExpr> columns;
	private String tableName;
	private String terminatedSinal;
	private AtomicInteger ai;
	public ParseDoLoadRunnable(List<SQLExpr> columns,String tableName,
			ServerLoadDataInfileHandler serverLoadDataInfileHandler,
			List<String[]> listRow,AtomicInteger ai,String terminatedSinal) {
		this.columns=columns;
		this.tableName=tableName;
		this.listRow=listRow;
		this.serverLoadDataInfileHandler=serverLoadDataInfileHandler;
		this.ai=ai;
		this.terminatedSinal=terminatedSinal;
	}

	@Override
	public void run() {
		if(serverLoadDataInfileHandler!=null){
			for(String[] row:listRow){
				try {
					serverLoadDataInfileHandler.parseOneLine(columns, tableName, row, false, terminatedSinal);
				} catch (SQLSyntaxErrorException e) {
					e.printStackTrace();
				}
				finally{
					ai.getAndIncrement();
				}
			}
		}
	}
}