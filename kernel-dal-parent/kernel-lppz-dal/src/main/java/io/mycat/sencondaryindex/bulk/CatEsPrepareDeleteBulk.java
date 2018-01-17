package io.mycat.sencondaryindex.bulk;

import io.mycat.config.model.TableConfig;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;

import java.sql.SQLSyntaxErrorException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.result.SearchResult;

public class CatEsPrepareDeleteBulk extends PrepareBulk{
	private static final Logger logger = LoggerFactory.getLogger(CatEsPrepareDeleteBulk.class);

	private TableConfig tableConfig;
	private String shardingValue;
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
	private String transNo;
	private String schemaName;
	public CatEsPrepareDeleteBulk(String schemaName,String transNo){
		this.schemaName=schemaName;
		this.transNo=transNo;
	}
	@Override
	public void bulk(List<SearchResult> listRes) {
		if(CollectionUtils.isNotEmpty(listRes)){
			Set<String> primaryValue=new LinkedHashSet<String>();
			for(SearchResult sr:listRes){
//				CatSecondaryIndexModel model = (CatSecondaryIndexModel)sr.getSource();
				String[] tmpStr=sr.getId().split("-");
				primaryValue.add(tmpStr[tmpStr.length-1]);
			}
			try {
				handleDelete2ndIdx(schemaName,tableConfig, primaryValue, shardingValue,transNo);
			} catch (SQLSyntaxErrorException e) {
				logger.error(e.getMessage(),e);
			}
		}		
	}
	
	public static void handleDelete2ndIdx(String schemaName,TableConfig tableConfig,
			Set<String> primaryValue, String shardingValue,String transNo) throws SQLSyntaxErrorException {
		if(CollectionUtils.isEmpty(tableConfig.getSecondaryIndexKeySet()))
			return;
		for(String col:tableConfig.getSecondaryIndexKeySet()){
			for(String pkValue:primaryValue){
				CatSecondaryIndexModel t=new CatSecondaryIndexModel(schemaName);
				t.setColumnName(col.toLowerCase());
				t.setOp(Operation.Delete);
				t.setPrimaryValue(pkValue);
				t.setShardingValue(shardingValue);
				t.setTbName(tableConfig.getName().toLowerCase());
				t.setTransNo(transNo);
				CatSecondaryEsIndexHandler.getInstance().handleDml(t);
			}
		}
	}
}