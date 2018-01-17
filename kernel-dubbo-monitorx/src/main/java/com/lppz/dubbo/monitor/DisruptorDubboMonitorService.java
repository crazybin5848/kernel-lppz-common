package com.lppz.dubbo.monitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import com.alibaba.dubbo.rpc.support.DubboInvokeDetail;
import com.google.common.collect.Maps;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lppz.dubbo.log.DubboLogInterface;
import com.lppz.dubbo.monitor.disruptor.DubboInvokeEvent;
import com.lppz.dubbo.monitor.disruptor.DubboInvokeEventFactory;
import com.lppz.dubbo.monitor.disruptor.DubboInvokeEventHandler;
import com.lppz.dubbo.monitor.disruptor.DubboInvokeEventProducerWithTranslator;
import com.lppz.dubbo.monitor.domain.DubboInvoke;
import com.lppz.elasticsearch.result.Pager;
import com.lppz.elasticsearch.result.ResultBucket;
import com.lppz.util.logback.LogBackKafkaVo;

/**
 * MonitorService
 *
 * @author Bin.Zou
 */
@Service(delay = -1)
public class DisruptorDubboMonitorService implements MonitorService {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorDubboMonitorService.class);

    public static final String CLASSNAME = DisruptorDubboMonitorService.class.getName() + ".";

    private DubboInvokeEventProducerWithTranslator producer;
    
    @Reference(protocol="dubbo")
    private DubboLogInterface restHttpLogInterface;
    
    @PostConstruct
    private void init() {
    		DubboInvokeEventFactory factory = new DubboInvokeEventFactory();
		int bufferSize = 1024;
		Disruptor<DubboInvokeEvent> disruptor = new Disruptor<DubboInvokeEvent>(factory,
				bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, 
				new BlockingWaitStrategy());
		disruptor
		.handleEventsWith(new DubboInvokeEventHandler[] { new DubboInvokeEventHandler()});
		disruptor.start();
		RingBuffer<DubboInvokeEvent> ringBuffer = disruptor.getRingBuffer();
		producer = new DubboInvokeEventProducerWithTranslator(
				ringBuffer);
    }

    public void collect(URL statistics) {
    		producer.onData(statistics);
        if (logger.isInfoEnabled()) {
//            logger.info("collect statistics: " + statistics);
        }
    }

    public List<URL> lookup(URL query) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 统计调用数据用于图表展示
     *
     * @param dubboInvoke
     */
    public List<DubboInvoke> countDubboInvoke(DubboInvoke dubboInvoke) {
        if (StringUtils.isEmpty(dubboInvoke.getService())) {
            logger.error("统计查询缺少必要参数！");
            throw new RuntimeException("统计查询缺少必要参数！");
        }
     	Map<String, List<ResultBucket>> tmpMap=EsStasticUtil.countDubboInvoke(dubboInvoke);
     	List<DubboInvoke> ls=new ArrayList<DubboInvoke>();
     	List<ResultBucket> rbList=tmpMap==null||tmpMap.isEmpty()
      		   ?null:tmpMap.get("invokeTime");
         if(rbList==null)
      	   return ls;
         for(ResultBucket rb:rbList){
        	 DubboInvoke di=new DubboInvoke();
        	 di.setInvokeTime(Long.parseLong(rb.getBucketKeyList().get(0)));
        	 di.setSuccess(rb.getMapResultStats().get("sumSucc").getSum());
        	 if(di.getSuccess()==0)
        		 di.setElapsed(BigDecimal.ZERO.doubleValue());
			else
			di.setElapsed(BigDecimal.valueOf(rb.getMapResultStats().get("sumElapsed").getSum())
							 .divide(BigDecimal.valueOf(di.getSuccess()),2).doubleValue());
        	 di.setMethod(dubboInvoke.getMethod());
        	 di.setType(dubboInvoke.getType());
        	 ls.add(di);
         }
         return ls;
//     	return dao.getList(CLASSNAME, "countDubboInvoke", dubboInvoke);
    }

    public List<String> getMethodsByService(DubboInvoke dubboInvoke) {
    	List<String> ls=new ArrayList<String>();
    	Map<String, List<ResultBucket>> map=EsStasticUtil.getMethodsByService(dubboInvoke);
    	List<ResultBucket> list=	map.get("method");
    	if(CollectionUtils.isEmpty(list))
    		return ls;
    	for(ResultBucket rb:list){
    		ls.addAll(rb.getBucketKeyList());
    	}
    	return ls;
    }

    /**
     * 统计各方法调用信息
     *
     * @param dubboInvoke
     * @return
     */
    public List<DubboInvoke> countDubboInvokeInfo(DubboInvoke dubboInvoke) {
        if (StringUtils.isEmpty(dubboInvoke.getService()) || StringUtils.isEmpty(dubboInvoke.getMethod())
                || StringUtils.isEmpty(dubboInvoke.getType())) {
            logger.error("统计查询缺少必要参数！");
            throw new RuntimeException("统计查询缺少必要参数！");
        }
        Map<String,List<ResultBucket>> tmpMap=EsStasticUtil.countDubboInvokeInfo(dubboInvoke);
       List<DubboInvoke> list=new ArrayList<DubboInvoke>();
       ResultBucket rb=tmpMap==null||tmpMap.isEmpty()
    		   ?null:CollectionUtils.isEmpty(tmpMap.get("succ"))?null:tmpMap.get("succ").get(0);
       if(rb==null)
    	   return list;
       DubboInvoke di=new DubboInvoke();
       di.setMaxConcurrent(BigDecimal.valueOf(rb.getMapResultStats().
    		   get("maxMaxConcurrent").getMax()).intValue());
       di.setFailure(rb.getMapResultStats().
    		   get("sumFail").getSum());
       di.setSuccess(rb.getMapResultStats().
    		   get("sumSucc").getSum());
       di.setElapsed(rb.getMapResultStats().
    		   get("sumElapsed").getSum());
       di.setMaxElapsed(BigDecimal.valueOf(rb.getMapResultStats().
    		   get("maxMaxElapsed").getMax()).intValue());
       list.add(di);
        return list;
    }

    /**
     * 统计系统方法调用排序信息
     *
     * @param dubboInvoke
     * @return
     */
    public Map<String, List<DubboInvoke>> countDubboInvokeTopTen(DubboInvoke dubboInvoke) {
        Map<String, List<DubboInvoke>> result = Maps.newHashMap();
        Map<String,List<ResultBucket>> tmpMap=EsStasticUtil.countDubboInvokeSFTopTen(dubboInvoke);
        result.put("success", new ArrayList<DubboInvoke>());
        result.put("failure", new ArrayList<DubboInvoke>());
        List<ResultBucket> succList=tmpMap.get("succ");
        if(succList!=null)
        for(ResultBucket rb:succList){
        	DubboInvoke di=new DubboInvoke();
        	di.setService(rb.getBucketKeyList().get(0));
        	di.setMethod(rb.getBucketKeyList().get(1));
        	di.setSuccess(rb.getMapResultStats().values().iterator().next().getSum());
        result.get("success").add(di);
        }
        List<ResultBucket> failList=tmpMap.get("fail");
        if(failList!=null)
        for(ResultBucket rb:failList){
        	DubboInvoke di=new DubboInvoke();
        	di.setService(rb.getBucketKeyList().get(0));
        	di.setMethod(rb.getBucketKeyList().get(1));
        	di.setFailure(rb.getMapResultStats().values().iterator().next().getSum());
        	result.get("failure").add(di);
        }
        return result;
    }
    
    public Pager<DubboInvokeDetail> getRestHttpLogList(DubboInvokeDetail dto,Integer page,Integer rows){
    		return restHttpLogInterface.getRestHttpLogList(dto,page,rows);
    }
	
    public Pager<LogBackKafkaVo> getLogBackList(LogBackKafkaVo dto,Integer page,Integer rows){
		return restHttpLogInterface.getLogBackList(dto,page,rows);
    }
}