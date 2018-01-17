package io.mycat.sencondaryindex.util;

import io.mycat.MycatServer;
import io.mycat.backend.datasource.PhysicalDBNode;
import io.mycat.backend.datasource.PhysicalDBPool;
import io.mycat.backend.datasource.PhysicalDatasource;
import io.mycat.backend.jdbc.JDBCDatasource;
import io.mycat.config.MycatConfig;
import io.mycat.config.model.SchemaConfig;
import io.mycat.config.model.TableConfig;
import io.mycat.route.RouteResultset;
import io.mycat.route.function.AbstractPartitionAlgorithm;
import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class MysqlDmlUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MysqlDmlUtil.class);

	public static void handleInsert2ndIdx2Es(String schemaName, String tbName,
			List<String> toBeAdded, PhysicalDatasource ddb) {
		if (ddb instanceof JDBCDatasource) {
			ResultSet rs = null;
			Statement stmt = null;
			StringBuilder selectSql = new StringBuilder("SELECT ");
			try {
				Connection conn = ((JDBCDatasource) ddb).getConnection();
				stmt = conn.createStatement();
				int i = 0;
				for (String column : toBeAdded) {
					selectSql.append(column);
					if (i++ < toBeAdded.size() - 1)
						selectSql.append(",");
				}
				selectSql.append(" FROM ").append(tbName);
				rs = stmt.executeQuery(selectSql.toString());
				while (rs.next()) {
					String pkValue = rs.getString("idxpk");
					String shardingValue = rs.getString("shardingPc");
					for (String col : toBeAdded) {
						if (col.contains(" "))
							continue;
						String value = rs.getString(col);
						CatSecondaryIndexModel model = new CatSecondaryIndexModel(
								schemaName);
						model.setColumnName(col.toLowerCase());
						model.setIdxValue(value==null?"NULL":value);
						model.setOp(Operation.Insert);
						model.setPrimaryValue(pkValue);
						model.setShardingValue(shardingValue);
						model.setTbName(tbName.toLowerCase());
						CatSecondaryEsIndexHandler.getInstance()
								.handleNoTransDml(model);
					}
				}
			} catch (SQLException e) {
				LOGGER.error(e.getMessage() + " select sql:" + selectSql, e);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	public static Map<String,Map<String,String>> fetchInsertId4Rollback(String schemaName,final String tbName,
			Set<String> shardingValueArray, final Set<String> pkArray) {
		MycatConfig config = MycatServer.getInstance().getConfig();
		SchemaConfig sc = config.getSchemas().get(schemaName);
		final TableConfig tc = sc.getTables().get(tbName);
		AbstractPartitionAlgorithm algorithm = null;
		if (tc.getRule() != null)
			algorithm = tc.getRule().getRuleAlgorithm();
		else
			algorithm = tc.getRootParent().getRule().getRuleAlgorithm();
		if (shardingValueArray != null) {
			final Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();
//			final AtomicInteger ai=new AtomicInteger(0);
			Set<Integer> nodeIndexSet=new LinkedHashSet<Integer>();
			for(String shardingValue:shardingValueArray){
				Integer nodeIndex = algorithm.calculate(shardingValue);
				nodeIndexSet.add(nodeIndex);
			}
			for(Integer nodeIndex:nodeIndexSet){
				PhysicalDBNode pb = config.getDataNodes().get(tc.getDataNodes().get(nodeIndex));
				final PhysicalDBPool db = pb.getDbPool();
				for (final PhysicalDatasource ddb : db.getAllDataSources()) {
					if (ddb instanceof JDBCDatasource) {
						ResultSet rs = null;
						Statement stmt = null;
						StringBuilder selectSql = new StringBuilder("SELECT ");
						String sql=null;
						try {
							Connection conn = ((JDBCDatasource) ddb).getConnection();
							stmt = conn.createStatement();
							selectSql.append(tc.getPrimaryKey()).append(" as pk,");
							int i=0;
							for (String column : tc.getSecondaryIndexKeySet()) {
								selectSql.append(column);
								if (i++ < tc.getSecondaryIndexKeySet().size() - 1)
									selectSql.append(",");
							}
							selectSql.append(" FROM ").append(tbName).append(" where ")
							.append(tc.getPrimaryKey()).append(" in(");
							for(String pk:pkArray){
								selectSql.append("'").append(pk).append("'").append(",");
							}
							sql=selectSql.substring(0,selectSql.length()-1)+")";
							rs = stmt.executeQuery(sql);
							while (rs.next()) {
								String pkValue = rs.getString("pk");
								Map<String,String> tmpMap=new HashMap<String,String>();
								for(String col:tc.getSecondaryIndexKeySet()){
									tmpMap.put(col, rs.getString(col));
								}
								map.put(pkValue, tmpMap);
							}
						} catch (SQLException e) {
							LOGGER.error(e.getMessage() + " select sql:" + sql, e);
						} finally {
							try {
								if (stmt != null)
									stmt.close();
								if (rs != null)
									rs.close();
							} catch (SQLException e) {
								LOGGER.error(e.getMessage(), e);
							}
						}
					}
				}
//				new Thread(new Runnable(){
//					@Override
//					public void run() {
//						try {
//							
//						} finally{
//							ai.getAndAdd(1);
//						}
//					}
//				}).start();
			}
			return map;
//			while(true){
//				if(ai.get()==nodeIndexSet.size()){
//					
//				}
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//				}
//			}
		}
		return null;
	}
	
	public static Set<String> fetchIds4MultiKeyUpdateDel(RouteResultset rrs,
			final TableConfig tc,Integer nodeIndex, String where) {
		MycatConfig config = MycatServer.getInstance().getConfig();
		Set<String> primaryValueSet=new LinkedHashSet<String>();
		PhysicalDBNode pb = config.getDataNodes().get(tc.getDataNodes().get(nodeIndex));
		handelPkSet(tc.getName(), where, tc, primaryValueSet, pb);
		return primaryValueSet;
	}
	
	public static Map<String,Set<String>> fetchIds4MultiKeyUpdateDel(RouteResultset rrs,
			final TableConfig tc,String where) {
		MycatConfig config = MycatServer.getInstance().getConfig();
		Map<String,Set<String>> primaryValueMap=new LinkedHashMap<String,Set<String>>();
		for(String ds:tc.getDataNodes()){
			PhysicalDBNode pb=config.getDataNodes().get(ds);
			handelPkSetNoShardingValue(tc.getName(), where, tc, primaryValueMap, pb);
		}
		return primaryValueMap;
	}

	private static void handelPkSet(String tbName,
			String where, final TableConfig tc,
			Set<String> primaryValueSet, PhysicalDBNode pb) {
		final PhysicalDBPool db = pb.getDbPool();
		for (final PhysicalDatasource ddb : db.getAllDataSources()) {
			if (ddb instanceof JDBCDatasource) {
				ResultSet rs = null;
				Statement stmt = null;
				StringBuilder selectSql = new StringBuilder("SELECT ");
				try {
					Connection conn = ((JDBCDatasource) ddb).getConnection();
					stmt = conn.createStatement();
					selectSql.append(tc.getPrimaryKey()).append(" as pk");
					selectSql.append(" FROM ").append(tbName).append(" where ").append(where);
					rs = stmt.executeQuery(selectSql.toString().replaceAll("\n", " "));
					while (rs.next()) {
						String pkValue = rs.getString("pk");
						primaryValueSet.add(pkValue);
					}
				} catch (SQLException e) {
					LOGGER.error(e.getMessage() + " select sql:" + selectSql, e);
				} finally {
					try {
						if (stmt != null)
							stmt.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	private static void handelPkSetNoShardingValue(String tbName,
			String where, final TableConfig tc,
			Map<String,Set<String>> primaryValueMap, PhysicalDBNode pb) {
		String shardingCol=null;
		if(StringUtils.isNotBlank(tc.getPartitionColumn()))
			  shardingCol=tc.getPartitionColumn();
		else if(StringUtils.isNotBlank(tc.getJoinKey()))
			  shardingCol=tc.getJoinKey();
		else if(StringUtils.isNotBlank(tc.getRootParentKey()))
			shardingCol=tc.getRootParentKey();
		final PhysicalDBPool db = pb.getDbPool();
		for (final PhysicalDatasource ddb : db.getAllDataSources()) {
			if (ddb instanceof JDBCDatasource) {
				ResultSet rs = null;
				Statement stmt = null;
				StringBuilder selectSql = new StringBuilder("SELECT ");
				try {
					Connection conn = ((JDBCDatasource) ddb).getConnection();
					stmt = conn.createStatement();
					selectSql.append(tc.getPrimaryKey()).append(" as pk,").append(shardingCol).append(" as shardvalue");
					selectSql.append(" FROM ").append(tbName);
					if(StringUtils.isNotBlank(where)){
						selectSql.append(" where ").append(where);
					}
					rs = stmt.executeQuery(selectSql.toString().replaceAll("\n", " "));
					while (rs.next()) {
						String pkValue = rs.getString("pk");
						String shardvalue = rs.getString("shardvalue");
						Set<String> sets=primaryValueMap.get(shardvalue);
						if(CollectionUtils.isEmpty(sets)){
							sets=new LinkedHashSet<String>();
						}
						sets.add(pkValue);
						primaryValueMap.put(shardvalue, sets);
					}
				} catch (SQLException e) {
					LOGGER.error(e.getMessage() + " select sql:" + selectSql, e);
				} finally {
					try {
						if (stmt != null)
							stmt.close();
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
	}
}