package com.lppz.jstorm.es;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.support.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.utils.Utils;

import com.alibaba.jstorm.batch.BatchId;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.jstorm.JstormLppzUtil;
import com.lppz.util.EsJsonSourceModel;
import com.lppz.util.kryo.KryoUtil;

public class EsAckHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7131514510138868410L;
	private static final Logger LOG = LoggerFactory.getLogger(EsAckHandler.class);

	Map<BatchId,List<EsAckObj>> map=new HashMap<BatchId,List<EsAckObj>>();
	public void ack(EsAckObj eao, SpoutOutputCollector collector,
			AbstractClient esClient) {
		List<EsAckObj> ll=map.get(eao.getBatchId());
		if(eao.getId().equals(JstormLppzUtil.COMMIT)){
			handle(ll,eao.getBatchId());
			return;
		}
		if(ll==null)
			ll=new ArrayList<EsAckObj>();
		ll.add(eao);
		map.put(eao.getBatchId(), ll);
	}

	private void handle(List<EsAckObj> ll, BatchId batchId) {
		List<EsModel> esModelList=new ArrayList<EsModel>();
		for(EsAckObj ea:ll){
			EsJsonSourceModel ejsm=new EsJsonSourceModel();
			ejsm.setStatus(1);
			EsModel em=new EsModel(ea.getIndex(),ea.getType(),ea.getId(),ejsm,EsDMlEnum.Update);
			esModelList.add(em);
		}
		BulkResponse blr=LppzEsComponent.getInstance().batchUpdateDelete(esModelList);
		if(!blr.hasFailures()){
			map.remove(batchId);
			LOG.info("succ update es");
		}
	}

	public void fail(EsAckObj sr, SpoutOutputCollector collector,
			AbstractClient esClient) {
		LOG.error(sr.toString()+" has failded,need retry send ...");
		SearchResult sar=LppzEsComponent.getInstance().searchById(sr.getIndex(), sr.getType(), sr.getId());
		byte[] b = null;
		try {
			b = KryoUtil.kyroSeriLize(sar, -1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BatchId batchId=sr.getBatchId();
		collector.emit(Utils.tuple(batchId,b),sr);
	}
}
