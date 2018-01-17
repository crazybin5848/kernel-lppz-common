package io.mycat.sencondaryindex.kafka;

import io.mycat.sencondaryindex.es.CatSecondaryEsIndexHandler;
import io.mycat.sencondaryindex.kafka.producer.DalKafkaProducer;
import io.mycat.sencondaryindex.kafka.producer.DalKafkaProducerConfiguration;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel;
import io.mycat.sencondaryindex.model.CatSecondaryIndexModel.Operation;
import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.util.disruptor.sender.IDal2ndSender;


public class DalKafkaEsProducerProxy implements IDal2ndSender<String>{
	private static final Logger logger = LoggerFactory.getLogger(DalKafkaEsProducerProxy.class);

	private DalKafkaEsProducerProxy(){}
	private static DalKafkaEsProducerProxy instance=new DalKafkaEsProducerProxy();
	public static DalKafkaEsProducerProxy getInstance(){
		return instance;
	}
	private DalKafkaProducer kafkaProducer;
	public DalKafkaProducer getKafkaProducer() {
		return kafkaProducer;
	}
	public void build(String kafkaBrokerPath) throws IOException{
		try {
			this.kafkaProducer=DalKafkaProducerConfiguration.createProducer(kafkaBrokerPath);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public void sendMsg(CatSecondaryIndexModel t){
		String idxName=CatSecondaryEsIndexHandler.getInstance().buildEsIdx(t.getSchemaName(),t.getTbName(),t.getColumnName(), null);
		BaseEsParamTypeEvent type=new BaseEsParamTypeEvent();
		type.setEsOperType(t.getOp().name());
		type.setIdxName(idxName);
		type.setTypeName(CatSecondaryIndexModel.class.getName());
		type.setId(t.buildEsId());
		CatSecondaryEsIndexHandler.getInstance().setTypeCurrIdx(type);
		String params=JSON.toJSONString(type);
		t.setOp(null);
		t.setTbName(null);
		t.setColumnName(null);
		t.setPrimaryValue(null);
//		t.setCatId(ZktoXmlMain.getMyid());
		t.setSchemaName(null);
		t.setDmlType(Operation.Select.name());
		String dto=JSON.toJSONString(t);
		Dal2ndIdxKafkaModel model=new Dal2ndIdxKafkaModel(dto,params,null);
		try {
			kafkaProducer.sendMsg(model);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void sendMsgOrderly(String dto,Object key,String... params){
		Dal2ndIdxKafkaModel model=new Dal2ndIdxKafkaModel(dto,params[0],(String)key);
		try {
			kafkaProducer.sendMsg(model,(String)key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void sendMsg(String dto,String... params){
		Dal2ndIdxKafkaModel model=new Dal2ndIdxKafkaModel(dto,params[0],null);
		try {
			kafkaProducer.sendMsg(model);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public void destory() {
		try {
			kafkaProducer.destroy(300);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
}
