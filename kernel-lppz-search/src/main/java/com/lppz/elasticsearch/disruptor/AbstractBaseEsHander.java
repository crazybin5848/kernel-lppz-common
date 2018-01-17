package com.lppz.elasticsearch.disruptor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;

import com.lppz.elasticsearch.EsModel;
import com.lppz.util.disruptor.BaseErrorHandler;
import com.lppz.util.disruptor.BaseHandler;

public abstract class AbstractBaseEsHander implements BaseHandler<EsModel> {
	private Logger logger = Logger.getLogger(AbstractBaseEsHander.class);
	Map<EsError, ErrorMsg> errorMap = new LinkedHashMap<EsError, ErrorMsg>();
	protected BaseErrorHandler<EsModel> errorHandler;

	@Override
	public void handle(List<EsModel> list) {
		BulkResponse resp = null;
		try {
			resp = doHandleBatch(list);
			// if(resp==null){
			// list.clear();
			// return;
			// }
			if (resp.hasFailures()) {
				BulkItemResponse[] items = resp.getItems();
				for (BulkItemResponse item : items) {
					if (item.isFailed()) {
						EsError error = new EsError(item.getIndex(),
								item.getType(), item.getId());
						if (errorMap.containsKey(error)) {
							int retryNum = errorMap.get(error).getRetryCount();
							if (retryNum > 100)
								logger.warn(error.toString() + " has retryed:"
										+ retryNum);
							error.setRetryNum(retryNum + 1);
						}
						errorMap.put(error, new ErrorMsg(error.getRetryNum(),
								item.getFailureMessage(),item.getFailure()));
						// logger.error("send es error message:"+item.getFailureMessage());
						// logger.error("send error id:{"+item.getId()+"},index is:{"+item.getIndex()+"}");
					}
				}
				Iterator<EsModel> it = list.iterator();
				String errorMsg = null;
				while (it.hasNext()) {
					EsModel model = it.next();
					EsError tmperror = new EsError(model.getIndex(),
							model.getType(), model.getId());
					if (!errorMap.containsKey(tmperror)) {
						it.remove();
					}
					if (errorMap.containsKey(tmperror)) {
						ErrorMsg msg = errorMap.get(tmperror);
						errorMsg = msg.getFailurMsg();
						Integer retryCount = msg.getRetryCount();
						int limit = errorHandler == null ? 500 : errorHandler
								.getRetryCount();        
						if ((errorMsg!=null&&errorMsg.contains("document missing"))
								||(limit != Integer.MAX_VALUE && retryCount >= limit)) {
							it.remove();
							errorMap.remove(tmperror);
							if (errorHandler != null){
								model.setFailure(msg.getFailure());
								errorHandler.handler(model);
							}
						}
						logger.error(tmperror.toString() + " has retryed "
								+ limit
								+ " times,will not retry any more,ERROR:"
								+ errorMsg);
					}
				}
//				logger.error(resp.buildFailureMessage());
				if (!list.isEmpty()) {
					logger.warn(list.size() + " " + list.get(0).getType()
							+ " has partly Successfully sent to Es");
				}
			} else {
				if (errorHandler != null && errorHandler.isLogInfo())
					logger.info(list.size() + " " + list.get(0).getType()
							+ " has sent to Es");
				else
					logger.warn(list.size() + " " + list.get(0).getType()
							+ " has sent to Es");
				list.clear();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected abstract BulkResponse doHandleBatch(List<EsModel> list);

	public BaseErrorHandler<EsModel> getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(BaseErrorHandler<EsModel> errorHandler) {
		this.errorHandler = errorHandler;
	}

	public AbstractBaseEsHander buildErrorHandler(
			BaseErrorHandler<EsModel> errorHandler) {
		setErrorHandler(errorHandler);
		return this;
	}
}