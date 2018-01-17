package com.lppz.util.thread.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.thread.AsyncBaseHandler;
import com.lppz.util.thread.ShardingByIpAndPortComponent;

/**
 * @author zoubin
 * @param <F>
 *
 */
public abstract class BaseFetchTrigger<F,T> extends ShardingByIpAndPortComponent{
	private final Logger logger = LoggerFactory.getLogger(BaseFetchTrigger.class);
	
	protected boolean clusterMode;
	private AsyncBaseHandler<F,T> asyncBaseHandler;
	
	protected int fetchSize;
	
	public void setAsyncBaseHandler(AsyncBaseHandler<F,T> asyncBaseHandler) {
		this.asyncBaseHandler = asyncBaseHandler;
	}

	@Override
	public void init() throws Exception {
		super.init();
		logger.info("nodeList:{},"
				+ ":{}",getNodeList(),clusterMode);
		logger.info("nodeNum:{},node:{}",getClusterMap().size(),getNode());
		new Thread(new Runnable(){
			@Override
			public void run() {
		while(true){
			long startTime = System.currentTimeMillis();
			try {
				List<String> pks = fetchPkIds();
				if (pks.isEmpty()) {
					logger.info("+++++++++++ no pk to create end cost {}s",(System.currentTimeMillis() - startTime)/1000f);
					Thread.sleep(5000);
					continue;
				}
				asyncBaseHandler.handle(pks,getNumPerThread(),getNumAfertThread(),getFetchojb(),getAobj());
			} catch (Exception e) {
				logger.error(e.toString(),e);
			}
		}}}).start();
	}
	protected abstract List<String> fetchPkIds();

	/**
	 * @param clusterMode the clusterMode to set
	 */
	public void setClusterMode(boolean clusterMode) {
		this.clusterMode = clusterMode;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	
	protected abstract int getNumPerThread( ); 
	protected abstract int getNumAfertThread( ); 
	protected abstract Object[] getFetchojb( ); 
	protected abstract Object[] getAobj( ); 
	
	
}