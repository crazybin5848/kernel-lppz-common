package io.mycat.sencondaryindex.es;

import io.mycat.config.loader.zkprocess.comm.ZkConfig;
import io.mycat.config.loader.zkprocess.comm.ZkParamCfg;
import io.mycat.config.loader.zkprocess.zktoxml.ZktoXmlMain;
import io.mycat.sencondaryindex.kafka.DalKafkaEsProducerProxy;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;
import io.mycat.sencondaryindex.model.CatTransDetailModel;
import io.mycat.sencondaryindex.model.CatTransModel;
import io.mycat.sencondaryindex.model.CatTransRollBackModel;
import io.mycat.sencondaryindex.util.MysqlDmlUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.disruptor.BaseEsLogEventCRUD2Sender;
import com.lppz.util.disruptor.BaseErrorHandler;
import com.lppz.util.disruptor.sender.BaseClusterdDisruptorSender;
import com.lppz.util.disruptor.sender.BaseEvent2Sender;
import com.lppz.util.disruptor.sender.BaseEvent2SenderFactory;
import com.lppz.util.disruptor.sender.IDal2ndSender;
@SuppressWarnings("rawtypes")
public class CatSecondaryEsIndexHandler  {
	private static CatSecondaryEsIndexHandler instance=new CatSecondaryEsIndexHandler();
	
	private CatSecondaryEsIndexHandler(){
		String mark=ZkConfig.getInstance().getValue(ZkParamCfg.ZK_CFG_2nd_USEKAFKA);
		if(StringUtils.isBlank(mark)){
			logSender=BaseClusterdDisruptorSender.build(new BaseEvent2SenderFactory<String>(){
				@Override
				public BaseEvent2Sender<String> build() {
					BaseErrorHandler<EsModel> errorHandler=new BaseErrorHandler<EsModel>(){
						@Override
						public void handler(EsModel u) {
						}

						@Override
						public int getRetryCount() {
							return 0;
						}

						@Override
						public boolean isLogInfo() {
							return true;
						}
					};
					return BaseEsLogEventCRUD2Sender.create(10000, 1000,errorHandler);
				}
			},3);
		}
		else{
			logSender=DalKafkaEsProducerProxy.getInstance();
		}
	}
	public static CatSecondaryEsIndexHandler getInstance(){
		return instance;
	}
	private static final Logger logger = LoggerFactory.getLogger(CatSecondaryEsIndexHandler.class);

	public static final String IDXTABLENAMEESPREFIX = "idx-mycat-secondaryidx-";
	public IDal2ndSender getLogSender() {
		return logSender;
	}
	
	IDal2ndSender logSender;
	
	private final Map<String,CatTransModel> map=new ConcurrentHashMap<String,CatTransModel>();
	
	public void rollBackAll(){
		for(CatTransModel model:map.values()){
			doRollback2ndIdx(false, model);
		}
	}
	
	public void handlePostDml(String transNo,Boolean isCommit) {
		if(transNo==null)
			return;
		CatTransModel model=map.remove(transNo);
		if(model==null||CollectionUtils.isEmpty(model.getDetailModelList()))
			return;
		if(isCommit!=null&&isCommit){
			doCommit2ndIdx(model);
		}
		else {
			doRollback2ndIdx(isCommit, model);
		}
	}
	
	public void doRollback2ndIdx(Boolean isCommit, CatTransModel model) {
		Map<String,CatTransRollBackModel> insertdetailMap=new HashMap<String,CatTransRollBackModel>();
		for(CatTransDetailModel dm:model.getDetailModelList()){
			//pre rollback Insert
			if(Operation.Insert.name().equals(dm.getDmlType())&&isCommit!=null){
				String[] tmpSchema=dm.getIndexName().split("-");
				String schemaName=tmpSchema[3].toUpperCase();
				String tbName=tmpSchema[4].toUpperCase();
				String[] tmpid=dm.getIdxid().split("-");
				CatTransRollBackModel rbk=insertdetailMap.get(schemaName+"-"+tbName);
				if(rbk==null)
					rbk=new CatTransRollBackModel();
				rbk.getShardingValueSet().add(tmpid[0]);
				rbk.getPkSet().add(tmpid[2]);
				rbk.getDetailModelList().add(dm);
				insertdetailMap.put(schemaName+"-"+tbName, rbk);
			}
			//rollback Delete and Update and duplicaion sql insert exception 
			else {
				BaseEsParamTypeEvent type = buildBaseType(model, dm);
				handleRollback(type,null);
			}
		}
		//do rollback insert
		if(!insertdetailMap.isEmpty()){
			for(String key:insertdetailMap.keySet()){
				String[] tmpSchema=key.split("-");
				String schemaName=tmpSchema[0].toUpperCase();
				String tbName=tmpSchema[1].toUpperCase();
				CatTransRollBackModel crbk=insertdetailMap.get(key);
				Map<String,Map<String,String>> mapRealPk=MysqlDmlUtil.fetchInsertId4Rollback(schemaName, tbName, crbk.getShardingValueSet(), crbk.getPkSet());
				for(CatTransDetailModel dm:crbk.getDetailModelList()){
					String[] tmpid=dm.getIdxid().split("-");
					//db real exist such pk,so update its status to Select
					if(mapRealPk.containsKey(tmpid[2])){
						BaseEsParamTypeEvent type = buildBaseType(model, dm);
						Map<String,String> map=mapRealPk.get(tmpid[2]);
						handleRollback(type,map.get(tmpid[1]));
					}
					//db 不存在这个记录直接干掉索引
					else{
						BaseEsParamTypeEvent type = buildBaseType(model, dm);
						type.setEsOperType(Operation.Delete.name());
						String params=JSON.toJSONString(type);
						CatSecondaryIndexModel t=new CatSecondaryIndexModel();
						t.setDmlType(Operation.Delete.name());
						String dto=JSON.toJSONString(t);
						logSender.sendMsgOrderly(dto,type.getId(),params);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doCommit2ndIdx(CatTransModel model) {
		for(CatTransDetailModel dm:model.getDetailModelList()){
			BaseEsParamTypeEvent type = buildBaseType(model, dm);
			//commit Delete
			if(Operation.Delete.name().equals(dm.getDmlType())){
				type.setEsOperType(Operation.Delete.name());
				String params=JSON.toJSONString(type);
				CatSecondaryIndexModel t=new CatSecondaryIndexModel();
				t.setDmlType(Operation.Delete.name());
				String dto=JSON.toJSONString(t);
				logSender.sendMsgOrderly(dto,type.getId(),params);
				continue;
			}
			//commit Insert and Update
			type.setEsOperType(Operation.Update.name());
			String params=JSON.toJSONString(type);
			CatSecondaryIndexModel t=new CatSecondaryIndexModel();
			t.setDmlType(Operation.Select.name());
			t.setIdxValue(dm.getTmpValue());
			String dto=JSON.toJSONString(t);
			logSender.sendMsgOrderly(dto,type.getId(),params);
		}
	}
	
	private BaseEsParamTypeEvent buildBaseType(CatTransModel model,
			CatTransDetailModel dm) {
		BaseEsParamTypeEvent type=new BaseEsParamTypeEvent();
		type.setIdxName(dm.getIndexName());
		type.setTypeName(model.getType());
		type.setId(dm.getIdxid());
		type.setIdxCurrday(model.getIdxCurrsuffix());
		return type;
	}
	
	@SuppressWarnings("unchecked")
	private void handleRollback(BaseEsParamTypeEvent type,String idxValue) {
		type.setEsOperType(Operation.Update.name());
		String params=JSON.toJSONString(type);
		CatSecondaryIndexModel t=new CatSecondaryIndexModel();
		t.setDmlType(Operation.Select.name());
		t.setIdxValue(idxValue);
		String dto=JSON.toJSONString(t);
		logSender.sendMsgOrderly(dto,type.getId(),params);
	}
	
	@SuppressWarnings("unchecked")
	public void handleNoTransDml(CatSecondaryIndexModel t) {
		String idxName=buildEsIdx(t.getSchemaName(),t.getTbName(),t.getColumnName(), null);
		BaseEsParamTypeEvent type=new BaseEsParamTypeEvent();
		type.setEsOperType(t.getOp().name());
		type.setIdxName(idxName);
		type.setTypeName(CatSecondaryIndexModel.class.getName());
		type.setId(t.buildEsId());
		setTypeCurrIdx(type);
		String params=JSON.toJSONString(type);
		t.setOp(null);
		t.setTbName(null);
		t.setColumnName(null);
		t.setPrimaryValue(null);
		t.setCatId(ZktoXmlMain.getMyid());
		t.setSchemaName(null);
		t.setDmlType(Operation.Select.name());
		String dto=JSON.toJSONString(t);
		logSender.sendMsg(dto, params);
	}
	
	@SuppressWarnings("unchecked")
	public void handleDml(CatSecondaryIndexModel t) {
		String idxName=buildEsIdx(t.getSchemaName(),t.getTbName(),t.getColumnName(), null);
		BaseEsParamTypeEvent type=new BaseEsParamTypeEvent();
		type.setIdxName(idxName);
		type.setTypeName(CatSecondaryIndexModel.class.getName());
		type.setId(t.buildEsId());
		setTypeCurrIdx(type);
		buildTransMap(t, type);
		type.setEsOperType(t.getOp().name());
		//delete对es来说是改标记，是update
		if(Operation.Delete.name().equals(t.getOp().name()))
		type.setEsOperType(Operation.Update.name());
		String params=JSON.toJSONString(type);
		t.setOp(null);
		t.setTbName(null);
		t.setColumnName(null);
		t.setPrimaryValue(null);
		t.setSchemaName(null);
		t.setTransNo(null);
		t.setIdxValue(null);
		t.setCatId(ZktoXmlMain.getMyid());
		String dto=JSON.toJSONString(t);
		logSender.sendMsgOrderly(dto,type.getId(),params);
	}
	
	private void buildTransMap(CatSecondaryIndexModel t,
			BaseEsParamTypeEvent type) {
		CatTransModel catmodel=map.get(t.getTransNo());
		if(catmodel==null)
			catmodel=new CatTransModel();
		catmodel.setIdxCurrsuffix(type.getIdxCurrday());
		catmodel.setType(type.getTypeName());
		CatTransDetailModel dm=new CatTransDetailModel();
		dm.setDmlType(t.getDmlType());
		dm.setIdxid(type.getId());
		dm.setTmpValue(t.getIdxValue());
		dm.setIndexName(type.getIdxName());
		catmodel.getDetailModelList().add(dm);
		map.put(t.getTransNo(), catmodel);
	}
	
	public void setTypeCurrIdx(BaseEsParamTypeEvent type) {
			type.setIdxCurrday("2017");
	}
	
	@SuppressWarnings("unchecked")
	public void handleBatchScrollDml(CatSecondaryIndexModel t,String id,String idxCurrday) {
		BaseEsParamTypeEvent type=new BaseEsParamTypeEvent();
		type.setEsOperType(t.getOp().name());
		type.setIdxName(buildEsIdx(t.getSchemaName(),t.getTbName(), t.getColumnName(), null));
		type.setTypeName(CatSecondaryIndexModel.class.getName());
		type.setId(id);
		type.setIdxCurrday(idxCurrday);
		buildTransMap(t, type);
		type.setEsOperType(t.getOp().name());
		if(Operation.Delete.name().equals(t.getOp().name()))
		type.setEsOperType(Operation.Update.name());
		String params=JSON.toJSONString(type);
		t.setOp(null);
		t.setTbName(null);
		t.setColumnName(null);
		t.setPrimaryValue(null);
		t.setSchemaName(null);
		t.setTransNo(null);
		t.setIdxValue(null);
		t.setCatId(ZktoXmlMain.getMyid());
		String dto=JSON.toJSONString(t);
		logSender.sendMsgOrderly(dto,id,params);
	}
	
	public void handleDDl(CatSecondaryIndexModel t) {
		if(Operation.ADDIDX.equals(t.getOp())){
			String crIdx=buildEsIdx(t.getSchemaName(),t.getTbName(),t.getColumnName(), "2017");
			LppzEsComponent.getInstance().createIndex(crIdx);
			logger.info("create idx "+crIdx+" success!");
		}
		else if(Operation.DROPIDX.equals(t.getOp())){
			String idxName=buildEsIdx(t.getSchemaName(),t.getTbName(),t.getColumnName(), "*");
			if(LppzEsComponent.getInstance().isIndexExists(idxName)){
				LppzEsComponent.getInstance().deleteIndex(idxName);
				logger.info("delete idx "+idxName+" success!");
			}
		}
	}
	
	
	public String buildEsIdx(String schema,String tbName,String column,String tenant){
		StringBuilder sb=new StringBuilder(IDXTABLENAMEESPREFIX);
		sb.append(schema.toLowerCase()).append("-").
		append(tbName.toLowerCase()).append("-").append(column.toLowerCase()).append("-");
		if(StringUtils.isNotBlank(tenant))
			sb.append(tenant);
//			tenant=new SimpleDateFormat("yyyy").format(new Date());
		return sb.toString();
	}
}