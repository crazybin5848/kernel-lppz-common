package com.lppz.elasticsearch.disruptor;

import java.util.List;

import org.elasticsearch.action.bulk.BulkResponse;

import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.LppzEsComponent;

public class BaseEsHandler extends AbstractBaseEsHander{

	@Override
	protected BulkResponse doHandleBatch(List<EsModel> list) {
		return LppzEsComponent.getInstance().batch(list);
	}
}