package com.lppz.util.logback;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.lppz.util.FileReaderUtils;
import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.producer.BaseKafkaProducer;

public class KafkaAppender extends AppenderBase<ILoggingEvent> {

	@SuppressWarnings("rawtypes")
	BaseKafkaProducer<LogBackKafkaVo> producer;
	private static Set<String> includeSet = new HashSet<String>();
	private String includingPackage;
	private String kafkaBrokerPath;
	public String getKafkaBrokerPath() {
		return kafkaBrokerPath;
	}

	public void setKafkaBrokerPath(String kafkaBrokerPath) {
		this.kafkaBrokerPath = kafkaBrokerPath;
	}

	private boolean needFilter=true;

	public boolean isNeedFilter() {
		return needFilter;
	}

	public void setNeedFilter(boolean needFilter) {
		this.needFilter = needFilter;
	}


	public String getIncludingPackage() {
		return includingPackage;
	}

	public void setIncludingPackage(String includingPackage) {
		this.includingPackage = includingPackage;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void append(ILoggingEvent eventObject) {
		if (needFilter) {
			boolean flag=false;
			if(CollectionUtils.isNotEmpty(includeSet)){
				for(String regex:includeSet){
					if(eventObject.getLoggerName().matches(regex)){
						flag=true;
						break;
					}
				}
			}
			
			if(!flag)
				return;
		}
		LogBackKafkaVo vo = new LogBackKafkaVo().build(eventObject);
		if (producer != null)
			try {
				producer.sendMsg(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void start() {
		super.start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initProducer();
			}

			
		}).start();
	}
	
	@SuppressWarnings("static-access")
	private void initProducer() {
		while (!FileReaderUtils.existsFile("kafka.properties")) {
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		if (needFilter) {
			if (StringUtils.isBlank(includingPackage))
				return;
			for (String s : includingPackage.split(",")) {
				includeSet.add(s+".*");
			}
		}
		producer = new LogBackLoggerProducer();
		try {
			producer.kafkaProducerConfig=producer.initConfig(kafkaBrokerPath);
			producer.setProducer_type(KafkaConstant.ASYNC);
			producer.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		super.stop();
	}
}