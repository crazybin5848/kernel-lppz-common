package com.lppz.hbase.client.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.BaseClientUtil;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.Constants;
import org.apache.hadoop.hbase.client.coprocessor.MapjoinHbaseClient;
import org.apache.hadoop.hbase.client.coprocessor.agggroupby.AggregationGroupByClient;
import org.apache.hadoop.hbase.client.coprocessor.model.ddl.HBaseDDLResult;
import org.apache.hadoop.hbase.client.coprocessor.model.idx.RowKeyComposition;
import org.apache.hadoop.hbase.zookeeper.RecoverableZooKeeper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.util.StringUtils;
import com.lppz.util.http.BaseHttpClientsComponent;
import com.lppz.util.http.FutureWrapper;
import com.lppz.util.http.enums.HttpMethodEnum;
import com.lppz.util.kryo.KryoUtil;

public class BaseHbaseClientConfig extends BaseHttpClientsComponent {
	protected static RecoverableZooKeeper rz;
	private static final Logger logger = LoggerFactory.getLogger(BaseHbaseClientConfig.class);
	protected static HTablePool hTablePool;
	protected static MapjoinHbaseClient joinHbaseClient;
	protected static AggregationGroupByClient aggregationGroupByClient;
	protected static AggregationClient aggregationClient;
	protected static org.apache.hadoop.hbase.client.coprocessor.agg.AggregationClient aggClient;
	protected static Map<String,Map<String,String>> mapCache;
	
	public synchronized void init(Configuration cfg) {
		try {
			super.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rz==null)
		rz=BaseClientUtil.getZkFromHbaseConf(cfg);
	}
	
	public synchronized void initConf(Configuration cf) throws IOException{
		if(hTablePool==null){
			joinHbaseClient = new MapjoinHbaseClient(cf);
			hTablePool = new HTablePool(cf, 1000);
			aggregationGroupByClient=new AggregationGroupByClient(cf);
			aggregationClient=new AggregationClient(cf);
			aggClient = new org.apache.hadoop.hbase.client.coprocessor.agg.AggregationClient(cf);
			initMapCache();
		}
	}

	static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3, DaemonThreadFactory.INSTANCE);
	ScheduledFuture<?> sendFuture;
	private void initMapCache(){
		initMap();
		long monitorInterval = 3000l;
		sendFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                initMap();
            }
        }, monitorInterval, monitorInterval, TimeUnit.MILLISECONDS);
	}

	protected Map<String,Map<String,String>> initMap() {
		try {
        	HBaseDDLResult result=BaseHbaseClientConfig.this.doDDL("/services/hbaseddl/listTableDesc", null);
        	mapCache=result.getHbaseDescMap();
        	logger.debug("mapCache:{} has been synced",mapCache);
        } catch (Throwable t) { // 防御性容错
            logger.error("Unexpected error occur at sync statistic, cause: " + t.getMessage(), t);
        }
		return mapCache;
	}
	
	protected HBaseDDLResult doDDL(String hostSuffix,String entity) throws Exception {
		super.initHttpClient();
		String hostPort = getClusterdDDLHostByZk();
		HttpRequestBase httpPost = super.createReqBase("http://" + hostPort
				+ hostSuffix, HttpMethodEnum.POST);
		if(entity!=null){
			StringEntity s = new StringEntity(entity, "UTF-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			((HttpPost) httpPost).setEntity(s);
		}
		FutureWrapper fwPost = super.doHttpExec(httpPost, null, 0);
		try {
			HttpResponse hrPost = fwPost.getFh().get();
			HBaseDDLResult result = JSON.parseObject(StringUtils
					.convertStreamToString(hrPost.getEntity().getContent()),
					HBaseDDLResult.class);
			if (result.getExcp() != null)
				throw result.getExcp();
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			super.closeHttpClient();
		}
	}
	
	private String getClusterdDDLHostByZk(){
		if(rz==null)
			return null;
		try {
			List<String> listHost=new ArrayList<String>();
			List<String> paths=rz.getChildren(Constants.ZOOBINGOADMIN, false);
			if(CollectionUtils.isNotEmpty(paths)){
				for(String path:paths){
					byte[] b=rz.getData(Constants.ZOOBINGOADMIN+"/"+path, true, null);
					listHost.add(KryoUtil.kyroDeSeriLize(b, String.class));
				}
				int i=ThreadLocalRandom.current().nextInt(listHost.size());
				return listHost.get(i);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	protected RowKeyComposition getRKCFromMapCache(String hbaseTable,String hcd) {
		if(mapCache==null)
			mapCache=initMap();
		Map<String,String> hdt= mapCache.get(hbaseTable);
		RowKeyComposition rkc=JSON.parseObject(hdt.get(hcd), RowKeyComposition.class);
		return rkc;
	}
}
