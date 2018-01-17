package com.lppz.elasticsearch.disruptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.util.disruptor.BaseEvent;

public class BaseCRUDEsLogEventHandler extends BaseEsLogEventHandler  {
	private Logger logger = Logger.getLogger(BaseCRUDEsLogEventHandler.class);

	public BaseCRUDEsLogEventHandler(){super();}
	
	@Override
	public void onEvent(BaseEvent<String> longEvent, long l, boolean b)
			throws Exception {
		String value = longEvent.getValue();
		String param = longEvent.getParam();
		try {
			BaseEsParamTypeEvent bep=JSON.parseObject(param, BaseEsParamTypeEvent.class);
			if(StringUtils.isBlank(value)){
				super.onEvent(longEvent, l, b);
				return;
			}
			String _id=bep.getId();
			if(StringUtils.isEmpty(_id)){
				_id=Long.toString(
						Math.abs(UUID.randomUUID().getMostSignificantBits()),
						36);
			}
			
			String currDay = StringUtils.isBlank(bep.getIdxCurrday())?
					new SimpleDateFormat("yyyy").format(new Date()):bep.getIdxCurrday();
			String esType=bep.getEsOperType();
			this.baseList.add(new EsModel(bep.getIdxName() + currDay,
					bep.getTypeName(), _id,value, buildEsType(esType)));
			if (!baseList.isEmpty()
					&& baseList.size() >= size) {
				this.handler.handle(baseList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return;
		}
	}
	
	private EsDMlEnum buildEsType(String esType) {
		return EsDMlEnum.valueOf(esType);
	}

}