package com.lppz.canal.etl.extract;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.bean.TableBean;
import com.lppz.canal.client.AbstractClient;
import com.lppz.canal.enums.EtlEnums;
import com.lppz.canal.enums.OperEnums;
import com.lppz.canal.exception.YamlLoadException;
import com.lppz.canal.model.ChangeColumn;
import com.lppz.canal.service.QueueSenderService;

/**
 * 从cancal拉取binlog信息，并转换为TableModel对象然后发送到rocketmq
 * 1、初始化cancal客户端并开启客户端连接
 * 2、获取到binlog后解析log并封装成ChangeDataBean对象集合
 * 3、发送到下一节点
 * @author licheng
 *
 */
@Component
public class ExtractTask extends AbstractClient<ChangeDataBean> implements DisposableBean {
	
	private Logger logger = LoggerFactory.getLogger(ExtractTask.class);

	@Value("${cancal.server.address}")
	private String cancalSeverAddress;
	
	@Value("${cancal.server.port}")
	private int cancalServerPort;
	
	@Value("${task.extract.isRun:false}")
	private boolean isRun;
	
	@Resource
	private JedisCluster jedisCluster;
	
	@Resource
	private QueueSenderService queueSenderService;
	
	@PostConstruct
	public void sendToRocketmq(){
		if (!isRun) {
			return;
		}
		try {
			initClient();
			super.start();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				
				public void run() {
					try {
						logger.info("## stop the canal client");
						closeTask();
					} catch (Throwable e) {
						logger.warn("##something goes wrong when stopping canal:\n{}", ExceptionUtils.getFullStackTrace(e));
					} finally {
						logger.info("## canal client is down.");
					}
				}
				
			});
		} catch (YamlLoadException e) {
			logger.error("初始化canal客户端异常", e);
		}catch (Exception e) {
			logger.error("同步canal异常", e);
		}
	}
	
	public void initClient() throws YamlLoadException{
		// 根据ip，直接创建链接，无HA的功能
		String destination = "example";
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(cancalSeverAddress,
				cancalServerPort), destination, "", "");
		setConnector(connector);
	}
	
	private void closeTask(){
		super.stop();
	}

	@Override
	public void destroy() throws Exception {
		super.stop();
	}

	@Override
	public List<ChangeDataBean> analyzeRow(Entry entry) {
		return dealRow(entry);
	}

	@Override
	public boolean validateRow(List<ChangeDataBean> t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendToRocketmq(List<ChangeDataBean> datas) {
		queueSenderService.sendNext(EtlEnums.TRANSFORM, datas);
	}
	
	private List<ChangeDataBean> dealRow(Entry entry) {
		List<ChangeDataBean> rows = null;
    	long executeTime = entry.getHeader().getExecuteTime();
    	if (entry.getEntryType() == EntryType.ROWDATA) {
             RowChange rowChange = null;
             try {
                 rowChange = RowChange.parseFrom(entry.getStoreValue());
             } catch (Exception e) {
                 throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
             }
             
             EventType eventType = rowChange.getEventType();
             
             String schemaName = entry.getHeader().getSchemaName();
             String tableName = entry.getHeader().getTableName();
             rows = parsToTableModel(rowChange, eventType, executeTime, schemaName, tableName);
         }
    	 return rows;
	}
	
	private List<ChangeDataBean> parsToTableModel(RowChange rowChage, EventType eventType, long executeTime, String schema, String table) {
		List<ChangeDataBean> datas = new ArrayList<>();
		List<Map<String,String>> changeRows = getChangeRows(rowChage.getRowDatasList(),eventType);
		if (changeRows != null && !changeRows.isEmpty()) {
			for (Map<String, String> row : changeRows) {
				ChangeDataBean binlogModel = new ChangeDataBean();
				binlogModel.setValues(row);
				binlogModel.setSchema(schema);
				binlogModel.setTable(table);
				binlogModel.setOperEnums(getOptype(eventType));
				datas.add(binlogModel);
			}
		}
		return datas;
	}
	
	private OperEnums getOptype(EventType eventType) {
    	return OperEnums.valueOf(eventType.getNumber());
	}

	/**
     * 转换列数据
     * @param rowDatasList
     * @param eventType
     * @return
     */
    private List<Map<String,String>> getChangeRows(List<RowData> rowDatasList, EventType eventType) {
    	List<Map<String,String>> rows = new ArrayList<Map<String,String>>();
    	for (RowData rowData : rowDatasList) {
            if (eventType == EventType.DELETE) {
            	rows.add(getColumns(rowData.getBeforeColumnsList()));
            } else if (eventType == EventType.INSERT) {
            	rows.add(getColumns(rowData.getAfterColumnsList()));
            } else {
            	rows.add(getColumns(rowData.getAfterColumnsList()));
            }
        }
		return rows;
	}

    /**
     * 转换修改数据
     * @param columns
     * @return
     */
	private Map<String,String> getColumns(List<Column> columns) {
		Map<String,String> changeColumns = new HashMap<>();
		for (Column column : columns) {
			changeColumns.put(column.getName(), column.getValue());
        }
		return changeColumns;
	}
	
	/**
	 * 过滤被修改字段
	 * 由于update操作时，修改前的字段updated属性都为false，根据修改后字段过滤修改前字段
	 * @param columns
	 * @param afterColumns
	 * @return
	 */
	private List<ChangeColumn> getChangeColumns(TableBean table, List<Column> columns, List<ChangeColumn> afterColumns){
		List<ChangeColumn> changeColumns = new ArrayList<ChangeColumn>();
		List<String> afterColumnNames = new ArrayList<String>();
		ChangeColumn changeColumn = null;
		String columnName = null;
		String primaryKey = table.getPrimaryKey();
		String parentKey = table.getForeignKey();
		List<String> keys = new ArrayList<>();
		keys.add(primaryKey);
		if(StringUtils.isNotBlank(parentKey)){
			keys.add(parentKey);
		}
		
		if(afterColumns != null){
			for(ChangeColumn afterColumn :afterColumns){
				afterColumnNames.add(afterColumn.getName());
			}
		}
		
		for (Column column : columns) {
			columnName = column.getName();
			if(CollectionUtils.isEmpty(afterColumns)&&column.getUpdated()
					||afterColumnNames.indexOf(columnName.toLowerCase()) > -1
					||keys.indexOf(columnName.toLowerCase()) > -1){
				changeColumn = new ChangeColumn(column.getName(),column.getValue(),column.getMysqlType(),String.valueOf(column.getUpdated()));
				changeColumns.add(changeColumn);
			}
        }
		return changeColumns;
	}

}
