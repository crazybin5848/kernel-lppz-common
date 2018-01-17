package com.lppz.canal.etl.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.bean.TableBean;
import com.lppz.canal.bean.TableRelationBean;
import com.lppz.canal.consumer.AbstractConsumer;
import com.lppz.canal.enums.EtlEnums;
import com.lppz.canal.etl.transform.convertor.AbstractHBaseConvertor;
import com.lppz.canal.etl.transform.convertor.HBaseConvertorFactory;
import com.lppz.canal.exception.YamlLoadException;
import com.lppz.canal.service.QueueSenderService;
import com.lppz.util.rocketmq.enums.OtherRocketMqConsumerGroup;
import com.lppz.util.rocketmq.enums.OtherRocketMqTopic;

/**
 * 从rocketmq拉取ChangeDataBean信息，并构建hbase的rowkey，转义字段名和值中特殊字符，然后发送到下一节点
 * 1、初始化表关联配置
 * 2、启动消费线程，获取mq消息
 * 3、校验表是否需要处理
 * 4、通过工厂类获取Convertor
 * 5、构造rowkey
 * 6、转义字段名和值
 * 7、发送到下一节点
 * @author licheng
 *
 */
@Component
public class TransformTask extends AbstractConsumer<ChangeDataBean, ChangeDataBean, Object> implements DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(TransformTask.class);
	
    public static String GET_MAIN_ID_REDIS_LUA_SCRIPT = "function getParent(key) return call('get',key) end do local f = function (key) local tmpValue = getParent(key) if tmpValue then return key else return getParent(tmpValue) end end return getParent(KEYS[1]) end";


	@Value("${rocketmq.nameserver}")
	private String nameSrv;
	private String tag = EtlEnums.TRANSFORM.getName();
	@Value("${rocketmq.consumer.batchSize:1000}")
	private int batchSize;
	@Value("${rocketmq.consumer.batchTimeStep:1000}")
	private int batchTimeStep;
	@Value("${rocketmq.consumer.minTreadNum:1}")
	private int consumerMinTreadNum;
	@Value("${rocketmq.consumer.maxTreadNum:10}")
	private int consumerMaxTreadNum;
	private String topic = OtherRocketMqTopic.CANCALMSG.getId();
	private String consumerGroup = OtherRocketMqConsumerGroup.CANCALMSG.getId();
	@Value("${task.transform.isRun:false}")
	private boolean isRun;
	private TableRelationBean tables;
	
	@Resource
	private Jedis jedis;
	@Resource
	private JedisCluster jedisCluster;
	
	@Resource
	private QueueSenderService queueSenderService;
	
	@PostConstruct
	public void startTask(){
		if (!isRun) {
			return;
		}
		try {
			initTableBean();
		} catch (YamlLoadException e) {
			throw new RuntimeException(e);
		}
		startConsume();
	}
	
	/**
	 * 如果变更表不在配置的范围内，直接pass
	 */
	@Override
	public List<ChangeDataBean> validate(List<ChangeDataBean> t) {
		List<ChangeDataBean> dealDatas = new ArrayList<>();
		if (tables != null) {
			for (ChangeDataBean data : t) {
				if (tables.getTables().containsKey(data.getTable())) {
					dealDatas.add(data);
				}
			}
		}
		return dealDatas;
	}
	
	/**
	 * 根据表获取Convertor
	 */
	@Override
	public Object dealMsg(List<ChangeDataBean> t) {
		TableBean tableBean = null;
		for (ChangeDataBean data : t) {
			tableBean = tables.getTables().get(data.getTable());
			AbstractHBaseConvertor convertor = HBaseConvertorFactory.getInstance().getConvertor(tableBean, jedis, jedisCluster);
			data.setRowkey(convertor.buildRowKey(data.getValues()));
			data.setValues(convertor.convert(data.getValues()));
			data.setHbaseTable(tableBean.getHbaseTableName());
			data.setHbaseCf(tableBean.getHbaseCFName());
		}
		
		queueSenderService.sendNext(EtlEnums.LOAD, t);
		return null;
	}

	@Override
	public void dealRollBack(List<ChangeDataBean> s) {
		// TODO Auto-generated method stub
	}
	
	private void initTableBean() throws YamlLoadException {
		org.springframework.core.io.Resource[] resources = null;
		try {
			resources = getResources("classpath*:/META-INF/canal-table-relation.yaml");
			if(resources == null || resources.length == 0){
				throw new YamlLoadException("not found canal-table-relation.yaml");
			}
			tables = (TableRelationBean) new Yaml().load(resources[0].getInputStream());
		} catch (IOException e) {
			logger.error("加载表关联关系错误", e);
			throw new YamlLoadException("table-relation load exception");
		}
	}

	@Override
	public String getNameSrv() {
		return nameSrv;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public String getConsumerGroup() {
		return consumerGroup;
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public int getBatchTimeStep() {
		return batchTimeStep;
	}

	@Override
	public int getConsumerMinTreadNum() {
		return consumerMinTreadNum;
	}

	@Override
	public int getConsumerMaxThreadNum() {
		return consumerMaxTreadNum;
	}

}
