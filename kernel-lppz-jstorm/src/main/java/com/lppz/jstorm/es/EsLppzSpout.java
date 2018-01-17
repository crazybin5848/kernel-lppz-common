package com.lppz.jstorm.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.support.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.alibaba.jstorm.batch.BatchId;
import com.lppz.elasticsearch.EsClientUtil;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.PrepareBulk;
import com.lppz.elasticsearch.query.SearchQuery;
import com.lppz.elasticsearch.query.fielditem.FieldItem;
import com.lppz.elasticsearch.query.fielditem.TermKvItem;
import com.lppz.elasticsearch.result.SearchResult;
import com.lppz.jstorm.JstormLppzUtil;
import com.lppz.util.kryo.KryoUtil;

public class EsLppzSpout implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(EsLppzSpout.class);
	protected SpoutOutputCollector collector;
	protected Map<String, String> conf;
	private transient AbstractClient esClient;
	private String index;
	private int timeMillis = 60000;
	private SearchQuery searchQuery;
	private int scrollSize;
	private String types;
	private transient PrepareBulk prepareBulk;
	private EsAckHandler handler;
	private int thisTask;

	public EsLppzSpout() {
	}

	public EsLppzSpout(SearchQuery searchQuery, String index, String types,
			int scrollSize, int timeMillis, EsAckHandler handler) {
		this.searchQuery = searchQuery;
		this.index = index;
		this.types = types;
		this.scrollSize = scrollSize;
		this.timeMillis = timeMillis;
		if (handler != null)
			this.handler = handler;
		else
			this.handler = new EsAckHandler();
	}

	@Override
	public void open(Map<String, String> conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		this.conf = conf;
		String esYamlConfPath = (String) conf
				.get(JstormLppzUtil.ESCLUSTERPATH);
		try {
			if (StringUtils.isNotBlank(esYamlConfPath)) {
				if (new File(esYamlConfPath).exists()) {
					esClient = EsClientUtil
							.buildPoolClientProxy(new FileInputStream(
									esYamlConfPath));
				}
			} else {
				Resource res = new ClassPathResource(
						"/META-INF/es-cluster.yaml");
				if (res.exists())
					esClient = EsClientUtil.buildPoolClientProxy(res
							.getInputStream());
			}
			LppzEsComponent.getInstance().setClient(esClient);
			prepareBulk = new StormPrepareBulk(collector);
			int taskSize = context.getComponentTasks(
					context.getThisComponentId()).size();
			thisTask = context.getThisTaskIndex();
			if (taskSize > 1) {
				throw new EsSingleSpoutException("EsSpout can only run in Single mode!");
//				if (searchQuery == null) {
//					searchQuery = new SearchQuery();
//					List<SearchQuery> searchQueryList = new ArrayList<SearchQuery>();
//					buildShardSearchQueryList(searchQueryList, taskSize);
//					searchQuery.setSearchQueryList(searchQueryList);
//				} else {
//					if (searchQuery.getSearchQueryList() == null)
//						searchQuery
//								.setSearchQueryList(new ArrayList<SearchQuery>());
//					List<SearchQuery> searchQueryList = searchQuery
//							.getSearchQueryList();
//					buildShardSearchQueryList(searchQueryList, taskSize);
//				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.info("Successfully do prepare");
	}

//	private void buildShardSearchQueryList(List<SearchQuery> searchQueryList,
//			int taskSize) {
//		BoolSearchQuery bsq = new BoolSearchQuery(Operator.OR);
//		for (int i = thisTask; i < 30; i += taskSize) {
//			bsq.addFileItem(new TermKvItem("shardId", i));
//		}
//		searchQueryList.add(bsq);
//	}
	
	public EsLppzSpout buildStatusSearchQueryList() {
		searchQuery = new SearchQuery();
		List<FieldItem> l=new ArrayList<FieldItem>(1);
		l.add(new TermKvItem("status", 0));
		searchQuery.setFieldItemList(l);
		return this;
	}

	@Override
	public void close() {
		esClient.close();
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public void nextTuple() {
		LppzEsComponent.getInstance().scrollSearch(new String[] { index },
				new String[] { types }, searchQuery, scrollSize, timeMillis,
				prepareBulk);
		try {
			Thread.sleep(6000*10);
			LOG.info("nextTuple begin");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ack(Object msgId) {
		EsAckObj eao = (EsAckObj) msgId;
		handler.ack(eao, collector, esClient);
	}

	@Override
	public void fail(Object msgId) {
		EsAckObj eao = (EsAckObj) msgId;
		handler.fail(eao, collector, esClient);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("BatchId", "ByteArray"));
		 declarer.declareStream(JstormLppzUtil.COMMIT, new Fields("BatchId"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	private static class StormPrepareBulk extends PrepareBulk {
		private SpoutOutputCollector collector;

		public StormPrepareBulk(SpoutOutputCollector collector) {
			this.collector = collector;
		}

		@Override
		public void bulk(final List<SearchResult> srList) {
			if(CollectionUtils.isEmpty(srList))
				return;
			BatchId batchId = BatchId.mkInstance();
			for(SearchResult sr:srList){
				byte[] b = null;
				try {
					b = KryoUtil.kyroSeriLize(sr, -1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				collector.emit(Utils.tuple(batchId,b),
							new EsAckObj(sr.getIndex(), sr.getType(), sr.getId(),batchId));
			}
			EsAckObj ea=new EsAckObj();
			ea.setBatchId(batchId);
			ea.setId(JstormLppzUtil.COMMIT);
			collector.emit(JstormLppzUtil.COMMIT,Utils.tuple(batchId),
					ea);
		}
	}
}