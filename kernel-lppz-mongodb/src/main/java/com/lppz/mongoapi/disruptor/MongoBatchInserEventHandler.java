package com.lppz.mongoapi.disruptor;

import java.util.Map;

import org.bson.Document;

import com.lppz.util.disruptor.BaseEvent;
import com.lppz.util.disruptor.BaseEventHandler;

public class MongoBatchInserEventHandler extends BaseEventHandler<Map<String, Document>,BaseEvent<Map<String, Document>>> {

	protected int size;
	public MongoBatchInserEventHandler(){}
	public MongoBatchInserEventHandler(int size,MongoBathInsertHandler handler){
		this.size=size;
		super.handler=handler;
	}
	
	@Override
	public void onEvent(BaseEvent<Map<String, Document>> longEvent, long l, boolean b)
			throws Exception {
		Map<String, Document> value = longEvent.getValue();
		if(value==null){
			super.onEvent(longEvent, l, b);
			return;
		}
		this.baseList.add(value);
		if (!baseList.isEmpty()
				&& baseList.size() >= size) {
			this.handler.handle(baseList);
		}
	}
}
