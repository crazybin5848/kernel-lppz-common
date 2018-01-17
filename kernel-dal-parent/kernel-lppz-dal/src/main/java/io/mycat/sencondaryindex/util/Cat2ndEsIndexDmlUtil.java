package io.mycat.sencondaryindex.util;

import io.mycat.config.model.TableConfig;
import io.mycat.route.util.RouterUtil;
import io.mycat.sencondaryindex.bulk.CatEsPkRangePrepareUpdateBulk;
import io.mycat.sencondaryindex.bulk.CatEsPrepareDeleteBulk;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;
import io.mycat.sqlengine.mpp.ColumnRoutePair;
import io.mycat.util.StringUtil;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

public class Cat2ndEsIndexDmlUtil extends RouterUtil{
	private static Cat2ndEsIndexDmlUtil instance=new Cat2ndEsIndexDmlUtil();
	public static Cat2ndEsIndexDmlUtil getInstance(){
		return instance;
	}
	private Cat2ndEsIndexDmlUtil(){}
	public boolean checkIsContain2ndIdx(Set<String> colSet,
			Set<String> secondaryIndexKeySet) {
		for (String s : colSet) {
			if (secondaryIndexKeySet.contains(s.toLowerCase()))
				return true;
		}
		return false;
	}

	public void handleInsert2ndIdx(String schemaName,String sql, TableConfig tableConfig,
			String primaryKey, String shardingValue,String transNo)
			throws SQLSyntaxErrorException {
		MySqlInsertStatement insertStmt = (MySqlInsertStatement) (new MySqlStatementParser(
				sql)).parseInsert();
		String tbName = tableConfig.getName().toLowerCase();
		Set<String> set = tableConfig.getSecondaryIndexKeySet();
		Set<String> colSets = new TreeSet<String>();
		for (SQLExpr s : insertStmt.getColumns()) {
			colSets.add(s.toString().replaceAll("`", ""));
		}
		if (!colSets.containsAll(set))
			throw new SQLSyntaxErrorException(
					"insert columns must contain all secondaryidx column!");
		for (String col : set) {
			int keyIndex = getJoinKeyIndex(insertStmt.getColumns(),
					col.toUpperCase());
			String idxValue = insertStmt.getValues().getValues().get(keyIndex)
					.toString();
			idxValue = StringUtil.trimSingleQuote(idxValue);
			int pkIndex = getJoinKeyIndex(insertStmt.getColumns(),
					primaryKey.toUpperCase());
			String pkValue = insertStmt.getValues().getValues().get(pkIndex)
					.toString();
			pkValue = StringUtil.trimSingleQuote(pkValue);
			CatSecondaryIndexModel model = new CatSecondaryIndexModel(schemaName);
			model.setColumnName(col);
			model.setIdxValue(idxValue);
			model.setOp(Operation.Insert);
			model.setTbName(tbName);
			model.setShardingValue(shardingValue);
			model.setPrimaryValue(pkValue);
			model.setTransNo(transNo==null?"":transNo);
			model.setDmlType(Operation.Insert.name());
			CatSecondaryEsIndexHandler.getInstance().handleDml(model);
		}
	}
	
	public void handleUpdate2ndIdx(String schemaName,MySqlUpdateStatement updateStmt, TableConfig tableConfig,
			Set<String> primaryValue, String shardingValue, String transNo) throws SQLSyntaxErrorException {
		CatEsPkRangePrepareUpdateBulk.handleUpdate2ndIdx(schemaName,updateStmt, tableConfig, primaryValue, shardingValue,transNo); 
	}
	
	public void handleDelete2ndIdx(String schemaName,TableConfig tableConfig,
			Set<String> primaryValue, String shardingValue, String transNo) throws SQLSyntaxErrorException {
		CatEsPrepareDeleteBulk.handleDelete2ndIdx(schemaName,tableConfig, primaryValue, shardingValue,transNo);
	}
	
	public void handleUpdate2ndRangeIdx(String schemaName,MySqlUpdateStatement updateStmt, TableConfig tableConfig,
			String beginPk,String endPk,String primarkKey,String shardingValue, String transNo) throws SQLSyntaxErrorException {
		String tbName = updateStmt.getTableName().getSimpleName();
		CatSecondaryIndexModel min = new CatSecondaryIndexModel(schemaName);
		min.setColumnName(primarkKey.toLowerCase());
		min.setIdxValue(beginPk);
		min.setTbName(tbName.toLowerCase());
		CatSecondaryIndexModel max = new CatSecondaryIndexModel(schemaName);
		max.setColumnName(primarkKey.toLowerCase());
		max.setIdxValue(endPk);
		max.setTbName(tbName.toLowerCase());
		Cat2ndEsIndexSearchUtil.getInstance().scroll2ndIdxRangeForIdList(min, max, shardingValue, tableConfig, updateStmt,transNo);
	}
	
	@SuppressWarnings("unchecked")
	public void handleUpdate2ndIdxWithoutPk(String schemaName,MySqlUpdateStatement updateStmt, TableConfig tableConfig,
			Map<String, Set<ColumnRoutePair>> columnsMap, String shardingValue, String transNo) throws SQLSyntaxErrorException {
		String tbName = updateStmt.getTableName().getSimpleName();
		Map<String,Object> map=buildDelMap(schemaName,columnsMap, tbName);
		boolean isRange =(boolean)map.get("isRange");
		List<CatSecondaryIndexModel> list = (List<CatSecondaryIndexModel>)map.get("list");
		if (!list.isEmpty()) {
			// select .. where sex between 1 and
			// 9,如果between里面还有and语句就不走rangescan
			List<SQLUpdateSetItem> itemList=buildItemList(updateStmt.getItems(),tableConfig.getSecondaryIndexKeySet());
			if (list.size() == 2 && isRange) {
				Cat2ndEsIndexSearchUtil
						.getInstance().scroll2ndIdxRange2EsUpdate(list.get(0), list.get(1),itemList,shardingValue,transNo);
			} else {
				Cat2ndEsIndexSearchUtil
				.getInstance().scroll2ndIdx2EsUpdate(list, itemList,shardingValue,transNo);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void handleDelete2ndIdxWithoutPk(String schemaName,TableConfig tableConfig,
			Map<String, Set<ColumnRoutePair>> columnsMap, String shardingValue, String transNo) throws SQLSyntaxErrorException {
		String tbName = tableConfig.getName().toLowerCase();
		Map<String,Object> map=buildDelMap(schemaName,columnsMap, tbName);
		boolean isRange =(boolean)map.get("isRange");
		List<CatSecondaryIndexModel> list = (List<CatSecondaryIndexModel>)map.get("list");
		if (!list.isEmpty()) {
			// select .. where sex between 1 and
			// 9,如果between里面还有and语句就不走rangescan
			if (list.size() == 2 && isRange) {
				Cat2ndEsIndexSearchUtil
				.getInstance().scroll2ndIdxRange2EsDelete(list.get(0), list.get(1),tableConfig,shardingValue,transNo);
			} else {
				Cat2ndEsIndexSearchUtil
 				.getInstance().scroll2ndIdx2EsDelete(list, tableConfig,shardingValue,transNo);
			}
		}
	}
	
	private Map<String,Object> buildDelMap(String schemaName,Map<String, Set<ColumnRoutePair>> columnsMap,String tbName){
		List<CatSecondaryIndexModel> list = new ArrayList<CatSecondaryIndexModel>(
				columnsMap.size());
		Map<String,Object> map=new HashMap<String,Object>(2);
		boolean isRange = false;
		String col=columnsMap.keySet().iterator().next();
			Set<ColumnRoutePair> v1 = columnsMap.get(col);
			for (ColumnRoutePair ccp : v1) {
				if (ccp.colValue != null) {
					CatSecondaryIndexModel model = new CatSecondaryIndexModel(schemaName);
					model.setColumnName(col.toLowerCase());
					model.setIdxValue(ccp.colValue);
					model.setTbName(tbName.toLowerCase());
					list.add(model);
				} else if (ccp.rangeValue != null) {
					isRange = true;
					CatSecondaryIndexModel min = new CatSecondaryIndexModel(schemaName);
					min.setColumnName(col.toLowerCase());
					min.setIdxValue(ccp.rangeValue.beginValue
							.toString());
					min.setTbName(tbName.toLowerCase());
					list.add(min);
					CatSecondaryIndexModel max = new CatSecondaryIndexModel(schemaName);
					max.setColumnName(col.toLowerCase());
					max.setIdxValue(ccp.rangeValue.endValue
							.toString());
					max.setTbName(tbName.toLowerCase());
					list.add(max);
				}
			}
			map.put("isRange", isRange);
			map.put("list", list);
			return map;
	}

	private List<SQLUpdateSetItem> buildItemList(
			List<SQLUpdateSetItem> items, Set<String> secondaryIndexKeySet) {
		List<SQLUpdateSetItem> list=new ArrayList<SQLUpdateSetItem>();
		for(SQLUpdateSetItem item:items){
			if(secondaryIndexKeySet.contains(item.getColumn().toString().toLowerCase()))
				list.add(item);
		}
		return list;
	}
	
	public void handleDelete2ndRangeIdx(String schemaName,TableConfig tableConfig, String beginPk,String endPk, String primaryKey, String shardingValue, String transNo) {
		String tbName = tableConfig.getName().toLowerCase();
		CatSecondaryIndexModel min = new CatSecondaryIndexModel(schemaName);
		min.setColumnName(primaryKey.toLowerCase());
		min.setIdxValue(beginPk);
		min.setTbName(tbName.toLowerCase());
		CatSecondaryIndexModel max = new CatSecondaryIndexModel(schemaName);
		max.setColumnName(primaryKey.toLowerCase());
		max.setIdxValue(endPk);
		max.setTbName(tbName.toLowerCase());
		Cat2ndEsIndexSearchUtil.getInstance().scroll2ndIdxRangeForDelete(min, max, shardingValue, tableConfig,transNo);
	}
}