package io.mycat.sencondaryindex.bulk;

import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;
import io.mycat.util.StringUtil;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.result.SearchResult;

public class CatEsPrepareUpdateBulk extends PrepareBulk{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3433654270294182274L;
	private List<SQLUpdateSetItem> itemList;
	private String tableName;
	private String schemaName;
	private String transNo;
	public CatEsPrepareUpdateBulk(String schemaName,String transNo){
		this.schemaName=schemaName;
		this.transNo=transNo;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<SQLUpdateSetItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SQLUpdateSetItem> itemList) {
		this.itemList = itemList;
	}
	@Override
	public void bulk(List<SearchResult> listRes) {
		if(CollectionUtils.isEmpty(itemList))
			return;
		if(CollectionUtils.isNotEmpty(listRes)){
			for(SearchResult sr:listRes){
				CatSecondaryIndexModel model=(CatSecondaryIndexModel)sr.getSource();
				String id=sr.getId();
				String[] tmp=sr.getIndex().split("-");
				String idxCurrSuffix=tmp[tmp.length-1];
				for(SQLUpdateSetItem item:itemList){
					StringBuilder sb=new StringBuilder("");
					String[] idArray=id.split("-");
					for(int i=0;i<idArray.length;i++){
						if(i==1)
						  sb.append(item.getColumn().toString()).append("-");
						else
						  sb.append(idArray[i]).append("-");
					}
					String column=item.getColumn().toString().toLowerCase();
					String idxValue=StringUtil.trimSingleQuote(item.getValue().toString());
					model.setColumnName(column);
					model.setIdxValue(idxValue);
					model.setOp(Operation.Update);
					model.setTbName(tableName);
					model.setSchemaName(schemaName);
					model.setTransNo(transNo);
					CatSecondaryEsIndexHandler.getInstance().handleBatchScrollDml(model,sb.substring(0,sb.length()-1),idxCurrSuffix);
				}
			}
		}
	}
}