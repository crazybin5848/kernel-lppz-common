package com.lppz.elasticsearch.disruptor.scroll;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.lppz.configuration.es.BaseEsParamTypeEvent;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;

public class BaseEsScrollLogEventHandler extends BaseEventHandler<EsScrollModel,BaseEvent<String>>  {
	protected int size;
	public BaseEsScrollLogEventHandler(){}
	public BaseEsScrollLogEventHandler(int size){
		this.size=size;
		super.handler=new ScrollEsHander();
	}
	
	@Override
	public void onEvent(BaseEvent<String> longEvent, long l, boolean b)
			throws Exception {
		String value = longEvent.getValue();
		String param = longEvent.getParam();
		BaseEsParamTypeEvent bep=JSON.parseObject(param, BaseEsParamTypeEvent.class);
		if(StringUtils.isBlank(value)){
			super.onEvent(longEvent, l, b);
			return;
		}
		this.baseList.add(new EsScrollModel(bep.getIdxName(),
				bep.getTypeName(),bep.getTermField(),value));
		if (!baseList.isEmpty()
				&& baseList.size() >= size) {
			this.handler.handle(baseList);
		}
	}

}