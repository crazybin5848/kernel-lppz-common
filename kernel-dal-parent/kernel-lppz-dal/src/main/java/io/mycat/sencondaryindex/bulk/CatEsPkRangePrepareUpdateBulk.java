package io.mycat.sencondaryindex.bulk;

import io.mycat.config.model.TableConfig;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;
import io.mycat.util.StringUtil;

import java.sql.SQLSyntaxErrorException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.result.SearchResult;

public class CatEsPkRangePrepareUpdateBulk extends PrepareBulk{
	private static final Logger logger = LoggerFactory.getLogger(CatEsPkRangePrepareUpdateBulk.class);

	private MySqlUpdateStatement updateStmt;
	private TableConfig tableConfig;
	private String shardingValue;

	public MySqlUpdateStatement getUpdateStmt() {
		return updateStmt;
	}

	public void setUpdateStmt(MySqlUpdateStatement updateStmt) {
		this.updateStmt = updateStmt;
	}

	public TableConfig getTableConfig() {
		return tableConfig;
	}

	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}

	public String getShardingValue() {
		return shardingValue;
	}

	public void setShardingValue(String shardingValue) {
		this.shardingValue = shardingValue;
	}
	private String schemaName;

	private String transNo;
	public CatEsPkRangePrepareUpdateBulk(String schemaName,String transNo){
		this.schemaName=schemaName;
		this.transNo=transNo;
	}
	public static void handleUpdate2ndIdx(String schemaName,MySqlUpdateStatement updateStmt, TableConfig tableConfig,
			Set<String> primaryValue, String shardingValue, String transNo) throws SQLSyntaxErrorException {
		String tbName = updateStmt.getTableName().getSimpleName();
		Set<String> set = tableConfig.getSecondaryIndexKeySet();
		for (String pkValue : primaryValue) {
			for(SQLUpdateSetItem item:updateStmt.getItems()){
				String column=item.getColumn().toString().toLowerCase();
				if(!set.contains(column))
						continue;
				String idxValue=StringUtil.trimSingleQuote(item.getValue().toString());
				pkValue=StringUtil.trimSingleQuote(pkValue);
				CatSecondaryIndexModel model = new CatSecondaryIndexModel(schemaName);
				model.setColumnName(column);
				model.setIdxValue(idxValue);
				model.setOp(Operation.Update);
				model.setTbName(tbName);
				model.setPrimaryValue(pkValue);
				model.setShardingValue(shardingValue);
				model.setTransNo(transNo);
				CatSecondaryEsIndexHandler.getInstance().handleDml(model);
			}
		}
	}
	
	@Override
	public void bulk(List<SearchResult> listRes) {
		if(CollectionUtils.isNotEmpty(listRes)){
			Set<String> primaryValue=new LinkedHashSet<String>();
			for(SearchResult sr:listRes){
				CatSecondaryIndexModel model = (CatSecondaryIndexModel)sr.getSource();
				primaryValue.add(model.getIdxValue());
			}
			try {
				handleUpdate2ndIdx(schemaName,updateStmt, tableConfig, primaryValue, shardingValue,transNo);
			} catch (SQLSyntaxErrorException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
}