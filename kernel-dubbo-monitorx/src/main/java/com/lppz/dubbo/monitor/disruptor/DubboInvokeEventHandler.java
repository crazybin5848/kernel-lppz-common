package com.lppz.dubbo.monitor.disruptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.monitor.MonitorService;
import com.lmax.disruptor.EventHandler;
import com.lppz.dubbo.monitor.domain.DubboInvoke;
import com.lppz.elasticsearch.EsModel;
import com.lppz.elasticsearch.EsModel.EsDMlEnum;
import com.lppz.elasticsearch.LppzEsComponent;
import com.lppz.elasticsearch.disruptor.ContantIndexName;

public class DubboInvokeEventHandler implements EventHandler<DubboInvokeEvent> {
	private static final Logger logger = LoggerFactory
			.getLogger(DubboInvokeEventHandler.class);
	private static final String POISON_PROTOCOL = "poison";
	private static final String TIMESTAMP = "timestamp";
	private long startTime = System.currentTimeMillis();
	List<EsModel> esModelList = new ArrayList<EsModel>();
    private int size=100;
    private int time=3000;
	
	@Override
	public void onEvent(DubboInvokeEvent longEvent, long l, boolean b)
			throws Exception {
		URL statistics = longEvent.getStatistics();
		collect2DataBase(statistics);
		if (!esModelList.isEmpty()
				&& (esModelList.size() == size || (System
						.currentTimeMillis() - startTime) >= time)) {
			BulkResponse resp = LppzEsComponent.getInstance().batch(esModelList);
    			logger.info(resp.toString());
			if(resp.hasFailures()){
				logger.error(resp.buildFailureMessage());
			}
	        logger.info("Send statistics to monitor DB:" + esModelList.size());
			esModelList.clear();
			startTime = System.currentTimeMillis();
		}
	}

	private void collect2DataBase(URL statistics) throws Exception {
		if (POISON_PROTOCOL.equals(statistics.getProtocol())) {
			return;
		}
		String timestamp = statistics.getParameter(Constants.TIMESTAMP_KEY);
		Date now;
		if (timestamp == null || timestamp.length() == 0) {
			now = new Date();
		} else if (timestamp.length() == "yyyyMMddHHmmss".length()) {
			now = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
		} else {
			now = new Date(Long.parseLong(timestamp));
		}
		DubboInvoke dubboInvoke = new DubboInvoke();
		dubboInvoke.setTimeParticle(null);
		try {
			if (statistics.hasParameter(MonitorService.PROVIDER)) {
				dubboInvoke.setType(MonitorService.CONSUMER);
				dubboInvoke.setConsumer(statistics.getHost());
				dubboInvoke.setProvider(statistics.getParameter(MonitorService.PROVIDER));
				int i = dubboInvoke.getProvider().indexOf(':');
				if (i > 0) {
					dubboInvoke.setProvider(dubboInvoke.getProvider()
							.substring(0, i));
				}
			} else {
				dubboInvoke.setType(MonitorService.PROVIDER);
				dubboInvoke.setConsumer(statistics.getParameter(MonitorService.CONSUMER));
				int i = dubboInvoke.getConsumer().indexOf(':');
				if (i > 0) {
					dubboInvoke.setConsumer(dubboInvoke.getConsumer()
							.substring(0, i));
				}
				dubboInvoke.setProvider(statistics.getHost());
			}
			dubboInvoke.setInvokeDate(now);
			dubboInvoke.setService(statistics.getServiceInterface());
			dubboInvoke.setMethod(statistics.getParameter(MonitorService.METHOD));
			dubboInvoke.setInvokeTime(statistics.getParameter(TIMESTAMP,
					System.currentTimeMillis()));
			dubboInvoke.setSuccess(statistics.getParameter(MonitorService.SUCCESS, 0));
			dubboInvoke.setFailure(statistics.getParameter(MonitorService.FAILURE, 0));
			dubboInvoke.setElapsed(statistics.getParameter(MonitorService.ELAPSED, 0));
			dubboInvoke.setConcurrent(statistics.getParameter(MonitorService.CONCURRENT, 0));
			dubboInvoke.setMaxElapsed(statistics.getParameter(MonitorService.MAX_ELAPSED, 0));
			dubboInvoke.setMaxConcurrent(statistics.getParameter(
					MonitorService.MAX_CONCURRENT, 0));
			if (dubboInvoke.getSuccess() == 0 && dubboInvoke.getFailure() == 0
					&& dubboInvoke.getElapsed() == 0
					&& dubboInvoke.getConcurrent() == 0
					&& dubboInvoke.getMaxElapsed() == 0
					&& dubboInvoke.getMaxConcurrent() == 0) {
				return;
			}
			String currDay = new SimpleDateFormat("yyyy").format(dubboInvoke.getInvokeDate());
			dubboInvoke.buildTime();
			esModelList.add(new EsModel(ContantIndexName.INDEX
					+ currDay,dubboInvoke
					.getClass().getName(),Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()),36),dubboInvoke,EsDMlEnum.Insert));
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}
}