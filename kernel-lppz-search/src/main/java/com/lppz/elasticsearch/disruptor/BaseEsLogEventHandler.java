package com.lppz.elasticsearch.disruptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;

public class BaseEsLogEventHandler extends BaseEventHandler<EsModel,BaseEvent<String>>  {
	private String typeName;
	private String idxName;
	private String idxCurrday;
	protected int size;
	public BaseEsLogEventHandler(){}
	public BaseEsLogEventHandler(String idxName,String typeName,int size,String idxCurrday){
		this.idxName=idxName;
		this.typeName=typeName;
		this.size=size;
		this.idxCurrday=idxCurrday;
		super.handler=new BaseEsHandler();
	}
	
	@Override
	public void onEvent(BaseEvent<String> longEvent, long l, boolean b)
			throws Exception {
		String value = longEvent.getValue();
		
		if(StringUtils.isBlank(value)){
			super.onEvent(longEvent, l, b);
			return;
		}
		String _id=longEvent.getParam()==null?null:longEvent.getParam();
		
		if(StringUtils.isEmpty(_id)){
			_id=Long.toString(
					Math.abs(UUID.randomUUID().getMostSignificantBits()),
					36);
		}
		
		String currDay = StringUtils.isBlank(idxCurrday)?
				new SimpleDateFormat("yyyy").format(new Date()):idxCurrday;
		this.baseList.add(new EsModel(idxName + currDay,
				typeName, _id,value, EsDMlEnum.Insert));
		if (!baseList.isEmpty()
				&& baseList.size() >= size) {
			this.handler.handle(baseList);
		}
	}

}