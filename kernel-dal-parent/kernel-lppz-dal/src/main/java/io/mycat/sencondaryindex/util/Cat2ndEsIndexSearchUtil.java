package io.mycat.sencondaryindex.util;

import io.mycat.config.model.TableConfig;
import io.mycat.sencondaryindex.bulk.CatEsPkRangePrepareUpdateBulk;
import io.mycat.sencondaryindex.bulk.CatEsPrepareDeleteBulk;
import io.mycat.sencondaryindex.bulk.CatEsPrepareUpdateBulk;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.query.BoolSearchQuery;
import com.lppz.elasticsearch.query.Operator;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.RangeItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.result.SearchAllResult;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.elasticsearch.search.SearchCondition;

public class Cat2ndEsIndexSearchUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(Cat2ndEsIndexSearchUtil.class);
	private static Cat2ndEsIndexSearchUtil instance = new Cat2ndEsIndexSearchUtil();

	private Cat2ndEsIndexSearchUtil() {
	}

	public static Cat2ndEsIndexSearchUtil getInstance() {
		return instance;
	}

	public Set<String> search(List<CatSecondaryIndexModel> list) {
		SearchCondition searchCond = buildSearchCond(list);
		return fetch(searchCond);
	}

	public void scroll2ndIdx2EsUpdate(List<CatSecondaryIndexModel> list,
			List<SQLUpdateSetItem> itemList, String shardingValue,String transNo) {
		SearchCondition searchCond = buildSearchCond(list, shardingValue);
		doBatchScrollHandleUpdate(list.get(0).getSchemaName(),searchCond, list.get(0).getTbName(), itemList,transNo);
	}

	public void scroll2ndIdx2EsDelete(List<CatSecondaryIndexModel> list,
			TableConfig tc, String shardingValue,String transNo) {
		SearchCondition searchCond = buildSearchCond(list, shardingValue);
		doBatchScrollHandleDelete(list.get(0).getSchemaName(),searchCond, tc, shardingValue,transNo);
	}

	public void scroll2ndIdxRange2EsUpdate(CatSecondaryIndexModel min,
			CatSecondaryIndexModel max, List<SQLUpdateSetItem> itemList,
			String shardingValue,String transNo) {
		if (min == null || max == null)
			return;
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(max.getSchemaName(),
				max.getTbName(), max.getColumnName(), "*");
		SearchCondition searchCond = buildRangeSearchCond(idxName, min, max,
				shardingValue);
		doBatchScrollHandleUpdate(max.getSchemaName(),searchCond, max.getTbName(), itemList,transNo);
	}

	public void scroll2ndIdxRangeForIdList(CatSecondaryIndexModel min,
			CatSecondaryIndexModel max, String shardingValue,
			TableConfig tableConfig, MySqlUpdateStatement updateStmt,String transNo) {
		if (min == null || max == null)
			return;
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(max.getSchemaName(),
				max.getTbName(), max.getColumnName(), "*");
		SearchCondition searchCond = buildRangeSearchCond(idxName, min, max,
				shardingValue);
		CatEsPkRangePrepareUpdateBulk bulk = new CatEsPkRangePrepareUpdateBulk(max.getSchemaName(),transNo);
		bulk.setShardingValue(shardingValue);
		bulk.setTableConfig(tableConfig);
		bulk.setUpdateStmt(updateStmt);
		LppzEsComponent.getInstance().scrollSearch(
				new String[] { searchCond.getIdxName() },
				searchCond.getTypes(), searchCond.getSearchQuery(), 100000,
				60000, bulk);
	}

	public void scroll2ndIdxRangeForDelete(CatSecondaryIndexModel min,
			CatSecondaryIndexModel max, String shardingValue,
			TableConfig tableConfig,String transNo) {
		if (min == null || max == null)
			return;
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(max.getSchemaName(),
				max.getTbName(), max.getColumnName(), "*");
		SearchCondition searchCond = buildRangeSearchCond(idxName, min, max,
				shardingValue);
		CatEsPrepareDeleteBulk bulk = new CatEsPrepareDeleteBulk(max.getSchemaName(),transNo);
		bulk.setShardingValue(shardingValue);
		bulk.setTableConfig(tableConfig);
		LppzEsComponent.getInstance().scrollSearch(
				new String[] { searchCond.getIdxName() },
				searchCond.getTypes(), searchCond.getSearchQuery(), 100000,
				60000, bulk);
	}

	private void doBatchScrollHandleUpdate(String schemaName,SearchCondition searchCond,
			String tableName, List<SQLUpdateSetItem> itemList,String transNo) {
		if (CollectionUtils.isEmpty(itemList))
			return;
		CatEsPrepareUpdateBulk prepareBulk = new CatEsPrepareUpdateBulk(schemaName,transNo);
		prepareBulk.setTableName(tableName);
		prepareBulk.setItemList(itemList);
		LppzEsComponent.getInstance().scrollSearch(
				new String[] { searchCond.getIdxName() },
				searchCond.getTypes(), searchCond.getSearchQuery(), 100000,
				60000, prepareBulk);
	}

	public Set<String> searchRange(CatSecondaryIndexModel min,
			CatSecondaryIndexModel max) {
		if (min == null || max == null)
			return null;
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(
				max.getSchemaName(),max.getTbName(), max.getColumnName(), "*");
		SearchCondition searchCond = buildRangeSearchCond(idxName, min, max);
		return buildScrollRouteSearch(searchCond);
	}

	private Set<String> buildScrollRouteSearch(SearchCondition searchCond) {
		final Set<String> listShard = new LinkedHashSet<String>();
		searchCond.setSearchType(SearchType.QUERY_AND_FETCH);
		LppzEsComponent.getInstance().scrollSearch(
				new String[] { searchCond.getIdxName() },
				searchCond.getTypes(), searchCond.getSearchQuery(), 100000,
				60000, new PrepareBulk() {
					@Override
					public void bulk(List<SearchResult> listRes) {
						if (CollectionUtils.isNotEmpty(listRes)) {
							for (SearchResult sr : listRes) {
								CatSecondaryIndexModel type = (CatSecondaryIndexModel) sr
										.getSource();
								listShard.add(type.getShardingValue());
							}
						}
					}
				});
		return listShard;
	}

	private Set<String> fetch(SearchCondition searchCond) {
		searchCond.setSearchType(SearchType.QUERY_AND_FETCH);
		SearchAllResult sar = LppzEsComponent.getInstance().search(searchCond);
		Set<String> listShard = new LinkedHashSet<String>(sar
				.getResultSearchList().size());
		for (SearchResult sr : sar.getResultSearchList()) {
			CatSecondaryIndexModel type = (CatSecondaryIndexModel) sr
					.getSource();
			listShard.add(type.getShardingValue());
		}
		return listShard;
	}

	private SearchCondition buildSearchCond(String idxName,
			List<CatSecondaryIndexModel> list, String... shardingValue) {
		SearchQuery searchQuery = new SearchQuery();
		SearchCondition sc = null;
		if (list.size() == 1) {
			TermKvItem tki = new TermKvItem();
			tki.setTermField("idxValue");
			tki.setTermValue(list.get(0).getIdxValue());
			searchQuery.setFieldItemList(new ArrayList<FieldItem>(1));
			searchQuery.getFieldItemList().add(tki);
			sc = new SearchCondition(searchQuery, idxName, new String[] { list
					.get(0).getClass().getName() });
		} else {
			searchQuery.setSearchQueryList(new ArrayList<SearchQuery>());
			BoolSearchQuery bsq = new BoolSearchQuery(Operator.OR);
			searchQuery.getSearchQueryList().add(bsq);
			for (CatSecondaryIndexModel model : list) {
				TermKvItem tki = new TermKvItem();
				tki.setTermField("idxValue");
				tki.setTermValue(model.getIdxValue());
				bsq.addFileItem(tki);
			}
			sc = new SearchCondition(searchQuery, idxName,
					new String[] { CatSecondaryIndexModel.class.getName() });
		}
		if (shardingValue != null && shardingValue.length > 0) {
			TermKvItem tsi = new TermKvItem();
			tsi.setTermField("shardingValue");
			tsi.setTermValue(shardingValue[0]);
			if (CollectionUtils.isEmpty(searchQuery.getFieldItemList()))
				searchQuery.setFieldItemList(new ArrayList<FieldItem>(1));
			searchQuery.getFieldItemList().add(tsi);
		}
		//only search commited or rollbacked items in es
		if (CollectionUtils.isEmpty(searchQuery.getFieldItemList()))
			searchQuery.setFieldItemList(new ArrayList<FieldItem>(1));
		TermKvItem tti = new TermKvItem();
		tti.setTermField("dmlType");
		tti.setTermValue(Operation.Select.name());
		searchQuery.getFieldItemList().add(tti);
		return sc;
	}

	private SearchCondition buildRangeSearchCond(String idxName,
			CatSecondaryIndexModel min, CatSecondaryIndexModel max,
			String... shardingValue) {
		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setSearchQueryList(new ArrayList<SearchQuery>());
		BoolSearchQuery bsq = new BoolSearchQuery(Operator.AND);
		searchQuery.getSearchQueryList().add(bsq);
		//only search commited or rollbacked items in es
		TermKvItem tti = new TermKvItem();
		tti.setTermField("dmlType");
		tti.setTermValue(Operation.Select.name());
		bsq.addFileItem(tti);
		if (min != null) {
			RangeItem ri = new RangeItem();
			ri.setTermField("idxValue");
			ri.setGeStr(min.getIdxValue());
			bsq.addFileItem(ri);
		}
		if (max != null) {
			RangeItem ri = new RangeItem();
			ri.setTermField("idxValue");
			ri.setLeStr(max.getIdxValue());
			bsq.addFileItem(ri);
		}
		if (shardingValue != null && shardingValue.length > 0) {
			TermKvItem tki = new TermKvItem();
			tki.setTermField("shardingValue");
			tki.setTermValue(shardingValue[0]);
			bsq.addFileItem(tki);
		}
		SearchCondition sc = new SearchCondition(searchQuery, idxName,
				new String[] { CatSecondaryIndexModel.class.getName() });
		return sc;
	}

	private SearchCondition buildSearchCond(List<CatSecondaryIndexModel> list,
			String... shardingValue) {
		if (CollectionUtils.isEmpty(list))
			return null;
		CatSecondaryIndexModel t = list.get(0);
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(t.getSchemaName(),
				t.getTbName(), t.getColumnName(), "*");
		SearchCondition searchCond = buildSearchCond(idxName, list,
				shardingValue);
		return searchCond;
	}

	public void scroll2ndIdxRange2EsDelete(CatSecondaryIndexModel min,
			CatSecondaryIndexModel max, TableConfig tc, String shardingValue,String transNo) {
		if (min == null || max == null)
			return;
		String idxName = CatSecondaryEsIndexHandler.getInstance().buildEsIdx(max.getSchemaName(),
				max.getTbName(), max.getColumnName(), "*");
		SearchCondition searchCond = buildRangeSearchCond(idxName, min, max,
				shardingValue);
		doBatchScrollHandleDelete(max.getSchemaName(),searchCond, tc, shardingValue,transNo);
	}

	private void doBatchScrollHandleDelete(String schemaName,SearchCondition searchCond,
			TableConfig tc, String shardingValue,String transNo) {
		CatEsPrepareDeleteBulk prepareBulk = new CatEsPrepareDeleteBulk(schemaName,transNo);
		prepareBulk.setShardingValue(shardingValue);
		prepareBulk.setTableConfig(tc);
		LppzEsComponent.getInstance().scrollSearch(
				new String[] { searchCond.getIdxName() },
				searchCond.getTypes(), searchCond.getSearchQuery(), 100000,
				60000, prepareBulk);
	}
}