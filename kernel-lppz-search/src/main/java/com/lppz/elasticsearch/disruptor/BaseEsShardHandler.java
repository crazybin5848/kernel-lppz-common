package com.lppz.elasticsearch.disruptor;

import java.util.List;

import org.elasticsearch.action.bulk.BulkResponse;

import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.LppzEsComponent;

public class BaseEsShardHandler extends AbstractBaseEsHander {
	@Override
	protected BulkResponse doHandleBatch(List<EsModel> list) {
		return LppzEsComponent.getInstance().batchInsertShard(list);
	}
}