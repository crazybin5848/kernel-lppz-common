package io.mycat.sencondaryindex.kafka.producer;

import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSON;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.util.disruptor.BaseErrorHandler;

@SuppressWarnings("rawtypes")
public class KafkaErrorEsHandler implements BaseErrorHandler<EsModel>{
	private DalKafkaProducer prod;
	private int reTryCount;
	private boolean isLogInfo;
	private static final Logger logger = LoggerFactory.getLogger(KafkaErrorEsHandler.class);

	public KafkaErrorEsHandler(){
		Resource res=new ClassPathResource("kafka.properties");
		String kafkaBrokerPath;
		try {
			kafkaBrokerPath = res.getURL().getPath();
			prod=DalKafkaProducerConfiguration.createProducer(kafkaBrokerPath);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void handler(EsModel u) {
		BaseEsParamTypeEvent param=new BaseEsParamTypeEvent();
		param.setEsOperType(build(u.getEnumType()));
		param.setId(u.getId());
		String idx=u.getIndex().substring(0,u.getIndex().lastIndexOf("-"))+"-";
		String idxCurrday=u.getIndex().substring(u.getIndex().lastIndexOf("-")+1);
		param.setIdxCurrday(idxCurrday);
		param.setIdxName(idx);
		param.setTypeName(u.getType());
		String jparam=JSON.toJSONString(param);
		logger.info(u.toString()+" has reSend to Kafka");
		sendMsgOrderly((String)u.getJsonSource(),u.getId(), jparam);
	}
	
	private void sendMsgOrderly(String dto,Object key,String... params){
		Dal2ndIdxKafkaModel model=new Dal2ndIdxKafkaModel(dto,params[0],(String)key);
		try {
			prod.sendMsg(model,(String)key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	private String build(EsDMlEnum enumType) {
		return enumType.name();
	}
	@Override
	public int getRetryCount() {
		return reTryCount;
	}

	@Override
	public boolean isLogInfo() {
		return isLogInfo;
	}

	public DalKafkaProducer getProd() {
		return prod;
	}

	public void setProd(DalKafkaProducer prod) {
		this.prod = prod;
	}

	public int getReTryCount() {
		return reTryCount;
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

	public void setLogInfo(boolean isLogInfo) {
		this.isLogInfo = isLogInfo;
	}
}
